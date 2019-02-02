package co.techmagic.hr.presentation.mvp.presenter

import co.techmagic.hr.data.manager.impl.NetworkManagerImpl
import co.techmagic.hr.data.repository.TimeReportNetworkRepository
import co.techmagic.hr.data.store.TimeTrackerApi
import co.techmagic.hr.data.store.client.ApiClient
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.mvp.view.CalendarView

class TimeTrackerPresenter : BasePresenter<CalendarView>() {

    private val timeReportRepository: TimeReportRepository

    init {
        // TODO: 1/20/19 inject dependencies
        val okHttpClientClient = ApiClient.buildOkHttpClientClient()
        val retrofit = ApiClient.getRetrofit(okHttpClientClient)
        val timeTrackerApi = retrofit.create(TimeTrackerApi::class.java)
        timeReportRepository = TimeReportNetworkRepository(timeTrackerApi, NetworkManagerImpl.getNetworkManager())
    }
}