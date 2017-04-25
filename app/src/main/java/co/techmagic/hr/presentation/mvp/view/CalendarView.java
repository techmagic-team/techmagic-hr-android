package co.techmagic.hr.presentation.mvp.view;


import android.support.annotation.NonNull;

public interface CalendarView extends View {

    void displaySelectedFromDate(@NonNull String date);

    void displaySelectedToDate(@NonNull String date);

}
