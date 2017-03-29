package co.techmagic.hr.presentation.mvp.presenter;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.domain.interactor.employee.GetEmployee;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.HomeView;

public class HomePresenter extends BasePresenter<HomeView> {

    private IEmployeeRepository employeeRepository;
    private GetEmployee getEmployee;


    public HomePresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        getEmployee = new GetEmployee(employeeRepository);
        performGetEmployeesRequest();
    }


    @Override
    protected void onViewDetached() {
        getEmployee.unsubscribe();
    }


    private void performGetEmployeesRequest() {
        getEmployee.execute(new DefaultSubscriber<Employee>(view) {
            @Override
            public void onNext(Employee employee) {
                super.onNext(employee);
                view.hideProgress();
                view.showEmployeesList(employee.getDocs());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
        employeeRepository.getEmployees();
    }
}