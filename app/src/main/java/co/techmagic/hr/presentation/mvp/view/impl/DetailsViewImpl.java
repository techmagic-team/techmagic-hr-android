package co.techmagic.hr.presentation.mvp.view.impl;

import android.support.v4.app.Fragment;
import android.view.View;

import co.techmagic.hr.presentation.mvp.view.DetailsView;

public abstract class DetailsViewImpl extends ViewImpl implements DetailsView {


    public DetailsViewImpl(Fragment fragment, View contentView) {
        super(fragment, contentView);
    }
}
