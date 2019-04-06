package co.techmagic.hr.presentation.time_tracker.time_report_detail

import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppTimeReportDetailPresenter : BasePresenter<TimeReportDetailView, ITimeReportDetailRouter>(),
        TimeReportDetailPresenter {

    override fun onViewResumed() {
        super.onViewResumed()
        view?.showDate(getFormatedDate())
    }

    override fun changeProjectClicked() {
        router?.openSelectProject("12421", Calendar.getInstance().time)
    }

    override fun changeTaskClicked() {
        router?.openSelectTask("fq9qw9")
    }

    override fun descriptionChanged(description: String) {
    }

    override fun addFifteenMinutesClicked() {
    }

    override fun addThirtyMinutesClicked() {
    }

    override fun addOneHourClicked() {
    }

    override fun addEightHoursClicked() {
    }

    override fun increaseTimeClicked() {
    }

    override fun reduceTimeClicked() {
    }

    override fun startTimerClicked() {
    }

    override fun saveClicked() {
    }

    override fun deleteClicked() {

    }

    private fun getFormatedDate() = "Not implemented"
}