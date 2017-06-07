package co.techmagic.hr.presentation.mvp.presenter

import co.techmagic.hr.presentation.mvp.view.RequestTimeOffView

/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffPresenter : BasePresenter<RequestTimeOffView>() {

    fun loadData() {
        view.showProgress("loading...")
    }
}