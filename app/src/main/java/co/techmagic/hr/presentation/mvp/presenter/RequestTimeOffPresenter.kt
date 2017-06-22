package co.techmagic.hr.presentation.mvp.presenter

import android.util.Log
import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl
import co.techmagic.hr.domain.interactor.employee.GetTimeOffsByUser
import co.techmagic.hr.domain.interactor.employee.GetUserPeriods
import co.techmagic.hr.domain.interactor.employee.RequestTimeOff
import co.techmagic.hr.domain.pojo.*
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
    private val employeeRepository: EmployeeRepositoryImpl = EmployeeRepositoryImpl()
    private var getUserPeriods: GetUserPeriods = GetUserPeriods(employeeRepository)
    private var requestTimeOff: RequestTimeOff = RequestTimeOff(employeeRepository)
    private var getTimeOffsByUser: GetTimeOffsByUser = GetTimeOffsByUser(employeeRepository)

    var availableTimeOffsData: AvailableTimeOffsData? = null
        private set

    var requestTimeOffDateFrom: Calendar = Calendar.getInstance()
        private set

    var requestTimeOffDateTo: Calendar = Calendar.getInstance()
        private set

    lateinit var selectedPeriod: PeriodPair
        private set

    private var selectedTimeOffType: TimeOffType? = null

    private val userId: String = SharedPreferencesUtil.readUser().id

    private var usedTimeOffs: UsedTimeOffsByUserDto? = null

    companion object {
        private val TAG: String = RequestTimeOffPresenter.toString()

    }

    fun loadData() {
        getUserPeriods.execute(userId, object : DefaultSubscriber<AvailableTimeOffsData>() {
            override fun onNext(timeOffsData: AvailableTimeOffsData?) {
                this@RequestTimeOffPresenter.availableTimeOffsData = timeOffsData

                view?.hideProgress()
                if (timeOffsData != null) {
                    val periodPairsList: List<PeriodPair> = timeOffsData.timeOffsMap.keys.toList()
                    view?.showUserPeriods(periodPairsList)
                    selectedPeriod = periodPairsList[0]
                }

                loadUserTimeOffs()
            }

            override fun onError(e: Throwable?) {
                super.onError(e)
                view?.showTimeOffsDataError()
                view?.hideProgress()
            }
        })
    }

    private fun loadUserTimeOffs() {
        if (availableTimeOffsData != null && availableTimeOffsData!!.timeOffsMap.keys.size > 0) {
            val timeOffRequestByUser: TimeOffRequestByUserAllPeriods = TimeOffRequestByUserAllPeriods(userId, availableTimeOffsData!!.timeOffsMap.keys)

            getTimeOffsByUser.execute(timeOffRequestByUser, object : DefaultSubscriber<UsedTimeOffsByUserDto>() {
                override fun onNext(usedTimeOffsByUserDto: UsedTimeOffsByUserDto?) {
                    if (usedTimeOffsByUserDto != null) {
                        usedTimeOffs = usedTimeOffsByUserDto
                        view?.hideProgress()

                        showRequestedTimeOffs()
                    }
                }

                override fun onError(e: Throwable?) {
                    view?.showErrorLoadingRequestedTimeOffs()
                    view?.hideProgress()
                }
            })
        }
    }

    override fun onViewDetached() {
        super.onViewDetached()
        getUserPeriods.unsubscribe()
    }

    fun onFromDateClicked() {
        view?.hideProgress()
        view?.showDatePicker(requestTimeOffDateFrom, requestTimeOffDateTo, isDateFromPicker = true)
    }

    fun onToDateClicked() {
        view?.hideProgress()
        view?.showDatePicker(requestTimeOffDateFrom, requestTimeOffDateTo, isDateFromPicker = false)
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
        if (inputDataValid()) {
            val requestTimeOffDto: RequestTimeOffDto = RequestTimeOffDto(requestTimeOffDateFrom.timeInMillis, requestTimeOffDateTo.timeInMillis, userId, selectedTimeOffType!!)

            view?.showProgress()

            requestTimeOff.execute(requestTimeOffDto, object : DefaultSubscriber<RequestedTimeOffDto>() {
                override fun onNext(t: RequestedTimeOffDto?) {
                    view?.hideProgress()
                    view?.showRequestTimeOffSuccess()
                    loadData()
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

    private fun inputDataValid(): Boolean {
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
                    && ((requestTimeOffDateTo == periodEndCalendar) || requestTimeOffDateTo.before(periodEndCalendar))) {

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