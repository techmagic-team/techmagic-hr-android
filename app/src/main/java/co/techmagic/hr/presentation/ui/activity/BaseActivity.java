package co.techmagic.hr.presentation.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import co.techmagic.hr.presentation.mvp.presenter.BasePresenter;
import co.techmagic.hr.presentation.mvp.view.View;

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
}
