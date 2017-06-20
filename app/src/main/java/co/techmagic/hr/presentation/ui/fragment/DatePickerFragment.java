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

    private static final String ALLOW_SELECT_FUTURE_DATE = "allow_select_future_date";
    private onDatePickerSelectionListener datePickerSelectionListener;


    public static DatePickerFragment newInstance(boolean allowFutureDate) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ALLOW_SELECT_FUTURE_DATE, allowFutureDate);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        datePickerSelectionListener = (onDatePickerSelectionListener) context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        boolean allowFutureDate = false;

        if (b != null) {
            allowFutureDate = b.getBoolean(ALLOW_SELECT_FUTURE_DATE);
        }

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMaxDate(allowFutureDate ? DateUtil.getDateAfterYearInMillis() : c.getTimeInMillis());
        dialog.setCancelable(false);

        return dialog;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        Date date = c.getTime();

        final String fmtDate = DateUtil.getFormattedFullDate(date);
        final String utcDate = DateUtil.getDateInUTC(date);

        if (fmtDate != null) {
            datePickerSelectionListener.onDateSelected(fmtDate, utcDate);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        datePickerSelectionListener = null;
    }


    public interface onDatePickerSelectionListener {
        void onDateSelected(@NonNull String formattedDate, @NonNull String dateInUTC);
    }
}