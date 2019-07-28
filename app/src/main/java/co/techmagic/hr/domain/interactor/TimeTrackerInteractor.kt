package co.techmagic.hr.domain.interactor

import co.techmagic.hr.data.repository.time_tracker.ITimeTrackerDataSource
import co.techmagic.hr.device.time_tracker.tracker_service.TimeTracker

class TimeTrackerInteractor(timeTrackerDataSource: ITimeTrackerDataSource): TimeTracker by timeTrackerDataSource