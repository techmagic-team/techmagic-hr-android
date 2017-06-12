package co.techmagic.hr.domain.interactor.employee;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.common.TimeOffType;
import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.Holiday;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.RemainedTimeOffRequest;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.pojo.AllTimeOffsDto;
import co.techmagic.hr.domain.pojo.CalendarInfoDto;
import co.techmagic.hr.domain.pojo.HolidayDto;
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto;
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.util.DateUtil;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.functions.FuncN;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class GetRemainedTimeOffs extends DataUseCase<RemainedTimeOffRequest, RemainedTimeOffsAmountDto, IEmployeeRepository> {

    public GetRemainedTimeOffs(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }

    @Override
    protected Observable<RemainedTimeOffsAmountDto> buildObservable(RemainedTimeOffRequest remainedTimeOffRequest) {

        RemainedTimeOffsAmountDto remainedTimeOffsAmountDto = new RemainedTimeOffsAmountDto();

        Observable<RemainedTimeOffsAmountDto> totalIllnessObservable = repository.getTotalIllness(remainedTimeOffRequest).flatMap(new Func1<Integer, Observable<RemainedTimeOffsAmountDto>>() {
            @Override
            public Observable<RemainedTimeOffsAmountDto> call(Integer daysAmount) {
                remainedTimeOffsAmountDto.getMap().put(TimeOffType.ILLNESS, daysAmount);
                return Observable.just(remainedTimeOffsAmountDto);
            }
        });

        Observable<RemainedTimeOffsAmountDto> totalVacationObservable = repository.getTotalIllness(remainedTimeOffRequest).flatMap(new Func1<Integer, Observable<RemainedTimeOffsAmountDto>>() {
            @Override
            public Observable<RemainedTimeOffsAmountDto> call(Integer daysAmount) {
                remainedTimeOffsAmountDto.getMap().put(TimeOffType.VACATION, daysAmount);
                return Observable.just(remainedTimeOffsAmountDto);
            }
        });

        Observable<RemainedTimeOffsAmountDto> totalDayOffsObservable = repository.getTotalIllness(remainedTimeOffRequest).flatMap(new Func1<Integer, Observable<RemainedTimeOffsAmountDto>>() {
            @Override
            public Observable<RemainedTimeOffsAmountDto> call(Integer daysAmount) {
                remainedTimeOffsAmountDto.getMap().put(TimeOffType.DAYOFF, daysAmount);
                return Observable.just(remainedTimeOffsAmountDto);
            }
        });

        Observable totalTimeOffsObservable = Observable.zip(totalIllnessObservable, totalVacationObservable, totalDayOffsObservable, new Func3<RemainedTimeOffsAmountDto, RemainedTimeOffsAmountDto, RemainedTimeOffsAmountDto, Object>() {
            @Override
            public Object call(RemainedTimeOffsAmountDto remainedTimeOffsAmountDto, RemainedTimeOffsAmountDto remainedTimeOffsAmountDto2, RemainedTimeOffsAmountDto remainedTimeOffsAmountDto3) {
                // now we have everything in remainedTimeOffsAmountDto
                return Observable.just(remainedTimeOffsAmountDto);
            }
        });

        TimeOffAllRequest timeOffAllRequest = new TimeOffAllRequest(remainedTimeOffRequest.getDateFrom().getGte(), remainedTimeOffRequest.getDateTo().getLte());

        AllTimeOffsDto allTimeOffsDto = new AllTimeOffsDto();

        Observable<RemainedTimeOffsAmountDto> illnessesObservable = repository.getAllIllnesses(timeOffAllRequest)
                .flatMap(new Func1<List<RequestedTimeOff>, Observable<RemainedTimeOffsAmountDto>>() {
                    @Override
                    public Observable<RemainedTimeOffsAmountDto> call(List<RequestedTimeOff> requestedTimeOffs) {
                        List<RequestedTimeOffDto> requested = mapCollection(requestedTimeOffs, true);
                        List<RequestedTimeOffDto> allIllnesses = mapCollection(requestedTimeOffs, false);

                        Integer totalDays = remainedTimeOffsAmountDto.getMap().get(TimeOffType.ILLNESS);
                        int remainedDays = calculateRemainedDays(totalDays, requested, allIllnesses);
                        remainedTimeOffsAmountDto.getMap().put(TimeOffType.ILLNESS, remainedDays);

                        return Observable.just(remainedTimeOffsAmountDto);
                    }
                });

        Observable<RemainedTimeOffsAmountDto> vacationsObservable = repository.getAllVacations(timeOffAllRequest)
                .flatMap(new Func1<List<RequestedTimeOff>, Observable<RemainedTimeOffsAmountDto>>() {
                    @Override
                    public Observable<RemainedTimeOffsAmountDto> call(List<RequestedTimeOff> requestedTimeOffs) {
                        List<RequestedTimeOffDto> requested = mapCollection(requestedTimeOffs, true);
                        List<RequestedTimeOffDto> allVacations = mapCollection(requestedTimeOffs, false);

                        Integer totalDays = remainedTimeOffsAmountDto.getMap().get(TimeOffType.VACATION);
                        int remainedDays = calculateRemainedDays(totalDays, requested, allVacations);
                        remainedTimeOffsAmountDto.getMap().put(TimeOffType.VACATION, remainedDays);

                        return Observable.just(remainedTimeOffsAmountDto);
                    }
                });

        Observable<RemainedTimeOffsAmountDto> dayOffObservable = repository.getAllDayOffs(timeOffAllRequest)
                .flatMap(new Func1<List<RequestedTimeOff>, Observable<RemainedTimeOffsAmountDto>>() {
                    @Override
                    public Observable<RemainedTimeOffsAmountDto> call(List<RequestedTimeOff> requestedTimeOffs) {
                        List<RequestedTimeOffDto> requested = mapCollection(requestedTimeOffs, true);
                        List<RequestedTimeOffDto> allDayoffs = mapCollection(requestedTimeOffs, false);

                        Integer totalDays = remainedTimeOffsAmountDto.getMap().get(TimeOffType.DAYOFF);
                        int remainedDays = calculateRemainedDays(totalDays, requested, allDayoffs);
                        remainedTimeOffsAmountDto.getMap().put(TimeOffType.DAYOFF, remainedDays);

                        return Observable.just(remainedTimeOffsAmountDto);
                    }
                });

        List<Observable<RemainedTimeOffsAmountDto>> observables = new ArrayList<>();
        observables.add(illnessesObservable);
        observables.add(vacationsObservable);
        observables.add(dayOffObservable);

        Observable<RemainedTimeOffsAmountDto> usedTimeOffsObservable = Observable.zip(observables, args -> remainedTimeOffsAmountDto);

        return Observable.concat(totalTimeOffsObservable, usedTimeOffsObservable);
    }

    private int calculateRemainedDays(Integer totalDays, List<RequestedTimeOffDto> requested, List<RequestedTimeOffDto> allIllnesses) {
        int usedDays = 0;

        if (!requested.isEmpty()) {
            usedDays += requested.size();
        }

        if (!allIllnesses.isEmpty()) {
            usedDays += allIllnesses.size();
        }

        if (totalDays == null) {
            totalDays = 0;
        }

        return totalDays - usedDays;
    }

    private List<RequestedTimeOffDto> mapCollection(List<RequestedTimeOff> requestedTimeOffs, boolean requestedOnly) {
        List<RequestedTimeOffDto> timeOffDtos = new ArrayList<>();

        if (requestedTimeOffs != null) {
            for (RequestedTimeOff requestedTimeOff : requestedTimeOffs) {
                RequestedTimeOffDto requestedTimeOffDto = map(requestedTimeOff);

                if (requestedTimeOffDto == null) {
                    return timeOffDtos;
                }

                // Means time off is waiting for response or already accepted
                if (requestedOnly && requestedTimeOffDto.getAccepted() == null || requestedTimeOffDto.isAccepted()) {
                    timeOffDtos.add(requestedTimeOffDto);
                } else if (!requestedOnly) {
                    timeOffDtos.add(requestedTimeOffDto);
                }
            }
        }

        return timeOffDtos;
    }


    private RequestedTimeOffDto map(RequestedTimeOff requestedTimeOff) {
        if (requestedTimeOff != null) {
            RequestedTimeOffDto requestedTimeOffDto = new RequestedTimeOffDto();
            requestedTimeOffDto.setAccepted(requestedTimeOff.getAccepted());
            requestedTimeOffDto.setCompanyId(requestedTimeOff.getCompanyId());
            requestedTimeOffDto.setDateFrom(DateUtil.parseStringDate(requestedTimeOff.getDateFrom()));
            requestedTimeOffDto.setDateTo(DateUtil.parseStringDate(requestedTimeOff.getDateTo()));
            requestedTimeOffDto.setPaid(requestedTimeOff.isPaid());
            requestedTimeOffDto.setUserId(requestedTimeOff.getUserId());

            return requestedTimeOffDto;
        }

        return null;
    }
}