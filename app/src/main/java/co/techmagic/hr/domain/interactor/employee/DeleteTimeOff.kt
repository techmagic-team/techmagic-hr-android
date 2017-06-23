package co.techmagic.hr.domain.interactor.employee

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.domain.interactor.DataUseCase
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto
import co.techmagic.hr.domain.repository.IEmployeeRepository
import rx.Observable

/**
 * Created by Roman Ursu on 6/23/17
 */
class DeleteTimeOff(iEmployeeRepository: IEmployeeRepository) : DataUseCase<RequestedTimeOffDto, Void, IEmployeeRepository>(iEmployeeRepository) {

    override fun buildObservable(requestedTimeOffDto: RequestedTimeOffDto?): Observable<Void> {
        if (requestedTimeOffDto!!.timeOffType == TimeOffType.ILLNESS) {
            return repository.deleteIllness(requestedTimeOffDto.id)
        } else {
            return repository.deleteVacation(requestedTimeOffDto.id)
        }
    }
}