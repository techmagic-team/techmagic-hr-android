package co.techmagic.hr.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.presentation.mvp.presenter.EmployeeDetailsPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.EmployeeDetailsViewImpl;
import co.techmagic.hr.presentation.ui.activity.HomeActivity;

public class EmployeeDetailsFragment extends BaseFragment<EmployeeDetailsViewImpl, EmployeeDetailsPresenter> {

    private Docs data;

    public static EmployeeDetailsFragment newInstance() {
        return new EmployeeDetailsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_details, container, false);
        ButterKnife.bind(this, view);
        getData();
        initUi();
        return view;
    }


    @Override
    protected EmployeeDetailsViewImpl initView() {
        return null;
    }


    @Override
    protected EmployeeDetailsPresenter initPresenter() {
        return new EmployeeDetailsPresenter();
    }


    private void initUi() {

    }


    private void getData() {
        Bundle b = getArguments();
        if (b != null) {
            data = b.getParcelable(HomeActivity.DOCS_OBJECT_PARAM);
        }
    }


}
