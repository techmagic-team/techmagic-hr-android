package co.techmagic.hr.presentation.ui.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

public interface OnDisplaySelectedDateListener {

    void displaySelectedFromDate(@NonNull String date, @Nullable Calendar from, @Nullable Calendar to);

    void displaySelectedToDate(@NonNull String date, @Nullable Calendar from, @Nullable Calendar to);

    void invalidDateRangeSelected(int resId);
}
