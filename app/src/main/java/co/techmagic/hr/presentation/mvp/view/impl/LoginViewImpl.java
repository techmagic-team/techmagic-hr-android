package co.techmagic.hr.presentation.mvp.view.impl;

import android.app.Activity;
import android.view.View;

import co.techmagic.hr.presentation.mvp.view.LoginView;

public abstract class LoginViewImpl extends ViewImpl implements LoginView {

    public LoginViewImpl(Activity activity, View contentView) {
        super(activity, contentView);
    }
}