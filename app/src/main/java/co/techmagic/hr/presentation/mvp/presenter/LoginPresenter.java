package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.repository.UserRepositoryImpl;
import co.techmagic.hr.data.request.ForgotPasswordRequest;
import co.techmagic.hr.data.request.LoginRequest;
import co.techmagic.hr.domain.interactor.user.ForgotPassword;
import co.techmagic.hr.domain.interactor.user.GetCompanies;
import co.techmagic.hr.domain.interactor.user.LoginUser;
import co.techmagic.hr.domain.repository.IUserRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.LoginView;
import co.techmagic.hr.presentation.util.TextUtil;

public class LoginPresenter extends BasePresenter<LoginView> {

    private IUserRepository userRepository;
    private LoginUser loginUser;
    private ForgotPassword forgotPassword;
    private GetCompanies getCompanies;

    private List<Company> companyList = new ArrayList<>();
    private String id;
    private String name;

    public LoginPresenter() {
        super();
        userRepository = new UserRepositoryImpl();
        loginUser = new LoginUser(userRepository);
        forgotPassword = new ForgotPassword(userRepository);
        getCompanies = new GetCompanies(userRepository);
    }


    @Override
    protected void onViewDetached() {
        loginUser.unsubscribe();
        forgotPassword.unsubscribe();
        getCompanies.unsubscribe();
    }


    public void onCreate() {
        performGetCompaniesRequest();
    }


    public void onLoginClick(String email, String password) {
        if (isValidCredentials(email, password)) {
            performLogInRequest(email, password);
        }
    }


    public void onSendClick(String email) {
        if (id == null) {
            view.onCompanyError();
            return;
        }

        // In case if user left field empty
        if (email.isEmpty()) {
            view.showEmptyForgotPassEmailError();
            return;
        }

        if (TextUtil.isValidEmail(email)) {
            performForgotPasswordRequest(email);
        } else {
            view.onForgotPassEmailError();
        }
    }


    public void handleSelectCompanyClick() {
        if (companyList == null || companyList.isEmpty()) {
            view.showProgress();
            performGetCompaniesRequest();
        } else {
            view.showCompanySelectionDialogView(companyList);
        }
    }


    public void handleEmailChange(String email) {
        // In case if user left field empty
        if (email.isEmpty()) {
            view.showEmptyEmailError();
            return;
        }

        if (TextUtil.isValidEmail(email)) {
            view.hideEmailError();
        } else {
            view.onEmailError();
        }
    }


    public void handlePasswordChange(String password) {
        if (password.isEmpty()) {
            view.setPasswordToggleEnabled(false);
            view.hidePasswordError();
            return;
        }

        if (password.length() < TextUtil.PASSWORD_MINIMUM_LENGTH) {
            view.showShortPasswordMessage();
            return;
        }

        if (TextUtil.isValidPassword(password)) {
            view.hidePasswordError();
        } else {
            view.onPasswordError();
        }
    }


    public void handleForgotPassEmailChange(String email) {
        // In case if user left field empty
        if (email.isEmpty()) {
            view.showEmptyForgotPassEmailError();
            return;
        }

        if (TextUtil.isValidEmail(email)) {
            view.hideForgotPassEmailError();
        } else {
            view.onForgotPassEmailError();
        }
    }


    public void onCompanySelected(String id, String name) {
        this.id = id;
        this.name = name;

        view.updateSelectedCompanyView(name);
    }


    private boolean isValidCredentials(String email, String password) {
        boolean isValid;

        if (id == null) {
            view.onCompanyError();
            return false;
        }

        if (TextUtil.isValidEmail(email)) {
            isValid = true;
        } else {
            isValid = false;
            view.onEmailError();
        }

        if (isValid) {
            if (TextUtil.isValidPassword(password)) {
                isValid = true;
            } else {
                isValid = false;
                view.onPasswordError();
            }
        }

        return isValid;
    }


    private void performLogInRequest(@NonNull String email, @NonNull String password) {
        view.showProgress();
        final LoginRequest request = new LoginRequest(id, email, password);
        loginUser.execute(request, new DefaultSubscriber<User>(view) {
            @Override
            public void onNext(User user) {
                super.onNext(user);
                view.hideProgress();
                view.onLoginSuccess(user);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void performForgotPasswordRequest(@NonNull String email) {
        view.showProgress();
        final ForgotPasswordRequest request = new ForgotPasswordRequest(id, email);
        forgotPassword.execute(request, new DefaultSubscriber<Void>(view) {
            @Override
            public void onNext(Void aVoid) {
                super.onNext(aVoid);
                view.hideProgress();
                view.onForgotPasswordRequestSent();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void performGetCompaniesRequest() {
        getCompanies.execute(null, new DefaultSubscriber<List<Company>>(view) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }

            @Override
            public void onNext(List<Company> companies) {
                super.onNext(companies);
                view.hideProgress();
                companyList.addAll(companies);
            }
        });
    }
}