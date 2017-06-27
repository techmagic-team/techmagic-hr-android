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
import co.techmagic.hr.presentation.pojo.PeriodPair
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

    lateinit var selectedPeriod: PeriodPair
        private set

    companion object {
        private val TAG: String = RequestTimeOffPresenter.toString()

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
                    val periodPairsList: List<PeriodPair> = timeOffsData.timeOffsMap.keys.toList()
                    view?.showUserPeriods(periodPairsList)
                    selectedPeriod = periodPairsList[0]
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

    override fun onViewDetached() {
        super.onViewDetached()
        getUserPeriods.unsubscribe()
        requestTimeOff.unsubscribe()
        getTimeOffsByUser.unsubscribe()
        deleteRequestedTimeOff.unsubscribe()
        getUserProfile.unsubscribe()
    }

    fun onFromDateClicked() {
        view?.hideProgress()
        view?.showDatePicker(selectedPeriod.startDate.time, selectedPeriod.endDate.time, isDateFromPicker = true, allowPastDateSelection = userRole == Role.ROLE_ADMIN)
    }

    fun onToDateClicked() {
        view?.hideProgress()
        view?.showDatePicker(selectedPeriod.startDate.time, selectedPeriod.endDate.time, isDateFromPicker = false, allowPastDateSelection = userRole == Role.ROLE_ADMIN)
    }

    fun onTimeOffTypeClicked() {
        view?.hideProgress()
        view?.showTimeOffsDialog()
    }

    fun onTimeOffTypeSelected(timeOffType: TimeOffType) {
        this.selectedTimeOffType = timeOffType
        view?.selectTimeOff(timeOffType)

        showRequestedTimeOffs()
    }

    fun onFromDateSet(year: Int, month: Int, dayOfMonth: Int) {
        requestTimeOffDateFrom.set(Calendar.YEAR, year)
        requestTimeOffDateFrom.set(Calendar.MONTH, month)
        requestTimeOffDateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    fun onToDateSet(year: Int, month: Int, dayOfMonth: Int) {
        requestTimeOffDateTo.set(Calendar.YEAR, year)
        requestTimeOffDateTo.set(Calendar.MONTH, month)
        requestTimeOffDateTo.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    fun onRequestButtonClicked() {
        if (isInputDataValid()) {
            val requestTimeOffDto: RequestTimeOffDto = RequestTimeOffDto(requestTimeOffDateFrom.timeInMillis, requestTimeOffDateTo.timeInMillis, userId, selectedTimeOffType!!, userRole == Role.ROLE_ADMIN)

            view?.showProgress()

            requestTimeOff.execute(requestTimeOffDto, object : DefaultSubscriber<RequestedTimeOffDto>() {
                override fun onNext(requestedTimeOffDto: RequestedTimeOffDto?) {
                    view?.hideProgress()
                    view?.showRequestTimeOffSuccess()
                    loadRequestedTimeOffs()
                }

                override fun onError(e: Throwable?) {
                    Log.e(TAG, e?.message, e)
                    view?.hideProgress()
                    view?.showRequestTimeOffError()
                }
            })
        } else {
            view!!.showInvalidInputData()
        }
    }

    fun onFirstPeriodSelected() {
        selectedPeriod = availableTimeOffsData!!.timeOffsMap.keys.elementAt(0)
        showRequestedTimeOffs()
        return showTimeOffsAvailableDays()
    }

    fun onSecondPeriodSelected() {
        selectedPeriod = availableTimeOffsData!!.timeOffsMap.keys.elementAt(1)
        showRequestedTimeOffs()
        return showTimeOffsAvailableDays()
    }

    fun canBeDeleted(requestedTimeOffDto: RequestedTimeOffDto): Boolean {
        val today: Calendar = Calendar.getInstance()
        val timeOffStart: Calendar = Calendar.getInstance()
        timeOffStart.timeInMillis = requestedTimeOffDto.dateFrom.time
        timeOffStart.time

        return timeOffStart.after(today)
    }

    fun removeRequestedTimeOff(requestedTimeOffDto: RequestedTimeOffDto) {
        view?.showProgress()
        deleteRequestedTimeOff.execute(requestedTimeOffDto, object : DefaultSubscriber<Void>() {
            override fun onNext(requestedTimeOffDto: Void?) {
                view?.hideProgress()
                loadRequestedTimeOffs()
            }

            override fun onError(e: Throwable?) {
                view?.hideProgress()
                view?.showErrorDeletingRequestedTimeOff()
            }
        })
    }

    private fun isInputDataValid(): Boolean {
        val periodStartCalendar: Calendar = Calendar.getInstance()
        periodStartCalendar.set(Calendar.HOUR_OF_DAY, 0)
        periodStartCalendar.set(Calendar.MINUTE, 0)
        periodStartCalendar.set(Calendar.SECOND, 0)

        val periodEndCalendar: Calendar = Calendar.getInstance()
        periodEndCalendar.timeInMillis = selectedPeriod.endDate.time

        if (availableTimeOffsData != null && selectedTimeOffType != null) {
            val availableDays: Int? = availableTimeOffsData!!.timeOffsMap[selectedPeriod]!!.map[selectedTimeOffType]
            if (availableDays == null || availableDays <= 0) {
                return false
            }

            if (((requestTimeOffDateFrom == periodStartCalendar) || requestTimeOffDateFrom.after(periodStartCalendar))
                    && requestTimeOffDateFrom.before(periodEndCalendar)
                    && requestTimeOffDateTo.after(periodStartCalendar)
                    && ((requestTimeOffDateTo == periodEndCalendar) || requestTimeOffDateTo.before(periodEndCalendar))
                    && (requestTimeOffDateTo.after(requestTimeOffDateFrom) || requestTimeOffDateTo == requestTimeOffDateFrom)) {

                return true
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
}