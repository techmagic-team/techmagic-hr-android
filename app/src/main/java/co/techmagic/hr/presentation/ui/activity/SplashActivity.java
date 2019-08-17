package co.techmagic.hr.presentation.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import butterknife.ButterKnife;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.presentation.login.LoginActivity;
import co.techmagic.hr.presentation.mvp.presenter.SplashPresenter;
import co.techmagic.hr.presentation.mvp.view.SplashView;
import co.techmagic.hr.presentation.mvp.view.impl.SplashViewImpl;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class SplashActivity extends BaseActivity<SplashView, SplashPresenter> {

    private static final long DELAY = 1000; // To show 2 animations

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_splash);
    }


    @Override
    protected SplashView initView() {
        return new SplashViewImpl(this, findViewById(android.R.id.content)) {

        };
    }


    @Override
    protected SplashPresenter initPresenter() {
        return new SplashPresenter();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initHandler();
        initRunnableWithPostDelay();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }

        view.hideProgress();
    }


    private void initHandler() {
        if (handler == null) {
            handler = new Handler();
        }
    }


    private void initRunnableWithPostDelay() {
        view.showProgress();

        if (runnable == null) {
            runnable = this::startNextScreen;
        }
        handler.postDelayed(runnable, DELAY);
    }


    private void startNextScreen() {
        final User user = SharedPreferencesUtil.readUser();
        Intent i;
        if (user == null) {
            i = new Intent(SplashActivity.this, LoginActivity.class);
        } else {
            i = new Intent(SplashActivity.this, HomeActivity.class);
            mixpanelManager.sendLoggedInUserToMixpanel();
        }

        Bundle animation = ActivityOptions.makeCustomAnimation(this, R.anim.anim_slide_in, R.anim.anim_not_move).toBundle();
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i, animation);
        finish();
    }
}