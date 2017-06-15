package co.techmagic.hr.domain.interactor.employee

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.entity.DateFrom
import co.techmagic.hr.data.entity.DateTo
import co.techmagic.hr.data.request.RemainedTimeOffRequest
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

        return repository.getUserPeriods(userId).flatMap { periodsList ->
            val timeOffsObservableList: MutableList<Observable<AvailableTimeOffsData>> = mutableListOf()

            for ((dateFrom, dateTo) in periodsList) {
                val remainedTimeOffRequest: RemainedTimeOffRequest = RemainedTimeOffRequest(userId, DateFrom(dateFrom.time), DateTo(dateTo.time))
                val observable = getLoadTimeOffsObservable(remainedTimeOffRequest)
                        .flatMap { remainedTimeOffsAmountDto ->
                            val periodPair: PeriodPair = PeriodPair(dateFrom, dateTo)
                            availableTimeOffsData.timeOffsMap.put(periodPair, remainedTimeOffsAmountDto)
                            Observable.just(availableTimeOffsData)
                        }


                timeOffsObservableList.add(observable)
            }

            Observable.zip(timeOffsObservableList, { availableTimeOffsData })
        }
    }

    fun getLoadTimeOffsObservable(remainedTimeOffRequest: RemainedTimeOffRequest): Observable<RemainedTimeOffsAmountDto> {

        val remainedTimeOffsAmountDto = RemainedTimeOffsAmountDto()

        val totalIllnessObservable = prepareAvailableDaysObservable(remainedTimeOffsAmountDto, remainedTimeOffRequest, TimeOffType.ILLNESS)
        val totalVacationObservable = prepareAvailableDaysObservable(remainedTimeOffsAmountDto, remainedTimeOffRequest, TimeOffType.VACATION)
        val totalDayOffsObservable = prepareAvailableDaysObservable(remainedTimeOffsAmountDto, remainedTimeOffRequest, TimeOffType.DAYOFF)

        return Observable.zip(
                totalIllnessObservable,
                totalVacationObservable,
                totalDayOffsObservable
        ) { remainedTimeOffsAmountDto1, remainedTimeOffsAmountDto2, remainedTimeOffsAmountDto3 -> remainedTimeOffsAmountDto }
    }

    private fun prepareAvailableDaysObservable(remainedTimeOffsAmountDto: RemainedTimeOffsAmountDto, remainedTimeOffRequest: RemainedTimeOffRequest, timeOffType: TimeOffType): Observable<RemainedTimeOffsAmountDto> {
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
}