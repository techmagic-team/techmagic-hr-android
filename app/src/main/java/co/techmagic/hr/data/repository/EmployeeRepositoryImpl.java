package co.techmagic.hr.data.repository;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import co.techmagic.hr.common.TimeOffType;
import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.DatePeriod;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.HolidayDate;
import co.techmagic.hr.data.entity.RequestTimeOff;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.entity.TimeOffAmount;
import co.techmagic.hr.data.exception.NetworkConnectionException;
import co.techmagic.hr.data.manager.NetworkManager;
import co.techmagic.hr.data.manager.impl.NetworkManagerImpl;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.data.request.EmployeesByDepartmentRequest;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.data.request.TimeOffRequestByUser;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.data.store.client.ApiClient;
import co.techmagic.hr.domain.pojo.DatePeriodDto;
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by techmagic on 4/3/17
 */

public class EmployeeRepositoryImpl implements IEmployeeRepository {

    private static final String TAG = EmployeeRepositoryImpl.class.getSimpleName();
    private ApiClient client;
    private NetworkManager networkManager;
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());


    public EmployeeRepositoryImpl() {
        client = ApiClient.getApiClient();
        this.networkManager = NetworkManagerImpl.getNetworkManager();
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public Observable<Employee> getEmployees(EmployeeFiltersRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getEmployees(request.getQuery(), request.getProjectId(), request.getDepartmentId(), request.isLastWorkingDay(), request.getLeadId(), request.getOffset(), request.getLimit());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<Filter>> getFilterDepartments() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterDepartments();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<FilterLead>> getFilterLeadsWithEmployees() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterLeadsWithEmployees();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<Filter>> getProjectFilters() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterProjects();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getUserVacations(TimeOffRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getUserVacations(request.getUserId(), true, request.getDateFrom().getGte(), request.getDateTo().getLte());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getUserDayOffs(TimeOffRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getUserDayOffs(request.getUserId(), false, request.getDateFrom().getGte(), request.getDateTo().getLte());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getUserIllnesses(GetIllnessRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getUserIllnesses(request.getUserId(), request.getDateFrom().getGte(), request.getDateTo().getLte());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getAllVacations(TimeOffAllRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllVacations(request.getDateFrom(), request.getDateTo());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getAllDayOffs(TimeOffAllRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllDayOffs(request.getDateFrom(), request.getDateTo());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getAllIllnesses(TimeOffAllRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllIllnesses(request.getDateFrom(), request.getDateTo());
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<List<RequestedTimeOff>> getUsedVacationsByUser(TimeOffRequestByUser request) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .getUsedVacationsByUser(request.getUserId(), request.getDateFrom().getGte(), request.getDateTo().getLte());
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<List<RequestedTimeOff>> getUsedDayOffsByUser(TimeOffRequestByUser request) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .getUsedDayOffsByUser(request.getUserId(), request.getDateFrom().getGte(), request.getDateTo().getLte());
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<List<RequestedTimeOff>> getUsedIllnessesByUser(TimeOffRequestByUser request) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .getUsedIllnessesByUser(request.getUserId(), request.getDateFrom().getGte(), request.getDateTo().getLte());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<Employee> getAllEmployeesByDepartment(EmployeesByDepartmentRequest employeesByDepartmentRequest) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllEmployeesByDepartment(employeesByDepartmentRequest.getProjectId(), employeesByDepartmentRequest.getDepartmentId(), employeesByDepartmentRequest.isMyTeam());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<CalendarInfo>> getCalendar(TimeOffAllRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getCalendar(request.getDateFrom(), request.getDateTo());
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<List<HolidayDate>> getHolidays(TimeOffAllRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .getHolidays(request.getDateFrom(), request.getDateTo());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<Filter>> getRooms() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterRooms();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<FilterLead>> getFilterLeads() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getLeads();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<Filter>> getReasons() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterReasons();
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<RequestedTimeOffDto> requestVacation(TimeOffRequest request) {
        if (networkManager.isNetworkAvailable()) {
            RequestTimeOff requestTimeOff = new RequestTimeOff(request.getDateFrom().getGte(), request.getDateTo().getLte(), request.getUserId(), request.isPaid(), request.isAccepted());

            return client
                    .getEmployeeClient()
                    .requestVacation(requestTimeOff)
                    .map(getMapper(TimeOffType.VACATION));

        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<RequestedTimeOffDto> requestIllness(TimeOffRequest request) {
        if (networkManager.isNetworkAvailable()) {
            RequestTimeOff requestTimeOff = new RequestTimeOff(request.getDateFrom().getGte(), request.getDateTo().getLte(), request.getUserId(), request.isPaid(), request.isAccepted());

            return client
                    .getEmployeeClient()
                    .requestIllness(requestTimeOff)
                    .map(getMapper(TimeOffType.ILLNESS));

        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<Integer> getTotalVacation(TimeOffRequestByUser request) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .getTotalVacation(request.getUserId(), request.getDateFrom().getGte(), request.getDateTo().getLte())
                    .map(new Func1<TimeOffAmount, Integer>() {
                        @Override
                        public Integer call(TimeOffAmount timeOffAmount) {
                            if (timeOffAmount != null) {
                                return timeOffAmount.getAvailableDays();

                            } else {
                                return 0;
                            }
                        }
                    });
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<Integer> getTotalDayOff(TimeOffRequestByUser request) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .getTotalDayOff(request.getUserId(), request.getDateFrom().getGte(), request.getDateTo().getLte())
                    .map(new Func1<TimeOffAmount, Integer>() {
                        @Override
                        public Integer call(TimeOffAmount timeOffAmount) {
                            if (timeOffAmount != null) {
                                return timeOffAmount.getAvailableDays();

                            } else {
                                return 0;
                            }
                        }
                    });
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<Integer> getTotalIllness(TimeOffRequestByUser request) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .getTotalIllness(request.getUserId(), request.getDateFrom().getGte(), request.getDateTo().getLte())
                    .map(new Func1<TimeOffAmount, Integer>() {
                        @Override
                        public Integer call(TimeOffAmount timeOffAmount) {
                            if (timeOffAmount != null) {
                                return timeOffAmount.getAvailableDays();

                            } else {
                                return 0;
                            }
                        }
                    });
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<List<DatePeriodDto>> getUserPeriods(String userId) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .getUserPeriod(userId)
                    .map(new Func1<List<DatePeriod>, List<DatePeriodDto>>() {
                        @Override
                        public List<DatePeriodDto> call(List<DatePeriod> datePeriods) {
                            List<DatePeriodDto> datePeriodDtos = new ArrayList<>(2);
                            try {
                                if (datePeriods != null && datePeriods.size() > 1) {
                                    for (DatePeriod datePeriod : datePeriods) {
                                        Date dateFrom = DATE_FORMAT.parse(datePeriod.getDateFrom());
                                        Date dateTo = DATE_FORMAT.parse(datePeriod.getDateTo());

                                        datePeriodDtos.add(new DatePeriodDto(dateFrom, dateTo));
                                    }
                                }
                            } catch (ParseException e) {
                                Log.e(TAG, "Error parsing date", e);
                            }

                            return datePeriodDtos;
                        }
                    });
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<Void> deleteVacation(String timeOffId) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .deleteTimeOff(timeOffId);
        }

        return Observable.error(new NetworkConnectionException());
    }

    @Override
    public Observable<Void> deleteIllness(String timeOffId) {
        if (networkManager.isNetworkAvailable()) {
            return client
                    .getEmployeeClient()
                    .deleteIllness(timeOffId);
        }

        return Observable.error(new NetworkConnectionException());
    }

    private Func1<RequestedTimeOff, RequestedTimeOffDto> getMapper(TimeOffType timeOffType) {
        return new Func1<RequestedTimeOff, RequestedTimeOffDto>() {
            @Override
            public RequestedTimeOffDto call(RequestedTimeOff requestedTimeOff) {
                Date dateFrom = null;
                Date dateTo = null;
                try {
                    dateFrom = DATE_FORMAT.parse(requestedTimeOff.getDateFrom());
                    dateTo = DATE_FORMAT.parse(requestedTimeOff.getDateTo());

                } catch (ParseException e) {
                    Log.e(TAG, "Error parsing date", e);
                }

                RequestedTimeOffDto requestedTimeOffDto = new RequestedTimeOffDto();
                requestedTimeOffDto.setAccepted(requestedTimeOff.getAccepted());
                requestedTimeOffDto.setId(requestedTimeOff.getId());
                requestedTimeOffDto.setCompanyId(requestedTimeOff.getCompanyId());
                requestedTimeOffDto.setDateFrom(dateFrom);
                requestedTimeOffDto.setDateTo(dateTo);
                requestedTimeOffDto.setPaid(requestedTimeOff.isPaid());
                requestedTimeOffDto.setUserId(requestedTimeOff.getUserId());
                requestedTimeOffDto.setTimeOffType(timeOffType);

                return requestedTimeOffDto;
            }
        };
    }
}