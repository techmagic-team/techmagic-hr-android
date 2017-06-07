package co.techmagic.hr.presentation.mvp.presenter

import co.techmagic.hr.presentation.mvp.view.RequestTimeOffView
import java.util.*

/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffPresenter : BasePresenter<RequestTimeOffView>() {

    private var dateFrom: Calendar = Calendar.getInstance()
    private var dateTo: Calendar = Calendar.getInstance()

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
}