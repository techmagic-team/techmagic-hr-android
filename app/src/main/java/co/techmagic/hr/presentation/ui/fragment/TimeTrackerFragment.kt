package co.techmagic.hr.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.techmagic.hr.R
import co.techmagic.hr.presentation.mvp.presenter.TimeTrackerPresenter
import co.techmagic.hr.presentation.mvp.view.TimeTrackerView
import co.techmagic.hr.presentation.mvp.view.impl.TimeTrackerViewImpl

class TimeTrackerFragment : BaseFragment<TimeTrackerView, TimeTrackerPresenter>() {

    override fun initView() = TimeTrackerViewImpl(this, activity!!.findViewById(android.R.id.content))

    override fun initPresenter() = TimeTrackerPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_time_tracker, container, false)
//        presenter.init()
        return view
    }
}