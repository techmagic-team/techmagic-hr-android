package co.techmagic.hr.presentation.mvp.view.impl;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.BasePresenter;
import co.techmagic.hr.presentation.mvp.view.View;
import co.techmagic.hr.presentation.ui.view.AnimatedProgressDialog;
import co.techmagic.hr.presentation.ui.view.ProgressDialogHelper;

public abstract class ViewImpl implements View {

    private android.view.View contentView;
    private Activity activity;
    private Fragment fragment;
    private ProgressDialogHelper progressDialogHelper;
    private AnimatedProgressDialog animatedDialog;


    public ViewImpl(Activity activity, android.view.View contentView, boolean isProgressWhiteBackground) {
        this.activity = activity;
        this.contentView = contentView;
        init(isProgressWhiteBackground);
    }


    public ViewImpl(Fragment fragment, android.view.View contentView) {
        this.fragment = fragment;
        this.contentView = contentView;
        init(false);
    }


    private void init(boolean isSplash) {
        progressDialogHelper = new ProgressDialogHelper();
        animatedDialog = new AnimatedProgressDialog(getContext(), isSplash ? R.style.DialogThemeWhite : R.style.DialogThemeDimmed);
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showMessage(int messageResId) {
        showMessage(getContext().getString(messageResId));
    }


    @Override
    public void showConnectionErrorMessage() {
        Snackbar.make(contentView, getContext().getString(R.string.message_connection_error), Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void showProgress() {
        animatedDialog.show();
    }


    @Override
    public void showProgress(String message) {
        animatedDialog.setMessage(message);
        animatedDialog.show();
    }


    @Override
    public void showProgress(int messageResId) {
        showProgress(getContext().getString(messageResId));
    }


    @Override
    public void showProgress(String message, String title) {
        progressDialogHelper.showProgress(getContext(), message, title);
    }


    @Override
    public void showProgress(int messageResId, int titleResId) {
        showProgress(getContext().getString(messageResId), getContext().getString(titleResId));
    }


    @Override
    public void hideProgress() {
        animatedDialog.hide();
    }


    @Override
    public void showSnackBarMessage(String message) {
        Snackbar.make(contentView, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void showSnackBarWrongLoginCredentialsError() {
        Snackbar.make(contentView, getContext().getString(R.string.message_wrong_password_or_email), Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void showSnackBarWrongCompanyOrEmailError() {
        Snackbar.make(contentView, getContext().getString(R.string.message_wrong_company_or_email), Snackbar.LENGTH_LONG).show();
    }


    public void initSwipeToRefresh(SwipeRefreshLayout swipeRefreshLayout, BasePresenter presenter) {
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
    }


    private Context getContext() {
        if (activity != null) {
            return activity;
        } else if (fragment != null) {
            return fragment.getContext();
        }

        return null;
    }
}