package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.domain.interactor.employee.GetDepartmentFilters;
import co.techmagic.hr.domain.interactor.employee.GetLeadFilters;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.SearchView;

public class SearchPresenter extends BasePresenter<SearchView> {

    private IEmployeeRepository employeeRepository;
    private GetDepartmentFilters getDepartmentFilters;
    private GetLeadFilters getLeadFilters;

    private List<FilterDepartment> departments = new ArrayList<>();
    private List<FilterLead> leads = new ArrayList<>();


    public SearchPresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        getDepartmentFilters = new GetDepartmentFilters(employeeRepository);
        getLeadFilters = new GetLeadFilters(employeeRepository);
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getDepartmentFilters.unsubscribe();
        getLeadFilters.unsubscribe();
    }


    public void onDepartmentFilterClick() {
        if (departments == null || departments.isEmpty()) {
            performGetDepartmentFiltersRequest();
        } else {
            view.showFilterByDepartmentDialog(departments);
        }
    }


    public void onLeadFilterClick() {
        if (leads == null || leads.isEmpty()) {
            performGetLeadFiltersRequest();
        } else {
            view.showFilterByLeadDialog(leads);
        }
    }


    public void performGetFiltersRequests() {
        performGetDepartmentFiltersRequest();
        performGetLeadFiltersRequest();
    }


    private void performGetDepartmentFiltersRequest() {
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
            view.showEmptyDepartmentFilters(R.string.tm_hr_search_activity_text_empty_department_filters);
        }
        departments.addAll(filterDepartments);
    }


    private void performGetLeadFiltersRequest() {
        getLeadFilters.execute(new DefaultSubscriber<List<FilterLead>>(view) {
            @Override
            public void onNext(List<FilterLead> filterLeads) {
                super.onNext(filterLeads);
                handleSuccessLeadFiltersResponse(filterLeads);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleSuccessLeadFiltersResponse(@NonNull List<FilterLead> filterLeads) {
        view.hideProgress();
        if (filterLeads.isEmpty()) {
            view.showEmptyLeadFilters(R.string.tm_hr_search_activity_text_empty_lead_filters);
        }
        leads.addAll(filterLeads);
    }
}