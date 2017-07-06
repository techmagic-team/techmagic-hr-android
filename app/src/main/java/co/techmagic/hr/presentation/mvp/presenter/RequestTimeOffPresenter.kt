package co.techmagic.hr.presentation.mvp.presenter

import android.util.Log
import co.techmagic.hr.common.Role
import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.entity.UserProfile
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl
import co.techmagic.hr.data.repository.UserRepositoryImpl
import co.techmagic.hr.data.request.GetMyProfileRequest
import co.techmagic.hr.domain.interactor.employee.DeleteTimeOff
import co.techmagic.hr.domain.interactor.employee.GetTimeOffsByUser
import co.techmagic.hr.domain.interactor.employee.GetUserPeriods
import co.techmagic.hr.domain.interactor.employee.RequestTimeOff
import co.techmagic.hr.domain.interactor.user.GetUserProfile
import co.techmagic.hr.domain.pojo.*
import co.techmagic.hr.domain.repository.IUserRepository
import co.techmagic.hr.presentation.DefaultSubscriber
import co.techmagic.hr.presentation.mvp.view.RequestTimeOffView
import co.techmagic.hr.presentation.pojo.AvailableTimeOffsData
import co.techmagic.hr.presentation.pojo.WorkingPeriod
import co.techmagic.hr.presentation.util.DateUtil
import co.techmagic.hr.presentation.util.SharedPreferencesUtil
import java.util.*

/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffPresenter : BasePresenter<RequestTimeOffView>() {
    private val userRepository: IUserRepository = UserRepositoryImpl()
    private val employeeRepository: EmployeeRepositoryImpl = EmployeeRepositoryImpl()

    private val deleteRequestedTimeOff: DeleteTimeOff = DeleteTimeOff(employeeRepository)
    private var getUserPeriods: GetUserPeriods = GetUserPeriods(employeeRepository)
    private var requestTimeOff: RequestTimeOff = RequestTimeOff(employeeRepository)
    private var getTimeOffsByUser: GetTimeOffsByUser = GetTimeOffsByUser(employeeRepository)
    private val getUserProfile: GetUserProfile = GetUserProfile(userRepository)

    private var userProfile: UserProfile? = null
    private val userId: String = SharedPreferencesUtil.readUser().id
    private val userRole: Role = Role.getRoleByCode(SharedPreferencesUtil.readUser().role)
    private var usedTimeOffs: UsedTimeOffsByUserDto? = null

    var selectedTimeOffType: TimeOffType? = null
        private set

    var availableTimeOffsData: AvailableTimeOffsData? = null
        private set

    var requestTimeOffDateFrom: Calendar = Calendar.getInstance()
        private set

    var requestTimeOffDateTo: Calendar = Calendar.getInstance()
        private set

    lateinit var selectedPeriod: WorkingPeriod
        private set

    companion object {
        private val TAG: String = RequestTimeOffPresenter.toString()

    }

    override fun onViewDetached() {
        super.onViewDetached()
        getUserPeriods.unsubscribe()
        requestTimeOff.unsubscribe()
        getTimeOffsByUser.unsubscribe()
        deleteRequestedTimeOff.unsubscribe()
        getUserProfile.unsubscribe()
    }

    fun loadData() {
        view?.showProgress()
        val getProfileRequest = GetMyProfileRequest(userId)
        getUserProfile.execute(getProfileRequest, object : DefaultSubscriber<UserProfile>() {
            override fun onNext(userProfile: UserProfile?) {
                view?.hideProgress()
                this@RequestTimeOffPresenter.userProfile = userProfile

                loadAvailableDays()
            }

            override fun onError(e: Throwable?) {
                super.onError(e)
                view?.hideProgress()
                view?.showUserProfileError()
            }
        })
    }

    fun loadAvailableDays() {
        view?.showProgress()
        getUserPeriods.execute(userId, object : DefaultSubscriber<AvailableTimeOffsData>() {
            override fun onNext(timeOffsData: AvailableTimeOffsData?) {
                this@RequestTimeOffPresenter.availableTimeOffsData = timeOffsData

                view?.hideProgress()
                if (timeOffsData != null) {
                    val periodPair: Pair<WorkingPeriod, WorkingPeriod> = Pair(getPeriod(true), getPeriod(false))

                    view?.showUserPeriods(periodPair)
                    selectedPeriod = periodPair.first
                }

                loadRequestedTimeOffs()
            }

            override fun onError(e: Throwable?) {
                super.onError(e)
                view?.hideProgress()
                view?.showTimeOffsDataError()
            }
        })
    }

    fun onFromDateClicked() {
        val bounds: Pair<Calendar, Calendar> = getBoundsForPicker()

        view?.hideProgress()
        view?.showDatePicker(bounds.first, bounds.second, isDateFromPicker = true, allowPastDateSelection = userRole == Role.ROLE_ADMIN)
    }

    fun onToDateClicked() {
        val bounds: Pair<Calendar, Calendar> = getBoundsForPicker()

        view?.hideProgress()
        view?.showDatePicker(bounds.first, bounds.second, isDateFromPicker = false, allowPastDateSelection = userRole == Role.ROLE_ADMIN)
    }

    fun onTimeOffTypeClicked() {
        view?.hideProgress()
        view?.showTimeOffsDialog()
    }

    fun onTimeOffTypeSelected(timeOffType: TimeOffType) {
        this.selectedTimeOffType = timeOffType
        view?.selectTimeOff(timeOffType)
        view?.enableDatePickers()
        view?.enableRequestButton()

        showTimeOffsAvailableDays()
        showRequestedTimeOffs()

        if (selectedTimeOffType == TimeOffType.DAYOFF) {
            val vacationsAvailable: Int = availableTimeOffsData!!.timeOffsMap[selectedPeriod]!!.map[TimeOffType.VACATION]!!

            if (selectedTimeOffType == TimeOffType.DAYOFF && vacationsAvailable > 0) {
                view?.showCantRequestDayOffBecauseOfVacations(Role.ROLE_USER)

                if (userRole == Role.ROLE_USER) {
                    view?.disableDatePickers()
                    view?.disableRequestButton()
                }
            }
        }
    }

    fun onFromDateSet(year: Int, month: Int, dayOfMonth: Int) {
        requestTimeOffDateFrom.timeZone = TimeZone.getTimeZone("UTC")
        requestTimeOffDateFrom.set(Calendar.YEAR, year)
        requestTimeOffDateFrom.set(Calendar.MONTH, month)
        requestTimeOffDateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        requestTimeOffDateFrom.set(Calendar.HOUR_OF_DAY, 0)
        requestTimeOffDateFrom.set(Calendar.MINUTE, 0)
        requestTimeOffDateFrom.set(Calendar.SECOND, 0)
        requestTimeOffDateFrom.set(Calendar.MILLISECOND, 0)
    }

    fun onToDateSet(year: Int, month: Int, dayOfMonth: Int) {
        requestTimeOffDateFrom.timeZone = TimeZone.getTimeZone("UTC")
        requestTimeOffDateTo.set(Calendar.YEAR, year)
        requestTimeOffDateTo.set(Calendar.MONTH, month)
        requestTimeOffDateTo.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        requestTimeOffDateTo.set(Calendar.HOUR_OF_DAY, 23)
        requestTimeOffDateTo.set(Calendar.MINUTE, 59)
        requestTimeOffDateTo.set(Calendar.SECOND, 59)
        requestTimeOffDateTo.set(Calendar.MILLISECOND, 999)
    }

    fun onRequestButtonClicked() {
        if (isInputDataValid()) {
            if (isValidTimeOffAvailability()) {
                if (!isOverlapWithRequestedTimeOff()) {
                    view!!.disableRequestButton()
                    val requestTimeOffDto: RequestTimeOffDto = RequestTimeOffDto(requestTimeOffDateFrom.timeInMillis, requestTimeOffDateTo.timeInMillis, userId, selectedTimeOffType!!, userRole == Role.ROLE_ADMIN)

                    view?.showProgress()

                    requestTimeOff.execute(requestTimeOffDto, object : DefaultSubscriber<RequestedTimeOffDto>() {
                        override fun onNext(requestedTimeOffDto: RequestedTimeOffDto?) {
                            view?.hideProgress()
                            view!!.enableRequestButton()
                            view?.showRequestTimeOffSuccess()
                            loadAvailableDays()
                        }

                        override fun onError(e: Throwable?) {
                            Log.e(TAG, e?.message, e)
                            view?.hideProgress()
                            view!!.enableRequestButton()
                            view?.showRequestTimeOffError()
                            loadAvailableDays()
                        }
                    })
                } else {
                    view!!.showOverlapErrorMessage()
                }
            }

        } else {
            view!!.showInvalidInputData()
        }
    }

    fun onFirstPeriodSelected() {
        selectedPeriod = getPeriod(true)
        showRequestedTimeOffs()
        showTimeOffsAvailableDays()
    }

    fun onSecondPeriodSelected() {
        selectedPeriod = getPeriod(false)
        showRequestedTimeOffs()
        return showTimeOffsAvailableDays()
    }

    fun canBeDeleted(requestedTimeOffDto: RequestedTimeOffDto): Boolean {
        if (userRole == Role.ROLE_ADMIN || userRole == Role.ROLE_HR) {
            return true
        } else {
            val today: Calendar = Calendar.getInstance()
            val timeOffStart: Calendar = Calendar.getInstance()
            timeOffStart.timeInMillis = requestedTimeOffDto.dateFrom.time
            timeOffStart.time

            return timeOffStart.after(today)
        }
    }

    fun removeRequestedTimeOff(requestedTimeOffDto: RequestedTimeOffDto) {
        view?.showProgress()
        deleteRequestedTimeOff.execute(requestedTimeOffDto, object : DefaultSubscriber<Void>() {
            override fun onNext(requestedTimeOffDto: Void?) {
                view?.hideProgress()
                loadAvailableDays()
            }

            override fun onError(e: Throwable?) {
                view?.hideProgress()
                view?.showErrorDeletingRequestedTimeOff()
            }
        })
    }

    fun getMinDatePickerDate(): Calendar {
        val today: Calendar = Calendar.getInstance()
        val periodStart: Calendar = Calendar.getInstance()
        periodStart.time = selectedPeriod.startDate

        if (periodStart.after(today)) {
            return periodStart
        } else {
            if (userRole == Role.ROLE_ADMIN || userRole == Role.ROLE_HR) {
                return periodStart
            } else {
                return today
            }
        }
    }

    fun getMaxDatePickerDate(): Calendar {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = selectedPeriod.endDate

        return calendar
    }

    fun getHolidays(): List<Calendar> {
        if (availableTimeOffsData != null) {
            val weekends: MutableList<Calendar> = getWeekends()
            val holidays: MutableList<Calendar> = availableTimeOffsData!!.timeOffsMap[selectedPeriod]!!.holidays
            val requestedTimeOffs = getRequestedTimeOffs()

            weekends.addAll(holidays)
            weekends.addAll(requestedTimeOffs)

            weekends.forEach { it.timeZone = TimeZone.getDefault() }

            return weekends

        } else {
            return listOf()
        }
    }

    private fun isOverlapWithRequestedTimeOff(): Boolean {
        val allTimeOffs: List<RequestedTimeOffDto> = usedTimeOffs!!.getAllTimeOffs()
        for (requestedTimeOff in allTimeOffs) {
            val alreadyRequestedFrom: Calendar = Calendar.getInstance()
            val alreadyRequestedTo: Calendar = Calendar.getInstance()

            alreadyRequestedFrom.timeInMillis = requestedTimeOff.dateFrom.time
            alreadyRequestedTo.timeInMillis = requestedTimeOff.dateTo.time

            if (DateUtil.isOverLapping(alreadyRequestedFrom, alreadyRequestedTo, requestTimeOffDateFrom, requestTimeOffDateTo)) {
                return true
            }
        }

        return false
    }

    private fun getPeriod(shouldBeEarlier: Boolean): WorkingPeriod {
        val period1: WorkingPeriod = availableTimeOffsData!!.timeOffsMap.keys.elementAt(0)
        val period2: WorkingPeriod = availableTimeOffsData!!.timeOffsMap.keys.elementAt(1)

        if (period1.startDate.before(period2.startDate)) {
            return if (shouldBeEarlier) period1 else period2
        } else {
            return if (shouldBeEarlier) period2 else period1
        }
    }

    private fun isHoliday(day: Calendar): Boolean {
        val holidays: MutableList<Calendar> = availableTimeOffsData!!.timeOffsMap[selectedPeriod]!!.holidays

        return holidays.any { calendar: Calendar ->
            (calendar.get(Calendar.YEAR) == day.get(Calendar.YEAR))
                    && (calendar.get(Calendar.MONTH) == day.get(Calendar.MONTH))
                    && (calendar.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH))
        }
    }

    private fun getBoundsForPicker(): Pair<Calendar, Calendar> {
        val calendarFrom: Calendar = Calendar.getInstance()
        calendarFrom.timeInMillis = selectedPeriod.startDate.time

        val calendarTo: Calendar = Calendar.getInstance()
        calendarTo.timeInMillis = selectedPeriod.endDate.time

        return Pair(calendarFrom, calendarTo)
    }

    private fun loadRequestedTimeOffs() {
        if (availableTimeOffsData != null && availableTimeOffsData!!.timeOffsMap.keys.size > 0) {
            val timeOffRequestByUser: TimeOffRequestByUserAllPeriods = TimeOffRequestByUserAllPeriods(userId, availableTimeOffsData!!.timeOffsMap.keys)

            getTimeOffsByUser.execute(timeOffRequestByUser, object : DefaultSubscriber<UsedTimeOffsByUserDto>() {
                override fun onNext(usedTimeOffsByUserDto: UsedTimeOffsByUserDto?) {
                    view?.hideProgress()
                    if (usedTimeOffsByUserDto != null) {
                        usedTimeOffs = usedTimeOffsByUserDto

                        showRequestedTimeOffs()
                    }
                }

                override fun onError(e: Throwable?) {
                    view?.hideProgress()
                    view?.showErrorLoadingRequestedTimeOffs()
                }
            })
        }
    }

    private fun isInputDataValid(): Boolean {
        val today: Calendar = Calendar.getInstance()
        val periodStart: Calendar = Calendar.getInstance()
        periodStart.timeInMillis = selectedPeriod.startDate.time
        periodStart.set(Calendar.HOUR_OF_DAY, 0)
        periodStart.set(Calendar.MINUTE, 0)
        periodStart.set(Calendar.SECOND, 0)

        val periodEnd: Calendar = Calendar.getInstance()
        periodEnd.timeInMillis = selectedPeriod.endDate.time

        if (availableTimeOffsData != null && selectedTimeOffType != null) {
            val availableDays: Int? = availableTimeOffsData!!.timeOffsMap[selectedPeriod]!!.map[selectedTimeOffType]
            if (availableDays == null || availableDays <= 0) {
                return false
            }

            if (((requestTimeOffDateFrom == periodStart) || requestTimeOffDateFrom.after(periodStart))
                    && requestTimeOffDateFrom.before(periodEnd)
                    && requestTimeOffDateTo.after(periodStart)
                    && ((requestTimeOffDateTo == periodEnd) || requestTimeOffDateTo.before(periodEnd))
                    && (requestTimeOffDateTo.after(requestTimeOffDateFrom)
                    || (requestTimeOffDateTo.get(Calendar.YEAR) == requestTimeOffDateFrom.get(Calendar.YEAR)
                    && (requestTimeOffDateTo.get(Calendar.MONTH) == requestTimeOffDateFrom.get(Calendar.MONTH))
                    && (requestTimeOffDateTo.get(Calendar.DAY_OF_MONTH) == requestTimeOffDateFrom.get(Calendar.DAY_OF_MONTH))))) {

                if (userRole == Role.ROLE_ADMIN || userRole == Role.ROLE_HR) {
                    return true
                } else {
                    return requestTimeOffDateFrom.after(today) || DateUtil.isSameDate(requestTimeOffDateFrom, today)
                }
            }
        }

        return false
    }

    private fun showRequestedTimeOffs() {
        if (usedTimeOffs != null && selectedPeriod != null && selectedTimeOffType != null) {
            view?.showRequestedTimeOffs(usedTimeOffs!!.timeOffMaps[selectedPeriod]!![selectedTimeOffType!!]!!)
        }
    }

    private fun showTimeOffsAvailableDays() {
        if (availableTimeOffsData != null) {
            val remainedTimeOffs: RemainedTimeOffsAmountDto? = availableTimeOffsData?.timeOffsMap!![selectedPeriod]

            if (selectedTimeOffType == null) {
                selectedTimeOffType = TimeOffType.VACATION
            }

            view?.showTimeOffsData(remainedTimeOffs!!.map[selectedTimeOffType])
        }
    }

    private fun getWeekends(): MutableList<Calendar> {
        val weekendsList: MutableList<Calendar> = mutableListOf()
        val calendarMinDate: Calendar = getMinDatePickerDate()
        val calendarMaxDate: Calendar = getMaxDatePickerDate()

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = calendarMinDate.timeInMillis

        while (calendar.before(calendarMaxDate)) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                val currentDay: Calendar = calendar.clone() as Calendar
                weekendsList.add(currentDay)
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return weekendsList
    }

    private fun getRequestedTimeOffs(): MutableList<Calendar> {
        val timeOffs: MutableList<Calendar> = mutableListOf()
        if (usedTimeOffs != null) {
            val requestedTimeOffMaps: MutableMap<TimeOffType, MutableList<RequestedTimeOffDto>>? = usedTimeOffs!!.timeOffMaps[selectedPeriod]
            if (requestedTimeOffMaps != null) {
                for (list in requestedTimeOffMaps.values) {
                    for (requestedTimeOff in list) {
                        val startDate: Calendar = Calendar.getInstance()
                        val endDate: Calendar = Calendar.getInstance()

                        startDate.timeInMillis = requestedTimeOff.dateFrom.time
                        endDate.timeInMillis = requestedTimeOff.dateTo.time

                        val calendar: Calendar = Calendar.getInstance()
                        calendar.timeInMillis = startDate.timeInMillis

                        while (calendar.before(endDate) || DateUtil.isSameDate(calendar, endDate)) {

                            val currentDay: Calendar = Calendar.getInstance()
                            currentDay.timeInMillis = calendar.timeInMillis

                            timeOffs.add(currentDay)
                            calendar.add(Calendar.DAY_OF_YEAR, 1)
                        }
                    }
                }
            }
        }

        return timeOffs
    }

    private fun isValidTimeOffAvailability(): Boolean {
        val requestedTimeOffDaysAmount: Int = calculateWorkingDaysAmount()

        val availableDays: Int = when (selectedTimeOffType) {
            TimeOffType.DAYOFF -> availableTimeOffsData!!.timeOffsMap[selectedPeriod]!!.map[TimeOffType.DAYOFF]!!
            TimeOffType.VACATION -> availableTimeOffsData!!.timeOffsMap[selectedPeriod]!!.map[TimeOffType.VACATION]!!
            TimeOffType.ILLNESS -> availableTimeOffsData!!.timeOffsMap[selectedPeriod]!!.map[TimeOffType.ILLNESS]!!
            else -> 0
        }

        if (requestedTimeOffDaysAmount > availableDays) {
            view?.showNotEnoughDaysAvailable()
            return false
        } else {
            return true
        }
    }

    private fun calculateWorkingDaysAmount(): Int {
        var daysAmount = 0
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = requestTimeOffDateFrom.timeInMillis

        while (calendar.before(requestTimeOffDateTo)
                || (calendar.get(Calendar.YEAR) == requestTimeOffDateTo.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == requestTimeOffDateTo.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == requestTimeOffDateTo.get(Calendar.DAY_OF_MONTH))) {

            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                    && !isHoliday(calendar)) {
                daysAmount++
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return daysAmount
    }
}