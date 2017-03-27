package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;

import co.techmagic.hr.data.entity.User;

public interface LoginView extends View {

    void onEmailError(int resId);

    void onPasswordError(int resId);

    void onForgotPassEmailError(int resId);

    void onLoginSuccess(@NonNull User user);

    void onForgotPasswordRequestSent();
}
