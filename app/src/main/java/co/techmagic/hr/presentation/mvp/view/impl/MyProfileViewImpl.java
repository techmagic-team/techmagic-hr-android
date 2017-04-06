package co.techmagic.hr.presentation.mvp.view.impl;

import android.support.v4.app.Fragment;
import co.techmagic.hr.presentation.mvp.view.MyProfileView;

public abstract class MyProfileViewImpl extends ViewImpl implements MyProfileView {

    public MyProfileViewImpl(Fragment fragment) {
        super(fragment);
    }
}
