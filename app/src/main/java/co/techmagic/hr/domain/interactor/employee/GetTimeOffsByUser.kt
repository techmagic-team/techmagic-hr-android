package co.techmagic.hr.domain.interactor.employee

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.data.entity.DateFrom
import co.techmagic.hr.data.entity.DateTo
import co.techmagic.hr.data.entity.RequestedTimeOff
import co.techmagic.hr.data.request.TimeOffRequestByUser
import co.techmagic.hr.domain.interactor.DataUseCase
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto
import co.techmagic.hr.domain.pojo.TimeOffRequestByUserAllPeriods
import co.techmagic.hr.domain.pojo.UsedTimeOffsByUserDto
import co.techmagic.hr.domain.repository.IEmployeeRepository
import co.techmagic.hr.presentation.pojo.WorkingPeriod
import co.techmagic.hr.presentation.util.DateUtil
import rx.Observable
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Roman Ursu on 5/12/17
 */

class GetTimeOffsByUser(iEmployeeRepository: IEmployeeRepository) : DataUseCase<TimeOffRequestByUserAllPeriods, UsedTimeOffsByUserDto, IEmployeeRepository>(iEmployeeRepository) {

    class PeriodAndTimeOff(val periodPair: WorkingPeriod, val timeOffType: TimeOffType) {
        var timeOffList: MutableList<RequestedTimeOffDto> = mutableListOf()
    }

    override fun buildObservable(request: TimeOffRequestByUserAllPeriods): Observable<UsedTimeOffsByUserDto> {
        val observables = ArrayList<Observable<PeriodAndTimeOff>>()

        for (period in request.periods) {
            val timeOffRequestByUser: TimeOffRequestByUser = TimeOffRequestByUser(request.userId, DateFrom(period.startDate.time), DateTo(period.endDate.time))
            val illnessesObservable = repository
                    .getUsedIllnessesByUser(timeOffRequestByUser)
                    .map({ timeOffs: List<RequestedTimeOff> ->
                        val periodAndTimeOff: PeriodAndTimeOff = PeriodAndTimeOff(period, TimeOffType.ILLNESS)
                        periodAndTimeOff.timeOffList.addAll(mapCollection(TimeOffType.ILLNESS, timeOffs))
                        periodAndTimeOff
                    })

            val vacationsObservable = repository
                    .getUsedVacationsByUser(timeOffRequestByUser)
                    .map({ timeOffs: List<RequestedTimeOff> ->
                        val periodAndTimeOff: PeriodAndTimeOff = PeriodAndTimeOff(period, TimeOffType.VACATION)
                        periodAndTimeOff.timeOffList.addAll(mapCollection(TimeOffType.VACATION, timeOffs))
                        periodAndTimeOff
                    })

            val dayOffObservable = repository
                    .getUsedDayOffsByUser(timeOffRequestByUser)
                    .map({ timeOffs: List<RequestedTimeOff> ->
                        val periodAndTimeOff: PeriodAndTimeOff = PeriodAndTimeOff(period, TimeOffType.DAYOFF)
                        periodAndTimeOff.timeOffList.addAll(mapCollection(TimeOffType.DAYOFF, timeOffs))
                        periodAndTimeOff
                    })

            observables.add(illnessesObservable)
            observables.add(vacationsObservable)
            observables.add(dayOffObservable)
        }

        return Observable.zip(observables, { args: Array<out Any>? -> processResult(args) })
    }

    private fun processResult(result: Array<out Any>?): UsedTimeOffsByUserDto {
        val timeOffs = UsedTimeOffsByUserDto()

        if (result == null) {
            return UsedTimeOffsByUserDto()
        }

        result
                .filterIsInstance<PeriodAndTimeOff>()
                .forEach {
                    if (timeOffs.timeOffMaps.containsKey(it.periodPair)) {
                        val map: MutableMap<TimeOffType, MutableList<RequestedTimeOffDto>> = timeOffs.timeOffMaps[it.periodPair]!!
                        if (map.containsKey(it.timeOffType)) {
                            map[it.timeOffType]!!.addAll(it.timeOffList)
                        } else {
                            map.put(it.timeOffType, it.timeOffList)
                        }
                    } else {
                        val map: MutableMap<TimeOffType, MutableList<RequestedTimeOffDto>> = Collections.synchronizedMap(HashMap<TimeOffType, MutableList<RequestedTimeOffDto>>())
                        timeOffs.timeOffMaps.put(it.periodPair, map)
                        map.put(it.timeOffType, it.timeOffList)
                    }
                }

        return timeOffs
    }

    private fun mapCollection(timeOffType: TimeOffType, requestedTimeOffs: List<RequestedTimeOff>?): List<RequestedTimeOffDto> {
        val timeOffDtos = ArrayList<RequestedTimeOffDto>()
        requestedTimeOffs?.mapNotNullTo(timeOffDtos) { map(timeOffType, it) }

        return timeOffDtos
    }

    private fun map(timeOffType: TimeOffType, requestedTimeOff: RequestedTimeOff?): RequestedTimeOffDto? {
        if (requestedTimeOff != null) {
            val requestedTimeOffDto = RequestedTimeOffDto()
            requestedTimeOffDto.isAccepted = requestedTimeOff.accepted
            requestedTimeOffDto.companyId = requestedTimeOff.companyId
            requestedTimeOffDto.dateFrom = DateUtil.parseStringDate(requestedTimeOff.dateFrom)
            requestedTimeOffDto.dateTo = DateUtil.parseStringDate(requestedTimeOff.dateTo)
            requestedTimeOffDto.isPaid = requestedTimeOff.isPaid
            requestedTimeOffDto.userId = requestedTimeOff.userId
            requestedTimeOffDto.id = requestedTimeOff.id
            requestedTimeOffDto.timeOffType = timeOffType

            return requestedTimeOffDto
        }

        return null
    }
}