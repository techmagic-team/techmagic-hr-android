package co.techmagic.hr.presentation.ui.view.calendar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class ScrollLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;


    public ScrollLayoutManager(Context context) {
        super(context);
    }


    public ScrollLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }


    public ScrollLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void enableScroll(boolean enableScroll) {
        this.isScrollEnabled = enableScroll;
    }


    public boolean isScrollEnabled() {
        return isScrollEnabled;
    }


    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}