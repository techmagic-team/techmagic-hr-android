package co.techmagic.hr.presentation.ui.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.techmagic.hr.R;
import co.techmagic.hr.presentation.ui.activity.HomeActivity;
import co.techmagic.hr.presentation.ui.view.OnDisplaySelectedDateListener;
import co.techmagic.hr.presentation.util.DateUtil;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final int MAX_YEAR = 20;
    private static final int MIN_YEAR = 2;

    private Calendar fromDate;
    private Calendar toDate;
    private boolean isDateFromPicker;
    private OnDisplaySelectedDateListener selectedDateListener;


    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        selectedDateListener = (OnDisplaySelectedDateListener) getTargetFragment();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        if (b != null) {
            fromDate = (Calendar) b.getSerializable(HomeActivity.CALENDAR_FROM_KEY);
            toDate = (Calendar) b.getSerializable(HomeActivity.CALENDAR_TO_KEY);
            isDateFromPicker = b.getBoolean(HomeActivity.SELECTED_DIALOG_KEY);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        setupLayoutViews(builder, inflater);

        return builder.create();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        Date date = c.getTime();

        final String fmtDate = DateUtil.getFormattedMonthAndYear(date);

        if (fmtDate != null) {
            if (isDateFromPicker) {
                fromDate = c;
                selectedDateListener.displaySelectedFromDate(fmtDate, c, null);
            } else {
                toDate = c;
                if (DateUtil.isValidSelectedDatesRange(fromDate, toDate)) {
                    selectedDateListener.displaySelectedToDate(fmtDate, null, c);
                } else {
                    selectedDateListener.invalidDateRangeSelected(R.string.tm_hr_calendar_fragment_message_alert_dialog_invalid_date);
                }
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        selectedDateListener = null;
    }


    private void setupLayoutViews(AlertDialog.Builder builder, LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.layout_dialog_date_picker, null);
        final NumberPicker monthPicker = (NumberPicker) rootView.findViewById(R.id.numPickerMonth);
        final NumberPicker yearPicker = (NumberPicker) rootView.findViewById(R.id.numPickerYear);

        setMonthAndYear(monthPicker, yearPicker);

        builder.setTitle(isDateFromPicker ? R.string.tm_hr_calendar_fragment_message_alert_dialog_title_from : R.string.tm_hr_calendar_fragment_message_alert_dialog_title_to);
        builder.setView(rootView)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());
    }


    private void setMonthAndYear(@NonNull NumberPicker monthPicker, @NonNull NumberPicker yearPicker) {
        Calendar cal = isDateFromPicker ? fromDate : toDate;

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

        DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
        monthPicker.setDisplayedValues(dfs.getShortMonths());

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(year - MIN_YEAR);
        yearPicker.setMaxValue(cal.get(Calendar.YEAR) + MAX_YEAR);
        yearPicker.setValue(year);
    }
}