package co.techmagic.hr.device.time_tracker.tracker_service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class HrAppTimeTrackerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return HrAppTimeTrackerBinder()
    }
}