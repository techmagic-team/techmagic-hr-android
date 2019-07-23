package co.techmagic.hr.device.time_tracker.tracker_service

import android.os.Binder
class HrAppTimeTrackerBinder(val timeTracker : IHrAppTimeTracker) : Binder() {

}
