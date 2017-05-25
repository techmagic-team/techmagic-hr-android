package co.techmagic.hr.presentation.mvp.view;


import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.List;

import co.techmagic.hr.data.entity.FilterDepartment;


public interface CalendarFiltersView extends View {

    void updateSelectedFromButtonText(@NonNull String date);

    void updateSelectedToButtonText(@NonNull String date);

    void showDatePicker(@NonNull Calendar from, @NonNull Calendar to, boolean isDateFromPicker);

    void inValidDateRange(int resId);

    void showFilterByDepartmentDialog(@NonNull List<FilterDepartment> departments);

    void showSelectedDepartmentFilter(@NonNull String id, @NonNull String filterName);

    void showEmptyDepartmentFiltersErrorMessage(int resId);
}
