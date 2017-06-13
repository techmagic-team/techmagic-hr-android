package co.techmagic.hr.domain.interactor.employee;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.common.TimeOffType;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.RemainedTimeOffRequest;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto;
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.util.DateUtil;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;

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

        Observable<RemainedTimeOffsAmountDto> totalIllnessObservable = prepareTotalDaysObservable(remainedTimeOffsAmountDto, remainedTimeOffRequest, TimeOffType.ILLNESS);

        return totalIllnessObservable;
//        Observable<RemainedTimeOffsAmountDto> totalVacationObservable = prepareTotalDaysObservable(remainedTimeOffsAmountDto, remainedTimeOffRequest, TimeOffType.VACATION);
//        Observable<RemainedTimeOffsAmountDto> totalDayOffsObservable = prepareTotalDaysObservable(remainedTimeOffsAmountDto, remainedTimeOffRequest, TimeOffType.DAYOFF);
//
//        Observable totalTimeOffsObservable = Observable.zip(
//                totalIllnessObservable,
//                totalVacationObservable,
//                totalDayOffsObservable,
//                (Func3<RemainedTimeOffsAmountDto, RemainedTimeOffsAmountDto, RemainedTimeOffsAmountDto, Object>)
//                        (remainedTimeOffsAmountDto1, remainedTimeOffsAmountDto2, remainedTimeOffsAmountDto3) -> Observable.just(remainedTimeOffsAmountDto1));
//
//        TimeOffAllRequest timeOffAllRequest = new TimeOffAllRequest(remainedTimeOffRequest.getDateFrom().getGte(), remainedTimeOffRequest.getDateTo().getLte());
//
//        Observable<RemainedTimeOffsAmountDto> illnessesObservable = prepareRemainedDaysObservable(remainedTimeOffsAmountDto, timeOffAllRequest, TimeOffType.ILLNESS);
//        Observable<RemainedTimeOffsAmountDto> vacationsObservable = prepareRemainedDaysObservable(remainedTimeOffsAmountDto, timeOffAllRequest, TimeOffType.VACATION);
//        Observable<RemainedTimeOffsAmountDto> dayOffObservable = prepareRemainedDaysObservable(remainedTimeOffsAmountDto, timeOffAllRequest, TimeOffType.DAYOFF);
//
//        List<Observable<RemainedTimeOffsAmountDto>> observables = new ArrayList<>();
//        observables.add(illnessesObservable);
//        observables.add(vacationsObservable);
//        observables.add(dayOffObservable);
//
//        Observable<RemainedTimeOffsAmountDto> usedTimeOffsObservable = Observable.zip(observables, args -> remainedTimeOffsAmountDto);
//
//        return Observable.concat(totalTimeOffsObservable, usedTimeOffsObservable);
    }

    private Observable<RemainedTimeOffsAmountDto> prepareRemainedDaysObservable(RemainedTimeOffsAmountDto remainedTimeOffsAmountDto, TimeOffAllRequest timeOffAllRequest, TimeOffType timeOffType) {
        Observable<List<RequestedTimeOff>> observable = getRemainedDaysObservableByTimeOfftype(timeOffType, timeOffAllRequest);

        if (observable == null) {
            return Observable.just(remainedTimeOffsAmountDto);
        }

        return observable.flatMap(new Func1<List<RequestedTimeOff>, Observable<RemainedTimeOffsAmountDto>>() {
            @Override
            public Observable<RemainedTimeOffsAmountDto> call(List<RequestedTimeOff> requestedTimeOffs) {
                List<RequestedTimeOffDto> requested = mapCollection(requestedTimeOffs, true);
                List<RequestedTimeOffDto> allIllnesses = mapCollection(requestedTimeOffs, false);

                Integer totalDays = remainedTimeOffsAmountDto.getMap().get(timeOffType);
                int remainedDays = calculateRemainedDays(totalDays, requested, allIllnesses);
                remainedTimeOffsAmountDto.getMap().put(timeOffType, remainedDays);

                return Observable.just(remainedTimeOffsAmountDto);
            }
        });
    }

    private Observable<RemainedTimeOffsAmountDto> prepareTotalDaysObservable(RemainedTimeOffsAmountDto remainedTimeOffsAmountDto, RemainedTimeOffRequest remainedTimeOffRequest, TimeOffType timeOffType) {
        Observable<Integer> observable = getTotalDaysObservableByTimeOfftype(timeOffType, remainedTimeOffRequest);

        if (observable == null) {
            return Observable.just(remainedTimeOffsAmountDto);
        }

        return observable.flatMap(new Func1<Integer, Observable<RemainedTimeOffsAmountDto>>() {
            @Override
            public Observable<RemainedTimeOffsAmountDto> call(Integer daysAmount) {
                remainedTimeOffsAmountDto.getMap().put(timeOffType, daysAmount);
                return Observable.just(remainedTimeOffsAmountDto);
            }
        });
    }

    private Observable<Integer> getTotalDaysObservableByTimeOfftype(TimeOffType timeOffType, RemainedTimeOffRequest remainedTimeOffRequest) {
        switch (timeOffType) {
            case ILLNESS:
                return repository.getTotalIllness(remainedTimeOffRequest);
            case VACATION:
                return repository.getTotalVacation(remainedTimeOffRequest);
            case DAYOFF:
                return repository.getTotalDayOff(remainedTimeOffRequest);
        }

        return null;
    }

    private Observable<List<RequestedTimeOff>> getRemainedDaysObservableByTimeOfftype(TimeOffType timeOffType, TimeOffAllRequest timeOffAllRequest) {
        switch (timeOffType) {
            case ILLNESS:
                return repository.getAllIllnesses(timeOffAllRequest);
            case VACATION:
                return repository.getAllVacations(timeOffAllRequest);
            case DAYOFF:
                return repository.getAllDayOffs(timeOffAllRequest);
        }

        return null;
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