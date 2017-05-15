package co.techmagic.hr.presentation.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.CalendarPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.CalendarViewImpl;
import co.techmagic.hr.presentation.ui.activity.CalendarFiltersActivity;
import co.techmagic.hr.presentation.ui.adapter.calendar.AllTimeOffs;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridEmployeeItemAdapter;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridItem;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;
import co.techmagic.hr.presentation.ui.view.OnCalendarViewReadyListener;
import co.techmagic.hr.presentation.ui.view.calendar.TimeTable;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class CalendarFragment extends BaseFragment<CalendarViewImpl, CalendarPresenter> implements GridEmployeeItemAdapter.OnEmployeeItemClickListener, OnCalendarViewReadyListener {

    @BindView(R.id.flCalFilters)
    View calFilters;
    @BindView(R.id.tvCalendarNoResults)
    TextView tvNoResults;
    @BindView(R.id.timeTable)
    TimeTable timeTable;

    private ActionBarChangeListener actionBarChangeListener;

    private boolean isMyTeamChecked = true; // by default
    private long fromInMillis = 0;
    private long toInMillis = 0;
    private String selDepId = null;


    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        actionBarChangeListener = (ActionBarChangeListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        presenter.setupPage();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        actionBarChangeListener.showBackButton();
        actionBarChangeListener.setActionBarTitle(getString(R.string.tm_hr_calendar_fragment_title));
        inflater.inflate(R.menu.menu_calendar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Used to handle the menu item click and startActivityForResult()
     * */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar_filters:
                startCalendarFiltersScreen();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        actionBarChangeListener = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();

        if (requestCode == CalendarFiltersActivity.CALENDAR_FILTERS_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                isMyTeamChecked = data.getBooleanExtra(CalendarFiltersActivity.SEL_MY_TEAM_EXTRA, false);
                fromInMillis = data.getLongExtra(CalendarFiltersActivity.SEL_FROM_DATE_EXTRA, 0);
                toInMillis = data.getLongExtra(CalendarFiltersActivity.SEL_TO_DATE_EXTRA, 0);
                selDepId = data.getStringExtra(CalendarFiltersActivity.SEL_DEP_ID_EXTRA);
            }

            if (fromInMillis == 0) {
                from = null;
            } else {
                from.setTimeInMillis(fromInMillis);
            }

            if (toInMillis == 0) {
                to.setTimeInMillis(fromInMillis);
            } else {
                to.setTimeInMillis(toInMillis);
            }

            presenter.updateCalendar(isMyTeamChecked, selDepId, from, to);
        }
    }


    @Override
    protected CalendarViewImpl initView() {
        return new CalendarViewImpl(this, getActivity().findViewById(android.R.id.content)) {
            @Override
            public <T extends IGridItem> void updateTableWithDateRange(@NonNull T item, @NonNull AllTimeOffs allTimeOffs, @NonNull Calendar from, @NonNull Calendar to) {
                tvNoResults.setVisibility(View.GONE);
                timeTable.setVisibility(View.VISIBLE);
                timeTable.setItemsWithDateRange(item, allTimeOffs, from, to, CalendarFragment.this, CalendarFragment.this);
            }

            @Override
            public void showNoResults() {
                timeTable.setVisibility(View.GONE);
                tvNoResults.setVisibility(View.VISIBLE);
            }

            @Override
            public void showClearFilters() {
                calFilters.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideClearFilters() {
                calFilters.setVisibility(View.GONE);
            }
        };
    }


    @Override
    protected CalendarPresenter initPresenter() {
        return new CalendarPresenter();
    }


    @Override
    public void onEmployeeItemClick() {
       // fragmentCallback.addDetailsFragment();
    }


    @Override
    public void onCalendarVisible() {
        view.hideProgress();
    }


    @OnClick(R.id.btnClearCalFilters)
    public void onClearFiltersClick() {
        SharedPreferencesUtil.saveMyTeamSelection(true);
        SharedPreferencesUtil.saveSelectedFromTime(0);
        SharedPreferencesUtil.saveSelectedToTime(0);
        SharedPreferencesUtil.saveSelectedCalendarDepartmentId(null);

        presenter.onClearFiltersClick();
        isMyTeamChecked = true;
        fromInMillis = 0;
        toInMillis = 0;
        selDepId = null;
    }


    private void startCalendarFiltersScreen() {
        Intent i = new Intent(getActivity(), CalendarFiltersActivity.class);
        i.putExtra(CalendarFiltersActivity.SEL_MY_TEAM_EXTRA, isMyTeamChecked);
        i.putExtra(CalendarFiltersActivity.SEL_FROM_DATE_EXTRA, fromInMillis);
        i.putExtra(CalendarFiltersActivity.SEL_TO_DATE_EXTRA, toInMillis);
        i.putExtra(CalendarFiltersActivity.SEL_DEP_ID_EXTRA, selDepId);
        startActivityForResult(i, CalendarFiltersActivity.CALENDAR_FILTERS_ACTIVITY_REQUEST_CODE);
    }
}