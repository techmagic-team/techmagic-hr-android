package co.techmagic.hr.presentation.mvp.view.impl;

import android.app.Fragment;
import co.techmagic.hr.presentation.mvp.view.EmployeeDetailsView;

public abstract class EmployeeDetailsViewImpl extends ViewImpl implements EmployeeDetailsView {


    public EmployeeDetailsViewImpl(Fragment fragment) {
        super(fragment);
    }
}
