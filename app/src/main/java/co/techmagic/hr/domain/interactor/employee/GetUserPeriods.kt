package co.techmagic.hr.domain.interactor.employee

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.entity.DateFrom
import co.techmagic.hr.data.entity.DateTo
import co.techmagic.hr.data.request.TimeOffRequestByUser
import co.techmagic.hr.domain.interactor.DataUseCase
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto
import co.techmagic.hr.domain.repository.IEmployeeRepository
import co.techmagic.hr.presentation.pojo.AvailableTimeOffsData
import co.techmagic.hr.presentation.pojo.PeriodPair
import rx.Observable

/**
 * Created by Roman Ursu on 6/12/17
 */
class GetUserPeriods(iEmployeeRepository: IEmployeeRepository) : DataUseCase<String, AvailableTimeOffsData, IEmployeeRepository>(iEmployeeRepository) {

    override fun buildObservable(userId: String): Observable<AvailableTimeOffsData> {

        val availableTimeOffsData: AvailableTimeOffsData = AvailableTimeOffsData()

        return repository.getUserPeriods(userId)
                .flatMap { periodsList ->
                    val timeOffsObservableList: MutableList<Observable<AvailableTimeOffsData>> = mutableListOf()

                    for ((dateFrom, dateTo) in periodsList) {
                        val timeOffRequestByUser: TimeOffRequestByUser = TimeOffRequestByUser(userId, DateFrom(dateFrom.time), DateTo(dateTo.time))
                        val observable = getLoadTimeOffsObservable(timeOffRequestByUser)
                                .map { remainedTimeOffsAmountDto ->
                                    val periodPair: PeriodPair = PeriodPair(dateFrom, dateTo)
                                    availableTimeOffsData.timeOffsMap.put(periodPair, remainedTimeOffsAmountDto)
                                    availableTimeOffsData
                                }


                        timeOffsObservableList.add(observable)
                    }

                    Observable.zip(timeOffsObservableList, { availableTimeOffsData })
                }
    }

    fun getLoadTimeOffsObservable(timeOffRequestByUser: TimeOffRequestByUser): Observable<RemainedTimeOffsAmountDto> {

        val remainedTimeOffsAmountDto = RemainedTimeOffsAmountDto()

        val totalIllnessObservable: Observable<Int> = repository.getTotalIllness(timeOffRequestByUser)
        val totalVacationObservable: Observable<Int> = repository.getTotalVacation(timeOffRequestByUser)
        val totalDayOffsObservable: Observable<Int> = repository.getTotalDayOff(timeOffRequestByUser)

        return Observable.zip(
                totalIllnessObservable,
                totalVacationObservable,
                totalDayOffsObservable)
        { totalIllness, totalVacation, totalDayOffs ->
            processResult(remainedTimeOffsAmountDto, totalIllness, totalVacation, totalDayOffs)
        }
    }

    private fun processResult(remainedTimeOffsAmountDto: RemainedTimeOffsAmountDto, totalIllness: Int?, totalVacation: Int?, totalDayOffs: Int?): RemainedTimeOffsAmountDto {
        remainedTimeOffsAmountDto.map.put(TimeOffType.ILLNESS, totalIllness)
        remainedTimeOffsAmountDto.map.put(TimeOffType.VACATION, totalVacation)
        remainedTimeOffsAmountDto.map.put(TimeOffType.DAYOFF, totalDayOffs)
        return remainedTimeOffsAmountDto
    }
}