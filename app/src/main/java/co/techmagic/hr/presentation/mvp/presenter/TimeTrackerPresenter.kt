package co.techmagic.hr.presentation.mvp.presenter

import co.techmagic.hr.data.manager.impl.NetworkManagerImpl
import co.techmagic.hr.data.repository.TimeReportNetworkRepository
import co.techmagic.hr.data.store.TimeTrackerApi
import co.techmagic.hr.data.store.client.ApiClient
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.mvp.view.CalendarView

import co.techmagic.hr.presentation.mvp.view.TimeTrackerView
import co.techmagic.hr.presentation.pojo.TimeReportViewModel

class TimeTrackerPresenter : BasePresenter<TimeTrackerView>() {

    private val timeReportRepository: TimeReportRepository

    init {
        // TODO: 1/20/19 inject dependencies
        val okHttpClientClient = ApiClient.buildOkHttpClientClient()
        val retrofit = ApiClient.getRetrofit(okHttpClientClient)
        val timeTrackerApi = retrofit.create(TimeTrackerApi::class.java)
        timeReportRepository = TimeReportNetworkRepository(timeTrackerApi, NetworkManagerImpl.getNetworkManager())
    }

    public fun test(){
        val mockReports = arrayListOf<TimeReportViewModel>()
        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        true,
                        false
                )
        )

        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )

        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )

        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )

        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )
        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )
        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )
        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )
        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )
        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )
        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )
        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        false
                )
        )

        mockReports.add(
                TimeReportViewModel(
                        "asfasf",
                        "Good&Co",
                        "Good&Co",
                        "Android Development",
                        "Add tracking on main screen",
                        140,
                        false,
                        true
                )
        )

        view.showReports(mockReports)
    }
}