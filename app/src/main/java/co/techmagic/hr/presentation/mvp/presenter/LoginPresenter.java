package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.repository.UserRepositoryImpl;
import co.techmagic.hr.data.request.ForgotPasswordRequest;
import co.techmagic.hr.data.request.LoginRequest;
import co.techmagic.hr.domain.interactor.user.ForgotPassword;
import co.techmagic.hr.domain.interactor.user.LoginUser;
import co.techmagic.hr.domain.repository.IUserRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.LoginView;
import co.techmagic.hr.presentation.util.ValidatingCredentilsUtil;

public class LoginPresenter extends BasePresenter<LoginView> {

    private IUserRepository userRepository;
    private LoginUser loginUser;
    private ForgotPassword forgotPassword;


    public LoginPresenter() {
        super();
        userRepository = new UserRepositoryImpl();
        loginUser = new LoginUser(userRepository);
        forgotPassword = new ForgotPassword(userRepository);
    }


    @Override
    protected void onViewDetached() {
        loginUser.unsubscribe();
    }


    public void onLoginClick(String email, String password) {
        if (isValidCredentials(email, password)) {
            performLogInRequest(email, password);
        }
    }


    public void onSendClick(String email) {
        if (ValidatingCredentilsUtil.isValidEmail(email)) {
            performForgotPasswordRequest(email);
        } else {
            view.onForgotPassEmailError(R.string.message_invalid_email);
        }
    }


    private boolean isValidCredentials(String email, String password) {
        boolean isValid = false;
        if (ValidatingCredentilsUtil.isValidEmail(email)) {
            isValid = true;
        } else {
            isValid = false;
            view.onEmailError(R.string.message_invalid_email);
        }

        if (ValidatingCredentilsUtil.isValidPassword(password)) {
            isValid = true;
        } else {
            isValid = false;
            view.onPasswordError(R.string.message_invalid_password);
        }

        return isValid;
    }


    private void performLogInRequest(@NonNull String email, @NonNull String password) {
        final LoginRequest request = new LoginRequest(IUserRepository.STUB_COMPANY_ID, email, password);
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
                view.showMessage(e.getMessage());
            }
        });
        userRepository.login(request);
    }


    private void performForgotPasswordRequest(@NonNull String email) {
        final ForgotPasswordRequest request = new ForgotPasswordRequest(IUserRepository.STUB_COMPANY_ID, email);
        forgotPassword.execute(request, new DefaultSubscriber<Void>(view) {
            @Override
            public void onNext(Void aVoid) {
                super.onNext(aVoid);
                view.hideProgress();
                view.onForgotPassWordRequestSent();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
                view.showMessage(e.getMessage());
            }
        });
    }
}