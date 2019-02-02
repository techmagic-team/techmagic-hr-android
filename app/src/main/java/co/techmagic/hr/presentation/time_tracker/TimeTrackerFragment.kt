package co.techmagic.hr.presentation.time_tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.techmagic.hr.R
import com.techmagic.viper.base.BaseViewFragment

class TimeTrackerFragment : BaseViewFragment<TimeTrackerPresenter>(), TimeTrackerView {
    companion object {
        fun newInstance(): TimeTrackerFragment = TimeTrackerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_tracker, container, false)
    }
}