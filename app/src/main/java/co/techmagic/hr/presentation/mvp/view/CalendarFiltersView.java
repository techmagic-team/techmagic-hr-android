package co.techmagic.hr.presentation.mvp.view;


import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.List;

import co.techmagic.hr.data.entity.Filter;


public interface CalendarFiltersView extends View {

    void updateSelectedFromButtonText(@NonNull String date);

    void updateSelectedToButtonText(@NonNull String date);

    void showDatePicker(@NonNull Calendar from, @NonNull Calendar to, boolean isDateFromPicker);

    void inValidDateRange(int resId);

    void showFilterByDepartmentDialog(@NonNull List<Filter> departments);

    void showSelectedDepartmentFilter(@NonNull String id, @NonNull String filterName);

    void showEmptyDepartmentFiltersErrorMessage(int resId);

    void showFilterByProjectDialog(@NonNull List<Filter> projects);

    void showSelectedProjectFilter(@NonNull String id, @NonNull String filterName);

    void showEmptyProjectFiltersErrorMessage(int resId);
}
