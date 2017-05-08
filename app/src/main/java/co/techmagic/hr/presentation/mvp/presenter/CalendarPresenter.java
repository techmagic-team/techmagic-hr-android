package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.data.request.EmployeesByDepartmentRequest;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.domain.interactor.employee.GetAllDayOffs;
import co.techmagic.hr.domain.interactor.employee.GetAllIllnesses;
import co.techmagic.hr.domain.interactor.employee.GetAllVacations;
import co.techmagic.hr.domain.interactor.employee.GetCalendar;
import co.techmagic.hr.domain.interactor.employee.GetEmployeesByDepartment;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.CalendarView;
import co.techmagic.hr.presentation.ui.adapter.calendar.AllTimeOffs;
import co.techmagic.hr.presentation.ui.adapter.calendar.ReadyToDisplayXitem;
import co.techmagic.hr.presentation.util.DateUtil;

public class CalendarPresenter extends BasePresenter<CalendarView> {

    private EmployeeRepositoryImpl employeeRepository;

    private GetAllDayOffs getAllDayOffs;
    private GetAllVacations getAllVacations;
    private GetAllIllnesses getAllIllnesses;
    private GetEmployeesByDepartment getEmployeesByDepartment;
    private GetCalendar getCalendar;

    private AllTimeOffs allTimeOffs;
    private ReadyToDisplayXitem xItem;

    private Calendar dateFrom = null;
    private Calendar dateTo = null;


    public CalendarPresenter() {
        employeeRepository = new EmployeeRepositoryImpl();
        getAllDayOffs = new GetAllDayOffs(employeeRepository);
        getAllVacations = new GetAllVacations(employeeRepository);
        getAllIllnesses = new GetAllIllnesses(employeeRepository);
        getEmployeesByDepartment = new GetEmployeesByDepartment(employeeRepository);
        getCalendar = new GetCalendar(employeeRepository);
        allTimeOffs = new AllTimeOffs();
        xItem = new ReadyToDisplayXitem();
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getAllDayOffs.unsubscribe();
        getAllVacations.unsubscribe();
        getAllIllnesses.unsubscribe();
        getEmployeesByDepartment.unsubscribe();
        getCalendar.unsubscribe();
    }


    public void setupPage() {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        showFromDate(from);
        showToDate(to);
    }


    public void performRequests() {
        performGetEmployeesByDepartmentRequest();
    }


    public void updateCalendar(@Nullable Calendar from, @Nullable Calendar to) {
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
        } else if (dateTo == null) {
            dateTo = c;
            dateTo.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        }

        view.updateTableWithDateRange(xItem, allTimeOffs, dateFrom, dateTo);
    }


    public void onFromButtonClick() {
        view.showDatePicker(dateFrom, dateTo, true);
    }


    public void onToButtonClick() {
        view.showDatePicker(dateFrom, dateTo, false);
    }


    private void showFromDate(Calendar c) {
        c.set(c.get(Calendar.YEAR), Calendar.JANUARY, 1);
        dateFrom = c;
        Date date = c.getTime();
        view.updateSelectedFromButtonText(DateUtil.getFormattedMonthAndYear(date));
    }


    private void showToDate(Calendar c) {
        c.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        dateTo = c;
        Date date = c.getTime();
        view.updateSelectedToButtonText(DateUtil.getFormattedMonthAndYear(date));
    }

    /**
     * Should be called after employees by department only
     */

    private void performGetAllTimeOffsRequests() {
        performGetHolidaysAtCalendarRequest();
        performGetAllDayOffsRequest();
        performGetAllVacationsRequest();
        performGetAllIllnessesRequest();
        // performGetAllRequestedRequest();
    }


    private void performGetEmployeesByDepartmentRequest() {
        view.showProgress();

        final EmployeesByDepartmentRequest request = new EmployeesByDepartmentRequest(false, ""); // todo
        getEmployeesByDepartment.execute(request, new DefaultSubscriber<Employee>(view) {
            @Override
            public void onNext(Employee response) {
                super.onNext(response);
                handleEmployeesByDepartmentSuccessResponse(response);
                performGetAllTimeOffsRequests();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
                performGetAllTimeOffsRequests();
            }
        });
    }


    private void handleEmployeesByDepartmentSuccessResponse(@NonNull Employee response) {
        view.hideProgress();
        final List<Docs> result = response.getDocs();
        if (result == null || result.isEmpty()) {
            view.showNoResults();
        } else {
            xItem.setEmployees(result);
        }
    }


    private void performGetHolidaysAtCalendarRequest() {
        view.showProgress();

        final TimeOffAllRequest request = new TimeOffAllRequest(dateFrom.getTimeInMillis(), dateTo.getTimeInMillis());
        getCalendar.execute(request, new DefaultSubscriber<List<CalendarInfo>>(view) {
            @Override
            public void onNext(List<CalendarInfo> response) {
                super.onNext(response);
                view.hideProgress();
                allTimeOffs.setCalendarInfo(response);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void performGetAllDayOffsRequest() {
        view.showProgress();

        final TimeOffAllRequest request = new TimeOffAllRequest(dateFrom.getTimeInMillis(), dateTo.getTimeInMillis());
        getAllDayOffs.execute(request, new DefaultSubscriber<List<RequestedTimeOff>>(view) {
            @Override
            public void onNext(List<RequestedTimeOff> dayOffs) {
                super.onNext(dayOffs);
                handleAllDayOffsSuccessResponse(dayOffs);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleAllDayOffsSuccessResponse(List<RequestedTimeOff> allDayOffs) {
        view.hideProgress();
        if (allDayOffs == null || allDayOffs.isEmpty()) {

        } else {
            List<RequestedTimeOff> actualDayOffs = new ArrayList<>();
            for (Docs d : xItem.getEmployees()) {
                for (RequestedTimeOff dayOff : allDayOffs) {
                    if (d.getId().equals(dayOff.getUserId())) {
                        actualDayOffs.add(dayOff);
                    }
                }
            }

            allTimeOffs.setDayOffs(actualDayOffs);
        }
    }


    private void performGetAllVacationsRequest() {
        view.showProgress();

        final TimeOffAllRequest request = new TimeOffAllRequest(dateFrom.getTimeInMillis(), dateTo.getTimeInMillis());
        getAllVacations.execute(request, new DefaultSubscriber<List<RequestedTimeOff>>(view) {
            @Override
            public void onNext(List<RequestedTimeOff> vacations) {
                super.onNext(vacations);
                handleAllVacationsSuccessResponse(vacations);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleAllVacationsSuccessResponse(List<RequestedTimeOff> allVacations) {
        view.hideProgress();
        if (allVacations == null || allVacations.isEmpty()) {

        } else {
            List<RequestedTimeOff> actualVacations = new ArrayList<>();
            for (Docs d : xItem.getEmployees()) {
                for (RequestedTimeOff vacation : allVacations) {
                    if (d.getId().equals(vacation.getUserId())) {
                        actualVacations.add(vacation);
                    }
                }
            }

            allTimeOffs.setVacations(actualVacations);
        }
    }


    private void performGetAllIllnessesRequest() {
        view.showProgress();

        final TimeOffAllRequest request = new TimeOffAllRequest(dateFrom.getTimeInMillis(), dateTo.getTimeInMillis());
        getAllIllnesses.execute(request, new DefaultSubscriber<List<RequestedTimeOff>>(view) {
            @Override
            public void onNext(List<RequestedTimeOff> illnesses) {
                super.onNext(illnesses);
                handleAllIllnessesSuccessResponse(illnesses);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleAllIllnessesSuccessResponse(List<RequestedTimeOff> allIllnesses) {
        view.hideProgress();
        if (allIllnesses == null || allIllnesses.isEmpty()) {

        } else {
            List<RequestedTimeOff> actualIllnesses = new ArrayList<>();
            for (Docs d : xItem.getEmployees()) {
                for (RequestedTimeOff illness : allIllnesses) {
                    if (d.getId().equals(illness.getUserId())) {
                        actualIllnesses.add(illness);
                    }
                }
            }

            allTimeOffs.setIllnesses(actualIllnesses);
        }

        // todo remove
        // should be called after all requests only
        view.updateTableWithDateRange(xItem, allTimeOffs, dateFrom, dateTo);
    }


    private void performGetAllRequestedRequest() {
        /*view.showProgress();

        final TimeOffAllRequest request = new TimeOffAllRequest(dateFrom.getTimeInMillis(), dateTo.getTimeInMillis());
        .execute(request, new DefaultSubscriber<List<RequestedTimeOff>>(view) {
            @Override
            public void onNext(List<RequestedTimeOff> requested) {
                super.onNext(requested);
                handleAllRequestedSuccessResponse(requested);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();

                // should be called after all requests only
                view.updateTableWithDateRange(employees, allTimeOffs, dateFrom, dateTo);
            }
        });*/
    }


    private void handleAllRequestedSuccessResponse(List<RequestedTimeOff> allRequested) {
        view.hideProgress();
        if (allRequested == null || allRequested.isEmpty()) {

        } else {
            List<RequestedTimeOff> actualRequested = new ArrayList<>();
            for (Docs d : xItem.getEmployees()) {
                for (RequestedTimeOff illness : allRequested) {
                    if (d.getId().equals(illness.getUserId())) {
                        actualRequested.add(illness);
                    }
                }
            }

            allTimeOffs.setRequested(actualRequested);
        }

        // should be called after all requests only
        // view.updateTableWithDateRange(employees, allTimeOffs, dateFrom, dateTo);
    }
}