package co.techmagic.hr.presentation.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.LoginPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.LoginViewImpl;

public class LoginActivity extends BaseActivity<LoginViewImpl, LoginPresenter> {

    @BindView(R.id.llLogin)
    LinearLayout loginView;
    @BindView(R.id.llForgotPass)
    LinearLayout forgotView;
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;
    @BindView(R.id.tvGoToSignIn)
    TextView tvGoToSignIn;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @Override
    protected void initLayout() {

    }


    @Override
    protected LoginViewImpl initView() {
        return null;
    }


    @Override
    protected LoginPresenter initPresenter() {
        loginPresenter = new LoginPresenter();
        return loginPresenter;
    }


    @OnClick(R.id.tvForgotPassword)
    void onForgotPasswordClick() {
        loginView.setVisibility(android.view.View.GONE);
        forgotView.setVisibility(android.view.View.VISIBLE);
    }


    @OnClick(R.id.tvGoToSignIn)
    void onGoToSignInClick() {
        forgotView.setVisibility(android.view.View.GONE);
        loginView.setVisibility(android.view.View.VISIBLE);
    }
}