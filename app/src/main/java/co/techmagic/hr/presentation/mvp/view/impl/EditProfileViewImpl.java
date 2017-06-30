package co.techmagic.hr.presentation.mvp.view.impl;

import android.app.Activity;
import android.view.View;

import co.techmagic.hr.presentation.mvp.view.EditProfileView;

public abstract class EditProfileViewImpl extends ViewImpl implements EditProfileView {

    public EditProfileViewImpl(Activity activity, View contentView) {
        super(activity, contentView, false);
    }
}