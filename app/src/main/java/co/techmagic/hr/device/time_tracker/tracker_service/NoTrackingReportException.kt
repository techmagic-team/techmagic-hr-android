package co.techmagic.hr.device.time_tracker.tracker_service

import java.lang.IllegalArgumentException

class NoTrackingReportException : IllegalArgumentException("No currently tracking report") {

}