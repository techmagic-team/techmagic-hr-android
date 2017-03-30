package co.techmagic.hr.presentation.mvp.presenter;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.domain.interactor.employee.GetEmployee;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.HomeView;

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


    public void loadEmployees(int offset, int visibleItemsCount) {
        if (!isDataLoading && (offset == 0 || visibleItemsCount != allItemsCount)) {
            view.addLoadingProgress();
            performGetEmployeesRequest(offset);
        }
    }


    private void removeLoading() {
        isDataLoading = false;
        view.hideLoadingProgress();
    }


    private void performGetEmployeesRequest(int offset) {
        isDataLoading = true;
        getEmployee.setLimit(ITEMS_COUNT);
        getEmployee.setOffset(offset);
        getEmployee.execute(new DefaultSubscriber<Employee>(view) {
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
    }


    private void handleSuccessResponse(Employee employee) {
        removeLoading();
        allItemsCount = employee.getCount();
        view.showEmployeesList(employee.getDocs());
    }
}