package co.techmagic.hr.presentation.time_tracker.info

import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.time_tracker.DateTimeProvider
import com.techmagic.viper.base.BasePresenter

public class HrAppTimeInfoPresenter(
        private val dateTimeProvider: DateTimeProvider,
        private val timeReportRepository: TimeReportRepository) :
        BasePresenter<TimeInfoView, TimeInfoRouter>(), TimeInfoPresenter {


}