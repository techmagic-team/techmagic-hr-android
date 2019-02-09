package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.domain.repository.TimeReportRepository
import com.techmagic.viper.Router
import com.techmagic.viper.base.BasePresenter

class HrAppTimeTrackerPresenter(val timeReportRepository: TimeReportRepository): BasePresenter<TimeTrackerView, Router>(), TimeTrackerPresenter{

}