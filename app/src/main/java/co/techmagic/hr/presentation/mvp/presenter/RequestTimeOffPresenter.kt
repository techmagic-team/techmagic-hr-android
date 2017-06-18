package co.techmagic.hr.presentation.mvp.presenter

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl
import co.techmagic.hr.domain.interactor.employee.GetRemainedTimeOffs
import co.techmagic.hr.domain.interactor.employee.GetUserPeriods
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto
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
    var getUserPeriods: GetUserPeriods = GetUserPeriods(EmployeeRepositoryImpl())

    var availableTimeOffsData: AvailableTimeOffsData? = null
        private set

    var requestTimeOffDateFrom: Calendar = Calendar.getInstance()
        private set

    var requestTimeOffDateTo: Calendar = Calendar.getInstance()
        private set

    private var timeOffType: TimeOffType? = null

    private val userId: String = SharedPreferencesUtil.readUser().id

    private var selectedPeriod: PeriodPair? = null

    fun loadData() {
        getUserPeriods.execute(userId, object : DefaultSubscriber<AvailableTimeOffsData>() {
            override fun onNext(timeOffsData: AvailableTimeOffsData?) {
                this@RequestTimeOffPresenter.availableTimeOffsData = timeOffsData

                if (timeOffsData != null) {
                    val periodPairsList: List<PeriodPair> = timeOffsData.timeOffsMap.keys.toList()
                    view?.showUserPeriods(periodPairsList)
                }
            }

            override fun onError(e: Throwable?) {
                super.onError(e)
                view?.showTimeOffsDataError()
                view?.hideProgress()
            }
        })
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
        this.timeOffType = timeOffType
        view?.selectTimeOff(timeOffType)
    }

    fun onFromDateSet(year: Int, month: Int, dayOfMonth: Int) {
        requestTimeOffDateFrom.set(Calendar.YEAR, year)
        requestTimeOffDateFrom.set(Calendar.MONTH, month)
        requestTimeOffDateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    fun onToDateSet(year: Int, month: Int, dayOfMonth: Int) {
        requestTimeOffDateFrom.set(Calendar.YEAR, year)
        requestTimeOffDateFrom.set(Calendar.MONTH, month)
        requestTimeOffDateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    fun onRequestButtonClicked() {

    }

    fun onFirstPeriodSelected() {
        selectedPeriod = availableTimeOffsData!!.timeOffsMap.keys.elementAt(0)
        return showTimeOffsData()
    }

    fun onSecondPeriodSelected() {
        selectedPeriod = availableTimeOffsData!!.timeOffsMap.keys.elementAt(1)
        return showTimeOffsData()
    }

    private fun showTimeOffsData() {
        if (availableTimeOffsData != null) {
            val remainedTimeOffs: RemainedTimeOffsAmountDto? = availableTimeOffsData?.timeOffsMap!![selectedPeriod]

            if (timeOffType == null) {
                timeOffType = TimeOffType.VACATION
            }

            view?.showTimeOffsData(remainedTimeOffs!!.map[timeOffType])
        }
    }
}