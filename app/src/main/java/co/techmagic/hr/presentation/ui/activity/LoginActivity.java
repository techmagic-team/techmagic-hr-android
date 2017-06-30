package co.techmagic.hr.presentation.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.data.entity.IFilterModel;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.presentation.mvp.presenter.LoginPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.LoginViewImpl;
import co.techmagic.hr.presentation.ui.EditableFields;
import co.techmagic.hr.presentation.ui.manager.FilterDialogManager;
import co.techmagic.hr.presentation.ui.FilterTypes;
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
    @BindView(R.id.etLoginEmail)
    EditText etLoginEmail;
    @BindView(R.id.etLoginPassword)
    EditText etLoginPassword;
    @BindView(R.id.etLoginForgotPassEmail)
    EditText etLoginForgotPassEmail;
    @BindView(R.id.tilForgotPassEmail)
    TextInputLayout tilForgotPassEmail;
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;
    @BindView(R.id.tvGoToSignIn)
    TextView tvGoToSignIn;
    @BindView(R.id.tvSelectCompany)
    TextView tvSelectCompany;

    private FilterDialogManager dialogManager;
    private boolean isLoginSelected = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
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
            public void showEmptyEmailError() {
                tilEmail.setError(getString(R.string.message_you_can_not_leave_this_empty));
            }

            @Override
            public void onEmailError() {
                tilEmail.setError(getString(R.string.message_invalid_email));
            }

            @Override
            public void hideEmailError() {
                tilEmail.setErrorEnabled(false);
            }

            @Override
            public void showShortPasswordMessage() {
                setPasswordToggleEnabled(true);
                tilPassword.setError(getString(R.string.message_short_password));
            }

            @Override
            public void setPasswordToggleEnabled(boolean enabled) {
                tilPassword.setPasswordVisibilityToggleEnabled(enabled);
            }

            @Override
            public void onPasswordError() {
                tilPassword.setError(getString(R.string.message_invalid_password));
            }

            @Override
            public void hidePasswordError() {
                setPasswordToggleEnabled(true);
                tilPassword.setErrorEnabled(false);
            }

            @Override
            public void onForgotPassEmailError() {
                tilForgotPassEmail.setError(getString(R.string.message_invalid_email));
            }

            @Override
            public void showEmptyForgotPassEmailError() {
                tilForgotPassEmail.setError(getString(R.string.message_you_can_not_leave_this_empty));
            }

            @Override
            public void hideForgotPassEmailError() {
                tilForgotPassEmail.setErrorEnabled(false);
            }

            @Override
            public void onLoginSuccess(@NonNull User user) {
                saveUserAndStartNextScreen(user);
                mixpanelManager.sendLoggedInUserToMixpanel();
            }

            @Override
            public void onForgotPasswordRequestSent() {
                showCheckEmailView();
            }

            @Override
            public void showCompanySelectionDialogView(@NonNull List<Company> companies) {
                dialogManager.showSelectFilterAlertDialog(companies, FilterTypes.COMPANY);
            }

            @Override
            public void onCompanyError() {
                showMessage(getString(R.string.please_select_company_error_text));
            }

            @Override
            public void updateSelectedCompanyView(String name) {
                tvSelectCompany.setText(name);
                dialogManager.dismissDialogIfOpened();
            }
        };
    }


    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
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
    public void onFilterSelected(@NonNull IFilterModel filter) {
        presenter.onCompanySelected(filter.getId(), filter.getName());
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
        if (!fieldContainsError()) {
            hideErrorStates();
            KeyboardUtil.hideKeyboard(this, getCurrentFocus());
            final String email = etLoginEmail.getText().toString().trim();
            final String password = etLoginPassword.getText().toString().trim();
            presenter.onLoginClick(email, password);
        }
    }


    private void handleOnForgotPasswordClick() {
        tilForgotPassEmail.setErrorEnabled(false);
        loginView.setVisibility(View.GONE);
        forgotView.setVisibility(View.VISIBLE);
        tilForgotPassEmail.requestFocus();
        isLoginSelected = false;
    }


    private void handleOnSendClick() {
        if (!fieldContainsError()) {
            KeyboardUtil.hideKeyboard(this, getCurrentFocus());
            tilEmail.setErrorEnabled(false);
            final String email = etLoginForgotPassEmail.getText().toString().trim();
            presenter.onSendClick(email);
        }
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
        tilForgotPassEmail.setErrorEnabled(false);
    }


    private void showCheckEmailView() {
        etLoginForgotPassEmail.setText("");
        tilForgotPassEmail.clearFocus();
        successView.setVisibility(View.VISIBLE);
    }


    private boolean fieldContainsError() {
        return tilEmail.isErrorEnabled() || tilPassword.isErrorEnabled() || tilForgotPassEmail.isErrorEnabled();
    }


    private void initUi() {
        dialogManager = new FilterDialogManager(this, this);
        presenter.onCreate();
        etLoginPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleOnLoginClick();
                return true;
            }
            return false;
        });

        etLoginEmail.addTextChangedListener(getTextChangeListener(etLoginEmail, EditableFields.CHANGE_EMAIL));
        etLoginPassword.addTextChangedListener(getTextChangeListener(etLoginPassword, EditableFields.CHANGE_PASSWORD));
        etLoginForgotPassEmail.addTextChangedListener(getTextChangeListener(etLoginForgotPassEmail, EditableFields.CHANGE_FORGOT_PASS_EMAIL));
    }


    private TextWatcher getTextChangeListener(final TextView textView, final EditableFields field) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView.removeTextChangedListener(this);
                // Do not cut spaces for Password field
                handleSelectedField(field == EditableFields.CHANGE_PASSWORD ? s.toString() : s.toString().trim(), field);
                textView.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }


    private void handleSelectedField(String selectedContent, EditableFields field) {
        switch (field) {
            case CHANGE_EMAIL:
                presenter.handleEmailChange(selectedContent);
                break;

            case CHANGE_PASSWORD:
                presenter.handlePasswordChange(selectedContent);
                break;

            case CHANGE_FORGOT_PASS_EMAIL:
                presenter.handleForgotPassEmailChange(selectedContent);
                break;
        }
    }


    private void saveUserAndStartNextScreen(@NonNull User user) {
        SharedPreferencesUtil.saveAccessToken(user.getAccessToken());
        SharedPreferencesUtil.saveUser(user);
        startHomeScreenWithFlags();
        mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_HOME_TAG);
    }


    private void startHomeScreenWithFlags() {
        Bundle animation = ActivityOptions.makeCustomAnimation(this, R.anim.anim_slide_in, R.anim.anim_not_move).toBundle();
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i, animation);
        finish();
    }
}