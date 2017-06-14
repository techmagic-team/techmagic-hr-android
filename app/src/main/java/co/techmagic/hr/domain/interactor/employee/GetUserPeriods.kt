package co.techmagic.hr.domain.interactor.employee

import co.techmagic.hr.domain.interactor.DataUseCase
import co.techmagic.hr.domain.pojo.DatePeriodDto
import co.techmagic.hr.domain.repository.IEmployeeRepository
import rx.Observable

/**
 * Created by Roman Ursu on 6/12/17
 */
class GetUserPeriods(iEmployeeRepository: IEmployeeRepository) : DataUseCase<String, List<DatePeriodDto>, IEmployeeRepository>(iEmployeeRepository) {

    override fun buildObservable(userId: String): Observable<List<DatePeriodDto>> {
        return repository.getUserPeriods(userId)
    }
}