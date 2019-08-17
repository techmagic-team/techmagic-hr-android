package co.techmagic.hr.presentation.time_tracker.info

import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.time_tracker.DateTimeProvider
import com.techmagic.viper.base.BasePresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class HrAppTimeInfoPresenter(
        private val dateTimeProvider: DateTimeProvider,
        private val timeReportRepository: TimeReportRepository) :
        BasePresenter<TimeInfoView, TimeInfoRouter>(), TimeInfoPresenter {

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        
        view?.showLoading()
        Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.hideLoading()

                    val reports = ArrayList<TimeReportViewModel>()
                    reports.add(TimeReportViewModel("Hours today", 0, 8))
                    reports.add(TimeReportViewModel("hours yesterday", 0, 8))
                    reports.add(TimeReportViewModel("this week", 0, 8))
                    reports.add(TimeReportViewModel("previous week", 0, 8))
                    reports.add(TimeReportViewModel("this month", 0, 8))
                    reports.add(TimeReportViewModel("previous month", 0, 8))

                    view?.showReports(reports)
                }
    }
}