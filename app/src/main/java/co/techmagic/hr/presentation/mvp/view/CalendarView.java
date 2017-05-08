package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;

import java.util.Calendar;

import co.techmagic.hr.presentation.ui.adapter.calendar.AllTimeOffs;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridItem;

public interface CalendarView extends View {

    <T extends IGridItem> void updateTableWithDateRange(@NonNull T item, @NonNull AllTimeOffs allTimeOffs, @NonNull Calendar from, @NonNull Calendar to);

    void updateSelectedFromButtonText(@NonNull String date);

    void updateSelectedToButtonText(@NonNull String date);

    void showDatePicker(@NonNull Calendar from, @NonNull Calendar to, boolean isDateFromPicker);

    void inValidDateRange(int resId);

    void showNoResults();
}
