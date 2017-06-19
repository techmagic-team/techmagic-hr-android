package co.techmagic.hr.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.presentation.mvp.presenter.LoginPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.LoginViewImpl;
import co.techmagic.hr.presentation.ui.adapter.FilterAdapter;
import co.techmagic.hr.presentation.util.KeyboardUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class LoginActivity extends BaseActivity<LoginViewImpl, LoginPresenter> implements FilterAdapter.OnFilterSelectionListener {

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
    @BindView(R.id.tvSelectCompany)
    TextView tvSelectCompany;

    private AlertDialog dialog;

    private LoginPresenter loginPresenter;
    private boolean isLoginSelected = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        presenter.onCreate();
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void onStop() {
        super.onStop();
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

            @Override
            public void showCompanySelectionDialogView(@NonNull List<Company> companies) {
                showSelectFilterAlertDialog(companies);
            }

            @Override
            public void onCompanyError() {
                showMessage(getString(R.string.please_select_company_error_text));
            }

            @Override
            public void updateSelectedCompanyView(String name) {
                tvSelectCompany.setText(name);
                dismissDialogIfOpened();
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


    @OnClick(R.id.rlFilterCompany)
    void onSelectCompanyClick() {
        presenter.handleSelectCompanyClick();
    }


    @Override
    public void onFilterSelected(@NonNull String id, @NonNull String name) {
        presenter.onCompanySelected(id, name);
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


    private void showSelectFilterAlertDialog(@Nullable List<Company> companies) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setupDialogViews(companies, builder);
        dialog = builder.show();
        dialog.findViewById(R.id.btnAlertDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }


    private void setupDialogViews(@Nullable List<Company> companies, AlertDialog.Builder builder) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_select_company_filter, null);
        builder.setView(view);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        setupSelectFilterRecyclerView(view, companies);
        setupDialogTitle(tvTitle);
    }


    private void setupDialogTitle(@NonNull TextView tvTitle) {
        tvTitle.setText(R.string.select_company_text);
    }


    private void setupSelectFilterRecyclerView(View view, @Nullable List<Company> companies) {
        RecyclerView rvFilters = (RecyclerView) view.findViewById(R.id.rvFilters);
        rvFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FilterAdapter adapter = new FilterAdapter(this, true);
        rvFilters.setAdapter(adapter);

        adapter.refresh(companies);
    }


    private void dismissDialogIfOpened() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}