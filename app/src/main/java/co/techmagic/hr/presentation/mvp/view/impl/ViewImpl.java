package co.techmagic.hr.presentation.mvp.view.impl;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.BasePresenter;
import co.techmagic.hr.presentation.mvp.view.View;
import co.techmagic.hr.presentation.ui.view.ProgressDialogHelper;

public abstract class ViewImpl implements View {

    private Activity activity;
    private Fragment fragment;
    private ProgressDialogHelper progressDialogHelper;


    public ViewImpl(Activity activity) {
        this.activity = activity;
        init();
    }


    public ViewImpl(Fragment fragment) {
        this.fragment = fragment;
        init();
    }


    private void init() {
        progressDialogHelper = new ProgressDialogHelper();
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
        showMessage(getContext().getString(R.string.message_connection_error));
    }


    @Override
    public void showProgress() {
        progressDialogHelper.showProgress(getContext(), getContext().getString(R.string.message_loading));
    }


    @Override
    public void showProgress(String message) {
        progressDialogHelper.showProgress(getContext(), message);
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
        progressDialogHelper.hideProgress();
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