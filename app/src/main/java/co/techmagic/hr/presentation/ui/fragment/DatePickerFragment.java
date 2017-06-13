package co.techmagic.hr.presentation.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import co.techmagic.hr.presentation.util.DateUtil;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private onDatePickerSelectionListener datePickerSelectionListener;


    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        datePickerSelectionListener = (onDatePickerSelectionListener) context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialog.setCancelable(false);

        return dialog;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        Date date = c.getTime();

        final String fmtDate = DateUtil.getFormattedFullDate(date);

        if (fmtDate != null) {
            datePickerSelectionListener.onDateSelected(fmtDate);
        }
    }


    public interface onDatePickerSelectionListener {
        void onDateSelected(@NonNull String formattedDate);
    }
}