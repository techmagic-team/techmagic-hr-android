package co.techmagic.hr.presentation.mvp.presenter

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl
import co.techmagic.hr.data.request.TimeOffAllRequest
import co.techmagic.hr.domain.interactor.employee.GetAllTimeOffs
import co.techmagic.hr.domain.pojo.AllTimeOffsDto
import co.techmagic.hr.presentation.DefaultSubscriber
import co.techmagic.hr.presentation.mvp.view.RequestTimeOffView
import java.util.*

/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffPresenter : BasePresenter<RequestTimeOffView>() {

    var getAllTimeOffs : GetAllTimeOffs = GetAllTimeOffs(EmployeeRepositoryImpl())

    var dateFrom: Calendar = Calendar.getInstance()
        private set

    var dateTo: Calendar = Calendar.getInstance()
        private set

    var timeOffType: TimeOffType? = null
        private set

    fun loadTimeOffDataData() {
        view.showProgress()


//        val request = TimeOffAllRequest(dateFrom.timeInMillis, dateTo.timeInMillis)
//        getAllTimeOffs.execute(request, object : DefaultSubscriber<AllTimeOffsDto>() {
//            override fun onNext(allTimeOffsDto: AllTimeOffsDto) {
//                view?.showTimeOffsData(allTimeOffsDto)
//            }
//
//            override fun onError(e: Throwable) {
//                super.onError(e)
//                view?.showTimeOffsDataError()
//                view?.hideProgress()
//            }
//        })
    }

    override fun onViewDetached() {
        super.onViewDetached()
        getAllTimeOffs.unsubscribe()
    }

    fun onFromDateClicked() {
        view?.hideProgress()
        view?.showDatePicker(dateFrom, dateTo, isDateFromPicker = true)
    }

    fun onToDateClicked() {
        view?.hideProgress()
        view?.showDatePicker(dateFrom, dateTo, isDateFromPicker = false)
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
        dateFrom.set(Calendar.YEAR, year)
        dateFrom.set(Calendar.MONTH, month)
        dateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    fun onToDateSet(year: Int, month: Int, dayOfMonth: Int) {
        dateFrom.set(Calendar.YEAR, year)
        dateFrom.set(Calendar.MONTH, month)
        dateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    fun onRequestButtonClicked() {

    }
}