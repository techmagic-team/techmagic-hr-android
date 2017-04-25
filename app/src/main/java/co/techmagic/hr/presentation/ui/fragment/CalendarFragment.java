package co.techmagic.hr.presentation.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.CalendarPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.CalendarViewImpl;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;
import co.techmagic.hr.presentation.ui.view.timetable.EmployeePlanItem;
import co.techmagic.hr.presentation.ui.view.timetable.TimeTable;

public class CalendarFragment extends BaseFragment<CalendarViewImpl, CalendarPresenter> {

    public static final String DIALOG_FRAGMENT_TAG = "dialog_fragment_tag";
    public static final String SELECTED_DIALOG_KEY = "selected_dialog_key";

    private ActionBarChangeListener actionBarChangeListener;

    @BindView(R.id.btnFrom)
    Button btnFrom;
    @BindView(R.id.btnTo)
    Button btnTo;
    @BindView(R.id.timeTable)
    TimeTable timeTable;


    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarChangeListener = (ActionBarChangeListener) context;
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        setUi();
        timeTable.setItems(generateSamplePlanData());
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        actionBarChangeListener.showBackButton();
        actionBarChangeListener.setActionBarTitle(getString(R.string.tm_hr_calendar_fragment_title));
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    protected CalendarViewImpl initView() {
        return new CalendarViewImpl(this, getActivity().findViewById(android.R.id.content)) {
            @Override
            public void displaySelectedFromDate(@NonNull String date) {
                btnFrom.setText(date);
            }


            @Override
            public void displaySelectedToDate(@NonNull String date) {
                btnTo.setText(date);
            }
        };
    }


    @Override
    protected CalendarPresenter initPresenter() {
        return new CalendarPresenter();
    }


    @OnClick(R.id.btnFrom)
    public void onFromButtonClick() {
        showDateDickerDialog(true);
    }


    @OnClick(R.id.btnTo)
    public void onToButtonClick() {
        showDateDickerDialog(false);
    }


    private void setUi() {

    }


    private void showDateDickerDialog(boolean isDateFromPicker) {
        DialogFragment dialog = DatePickerFragment.newInstance();
        Bundle b = new Bundle();
        b.putBoolean(SELECTED_DIALOG_KEY, isDateFromPicker);
        dialog.setArguments(b);
        dialog.show(getActivity().getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
    }


    private static List<EmployeePlanItem> generateSamplePlanData() {
        List<EmployeePlanItem> planItems = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            planItems.add(EmployeePlanItem.generateSample());

        return planItems;
    }
}