package co.techmagic.hr.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.MyProfilePresenter;
import co.techmagic.hr.presentation.mvp.view.impl.MyProfileViewImpl;

public class MyProfileFragment extends BaseFragment<MyProfileViewImpl, MyProfilePresenter> {


    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }


    @Override
    protected MyProfileViewImpl initView() {
        return null;
    }


    @Override
    protected MyProfilePresenter initPresenter() {
        return new MyProfilePresenter();
    }
}
