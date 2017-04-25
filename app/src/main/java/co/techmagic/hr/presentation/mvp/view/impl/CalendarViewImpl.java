package co.techmagic.hr.presentation.mvp.view.impl;

import android.support.v4.app.Fragment;
import android.view.View;

import co.techmagic.hr.presentation.mvp.view.CalendarView;

public abstract class CalendarViewImpl extends ViewImpl implements CalendarView {

    public CalendarViewImpl(Fragment fragment, View contentView) {
        super(fragment, contentView);
    }
}
