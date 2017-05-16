package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
import co.techmagic.hr.presentation.ui.adapter.calendar.ReadyToDisplayXitem;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class CalendarPresenter extends BasePresenter<CalendarView> {

    private EmployeeRepositoryImpl employeeRepository;
    private GetEmployeesByDepartment getEmployeesByDepartment;
    private GetCalendar getCalendar;
    private GetAllTimeOffs getAllTimeOffs;

    private ReadyToDisplayXitem xItem;

    private Calendar dateFrom = null;
    private Calendar dateTo = null;

    private boolean isMyTeam;
    private long fromInMillis = 0;
    private long toInMillis = 0;
    private String depId;


    public CalendarPresenter() {
        employeeRepository = new EmployeeRepositoryImpl();

        getAllTimeOffs = new GetAllTimeOffs(employeeRepository);
        getEmployeesByDepartment = new GetEmployeesByDepartment(employeeRepository);
        getCalendar = new GetCalendar(employeeRepository);
        xItem = new ReadyToDisplayXitem();
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
            updateCalendar(isMyTeam, depId, null, null);
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

            updateCalendar(isMyTeam, depId, from, to);
        }
    }


    public void updateCalendar(boolean isMyTeamChecked, String selDepId, @Nullable Calendar from, @Nullable Calendar to) {
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

        performRequests();
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


    private void performRequests() {
        performGetEmployeesByDepartmentRequest();
    }


    private boolean noFiltersSelected(@Nullable Calendar from, @Nullable Calendar to) {
        return isMyTeam && depId == null && from == null && to == null && fromInMillis == 0 && toInMillis == 0;
    }


    private void performGetAllTimeOffsRequests() {
        view.showProgress();
        final TimeOffAllRequest request = new TimeOffAllRequest(dateFrom.getTimeInMillis(), dateTo.getTimeInMillis());
        getAllTimeOffs.execute(request, new DefaultSubscriber<AllTimeOffsDto>(view) {
            @Override
            public void onNext(AllTimeOffsDto allTimeOffsDto) {
                view.hideProgress();

                UserAllTimeOffsMap userAllTimeOffsMap = prepareMap(xItem, allTimeOffsDto);
                view.updateTableWithDateRange(userAllTimeOffsMap, allTimeOffsDto.getCalendarInfo(), dateFrom, dateTo);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void performGetEmployeesByDepartmentRequest() {
        view.showProgress();

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
            view.showNoResults();
        } else {
            xItem.setEmployees(result);
        }
    }


    private UserAllTimeOffsMap prepareMap(ReadyToDisplayXitem xItem, AllTimeOffsDto allTimeOffs) {
        UserAllTimeOffsMap userAllTimeOffsMap = new UserAllTimeOffsMap();

        List<String> userIds = new ArrayList<>(xItem.getEmployees().size());
        for (Docs doc : xItem.getEmployees()) {
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
        for (Docs doc : xItem.getEmployees()) {

            List<UserTimeOff> userTimeOffs = new ArrayList<>();

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

            userAllTimeOffsMap.getMap().put(doc, userTimeOffs);
        }

        return userAllTimeOffsMap;
    }


    private UserTimeOff map(RequestedTimeOffDto requestedTimeOffDto) {
        if (requestedTimeOffDto == null) {
            return null;
        } else {
            UserTimeOff userTimeOff = new UserTimeOff();
            userTimeOff.setPaid(requestedTimeOffDto.isPaid());
            userTimeOff.setAccepted(requestedTimeOffDto.isAccepted());
            userTimeOff.setCompanyId(requestedTimeOffDto.getCompanyId());
            userTimeOff.setDateFrom(requestedTimeOffDto.getDateFrom());
            userTimeOff.setDateTo(requestedTimeOffDto.getDateTo());

            return userTimeOff;
        }
    }
}