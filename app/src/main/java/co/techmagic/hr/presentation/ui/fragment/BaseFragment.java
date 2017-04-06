package co.techmagic.hr.presentation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import co.techmagic.hr.presentation.mvp.presenter.BasePresenter;
import co.techmagic.hr.presentation.mvp.view.View;

public abstract class BaseFragment<VIEW extends View, PRESENTER extends BasePresenter> extends Fragment {

    protected PRESENTER presenter;

    protected VIEW view;

    protected abstract VIEW initView();

    protected abstract PRESENTER initPresenter();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = initView();
        presenter = initPresenter();
        presenter.attachView(view);
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
}
