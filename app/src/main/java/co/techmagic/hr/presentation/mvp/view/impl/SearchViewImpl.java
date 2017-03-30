package co.techmagic.hr.presentation.mvp.view.impl;

import android.app.Activity;
import co.techmagic.hr.presentation.mvp.view.SearchView;

public abstract class SearchViewImpl extends ViewImpl implements SearchView {

    public SearchViewImpl(Activity activity, android.view.View contentView) {
        super(activity, contentView);
    }
}
