package co.techmagic.hr.presentation.time_tracker.info

import co.techmagic.hr.data.entity.HolidayDate
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.data.entity.time_report.UserReportsResponse
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.time_tracker.Constants.EXPECTED_MINUTES_PER_DAY
import co.techmagic.hr.presentation.util.*
import com.techmagic.viper.base.BasePresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private typealias OrganizedReports = HashMap<String, MutableList<UserReport>>
private typealias OrganizedHolidays = HashMap<String, HolidayDate>
private typealias Result = Pair<OrganizedReports, OrganizedHolidays>

class HrAppTimeInfoPresenter(
        private val selectedDate: Calendar,
        private val userId: String,
        private val timeReportRepository: TimeReportRepository) :
        BasePresenter<TimeInfoView, TimeInfoRouter>(), TimeInfoPresenter {

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)

        view?.showLoading()
        call(loadReports(), {
            view?.hideLoading()
            view?.showReports(it)
        })
    }

    private fun loadReports(): Observable<List<TimeReportViewModel>> {
        val firstDayOfMonth = selectedDate.firstDayOfMonthDate()
        val lastDayOfMonth = selectedDate.lastDayOfMonthDate()
        val firstDayOfPreviousMonth = firstDayOfMonth.previousDay().firstDayOfMonthDate()

        var observables = ArrayList<Observable<Result>>(10)
        var weekDate = firstDayOfPreviousMonth
        while (weekDate.timeInMillis <= lastDayOfMonth.timeInMillis) {
            val weekReports = timeReportRepository
                    .getDayReports(userId, weekDate)
                    .map { convert(it) }
            observables.add(weekReports)
            weekDate = weekDate.nextWeek()
        }

        return Observable.zip(observables) {
            val dayReports = HashMap<String, MutableList<UserReport>>()
            val holidays = HashMap<String, HolidayDate>()

            for (result in it) {
                val pair = result as? Result
                pair ?: continue
                dayReports.putAll(pair.first)
                holidays.putAll(pair.second)
            }

            return@zip Pair(dayReports, holidays)

        }.map { convert(it) }.observeOn(AndroidSchedulers.mainThread())
    }

    private fun convert(response: UserReportsResponse): Result {
        val dayReports = HashMap<String, MutableList<UserReport>>()
        val holidays = HashMap<String, HolidayDate>()

        organizeReports(response.reports, dayReports)
        organizeHolidays(response.holidays, holidays)

        return Pair(dayReports, holidays)
    }

    private fun organizeReports(reports: List<UserReport>, into: OrganizedReports) {
        for (report in reports) {
            val key = key(report.date.toCalendar())
            val list = into[key]
            if (list != null) {
                list.add(report)
            } else {
                into[key] = ArrayList<UserReport>().also {
                    it.add(report)
                }
            }
        }
    }

    private fun organizeHolidays(holidays: List<HolidayDate>, into: OrganizedHolidays) {
        for (holiday in holidays) {
            val date = DateUtil.parseStringDate(holiday.date)?.toCalendar()
            if (date != null) {
                into[key(date)] = holiday
            }
        }
    }

    private fun convert(result: Result): List<TimeReportViewModel> {
        val today = selectedDate
        val yesterday = today.previousDay()

        val thisWeek = today.firstDayOfWeekDate()
        val previousWeek = thisWeek.previousWeek()

        val thisMonth = today.firstDayOfMonthDate()
        val previousMonth = thisMonth.previousDay().firstDayOfMonthDate()

        return listOf(
                formReport("hours today", today, today, result),
                formReport("hours yesterday", yesterday, yesterday, result),

                formReport("this week", thisWeek, thisWeek.addDays(6), result),
                formReport("previous week", previousWeek, previousWeek.addDays(6), result),

                formReport("this month", thisMonth, thisMonth.lastDayOfMonthDate(), result),
                formReport("previous month", previousMonth, previousMonth.lastDayOfMonthDate(), result)
        )
    }

    private fun key(date: Calendar) = date.formatDate()

    private fun formReport(name: String, start: Calendar, end: Calendar, result: Result): TimeReportViewModel {
        var workingDaysCount = 0
        var totalMinutes = 0

        val daysCount = (daysBetween(end, start) + 1).toInt() //inclusive
        for (i in 0 until daysCount) {
            val date = start.addDays(i)
            val weekday = date.get(Calendar.DAY_OF_WEEK)

            val isWorkingDay = weekday != Calendar.SATURDAY
                    && weekday != Calendar.SUNDAY
                    && result.second[key(date)] == null
            if (isWorkingDay) {
                workingDaysCount += 1
            }

            var reports = result.first[key(date)]
            if (reports != null) {
                for (report in reports) {
                    totalMinutes += report.minutes
                }
            }
        }
        return TimeReportViewModel(name, totalMinutes, workingDaysCount * EXPECTED_MINUTES_PER_DAY)
    }
}