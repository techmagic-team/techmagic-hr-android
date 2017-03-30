package co.techmagic.hr.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.presentation.mvp.presenter.LoginPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.LoginViewImpl;
import co.techmagic.hr.presentation.util.KeyboardUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class LoginActivity extends BaseActivity<LoginViewImpl, LoginPresenter> {

    @BindView(R.id.cvLogin)
    View loginView;
    @BindView(R.id.llForgotPass)
    View forgotView;
    @BindView(R.id.llSuccessView)
    View successView;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.tilForgotPassEmail)
    TextInputLayout tilForgotPassEmail;
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;
    @BindView(R.id.tvGoToSignIn)
    TextView tvGoToSignIn;

    private LoginPresenter loginPresenter;
    private boolean isLoginSelected = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_login);
    }


    @Override
    protected LoginViewImpl initView() {
        return new LoginViewImpl(this, findViewById(android.R.id.content)) {
            @Override
            public void onEmailError(int resId) {
                tilEmail.setError(getString(resId));
            }

            @Override
            public void onPasswordError(int resId) {
                tilPassword.setError(getString(resId));
            }

            @Override
            public void onForgotPassEmailError(int resId) {
                tilForgotPassEmail.setError(getString(resId));
            }

            @Override
            public void onLoginSuccess(@NonNull User user) {
                saveUserAndStartNextScreen(user);
            }

            @Override
            public void onForgotPasswordRequestSent() {
                showCheckEmailView();
            }
        };
    }


    @Override
    protected LoginPresenter initPresenter() {
        loginPresenter = new LoginPresenter();
        return loginPresenter;
    }


    @OnClick(R.id.flLogin)
    void onLoginClick() {
        handleOnLoginClick();
    }


    @OnClick(R.id.tvForgotPassword)
    void onForgotPasswordClick() {
        handleOnForgotPasswordClick();
    }


    @OnClick(R.id.flSend)
    void onSendClick() {
        handleOnSendClick();
    }


    @OnClick(R.id.tvGoToSignIn)
    void onGoToSignInClick() {
        handleGoToSignIn();
    }


    @Override
    public void onBackPressed() {
        if (isLoginSelected) {
            super.onBackPressed();
        } else {
            handleGoToSignIn();
        }
    }


    private void handleOnLoginClick() {
        hideErrorStates();
        KeyboardUtil.hideKeyboard(this, getCurrentFocus());
        final String email = tilEmail.getEditText().getText().toString().trim();
        final String password = tilPassword.getEditText().getText().toString().trim();
        presenter.onLoginClick(email, password);
    }


    private void handleOnForgotPasswordClick() {
        tilForgotPassEmail.setErrorEnabled(false);
        loginView.setVisibility(View.GONE);
        forgotView.setVisibility(View.VISIBLE);
        tilForgotPassEmail.requestFocus();
        isLoginSelected = false;
    }


    private void handleOnSendClick() {
        KeyboardUtil.hideKeyboard(this, getCurrentFocus());
        tilEmail.setErrorEnabled(false);
        final String email = tilForgotPassEmail.getEditText().getText().toString().trim();
        presenter.onSendClick(email);
    }


    private void handleGoToSignIn() {
        hideErrorStates();
        forgotView.setVisibility(View.GONE);
        successView.setVisibility(View.GONE);
        loginView.setVisibility(View.VISIBLE);
        tilEmail.requestFocus();
        isLoginSelected = true;
    }


    private void hideErrorStates() {
        tilEmail.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
    }


    private void showCheckEmailView() {
        tilForgotPassEmail.getEditText().setText("");
        tilForgotPassEmail.clearFocus();
        successView.setVisibility(View.VISIBLE);
    }


    private void saveUserAndStartNextScreen(@NonNull User user) {
        SharedPreferencesUtil.saveAccessToken(user.getAccessToken());
        SharedPreferencesUtil.saveUser(user);
        startHomeScreenWithFrags();
    }


    private void startHomeScreenWithFrags() {
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}