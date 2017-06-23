package co.techmagic.hr.presentation.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.BasePresenter;
import co.techmagic.hr.presentation.mvp.view.View;
import co.techmagic.hr.presentation.ui.fragment.BaseFragment;

public abstract class BaseActivity<VIEW extends View, PRESENTER extends BasePresenter> extends AppCompatActivity {

    protected VIEW view;
    protected PRESENTER presenter;

    protected abstract void initLayout();

    protected abstract VIEW initView();

    protected abstract PRESENTER initPresenter();

    protected static final int RC_READ_EXTERNAL_STORAGE_PERMISSION = 1004;

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


    protected void replaceFragment(BaseFragment fragment, String tag) {
        if (tag == null) {
            tag = fragment.getClass().getName();
        }
        FragmentManager fm = getSupportFragmentManager();
        boolean fragmentPopped = fm.popBackStackImmediate(tag, 0);

        if (!fragmentPopped && fm.findFragmentByTag(tag) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rlFragmentsContainer, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }


    protected void clearFragmentsBackStack(@NonNull FragmentActivity fragmentActivity) {
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }


    protected void removeFragmentFromBackStack() {
        getSupportFragmentManager().popBackStack();
    }


    protected boolean isReadExternalStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    protected void requestReadExternalStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_READ_EXTERNAL_STORAGE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }


    protected void showLogOutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.message_alert_dialog_title_log_out))
                .setMessage(getString(R.string.message_alert_dialog_message_are_you_sure_you_want_to_log_out))
                .setPositiveButton(R.string.message_text_yes, (dialog, which) -> view.logOut())
                .setNegativeButton(R.string.message_text_no, (dialog, which) -> dialog.dismiss())
                .show();
    }
}