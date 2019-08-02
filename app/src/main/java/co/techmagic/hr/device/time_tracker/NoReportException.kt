package co.techmagic.hr.device.time_tracker

class NoReportException(reportId: String) : IllegalArgumentException("No such report. Report id: $reportId")