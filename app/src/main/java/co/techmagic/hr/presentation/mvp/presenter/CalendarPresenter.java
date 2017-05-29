package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import co.techmagic.hr.common.TimeOffType;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.data.request.EmployeesByDepartmentRequest;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.domain.interactor.employee.GetAllTimeOffs;
import co.techmagic.hr.domain.interactor.employee.GetCalendar;
import co.techmagic.hr.domain.interactor.employee.GetEmployeesByDepartment;
import co.techmagic.hr.domain.pojo.AllTimeOffsDto;
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.CalendarView;
import co.techmagic.hr.presentation.pojo.UserAllTimeOffsMap;
import co.techmagic.hr.presentation.pojo.UserTimeOff;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class CalendarPresenter extends BasePresenter<CalendarView> {

    private EmployeeRepositoryImpl employeeRepository;
    private GetEmployeesByDepartment getEmployeesByDepartment;
    private GetCalendar getCalendar;
    private GetAllTimeOffs getAllTimeOffs;

    private List<Docs> employees;

    private Calendar dateFrom = null;
    private Calendar dateTo = null;

    private boolean isMyTeam;
    private long fromInMillis = 0;
    private long toInMillis = 0;
    private String depId;

    private boolean isCalendarUpdating = false;


    public CalendarPresenter() {
        employeeRepository = new EmployeeRepositoryImpl();
        getAllTimeOffs = new GetAllTimeOffs(employeeRepository);
        getEmployeesByDepartment = new GetEmployeesByDepartment(employeeRepository);
        getCalendar = new GetCalendar(employeeRepository);
        employees = new ArrayList<>();
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getAllTimeOffs.unsubscribe();
        getEmployeesByDepartment.unsubscribe();
        getCalendar.unsubscribe();
    }


    public void setupPage() {
        setupDefaultCalendarRange();
        isMyTeam = SharedPreferencesUtil.getMyTeamSelection();
        fromInMillis = SharedPreferencesUtil.getSelectedFromTime();
        toInMillis = SharedPreferencesUtil.getSelectedToTime();
        depId = SharedPreferencesUtil.getSelectedCalendarDepartmentId();

        if (fromInMillis == 0 && toInMillis == 0) {
            if (!isCalendarUpdating) {
                updateCalendar(isMyTeam, depId, null, null);
            }
        } else {
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();

            /* Set selected date. Otherwise - from January  */

            if (fromInMillis == 0) {
                showFromJanuaryDate(from);
            } else {
                from.setTimeInMillis(fromInMillis);
                dateFrom = from;
            }

            /* Set selected date. Otherwise to December */

            if (toInMillis == 0) {
                showToDecemberDate(to);
            } else {
                to.setTimeInMillis(toInMillis);
                dateTo = to;
            }

            if (!isCalendarUpdating) {
                updateCalendar(isMyTeam, depId, from, to);
            }
        }
    }


    public void updateCalendar(boolean isMyTeamChecked, String selDepId, @Nullable Calendar from, @Nullable Calendar to) {
        isCalendarUpdating = true;
        isMyTeam = isMyTeamChecked;
        depId = selDepId;

        if (noFiltersSelected(from, to)) {
            view.hideClearFilters();
        } else {
            view.showClearFilters();
        }

        if (from != null) {
            dateFrom = from;
        }

        if (to != null) {
            dateTo = to;
        }

        Calendar c = Calendar.getInstance();

        /* Date range didn't change earlier
        *  Set current year by default
        */

        if (dateFrom == null) {
            dateFrom = c;
            dateFrom.set(c.get(Calendar.YEAR), Calendar.JANUARY, 1);
        }

        if (dateTo == null) {
            dateTo = c;
            dateTo.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        }

        performGetEmployeesByDepartmentRequest();
    }


    public void onClearFiltersClick() {
        isMyTeam = true;
        fromInMillis = 0;
        toInMillis = 0;
        depId = null;
        view.hideClearFilters();
        setupDefaultCalendarRange();
        performGetEmployeesByDepartmentRequest();
    }


    public void onEmployeeClick(@NonNull String employeeId) {
        Docs selectedEmployee = null;
        for (Docs d : employees) {
            if (d.getId().equals(employeeId)) {
                selectedEmployee = d;
            }
        }

        if (selectedEmployee != null) {
            view.addDetailsFragment(selectedEmployee);
        }
    }


    private void setupDefaultCalendarRange() {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        showFromJanuaryDate(from);
        showToDecemberDate(to);
    }


    private void showFromJanuaryDate(Calendar c) {
        c.set(c.get(Calendar.YEAR), Calendar.JANUARY, 1);
        dateFrom = c;
    }


    private void showToDecemberDate(Calendar c) {
        c.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        dateTo = c;
    }


    private boolean noFiltersSelected(@Nullable Calendar from, @Nullable Calendar to) {
        return isMyTeam && depId == null && from == null && to == null && fromInMillis == 0 && toInMillis == 0;
    }


    private void performGetEmployeesByDepartmentRequest() {
        view.showProgress();

        isCalendarUpdating = true;
        final EmployeesByDepartmentRequest request = new EmployeesByDepartmentRequest(isMyTeam, depId);
        getEmployeesByDepartment.execute(request, new DefaultSubscriber<Employee>() {
            @Override
            public void onNext(Employee response) {
                super.onNext(response);
                handleEmployeesByDepartmentSuccessResponse(response);
                performGetAllTimeOffsRequests();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                performGetAllTimeOffsRequests();
            }
        });
    }


    private void handleEmployeesByDepartmentSuccessResponse(@NonNull Employee response) {
        final List<Docs> result = response.getDocs();
        if (result == null || result.isEmpty()) {
            employees.clear();
            view.showNoResults();
        } else {

            Collections.sort(result, (l1, l2) -> {
                final String name1 = l1.getLastName();
                final String name2 = l2.getLastName();
                return (name1).compareToIgnoreCase(name2);
            });

            employees.clear();
            employees.addAll(result);
        }
    }


    private void performGetAllTimeOffsRequests() {
        view.showProgress();
        final TimeOffAllRequest request = new TimeOffAllRequest(dateFrom.getTimeInMillis(), dateTo.getTimeInMillis());
        getAllTimeOffs.execute(request, new DefaultSubscriber<AllTimeOffsDto>() {
            @Override
            public void onNext(AllTimeOffsDto allTimeOffsDto) {
                handleAllTimeOffsSuccessResponse(allTimeOffsDto);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                isCalendarUpdating = false;
                view.hideProgress();
            }
        });
    }


    private void handleAllTimeOffsSuccessResponse(AllTimeOffsDto allTimeOffsDto) {
        UserAllTimeOffsMap userAllTimeOffsMap = prepareMap(allTimeOffsDto);

        if (allTimeOffsDto.getCalendarInfos() == null || allTimeOffsDto.getCalendarInfos().isEmpty()) {
            view.hideProgress();
            view.showNoResults();
        } else {
            if (userAllTimeOffsMap.isEmpty()) {
                view.hideProgress();
                view.showNoResults();
            } else {
                view.updateTableWithDateRange(userAllTimeOffsMap, allTimeOffsDto.getCalendarInfos(), dateFrom, dateTo);
            }
        }
        isCalendarUpdating = false;
    }


    private UserAllTimeOffsMap prepareMap(AllTimeOffsDto allTimeOffs) {
        UserAllTimeOffsMap userAllTimeOffsMap = new UserAllTimeOffsMap();

        List<String> userIds = new ArrayList<>(employees.size());
        for (Docs doc : employees) {
            userIds.add(doc.getId());
        }

        // clean collection
        Collection<List<RequestedTimeOffDto>> lists = allTimeOffs.getMap().values();
        for (List<RequestedTimeOffDto> timeOffList : lists) {
            Iterator<RequestedTimeOffDto> iterator = timeOffList.iterator();
            while (iterator.hasNext()) {
                RequestedTimeOffDto requestedTimeOffDto = iterator.next();
                String userId = requestedTimeOffDto.getUserId();

                if (!userIds.contains(userId)) {
                    iterator.remove();
                }
            }
        }

        // sort data by user
        for (Docs doc : employees) {
            List<UserTimeOff> userTimeOffs = new ArrayList<>();
            List<UserTimeOff> userRequestedTimeOffs = new ArrayList<>();

            // cycle all time offs
            Set<Map.Entry<TimeOffType, List<RequestedTimeOffDto>>> allTimeOffsEntries = allTimeOffs.getMap().entrySet();

            for (Map.Entry<TimeOffType, List<RequestedTimeOffDto>> entry : allTimeOffsEntries) {
                for (RequestedTimeOffDto requestedTimeOffDto : entry.getValue()) {
                    if (requestedTimeOffDto.getUserId().equals(doc.getId())) {
                        UserTimeOff userTimeOff = map(requestedTimeOffDto);

                        if (userTimeOff != null) {
                            userTimeOff.setTimeOffType(entry.getKey());
                            userTimeOffs.add(userTimeOff);
                        }
                    }
                }
            }

            // save user's all time offs
            userAllTimeOffsMap.getMap().put(doc, userTimeOffs);

            // cycle requested time offs only
            Set<Map.Entry<TimeOffType, List<RequestedTimeOffDto>>> requestedTimeOffsEntries = allTimeOffs.getRequestedMap().entrySet();

            for (Map.Entry<TimeOffType, List<RequestedTimeOffDto>> entry : requestedTimeOffsEntries) {
                for (RequestedTimeOffDto requestedTimeOffDto : entry.getValue()) {
                    if (requestedTimeOffDto.getUserId().equals(doc.getId())) {
                        UserTimeOff requestedTimeOff = map(requestedTimeOffDto);

                        if (requestedTimeOff.isAccepted()) {
                            // save requested only
                            requestedTimeOff.setTimeOffType(entry.getKey()); // TimeOffType.REQUESTED
                            userRequestedTimeOffs.add(requestedTimeOff);
                        }

                        // save user's requested time offs
                        userAllTimeOffsMap.getRequestedMap().put(doc, userRequestedTimeOffs);
                    }
                }
            }
        }

        return userAllTimeOffsMap;
    }


    private UserTimeOff map(RequestedTimeOffDto requestedTimeOffDto) {
        if (requestedTimeOffDto == null) {
            return null;
        } else {
            UserTimeOff userTimeOff = new UserTimeOff();
            userTimeOff.setPaid(requestedTimeOffDto.isPaid());
            userTimeOff.setCompanyId(requestedTimeOffDto.getCompanyId());
            userTimeOff.setDateFrom(requestedTimeOffDto.getDateFrom());
            userTimeOff.setDateTo(requestedTimeOffDto.getDateTo());
            userTimeOff.setAccepted(requestedTimeOffDto.getAccepted());

            return userTimeOff;
        }
    }
}