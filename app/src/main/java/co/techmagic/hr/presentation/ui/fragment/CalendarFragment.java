package co.techmagic.hr.presentation.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.presentation.mvp.presenter.CalendarPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.CalendarViewImpl;
import co.techmagic.hr.presentation.ui.adapter.calendar.AllTimeOffs;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;
import co.techmagic.hr.presentation.ui.view.OnDisplaySelectedDateListener;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridItem;
import co.techmagic.hr.presentation.ui.view.calendar.TimeTable;

public class CalendarFragment extends BaseFragment<CalendarViewImpl, CalendarPresenter> implements OnDisplaySelectedDateListener {

    @BindView(R.id.btnFrom)
    Button btnFrom;
    @BindView(R.id.btnTo)
    Button btnTo;
    @BindView(R.id.tvCalendarNoResults)
    TextView tvNoResults;
    @BindView(R.id.timeTable)
    TimeTable timeTable;

    private ActionBarChangeListener actionBarChangeListener;
    private FragmentCallback fragmentCallback;


    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        actionBarChangeListener = (ActionBarChangeListener) context;
        fragmentCallback = (FragmentCallback) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        initUi();
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
    public void onDetach() {
        super.onDetach();
        actionBarChangeListener = null;
        fragmentCallback = null;
    }


    @Override
    protected CalendarViewImpl initView() {
        return new CalendarViewImpl(this, getActivity().findViewById(android.R.id.content)) {
            @Override
            public <T extends IGridItem> void updateTableWithDateRange(@NonNull List<T> items, @NonNull AllTimeOffs allTimeOffs, @NonNull Calendar from, @NonNull Calendar to) {
                tvNoResults.setVisibility(View.GONE);
                timeTable.setVisibility(View.VISIBLE);
                timeTable.setItemsWithDateRange(items, allTimeOffs, from, to);
            }

            @Override
            public void updateSelectedFromButtonText(@NonNull String date) {
                btnFrom.setText(date);
            }

            @Override
            public void updateSelectedToButtonText(@NonNull String date) {
                btnTo.setText(date);
            }

            @Override
            public void showDatePicker(@NonNull Calendar from, @NonNull Calendar to, boolean isDateFromPicker) {
                fragmentCallback.addDatePickerFragment(CalendarFragment.this, from, to, isDateFromPicker);
            }

            @Override
            public void inValidDateRange(int resId) {
                view.showSnackBarMessage(getString(resId));
            }

            @Override
            public void showNoResults() {
                timeTable.setVisibility(View.GONE);
                tvNoResults.setVisibility(View.VISIBLE);
            }

            @Override
            public void updateHolidaysAtCalendar(@NonNull List<CalendarInfo> calendar) {
               // timeTable.displayHolidays(calendar);
            }

            @Override
            public void onCalendarInfoError(int resId) {

            }
        };
    }


    @Override
    protected CalendarPresenter initPresenter() {
        return new CalendarPresenter();
    }


    @Override
    public void displaySelectedFromDate(@NonNull String date, @Nullable Calendar from, @Nullable Calendar to) {
        view.updateSelectedFromButtonText(date);
        presenter.updateCalendar(from, to);
    }


    @Override
    public void displaySelectedToDate(@NonNull String date, @Nullable Calendar from, @Nullable Calendar to) {
        view.updateSelectedToButtonText(date);
        presenter.updateCalendar(from, to);
    }


    @Override
    public void invalidDateRangeSelected(int resId) {
        view.inValidDateRange(resId);
    }


    @OnClick(R.id.btnFrom)
    public void onFromButtonClick() {
        presenter.onFromButtonClick();
    }


    @OnClick(R.id.btnTo)
    public void onToButtonClick() {
        presenter.onToButtonClick();
    }


    private void initUi() {
        presenter.setupPage();
        presenter.performRequests();
    }
}