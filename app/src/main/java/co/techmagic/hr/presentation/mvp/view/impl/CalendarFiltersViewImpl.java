package co.techmagic.hr.presentation.mvp.view.impl;

import android.app.Activity;
import android.view.View;

import co.techmagic.hr.presentation.mvp.view.CalendarFiltersView;

public abstract class CalendarFiltersViewImpl extends ViewImpl implements CalendarFiltersView {

    public CalendarFiltersViewImpl(Activity activity, View contentView) {
        super(activity, contentView, false);
    }
}
