package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.domain.interactor.employee.GetDepartmentFilters;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.CalendarFiltersView;
import co.techmagic.hr.presentation.util.DateUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class CalendarFiltersPresenter extends BasePresenter<CalendarFiltersView> {

    private IEmployeeRepository employeeRepository;
    private GetDepartmentFilters getDepartmentFilters;
    private List<FilterDepartment> departments;

    private Calendar dateFrom = null;
    private Calendar dateTo = null;


    public CalendarFiltersPresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        getDepartmentFilters = new GetDepartmentFilters(employeeRepository);
        departments = new ArrayList<>();
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getDepartmentFilters.unsubscribe();
    }


    public void setupPage() {
        setDefaultDates();
        performGetDepartmentFiltersRequest();
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
        getDepartmentFilters.execute(new DefaultSubscriber<List<FilterDepartment>>(view) {
            @Override
            public void onNext(List<FilterDepartment> filterDepartments) {
                super.onNext(filterDepartments);
                handleSuccessDepartmentFiltersResponse(filterDepartments);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleSuccessDepartmentFiltersResponse(@NonNull List<FilterDepartment> filterDepartments) {
        view.hideProgress();
        if (filterDepartments.isEmpty()) {
            view.showEmptyDepartmentFiltersErrorMessage(R.string.tm_hr_search_activity_text_empty_department_filters);
        }

        /* Sorting by alphabetical order */
        Collections.sort(filterDepartments, (f1, f2) -> {
            final String name1 = f1.getName();
            final String name2 = f2.getName();
            return (name1).compareToIgnoreCase(name2);
        });

        departments.addAll(filterDepartments);

        final String depId = SharedPreferencesUtil.getSelectedCalendarDepartmentId();
        if (depId != null) {
            for (FilterDepartment d : departments) {
                if (depId.equals(d.getId())) {
                    view.showSelectedDepartmentFilter(d.getId(), d.getName());
                }
            }
        }
    }
}