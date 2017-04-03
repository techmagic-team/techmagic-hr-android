package co.techmagic.hr.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.BasePresenter;
import co.techmagic.hr.presentation.mvp.view.View;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public abstract class BaseActivity<VIEW extends View, PRESENTER extends BasePresenter> extends AppCompatActivity {

    protected VIEW view;
    protected PRESENTER presenter;

    protected abstract void initLayout();

    protected abstract VIEW initView();

    protected abstract PRESENTER initPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        view = initView();
        presenter = initPresenter();
        presenter.attachView(view);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
        presenter.pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }


    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }


    protected void startLoginScreen() {
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    protected void startHomeScreen() {
        startActivity(new Intent(this, HomeActivity.class));
    }


    protected void showLogOutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.message_alert_dialog_title_log_out))
                .setMessage(getString(R.string.message_alert_dialog_message_are_you_sure_you_want_to_log_out))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> logOut())
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void logOut() {
        SharedPreferencesUtil.clearPreferences();
        startLoginScreen();
    }
}
