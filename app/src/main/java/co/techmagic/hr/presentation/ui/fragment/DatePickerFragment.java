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

import co.techmagic.hr.presentation.ui.activity.HomeActivity;
import co.techmagic.hr.presentation.util.DateUtil;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private boolean isDateFromPicker;
   // private OnDisplaySelectedDateListener selectedDateListener;


    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      //  selectedDateListener = (OnDisplaySelectedDateListener) context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        if (b != null) {
            isDateFromPicker = b.getBoolean(HomeActivity.SELECTED_DIALOG_KEY);
        }

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.add(Calendar.DATE, 1);
        Date date = c.getTime();

        final String fmtDate = DateUtil.getFormattedMonthAndYear(date);

        if (fmtDate != null) {
            if (isDateFromPicker) {
              //  selectedDateListener.displaySelectedFromDate(fmtDate);
            } else {
              //  selectedDateListener.displaySelectedToDate(fmtDate);
            }
        }
    }
}