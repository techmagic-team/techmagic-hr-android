package co.techmagic.hr.presentation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.IFilterModel;
import co.techmagic.hr.presentation.mvp.presenter.CalendarFiltersPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.CalendarFiltersViewImpl;
import co.techmagic.hr.presentation.ui.manager.FilterDialogManager;
import co.techmagic.hr.presentation.ui.FilterTypes;
import co.techmagic.hr.presentation.ui.adapter.FilterAdapter;
import co.techmagic.hr.presentation.ui.fragment.NumberDatePickerFragment;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class CalendarFiltersActivity extends BaseActivity<CalendarFiltersViewImpl, CalendarFiltersPresenter> implements NumberDatePickerFragment.OnDatePickerFragmentListener,
        FilterAdapter.OnFilterSelectionListener {

    public static final int CALENDAR_FILTERS_ACTIVITY_REQUEST_CODE = 1002;
    public static final int CALENDAR_REQUEST_TIME_OFF_REQUEST_CODE = 1004;
    public static final int RESULT_FILTERS_CLEARED = 1003;

    public static final String SEL_MY_TEAM_EXTRA = "sel_my_team_extra";
    public static final String SEL_FROM_DATE_EXTRA = "sel_from_date_extra";
    public static final String SEL_TO_DATE_EXTRA = "sel_to_date_extra";
    public static final String SEL_DEP_ID_EXTRA = "sel_dep_id_extra";
    public static final String SEL_PROJECT_ID_EXTRA = "sel_project_id_extra";

    public static final String NUMBER_DATE_PICKER_FRAGMENT_TAG = "number_date_picker_fragment_tag";
    public static final String SELECTED_DIALOG_KEY = "selected_dialog_key";
    public static final String CALENDAR_FROM_KEY = "calendar_from_key";
    public static final String CALENDAR_TO_KEY = "calendar_to_key";

    @BindView(R.id.swTeam)
    Switch swTeam;
    @BindView(R.id.tvSelectedFrom)
    TextView tvSelectedFrom;
    @BindView(R.id.tvSelectedTo)
    TextView tvSelectedTo;
    @BindView(R.id.tvSelDep)
    TextView tvSelDep;
    @BindView(R.id.tvCalendarSelProject)
    TextView tvSelProject;

    private FilterDialogManager dialogManager;
    private FilterTypes filterTypes = FilterTypes.NONE;
    private ActionBar actionBar;

    private boolean isMyTeamChecked;
    private long fromInMillis = 0;
    private long toInMillis = 0;
    private String selDepId = null;
    private String selProjectId = null;
    private boolean filtersCleared = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (dialogManager.isDialogActive()) {
            dialogManager.dismissDialogIfOpened();
        } else {
            onBackClickWithSetResult();
        }
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_calendar_filters);
    }


    @Override
    protected CalendarFiltersViewImpl initView() {
        return new CalendarFiltersViewImpl(this, findViewById(android.R.id.content)) {
            @Override
            public void updateSelectedFromButtonText(@NonNull String date) {
                tvSelectedFrom.setText(date);
            }

            @Override
            public void updateSelectedToButtonText(@NonNull String date) {
                tvSelectedTo.setText(date);
            }

            @Override
            public void showDatePicker(@NonNull Calendar from, @NonNull Calendar to, boolean isDateFromPicker) {
                addDatePickerFragment(from, to, isDateFromPicker);
            }

            @Override
            public void inValidDateRange(int resId) {
                view.showSnackBarMessage(getString(resId));
            }

            @Override
            public void showFilterByDepartmentDialog(@NonNull List<Filter> departments) {
                filterTypes = FilterTypes.DEPARTMENT;
                dialogManager.dismissDialogIfOpened();
                dialogManager.showSelectFilterAlertDialog(departments, filterTypes);
            }

            @Override
            public void showSelectedDepartmentFilter(@NonNull String id, @NonNull String filterName) {
                tvSelDep.setText(filterName);
            }

            @Override
            public void showEmptyDepartmentFiltersErrorMessage(int resId) {
                showSnackBarMessage(getString(resId));
            }

            @Override
            public void showFilterByProjectDialog(@NonNull List<Filter> projects) {
                filterTypes = FilterTypes.PROJECT;
                dialogManager.dismissDialogIfOpened();
                dialogManager.showSelectFilterAlertDialog(projects, filterTypes);
            }

            @Override
            public void showSelectedProjectFilter(@NonNull String id, @NonNull String filterName) {
                tvSelProject.setText(filterName);
            }

            @Override
            public void showEmptyProjectFiltersErrorMessage(int resId) {
                showSnackBarMessage(getString(resId));
            }
        };
    }


    @Override
    protected CalendarFiltersPresenter initPresenter() {
        return new CalendarFiltersPresenter();
    }


    @Override
    public void displaySelectedFromDate(@NonNull String date, @Nullable Calendar from, @Nullable Calendar to) {
        if (from != null) {
            fromInMillis = from.getTimeInMillis();
        }

        view.updateSelectedFromButtonText(date);
    }


    @Override
    public void displaySelectedToDate(@NonNull String date, @Nullable Calendar from, @Nullable Calendar to) {
        if (to != null) {
            toInMillis = to.getTimeInMillis();
        }

        view.updateSelectedToButtonText(date);
    }


    @Override
    public void invalidDateRangeSelected(int resId) {
        view.inValidDateRange(resId);
    }


    @Override
    public void onFilterSelected(@NonNull IFilterModel model) {
        handleSelection(model.getId(), model.getName());
    }


    @Override
    public void addDatePickerFragment(@Nullable Calendar from, @Nullable Calendar to, boolean isDateFromPicker) {
        NumberDatePickerFragment fragment = NumberDatePickerFragment.newInstance();
        Bundle b = new Bundle();

        b.putBoolean(SELECTED_DIALOG_KEY, isDateFromPicker);
        b.putSerializable(CALENDAR_FROM_KEY, from);
        b.putSerializable(CALENDAR_TO_KEY, to);
        fragment.setArguments(b);
        fragment.show(getSupportFragmentManager(), NUMBER_DATE_PICKER_FRAGMENT_TAG);
    }


    @OnClick(R.id.rlFilterFrom)
    public void onFromClick() {
        presenter.onFromButtonClick();
    }


    @OnClick(R.id.rlFilterTo)
    public void onToClick() {
        presenter.onToButtonClick();
    }


    @Deprecated
    @OnClick(R.id.rlFilterByDepartment)
    public void onDepartmentClick() {
        presenter.onDepartmentFilterClick();
    }


    @OnClick(R.id.rlCalendarFilterByProject)
    public void onProjectClick() {
        presenter.onProjectFilterClick();
    }


    @OnClick(R.id.btnCalClear)
    public void onClearClick() {
        clearFilters();
    }


    @OnClick(R.id.btnCalApply)
    public void onApplyClick() {
        applyFilters();
    }


    private void onBackClickWithSetResult() {
        Intent i = new Intent();
        setResult(filtersCleared ? RESULT_FILTERS_CLEARED : Activity.RESULT_CANCELED, i);
        finish();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out);
    }


    private void clearFilters() {
        SharedPreferencesUtil.saveMyTeamSelection(true);
        SharedPreferencesUtil.saveSelectedFromTime(0);
        SharedPreferencesUtil.saveSelectedToTime(0);
        SharedPreferencesUtil.saveSelectedCalendarDepartmentId(null);
        SharedPreferencesUtil.saveSelectedCalendarProjectId(null);

        presenter.setDefaultDates();
        swTeam.setChecked(true);
        fromInMillis = 0;
        toInMillis = 0;
        tvSelDep.setText("");
        tvSelProject.setText("");
        selDepId = null;
        selProjectId = null;
        filterTypes = FilterTypes.NONE;
        filtersCleared = true;
    }


    private void applyFilters() {
        SharedPreferencesUtil.saveMyTeamSelection(isMyTeamChecked);
        SharedPreferencesUtil.saveSelectedFromTime(fromInMillis);
        SharedPreferencesUtil.saveSelectedToTime(toInMillis);
        SharedPreferencesUtil.saveSelectedCalendarDepartmentId(selDepId);
        SharedPreferencesUtil.saveSelectedCalendarProjectId(selProjectId);

        Intent i = new Intent();
        i.putExtra(SEL_MY_TEAM_EXTRA, isMyTeamChecked);
        i.putExtra(SEL_FROM_DATE_EXTRA, fromInMillis);
        i.putExtra(SEL_TO_DATE_EXTRA, toInMillis);
        i.putExtra(SEL_DEP_ID_EXTRA, selDepId);
        i.putExtra(SEL_PROJECT_ID_EXTRA, selProjectId);

        setResult(Activity.RESULT_OK, i);
        finish();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out);
    }


    private void handleSelection(String filterId, String name) {
        dialogManager.dismissDialogIfOpened();

        switch (filterTypes) {
            case DEPARTMENT:
                selDepId = filterId;
                tvSelDep.setText(name);
                break;

            case PROJECT:
                selProjectId = filterId;
                tvSelProject.setText(name);
                break;
        }
    }


    private void initUi() {
        dialogManager = new FilterDialogManager(this, this);
        setupActionBar();
        getData();
        swTeam.setChecked(isMyTeamChecked);
        swTeam.setOnCheckedChangeListener((buttonView, isChecked) -> isMyTeamChecked = isChecked);
        presenter.setupPage();
    }


    private void setupActionBar() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.tm_hr_calendar_filters_activity_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isMyTeamChecked = bundle.getBoolean(CalendarFiltersActivity.SEL_MY_TEAM_EXTRA);
            fromInMillis = bundle.getLong(CalendarFiltersActivity.SEL_FROM_DATE_EXTRA);
            toInMillis = bundle.getLong(CalendarFiltersActivity.SEL_TO_DATE_EXTRA);
            selDepId = bundle.getString(CalendarFiltersActivity.SEL_DEP_ID_EXTRA);
            selProjectId = bundle.getString(CalendarFiltersActivity.SEL_PROJECT_ID_EXTRA);
        }
    }
}