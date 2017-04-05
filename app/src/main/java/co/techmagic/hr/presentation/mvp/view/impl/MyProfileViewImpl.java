package co.techmagic.hr.presentation.mvp.view.impl;

import android.app.Fragment;
import co.techmagic.hr.presentation.mvp.view.MyProfileView;

public abstract class MyProfileViewImpl extends ViewImpl implements MyProfileView {

    public MyProfileViewImpl(Fragment fragment) {
        super(fragment);
    }
}
