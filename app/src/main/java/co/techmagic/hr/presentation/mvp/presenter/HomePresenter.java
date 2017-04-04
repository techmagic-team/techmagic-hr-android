package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.Nullable;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.domain.interactor.employee.GetEmployee;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.HomeView;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

import static co.techmagic.hr.presentation.ui.activity.HomeActivity.ITEMS_COUNT;

public class HomePresenter extends BasePresenter<HomeView> {

    private IEmployeeRepository employeeRepository;
    private GetEmployee getEmployee;

    private boolean isDataLoading = false;
    private int allItemsCount;


    public HomePresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        getEmployee = new GetEmployee(employeeRepository);
    }


    @Override
    protected void onViewDetached() {
        getEmployee.unsubscribe();
    }


    public void setupFiltersView() {
        final String depId = SharedPreferencesUtil.getSelectedDepartmentId();
        final String leadId = SharedPreferencesUtil.getSelectedLeadId();
        if (depId == null && leadId == null) {
            view.hideFiltersView();
        } else {
            view.showFiltersView();
        }
    }


    public void loadEmployeesAfterFilters(@Nullable String searchQuery, @Nullable String selDepId, @Nullable String selLeadId, int offset, int visibleItemsCount) {
        view.clearAdapter();
        loadEmployees(searchQuery, selDepId, selLeadId, offset, visibleItemsCount);
    }


    public void loadEmployees(@Nullable String searchQuery, @Nullable String selDepId, @Nullable String selLeadId, int offset, int visibleItemsCount) {
        if (!isDataLoading && (offset == 0 || visibleItemsCount != allItemsCount)) {
            view.addLoadingProgress();
            performGetEmployeesRequest(searchQuery, selDepId, selLeadId, offset);
        }
    }


    private void removeLoading() {
        isDataLoading = false;
        view.hideLoadingProgress();
    }


    private void performGetEmployeesRequest(@Nullable String searchQuery, @Nullable String selDepId, @Nullable String selLeadId, int offset) {
        isDataLoading = true;
        final EmployeeFiltersRequest request = new EmployeeFiltersRequest(searchQuery, selDepId, selLeadId, offset, ITEMS_COUNT, false);
        getEmployee.execute(request, new DefaultSubscriber<Employee>(view) {
            @Override
            public void onNext(Employee employee) {
                super.onNext(employee);
                handleSuccessResponse(employee);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                removeLoading();
            }
        });

        employeeRepository.getEmployees(request);
    }


    private void handleSuccessResponse(Employee employee) {
        removeLoading();
        allItemsCount = employee.getCount();

        if (allItemsCount == 0) {
            view.showNoResultsView(R.string.message_no_results);
        } else {
            view.showEmployeesList(employee.getDocs());
        }
    }
}