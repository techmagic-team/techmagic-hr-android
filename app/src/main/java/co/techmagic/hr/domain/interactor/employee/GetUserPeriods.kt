package co.techmagic.hr.domain.interactor.employee

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.entity.DateFrom
import co.techmagic.hr.data.entity.DateTo
import co.techmagic.hr.data.entity.HolidayDate
import co.techmagic.hr.data.request.TimeOffAllRequest
import co.techmagic.hr.data.request.TimeOffRequestByUser
import co.techmagic.hr.domain.interactor.DataUseCase
import co.techmagic.hr.domain.pojo.DatePeriodDto
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto
import co.techmagic.hr.domain.repository.IEmployeeRepository
import co.techmagic.hr.presentation.pojo.AvailableTimeOffsData
import co.techmagic.hr.presentation.pojo.WorkingPeriod
import co.techmagic.hr.presentation.util.DateUtil
import rx.Observable
import java.util.*

/**
 * Created by Roman Ursu on 6/12/17
 */
class GetUserPeriods(iEmployeeRepository: IEmployeeRepository) : DataUseCase<String, AvailableTimeOffsData, IEmployeeRepository>(iEmployeeRepository) {

    override fun buildObservable(userId: String): Observable<AvailableTimeOffsData> {
        val availableTimeOffsData: AvailableTimeOffsData = AvailableTimeOffsData()

        return repository.getUserPeriods(userId)
                .flatMap { periodsList ->
                    val timeOffsObservableList: MutableList<Observable<AvailableTimeOffsData>> = mutableListOf()

                    val sortedPeriodsList: MutableList<DatePeriodDto> = mutableListOf()
                    sortedPeriodsList.addAll(periodsList)
                    sortedPeriodsList.sortBy { (dateFrom) -> dateFrom }

                    for ((dateFrom, dateTo) in sortedPeriodsList) {
                        val timeOffRequestByUser: TimeOffRequestByUser = TimeOffRequestByUser(userId, DateFrom(dateFrom.time), DateTo(dateTo.time))
                        val observable = getLoadTimeOffsObservable(timeOffRequestByUser)
                                .map { remainedTimeOffsAmountDto ->
                                    val periodPair: WorkingPeriod = WorkingPeriod(dateFrom, prepareDateTo(dateTo))

                                    availableTimeOffsData.timeOffsMap.put(periodPair, remainedTimeOffsAmountDto)
                                    availableTimeOffsData
                                }

                        timeOffsObservableList.add(observable)
                    }

                    Observable.zip(timeOffsObservableList, { availableTimeOffsData })
                }
    }

    private fun prepareDateTo(dateTo: Date): Date {
        val dateToCalendar: Calendar = Calendar.getInstance()
        dateToCalendar.time = dateTo
        dateToCalendar.add(Calendar.DAY_OF_MONTH, -1)
        dateToCalendar.set(Calendar.HOUR_OF_DAY, 23)
        dateToCalendar.set(Calendar.MINUTE, 59)
        dateToCalendar.set(Calendar.SECOND, 59)
        return dateToCalendar.time
    }

    fun getLoadTimeOffsObservable(timeOffRequestByUser: TimeOffRequestByUser): Observable<RemainedTimeOffsAmountDto> {

        val remainedTimeOffsAmountDto = RemainedTimeOffsAmountDto()

        val totalIllnessObservable: Observable<Int> = repository.getTotalIllness(timeOffRequestByUser)
        val totalVacationObservable: Observable<Int> = repository.getTotalVacation(timeOffRequestByUser)
        val totalDayOffsObservable: Observable<Int> = repository.getTotalDayOff(timeOffRequestByUser)

        val timeOffAllRequest: TimeOffAllRequest = TimeOffAllRequest(timeOffRequestByUser.dateFrom.gte, timeOffRequestByUser.dateTo.lte)

        val holidaysObservable: Observable<List<Calendar>> = repository.getHolidays(timeOffAllRequest)
                .map({ calendarInfoList: List<HolidayDate> -> map(calendarInfoList) })

        return Observable.zip(
                totalIllnessObservable,
                totalVacationObservable,
                totalDayOffsObservable)
        { totalIllness, totalVacation, totalDayOffs ->
            processResult(remainedTimeOffsAmountDto, totalIllness, totalVacation, totalDayOffs)
        }
                .flatMap({ remainedTimeOffsAmountDtoReady: RemainedTimeOffsAmountDto ->
                    holidaysObservable
                            .map({ holidaysList: List<Calendar> ->
                                remainedTimeOffsAmountDtoReady.holidays.addAll(holidaysList)
                                remainedTimeOffsAmountDtoReady
                            })
                })
    }

    private fun map(calendarInfoList: List<HolidayDate>): List<Calendar> {
        val holidays: MutableList<Calendar> = mutableListOf()

        calendarInfoList.forEach {
            val date: Date = DateUtil.parseStringDate(it.date)
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = date.time

            holidays.add(calendar)
        }

        return holidays
    }

    private fun processResult(remainedTimeOffsAmountDto: RemainedTimeOffsAmountDto, totalIllness: Int?, totalVacation: Int?, totalDayOffs: Int?): RemainedTimeOffsAmountDto {
        remainedTimeOffsAmountDto.map.put(TimeOffType.ILLNESS, totalIllness)
        remainedTimeOffsAmountDto.map.put(TimeOffType.VACATION, totalVacation)
        remainedTimeOffsAmountDto.map.put(TimeOffType.DAYOFF, totalDayOffs)
        return remainedTimeOffsAmountDto
    }
}