package co.techmagic.hr.domain.interactor.employee

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.request.TimeOffRequestByUser
import co.techmagic.hr.domain.interactor.DataUseCase
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto
import co.techmagic.hr.domain.repository.IEmployeeRepository
import rx.Observable

/**
 * Created by Roman Ursu on 5/12/17
 */

class GetRemainedTimeOffs(iEmployeeRepository: IEmployeeRepository) : DataUseCase<TimeOffRequestByUser, RemainedTimeOffsAmountDto, IEmployeeRepository>(iEmployeeRepository) {

    override fun buildObservable(timeOffRequestByUser: TimeOffRequestByUser): Observable<RemainedTimeOffsAmountDto> {

        val remainedTimeOffsAmountDto = RemainedTimeOffsAmountDto()

        val totalIllnessObservable = prepareTotalDaysObservable(remainedTimeOffsAmountDto, timeOffRequestByUser, TimeOffType.ILLNESS)
        val totalVacationObservable = prepareTotalDaysObservable(remainedTimeOffsAmountDto, timeOffRequestByUser, TimeOffType.VACATION)
        val totalDayOffsObservable = prepareTotalDaysObservable(remainedTimeOffsAmountDto, timeOffRequestByUser, TimeOffType.DAYOFF)

        return Observable.zip(
                totalIllnessObservable,
                totalVacationObservable,
                totalDayOffsObservable
        ) { remainedTimeOffsAmountDto1, remainedTimeOffsAmountDto2, remainedTimeOffsAmountDto3 -> remainedTimeOffsAmountDto }
    }

    private fun prepareTotalDaysObservable(remainedTimeOffsAmountDto: RemainedTimeOffsAmountDto, timeOffRequestByUser: TimeOffRequestByUser, timeOffType: TimeOffType): Observable<RemainedTimeOffsAmountDto> {
        val observable = getTotalDaysObservableByTimeOffType(timeOffType, timeOffRequestByUser) ?: return Observable.just(remainedTimeOffsAmountDto)

        return observable.flatMap { daysAmount ->
            remainedTimeOffsAmountDto.map.put(timeOffType, daysAmount)
            Observable.just(remainedTimeOffsAmountDto)
        }
    }

    private fun getTotalDaysObservableByTimeOffType(timeOffType: TimeOffType, timeOffRequestByUser: TimeOffRequestByUser): Observable<Int>? {
        when (timeOffType) {
            TimeOffType.ILLNESS -> repository.getTotalIllness(timeOffRequestByUser)
            TimeOffType.VACATION -> repository.getTotalVacation(timeOffRequestByUser)
            TimeOffType.DAYOFF -> repository.getTotalDayOff(timeOffRequestByUser)
        }

        return null
    }

    companion object {

        private val TAG = GetRemainedTimeOffs::class.java.simpleName
    }
}