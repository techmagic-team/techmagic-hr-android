package co.techmagic.hr.presentation.ui.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.domain.pojo.CalendarInfoDto;
import co.techmagic.hr.presentation.mvp.presenter.CalendarPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.CalendarViewImpl;
import co.techmagic.hr.presentation.pojo.UserAllTimeOffsMap;
import co.techmagic.hr.presentation.ui.ProfileTypes;
import co.techmagic.hr.presentation.ui.activity.CalendarFiltersActivity;
import co.techmagic.hr.presentation.ui.activity.HomeActivity;
import co.techmagic.hr.presentation.ui.activity.RequestTimeOffActivity;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridEmployeeItemAdapter;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;
import co.techmagic.hr.presentation.ui.view.calendar.OnCalendarViewReadyListener;
import co.techmagic.hr.presentation.ui.view.calendar.TimeTable;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class CalendarFragment extends BaseFragment<CalendarViewImpl, CalendarPresenter> implements GridEmployeeItemAdapter.OnEmployeeItemClickListener, OnCalendarViewReadyListener {

    private static final String MIXPANEL_CALENDAR_FILTERS_TAG = "Calendar Filters";
    private static final String MIXPANEL_REQUEST_TIME_OFF_TAG = "Request Time Off";

    @BindView(R.id.flCalFilters)
    View calFilters;
    @BindView(R.id.tvCalendarNoResults)
    TextView tvNoResults;
    @BindView(R.id.timeTable)
    TimeTable timeTable;

    private ActionBarChangeListener actionBarChangeListener;
    private FragmentCallback fragmentCallback;

    private boolean isMyTeamChecked;
    private long fromInMillis = 0;
    private long toInMillis = 0;
    private String selDepId = null;
    private String selProjectId = null;

    private Calendar from = null;
    private Calendar to = null;


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
        getData();
        presenter.setupPage(isMyTeamChecked, selDepId, selProjectId, fromInMillis, toInMillis);
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
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar_filters:
                startCalendarFiltersScreen();
                return true;

            case R.id.action_request_time_off:
                startRequestTimeOffScreen();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        actionBarChangeListener = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CalendarFiltersActivity.CALENDAR_FILTERS_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                isMyTeamChecked = data.getBooleanExtra(CalendarFiltersActivity.SEL_MY_TEAM_EXTRA, false);
                fromInMillis = data.getLongExtra(CalendarFiltersActivity.SEL_FROM_DATE_EXTRA, 0);
                toInMillis = data.getLongExtra(CalendarFiltersActivity.SEL_TO_DATE_EXTRA, 0);
                selDepId = data.getStringExtra(CalendarFiltersActivity.SEL_DEP_ID_EXTRA);
                selProjectId = data.getStringExtra(CalendarFiltersActivity.SEL_PROJECT_ID_EXTRA);
            }

            if (resultCode == CalendarFiltersActivity.RESULT_FILTERS_CLEARED) {
                clearFilters();
            }

            getFromAndToIfExists();
            presenter.updateCalendar(isMyTeamChecked, selDepId, selProjectId, from, to);
        }
    }


    @Override
    protected CalendarViewImpl initView() {
        return new CalendarViewImpl(this, getActivity().findViewById(android.R.id.content)) {
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

            @Override
            public void updateTableWithDateRange(UserAllTimeOffsMap userAllTimeOffsMap, List<CalendarInfoDto> calendarInfo, Calendar dateFrom, Calendar dateTo) {
                tvNoResults.setVisibility(View.GONE);
                timeTable.setVisibility(View.VISIBLE);
                timeTable.setItemsWithDateRange(userAllTimeOffsMap, calendarInfo, dateFrom, dateTo, CalendarFragment.this, CalendarFragment.this);
                timeTable.scrollToCurrentMonth();
            }

            @Override
            public void addDetailsFragment(@NonNull UserProfile userProfile) {
                fragmentCallback.addDetailsFragment(userProfile, ProfileTypes.EMPLOYEE, HomeActivity.FRAGMENT_DETAILS_TAG);
            }
        };
    }


    @Override
    protected CalendarPresenter initPresenter() {
        return new CalendarPresenter();
    }


    @Override
    public void onEmployeeItemClick(@NonNull String employeeId) {
        presenter.onEmployeeClick(employeeId);
    }


    @Override
    public void onCalendarVisible() {
        view.hideProgress();
    }


    @OnClick(R.id.btnClearCalFilters)
    public void onClearFiltersClick() {
        clearFilters();
    }


    private void getData() {
        isMyTeamChecked = SharedPreferencesUtil.getMyTeamSelection();
        fromInMillis = SharedPreferencesUtil.getSelectedFromTime();
        toInMillis = SharedPreferencesUtil.getSelectedToTime();
        selDepId = SharedPreferencesUtil.getSelectedCalendarDepartmentId();
        selProjectId = SharedPreferencesUtil.getSelectedCalendarProjectId();
        getFromAndToIfExists();
    }


    private void getFromAndToIfExists() {
        if (fromInMillis != 0) {
            from = Calendar.getInstance();
            from.setTimeInMillis(fromInMillis);
        }

        if (toInMillis != 0) {
            to = Calendar.getInstance();
            to.setTimeInMillis(toInMillis);
        }
    }


    private void clearFilters() {
        SharedPreferencesUtil.saveMyTeamSelection(true);
        SharedPreferencesUtil.saveSelectedFromTime(0);
        SharedPreferencesUtil.saveSelectedToTime(0);
        SharedPreferencesUtil.saveSelectedCalendarDepartmentId(null);
        SharedPreferencesUtil.saveSelectedCalendarProjectId(null);

        isMyTeamChecked = true;
        fromInMillis = 0;
        toInMillis = 0;
        selDepId = null;
        selProjectId = null;
        presenter.onClearFiltersClick();
    }


    private void startCalendarFiltersScreen() {
        Bundle animation = ActivityOptions.makeCustomAnimation(getContext(), R.anim.anim_slide_in, R.anim.anim_not_move).toBundle();
        Intent intent = new Intent(getActivity(), CalendarFiltersActivity.class);
        intent.putExtra(CalendarFiltersActivity.SEL_MY_TEAM_EXTRA, isMyTeamChecked);
        intent.putExtra(CalendarFiltersActivity.SEL_FROM_DATE_EXTRA, fromInMillis);
        intent.putExtra(CalendarFiltersActivity.SEL_TO_DATE_EXTRA, toInMillis);
        intent.putExtra(CalendarFiltersActivity.SEL_DEP_ID_EXTRA, selDepId);
        intent.putExtra(CalendarFiltersActivity.SEL_PROJECT_ID_EXTRA, selProjectId);
        startActivityForResult(intent, CalendarFiltersActivity.CALENDAR_FILTERS_ACTIVITY_REQUEST_CODE, animation);
        mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_CALENDAR_FILTERS_TAG);
    }


    private void startRequestTimeOffScreen() {
        Bundle animation = ActivityOptions.makeCustomAnimation(getContext(), R.anim.anim_slide_in, R.anim.anim_not_move).toBundle();
        Intent intent = new Intent(getActivity(), RequestTimeOffActivity.class);
        startActivityForResult(intent, CalendarFiltersActivity.CALENDAR_REQUEST_TIME_OFF_REQUEST_CODE, animation);
        mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_REQUEST_TIME_OFF_TAG);
    }
}