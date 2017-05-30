package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.IFilterModel;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.domain.interactor.employee.GetDepartmentFilters;
import co.techmagic.hr.domain.interactor.employee.GetLeadFilters;
import co.techmagic.hr.domain.interactor.employee.GetProjectFilters;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.SearchView;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class SearchPresenter extends BasePresenter<SearchView> {

    private IEmployeeRepository employeeRepository;
    private GetDepartmentFilters getDepartmentFilters;
    private GetLeadFilters getLeadFilters;
    private GetProjectFilters getProjectFilters;

    private List<Filter> departments;
    private List<FilterLead> leads;
    private List<Filter> projects;


    public SearchPresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        getDepartmentFilters = new GetDepartmentFilters(employeeRepository);
        getLeadFilters = new GetLeadFilters(employeeRepository);
        getProjectFilters = new GetProjectFilters(employeeRepository);
        departments = new ArrayList<>();
        leads = new ArrayList<>();
        projects = new ArrayList<>();
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


    public void onFilterByProjectClick() {
        if (projects == null || projects.isEmpty()) {
            performGetProjectFiltersRequest();
        } else {
            view.showFilterByProjectDialog(projects);
        }
    }


    public void performGetFiltersRequests() {
        performGetDepartmentFiltersRequest();
        performGetLeadFiltersRequest();
        performGetProjectFiltersRequest();
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

        final String depId = SharedPreferencesUtil.getSelectedDepartmentId();
        if (depId != null) {
            for (Filter d : departments) {
                if (depId.equals(d.getId())) {
                    view.showSelectedDepartmentFilter(d.getId(), d.getName());
                }
            }
        }
    }


    private void performGetLeadFiltersRequest() {
        view.showProgress();
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
            view.showEmptyLeadFiltersErrorMessage(R.string.tm_hr_search_activity_text_empty_lead_filters);
        }

        sortByAlphabeticalOrder(filterLeads);
        leads.addAll(filterLeads);

        final String leadId = SharedPreferencesUtil.getSelectedLeadId();
        if (leadId != null) {
            for (FilterLead l : leads) {
                if (leadId.equals(l.getId())) {
                    view.showSelectedLeadFilter(l.getId(), l.getName());
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

        final String projectId = SharedPreferencesUtil.getSelectedProjectId();
        if (projectId != null) {
            for (Filter filter : projects) {
                if (projectId.equals(filter.getId())) {
                    view.showSelectedProjectFilter(filter.getId(), filter.getName());
                }
            }
        }
        view.requestSearchViewFocus();
    }


    private <T extends IFilterModel> void sortByAlphabeticalOrder(@NonNull List<T> filters) {
        Collections.sort(filters, (f1, f2) -> {
            final String name1 = f1.getName();
            final String name2 = f2.getName();
            return (name1).compareToIgnoreCase(name2);
        });
    }
}