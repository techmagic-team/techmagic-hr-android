package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.data.repository.UserRepositoryImpl;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.domain.interactor.employee.GetEmployee;
import co.techmagic.hr.domain.interactor.user.GetMyProfile;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.domain.repository.IUserRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.HomeView;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

import static co.techmagic.hr.presentation.ui.activity.HomeActivity.ITEMS_COUNT;

public class HomePresenter extends BasePresenter<HomeView> {

    private IEmployeeRepository employeeRepository;
    private IUserRepository userRepository;
    private GetEmployee getEmployee;
    private GetMyProfile getMyProfile;

    private boolean isDataLoading = false;
    private int allItemsCount;
    private Docs myProfileData = null;


    public HomePresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        getEmployee = new GetEmployee(employeeRepository);
        getMyProfile = new GetMyProfile(userRepository);
    }


    @Override
    protected void onViewDetached() {
        getEmployee.unsubscribe();
        getMyProfile.unsubscribe();
    }


    public void setupFiltersView(String depId, String leadId, String searchQuery) {
        if (depId == null && leadId == null && searchQuery == null) {
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


    public void handleMyProfileClick() {
        if (myProfileData == null) {
            view.disallowChangeTabClick();
            performGetMyProfileRequest();
        } else {
            view.allowChangeTabClick();
            handleEmployeeItemClick(myProfileData);
        }
    }


    public void handleEmployeeItemClick(@NonNull Docs docs) {
        if (docs.getId().equals(SharedPreferencesUtil.readUser().getId())) {
            // User clicked on it's profile
           // view.disallowChangeTabClick();
            myProfileData = docs;
            view.showMyProfile(docs);
        } else {
            // User clicked on Employee's profile
            view.allowChangeTabClick();
            view.showEmployeeDetails(docs);
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


    private void performGetMyProfileRequest() {
        view.showProgress();
        final String userId = SharedPreferencesUtil.readUser().getId();
        final GetMyProfileRequest request = new GetMyProfileRequest(userId);
        getMyProfile.execute(request, new DefaultSubscriber<Docs>(view) {
            @Override
            public void onNext(Docs docs) {
                super.onNext(docs);
                myProfileData = docs;
                view.hideProgress();
                view.showMyProfile(docs);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });

        userRepository.getMyProfile(request);
    }
}