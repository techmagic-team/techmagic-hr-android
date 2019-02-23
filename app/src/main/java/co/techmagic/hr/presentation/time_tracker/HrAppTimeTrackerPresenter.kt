package co.techmagic.hr.presentation.time_tracker

import android.os.Handler
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.ui.manager.quotes.QuotesManager
import com.techmagic.viper.Router
import com.techmagic.viper.base.BasePresenter

class HrAppTimeTrackerPresenter(
        val timeReportRepository: TimeReportRepository,
        val quotesManager: QuotesManager
) : BasePresenter<TimeTrackerView, Router>(), TimeTrackerPresenter {

    init {
        Handler().postDelayed({displayRandomQuote()}, 1000)
    }

    private fun displayRandomQuote(){
        view?.showEmptyMessage(quotesManager.getRandomFormatedQuote())
    }

}