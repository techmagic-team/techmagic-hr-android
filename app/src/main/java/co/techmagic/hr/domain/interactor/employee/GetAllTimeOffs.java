package co.techmagic.hr.domain.interactor.employee;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.common.TimeOffType;
import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.Holiday;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.pojo.AllTimeOffsDto;
import co.techmagic.hr.domain.pojo.CalendarInfoDto;
import co.techmagic.hr.domain.pojo.HolidayDto;
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class GetAllTimeOffs extends DataUseCase<TimeOffAllRequest, AllTimeOffsDto, IEmployeeRepository> {


    public GetAllTimeOffs(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<AllTimeOffsDto> buildObservable(TimeOffAllRequest timeOffAllRequest) {
        AllTimeOffsDto allTimeOffsDto = new AllTimeOffsDto();

        Observable<AllTimeOffsDto> illnessesObservable = repository.getAllIllnesses(timeOffAllRequest)
                .flatMap(new Func1<List<RequestedTimeOff>, Observable<AllTimeOffsDto>>() {
                    @Override
                    public Observable<AllTimeOffsDto> call(List<RequestedTimeOff> requestedTimeOffs) {
                        List<RequestedTimeOffDto> requested = mapCollection(requestedTimeOffs, true);
                        List<RequestedTimeOffDto> allIllnesses = mapCollection(requestedTimeOffs, false);

                        allTimeOffsDto.addRequested(requested);
                        allTimeOffsDto.getMap().put(TimeOffType.ILLNESS, allIllnesses);

                        return Observable.just(allTimeOffsDto);
                    }
                });

        Observable<AllTimeOffsDto> vacationsObservable = repository.getAllVacations(timeOffAllRequest)
                .flatMap(new Func1<List<RequestedTimeOff>, Observable<AllTimeOffsDto>>() {
                    @Override
                    public Observable<AllTimeOffsDto> call(List<RequestedTimeOff> requestedTimeOffs) {
                        List<RequestedTimeOffDto> requested = mapCollection(requestedTimeOffs, true);
                        List<RequestedTimeOffDto> allVacations = mapCollection(requestedTimeOffs, false);

                        allTimeOffsDto.addRequested(requested);
                        allTimeOffsDto.getMap().put(TimeOffType.VACATION, allVacations);

                        return Observable.just(allTimeOffsDto);
                    }
                });

        Observable<AllTimeOffsDto> dayOffObservable = repository.getAllVacations(timeOffAllRequest)
                .flatMap(new Func1<List<RequestedTimeOff>, Observable<AllTimeOffsDto>>() {
                    @Override
                    public Observable<AllTimeOffsDto> call(List<RequestedTimeOff> requestedTimeOffs) {
                        List<RequestedTimeOffDto> requested = mapCollection(requestedTimeOffs, true);
                        List<RequestedTimeOffDto> allDayoffs = mapCollection(requestedTimeOffs, false);

                        allTimeOffsDto.addRequested(requested);
                        allTimeOffsDto.getMap().put(TimeOffType.DAYOFF, allDayoffs);

                        return Observable.just(allTimeOffsDto);
                    }
                });

        Observable<AllTimeOffsDto> holidaysObservable = repository.getCalendar(timeOffAllRequest)
                .flatMap(new Func1<List<CalendarInfo>, Observable<AllTimeOffsDto>>() {
                    @Override
                    public Observable<AllTimeOffsDto> call(List<CalendarInfo> calendarInfos) {
                        allTimeOffsDto.setCalendarInfo(mapCalendarInfoCollection(calendarInfos));
                        return Observable.just(allTimeOffsDto);
                    }
                });

        List<Observable<AllTimeOffsDto>> observables = new ArrayList<>();
        observables.add(illnessesObservable);
        observables.add(vacationsObservable);
        observables.add(dayOffObservable);
        observables.add(holidaysObservable);

        return Observable.zip(observables, args -> (AllTimeOffsDto) args[0]);
    }


    private List<CalendarInfoDto> mapCalendarInfoCollection(List<CalendarInfo> calendarInfos) {
        List<CalendarInfoDto> calendarInfoDtos = new ArrayList<>();
        if (calendarInfos != null) {
            for (CalendarInfo calendarInfo : calendarInfos) {
                CalendarInfoDto calendarInfoDto = mapCalendarInfo(calendarInfo);
                if (calendarInfoDto != null) {
                    calendarInfoDtos.add(calendarInfoDto);
                }
            }
        }

        return calendarInfoDtos;
    }


    private CalendarInfoDto mapCalendarInfo(CalendarInfo calendarInfo) {
        if (calendarInfo != null) {
            CalendarInfoDto calendarInfoDto = new CalendarInfoDto();
            calendarInfoDto.setCurrent(calendarInfo.isCurrent());
            calendarInfoDto.setName(calendarInfo.getName());
            calendarInfoDto.setYear(calendarInfo.getYear());
            calendarInfoDto.setHolidays(mapHolidays(calendarInfo.getHolidays()));

            return calendarInfoDto;
        }

        return null;
    }


    private List<HolidayDto> mapHolidays(List<Holiday> holidays) {
        List<HolidayDto> holidayDtos = new ArrayList<>();
        if (holidays != null) {
            for (Holiday holiday : holidays) {
                HolidayDto holidayDto = map(holiday);
                if (holidayDto != null) {
                    holidayDtos.add(holidayDto);
                }
            }
        }

        return holidayDtos;
    }


    private HolidayDto map(Holiday holiday) {
        if (holiday != null) {
            HolidayDto holidayDto = new HolidayDto();
            holidayDto.setName(holiday.getName());
            holidayDto.setDate(holiday.getDate());

            return holidayDto;
        }

        return null;
    }


    private List<RequestedTimeOffDto> mapCollection(List<RequestedTimeOff> requestedTimeOffs, boolean requestedOnly) {
        List<RequestedTimeOffDto> timeOffDtos = new ArrayList<>();

        if (requestedTimeOffs != null) {
            for (RequestedTimeOff requestedTimeOff : requestedTimeOffs) {
                RequestedTimeOffDto requestedTimeOffDto = map(requestedTimeOff);

                if (requestedTimeOffDto == null) {
                    return timeOffDtos;
                }

                if (requestedOnly && requestedTimeOffDto.isAccepted()) {
                    timeOffDtos.add(requestedTimeOffDto);
                }
            }
        }

        return timeOffDtos;
    }


    private RequestedTimeOffDto map(RequestedTimeOff requestedTimeOff) {
        if (requestedTimeOff != null) {
            RequestedTimeOffDto requestedTimeOffDto = new RequestedTimeOffDto();
            requestedTimeOffDto.setAccepted(requestedTimeOff.isAccepted());
            requestedTimeOffDto.setCompanyId(requestedTimeOff.getCompanyId());
            requestedTimeOffDto.setDateFrom(requestedTimeOff.getDateFrom());
            requestedTimeOffDto.setDateTo(requestedTimeOff.getDateTo());
            requestedTimeOffDto.setPaid(requestedTimeOff.isPaid());
            requestedTimeOffDto.setUserId(requestedTimeOff.getUserId());

            return requestedTimeOffDto;
        }

        return null;
    }
}