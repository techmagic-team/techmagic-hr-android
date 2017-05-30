package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.domain.interactor.employee.GetDepartmentFilters;
import co.techmagic.hr.domain.interactor.employee.GetProjectFilters;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.CalendarFiltersView;
import co.techmagic.hr.presentation.util.DateUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class CalendarFiltersPresenter extends BasePresenter<CalendarFiltersView> {

    private IEmployeeRepository employeeRepository;
    private GetDepartmentFilters getDepartmentFilters;
    private GetProjectFilters getProjectFilters;
    private List<Filter> departments;
    private List<Filter> projects;

    private Calendar dateFrom = null;
    private Calendar dateTo = null;


    public CalendarFiltersPresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        getDepartmentFilters = new GetDepartmentFilters(employeeRepository);
        getProjectFilters = new GetProjectFilters(employeeRepository);
        departments = new ArrayList<>();
        projects = new ArrayList<>();
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getDepartmentFilters.unsubscribe();
        getProjectFilters.unsubscribe();
    }


    public void setupPage() {
        setDefaultDates();
        performGetDepartmentFiltersRequest();
        performGetProjectFiltersRequest();
    }


    public void onFromButtonClick() {
        view.showDatePicker(dateFrom, dateTo, true);
    }


    public void onToButtonClick() {
        view.showDatePicker(dateFrom, dateTo, false);
    }


    public void onDepartmentFilterClick() {
        if (departments == null || departments.isEmpty()) {
            performGetDepartmentFiltersRequest();
        } else {
            view.showFilterByDepartmentDialog(departments);
        }
    }


    public void onProjectFilterClick() {
        if (projects == null || projects.isEmpty()) {
            performGetProjectFiltersRequest();
        } else {
            view.showFilterByProjectDialog(projects);
        }
    }


    public void setDefaultDates() {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();

        final long fromInMillis = SharedPreferencesUtil.getSelectedFromTime();
        final long toInMillis = SharedPreferencesUtil.getSelectedToTime();

        /* Set selected date or from January otherwise */

        if (fromInMillis == 0) {
            showFromJanuaryDate(from);
        } else {
            from.setTimeInMillis(fromInMillis);
            dateFrom = from;
            Date date = from.getTime();
            view.updateSelectedFromButtonText(DateUtil.getFormattedMonthAndYear(date));
        }

        /* Set selected date or to December otherwise */

        if (toInMillis == 0) {
            showToDecemberDate(to);
        } else {
            to.setTimeInMillis(toInMillis);
            dateTo = to;
            Date date = to.getTime();
            view.updateSelectedToButtonText(DateUtil.getFormattedMonthAndYear(date));
        }
    }


    private void showFromJanuaryDate(Calendar c) {
        c.set(c.get(Calendar.YEAR), Calendar.JANUARY, 1);
        dateFrom = c;
        Date date = c.getTime();
        view.updateSelectedFromButtonText(DateUtil.getFormattedMonthAndYear(date));
    }


    private void showToDecemberDate(Calendar c) {
        c.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        dateTo = c;
        Date date = c.getTime();
        view.updateSelectedToButtonText(DateUtil.getFormattedMonthAndYear(date));
    }


    private void performGetDepartmentFiltersRequest() {
        view.showProgress();
        getDepartmentFilters.execute(new DefaultSubscriber<List<Filter>>(view) {
            @Override
            public void onNext(List<Filter> filters) {
                super.onNext(filters);
                handleSuccessDepartmentFiltersResponse(filters);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleSuccessDepartmentFiltersResponse(@NonNull List<Filter> filters) {
        view.hideProgress();
        if (filters.isEmpty()) {
            view.showEmptyDepartmentFiltersErrorMessage(R.string.tm_hr_search_activity_text_empty_department_filters);
        }

        sortByAlphabeticalOrder(filters);
        departments.addAll(filters);

        final String depId = SharedPreferencesUtil.getSelectedCalendarDepartmentId();
        if (depId != null) {
            for (Filter filter : departments) {
                if (depId.equals(filter.getId())) {
                    view.showSelectedDepartmentFilter(filter.getId(), filter.getName());
                }
            }
        }
    }


    private void performGetProjectFiltersRequest() {
        view.showProgress();
        getProjectFilters.execute(new DefaultSubscriber<List<Filter>>(view) {
            @Override
            public void onNext(List<Filter> filters) {
                super.onNext(filters);
                handleSuccessProjectFiltersResponse(filters);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleSuccessProjectFiltersResponse(@NonNull List<Filter> filters) {
        view.hideProgress();
        if (filters.isEmpty()) {
            view.showEmptyProjectFiltersErrorMessage(R.string.tm_hr_search_activity_text_empty_lead_filters);
        }

        sortByAlphabeticalOrder(filters);
        projects.addAll(filters);

        final String projectId = SharedPreferencesUtil.getSelectedCalendarProjectId();
        if (projectId != null) {
            for (Filter filter : projects) {
                if (projectId.equals(filter.getId())) {
                    view.showSelectedProjectFilter(filter.getId(), filter.getName());
                }
            }
        }
    }


    private void sortByAlphabeticalOrder(@NonNull List<Filter> filters) {
        Collections.sort(filters, (f1, f2) -> {
            final String name1 = f1.getName();
            final String name2 = f2.getName();
            return (name1).compareToIgnoreCase(name2);
        });
    }
}