package co.techmagic.hr.presentation.mvp.presenter

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.presentation.mvp.view.RequestTimeOffView
import java.util.*

/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffPresenter : BasePresenter<RequestTimeOffView>() {

    var dateFrom: Calendar = Calendar.getInstance()
        private set

    var dateTo: Calendar = Calendar.getInstance()
        private set

    var timeOffType: TimeOffType? = null
        private set

    fun loadData() {

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
}