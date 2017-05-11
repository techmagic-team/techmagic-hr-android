package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;

import java.util.Calendar;

import co.techmagic.hr.presentation.ui.adapter.calendar.AllTimeOffs;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridItem;

public interface CalendarView extends View {

    <T extends IGridItem> void updateTableWithDateRange(@NonNull T item, @NonNull AllTimeOffs allTimeOffs, @NonNull Calendar from, @NonNull Calendar to);

    void showNoResults();

    void showClearFilters();

    void hideClearFilters();
}
