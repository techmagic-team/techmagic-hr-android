package co.techmagic.hr.presentation.mvp.view.impl

import android.support.v4.app.Fragment
import co.techmagic.hr.presentation.mvp.view.TimeTrackerView

open abstract class TimeTrackerViewImpl(fragment: Fragment, contentView: android.view.View): ViewImpl(fragment, contentView), TimeTrackerView {

}