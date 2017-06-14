package co.techmagic.hr.presentation.mvp.presenter

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.entity.DateFrom
import co.techmagic.hr.data.entity.DateTo
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl
import co.techmagic.hr.data.request.RemainedTimeOffRequest
import co.techmagic.hr.domain.interactor.employee.GetRemainedTimeOffs
import co.techmagic.hr.domain.interactor.employee.GetUserPeriods
import co.techmagic.hr.domain.pojo.DatePeriodDto
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto
import co.techmagic.hr.presentation.DefaultSubscriber
import co.techmagic.hr.presentation.mvp.view.RequestTimeOffView
import co.techmagic.hr.presentation.util.SharedPreferencesUtil
import java.util.*

/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffPresenter : BasePresenter<RequestTimeOffView>() {

    var getAllTimeOffs: GetRemainedTimeOffs = GetRemainedTimeOffs(EmployeeRepositoryImpl())
    var getUserPeriods: GetUserPeriods = GetUserPeriods(EmployeeRepositoryImpl())

    var userPeriods: List<DatePeriodDto>? = null
        private set

    var requestTimeOffDateFrom: Calendar = Calendar.getInstance()
        private set

    var requestTimeOffDateTo: Calendar = Calendar.getInstance()
        private set

    var timeOffType: TimeOffType? = null
        private set

    var remainedDays: RemainedTimeOffsAmountDto? = null
        private set

    private val userId: String = SharedPreferencesUtil.readUser().id

    fun loadData() {
        loadTimePeriods()
    }

    override fun onViewDetached() {
        super.onViewDetached()
        getAllTimeOffs.unsubscribe()
    }

    private fun loadTimePeriods() {
        getUserPeriods.execute(userId, object : DefaultSubscriber<List<DatePeriodDto>>() {
            override fun onNext(datePeriods: List<DatePeriodDto>?) {
                this@RequestTimeOffPresenter.userPeriods = datePeriods

                if (datePeriods != null) {
                    loadTimeOffData(datePeriods[0])
                    view?.showPeriods(datePeriods)
                }
            }

            override fun onError(e: Throwable?) {
                super.onError(e)
                view?.showTimeOffsDataError()
                view?.hideProgress()
            }
        })
    }

    private fun loadTimeOffData(datePeriodDto: DatePeriodDto) {
        val remainedTimeOffRequest: RemainedTimeOffRequest = RemainedTimeOffRequest(userId, DateFrom(datePeriodDto.dateFrom.time), DateTo(datePeriodDto.dateTo.time))
        getAllTimeOffs.execute(remainedTimeOffRequest, object : DefaultSubscriber<RemainedTimeOffsAmountDto>() {
            override fun onNext(remainedDays: RemainedTimeOffsAmountDto) {
                this@RequestTimeOffPresenter.remainedDays = remainedDays
                view?.hideProgress()
                view?.showTimeOffsData()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view?.showTimeOffsDataError()
                view?.hideProgress()
            }
        })
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
}