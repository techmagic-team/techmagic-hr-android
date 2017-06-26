package co.techmagic.hr.presentation.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import co.techmagic.hr.presentation.mvp.presenter.BasePresenter;
import co.techmagic.hr.presentation.mvp.view.View;
import co.techmagic.hr.presentation.ui.manager.MixpanelManager;

public abstract class BaseFragment<VIEW extends View, PRESENTER extends BasePresenter> extends Fragment {

    protected PRESENTER presenter;

    protected VIEW view;

    protected abstract VIEW initView();

    protected abstract PRESENTER initPresenter();

    protected static final int RC_WRITE_EXTERNAL_STORAGE_PERMISSION = 1002;

    protected MixpanelManager mixpanelManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = initView();
        presenter = initPresenter();
        presenter.attachView(view);
        mixpanelManager = new MixpanelManager(getContext());
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }


    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }


    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }


    @Override
    public void onDestroy() {
        mixpanelManager.flush();
        super.onDestroy();
    }


    protected boolean isWriteExternalStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    protected void requestWriteExternalStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_WRITE_EXTERNAL_STORAGE_PERMISSION);
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }


    protected void removeFragmentFromBackStack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}