package co.techmagic.hr.presentation.ui.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

import co.techmagic.hr.data.entity.Docs;

public interface FragmentCallback {

    void addDetailsFragment(@NonNull Docs docs, @Nullable String tag);

    void addCalendarFragment();

    void addDatePickerFragment(@NonNull CalendarFragment targetFragment, @Nullable Calendar from, @Nullable Calendar to, boolean isDateFromPicker);
}