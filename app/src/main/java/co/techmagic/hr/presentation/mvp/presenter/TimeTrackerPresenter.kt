package co.techmagic.hr.presentation.mvp.presenter

import co.techmagic.hr.presentation.mvp.view.CalendarView
import co.techmagic.hr.presentation.mvp.view.TimeTrackerView
import co.techmagic.hr.presentation.pojo.TimeReportViewModel

class TimeTrackerPresenter: BasePresenter<TimeTrackerView>() {

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