package co.techmagic.hr.presentation.mvp.view.impl;

import android.app.Activity;
import android.view.View;

import co.techmagic.hr.presentation.mvp.view.SplashView;

/**
 * Created by techmagic on 4/3/17.
 */

public abstract class SplashViewImpl extends ViewImpl implements SplashView {

    public SplashViewImpl(Activity activity, View contentView) {
        super(activity, contentView, true);
    }
}
