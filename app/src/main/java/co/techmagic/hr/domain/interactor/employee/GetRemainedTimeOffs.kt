package co.techmagic.hr.domain.interactor.employee

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.request.RemainedTimeOffRequest
import co.techmagic.hr.domain.interactor.DataUseCase
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto
import co.techmagic.hr.domain.repository.IEmployeeRepository
import rx.Observable

/**
 * Created by Roman Ursu on 5/12/17
 */

class GetRemainedTimeOffs(iEmployeeRepository: IEmployeeRepository) : DataUseCase<RemainedTimeOffRequest, RemainedTimeOffsAmountDto, IEmployeeRepository>(iEmployeeRepository) {

    override fun buildObservable(remainedTimeOffRequest: RemainedTimeOffRequest): Observable<RemainedTimeOffsAmountDto> {

        val remainedTimeOffsAmountDto = RemainedTimeOffsAmountDto()

        val totalIllnessObservable = prepareTotalDaysObservable(remainedTimeOffsAmountDto, remainedTimeOffRequest, TimeOffType.ILLNESS)
        val totalVacationObservable = prepareTotalDaysObservable(remainedTimeOffsAmountDto, remainedTimeOffRequest, TimeOffType.VACATION)
        val totalDayOffsObservable = prepareTotalDaysObservable(remainedTimeOffsAmountDto, remainedTimeOffRequest, TimeOffType.DAYOFF)

        return Observable.zip(
                totalIllnessObservable,
                totalVacationObservable,
                totalDayOffsObservable
        ) { remainedTimeOffsAmountDto1, remainedTimeOffsAmountDto2, remainedTimeOffsAmountDto3 -> remainedTimeOffsAmountDto }
    }

    private fun prepareTotalDaysObservable(remainedTimeOffsAmountDto: RemainedTimeOffsAmountDto, remainedTimeOffRequest: RemainedTimeOffRequest, timeOffType: TimeOffType): Observable<RemainedTimeOffsAmountDto> {
        val observable = getTotalDaysObservableByTimeOffType(timeOffType, remainedTimeOffRequest) ?: return Observable.just(remainedTimeOffsAmountDto)

        return observable.flatMap { daysAmount ->
            remainedTimeOffsAmountDto.map.put(timeOffType, daysAmount)
            Observable.just(remainedTimeOffsAmountDto)
        }
    }

    private fun getTotalDaysObservableByTimeOffType(timeOffType: TimeOffType, remainedTimeOffRequest: RemainedTimeOffRequest): Observable<Int>? {
        when (timeOffType) {
            TimeOffType.ILLNESS -> repository.getTotalIllness(remainedTimeOffRequest)
            TimeOffType.VACATION -> repository.getTotalVacation(remainedTimeOffRequest)
            TimeOffType.DAYOFF -> repository.getTotalDayOff(remainedTimeOffRequest)
        }

        return null
    }

    companion object {

        private val TAG = GetRemainedTimeOffs::class.java.simpleName
    }
}