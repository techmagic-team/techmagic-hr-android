package co.techmagic.hr.domain.interactor.employee

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.request.TimeOffRequest
import co.techmagic.hr.domain.interactor.DataUseCase
import co.techmagic.hr.domain.pojo.RequestTimeOffDto
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto
import co.techmagic.hr.domain.repository.IEmployeeRepository
import rx.Observable

/**
 * Created by Roman Ursu on 6/12/17
 */
class RequestTimeOff(iEmployeeRepository: IEmployeeRepository) : DataUseCase<RequestTimeOffDto, RequestedTimeOffDto, IEmployeeRepository>(iEmployeeRepository) {

    override fun buildObservable(requestTimeOff: RequestTimeOffDto): Observable<RequestedTimeOffDto> {
        val isPaid: Boolean = getPaidInfo(requestTimeOff.timeOffType)
        val timeOffRequest: TimeOffRequest = TimeOffRequest(requestTimeOff.userId, isPaid, requestTimeOff.dateFrom, requestTimeOff.dateTo)

        return when (requestTimeOff.timeOffType) {
            TimeOffType.ILLNESS -> repository.requestIllness(timeOffRequest)
            else -> repository.requestVacation(timeOffRequest)
        }
    }

    private fun getPaidInfo(selectedTimeOffType: TimeOffType?): Boolean = when (selectedTimeOffType) {
        TimeOffType.VACATION -> true
        TimeOffType.DAYOFF -> false
        TimeOffType.ILLNESS -> true
        else -> false
    }
}