package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.data.repository.UserRepositoryImpl;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.domain.interactor.employee.GetEmployee;
import co.techmagic.hr.domain.interactor.user.GetUserProfile;
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
    private GetUserProfile getUserProfile;

    private boolean isDataLoading = false;
    private int allItemsCount;
    private UserProfile myProfileData = null;


    public HomePresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        getEmployee = new GetEmployee(employeeRepository);
        getUserProfile = new GetUserProfile(userRepository);
    }


    @Override
    protected void onViewDetached() {
        getEmployee.unsubscribe();
        getUserProfile.unsubscribe();
    }


    public void setupFiltersView(String depId, String leadId, String projectId, String searchQuery) {
        if (depId == null && leadId == null && projectId == null && searchQuery == null) {
            view.hideFiltersView();
        } else {
            view.showFiltersView();
        }
    }


    public void loadEmployeesAfterFilters(@Nullable String searchQuery, @Nullable String selDepId, @Nullable String selLeadId, @Nullable String projectId, int offset, int visibleItemsCount) {
        view.clearAdapter();
        loadEmployees(searchQuery, selDepId, selLeadId, projectId, offset, visibleItemsCount);
    }


    public void loadEmployees(@Nullable String searchQuery, @Nullable String selDepId, @Nullable String selLeadId, @Nullable String projectId, int offset, int visibleItemsCount) {
        if (!isDataLoading && (offset == 0 || visibleItemsCount != allItemsCount)) {
            view.addLoadingProgress();
            performGetEmployeesRequest(searchQuery, selDepId, selLeadId, projectId, offset);
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


    public void handleEmployeeItemClick(@NonNull UserProfile userProfile) {
        if (userProfile.getId().equals(SharedPreferencesUtil.readUser().getId())) {
            // User clicked on own profile
           // view.disallowChangeTabClick();
            myProfileData = userProfile;
            view.showMyProfile(userProfile);
        } else {
            // User clicked on Employee's profile
            view.allowChangeTabClick();
            view.showEmployeeDetails(userProfile);
        }
    }


    private void removeLoading() {
        isDataLoading = false;
        view.hideLoadingProgress();
    }


    private void performGetEmployeesRequest(@Nullable String searchQuery, @Nullable String selDepId, @Nullable String selLeadId, @Nullable String projectId, int offset) {
        isDataLoading = true;
        final EmployeeFiltersRequest request = new EmployeeFiltersRequest(searchQuery, selDepId, selLeadId, projectId, offset, ITEMS_COUNT, false);
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
        getUserProfile.execute(request, new DefaultSubscriber<UserProfile>(view) {
            @Override
            public void onNext(UserProfile userProfile) {
                super.onNext(userProfile);
                myProfileData = userProfile;
                view.hideProgress();
                view.showMyProfile(userProfile);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }
}