package co.techmagic.hr.presentation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import co.techmagic.hr.presentation.ui.FilterTypes;
import co.techmagic.hr.presentation.mvp.presenter.CalendarFiltersPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.CalendarFiltersViewImpl;
import co.techmagic.hr.presentation.ui.adapter.FilterAdapter;
import co.techmagic.hr.presentation.ui.fragment.DatePickerFragment;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class CalendarFiltersActivity extends BaseActivity<CalendarFiltersViewImpl, CalendarFiltersPresenter> implements DatePickerFragment.OnDatePickerFragmentListener,
        FilterAdapter.OnFilterSelectionListener {

    public static final int CALENDAR_FILTERS_ACTIVITY_REQUEST_CODE = 1002;

    public static final String SEL_MY_TEAM_EXTRA = "sel_my_team_extra";
    public static final String SEL_FROM_DATE_EXTRA = "sel_from_date_extra";
    public static final String SEL_TO_DATE_EXTRA = "sel_to_date_extra";
    public static final String SEL_DEP_ID_EXTRA = "sel_dep_id_extra";
    public static final String SEL_PROJECT_ID_EXTRA = "sel_project_id_extra";

    public static final String DIALOG_FRAGMENT_TAG = "dialog_fragment_tag";
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

    private FilterTypes filterTypes = FilterTypes.NONE;
    private ActionBar actionBar;
    private AlertDialog dialog;

    private boolean isMyTeamChecked = true;
    private long fromInMillis = 0;
    private long toInMillis = 0;
    private String selDepId = null;
    private String selProjectId = null;


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
                Intent i = new Intent();
                setResult(Activity.RESULT_CANCELED, i);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
                dismissDialogIfOpened();
                showSelectFilterAlertDialog(departments);
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
                dismissDialogIfOpened();
                showSelectFilterAlertDialog(projects);
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
    public void onFilterSelected(@NonNull String id, @NonNull String name) {
        handleSelection(id, name);
    }


    @Override
    public void addDatePickerFragment(@Nullable Calendar from, @Nullable Calendar to, boolean isDateFromPicker) {
        DatePickerFragment fragment = DatePickerFragment.newInstance();
        Bundle b = new Bundle();

        b.putBoolean(SELECTED_DIALOG_KEY, isDateFromPicker);
        b.putSerializable(CALENDAR_FROM_KEY, from);
        b.putSerializable(CALENDAR_TO_KEY, to);
        fragment.setArguments(b);
        fragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
    }


    @OnClick(R.id.rlFilterFrom)
    public void onFromClick() {
        presenter.onFromButtonClick();
    }


    @OnClick(R.id.rlFilterTo)
    public void onToClick() {
        presenter.onToButtonClick();
    }


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
        clearAllFilters();
    }


    @OnClick(R.id.btnCalApply)
    public void onApplyClick() {
        applyFilters();
    }


    private void clearAllFilters() {
        SharedPreferencesUtil.saveMyTeamSelection(true);
        SharedPreferencesUtil.saveSelectedFromTime(0);
        SharedPreferencesUtil.saveSelectedToTime(0);
        SharedPreferencesUtil.saveSelectedCalendarDepartmentId(null);
        SharedPreferencesUtil.saveSelectedCalendarProjectId(null);

        presenter.setDefaultDates();
        swTeam.setChecked(true);
        tvSelDep.setText("");
        tvSelProject.setText("");
        selDepId = null;
        selProjectId = null;
        filterTypes = FilterTypes.NONE;
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
    }


    private void handleSelection(String filterId, String name) {
        dismissDialogIfOpened();

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


    private <T extends IFilterModel> void showSelectFilterAlertDialog(@Nullable List<T> filters) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setupDialogViews(filters, builder);
        dialog = builder.show();
        dialog.findViewById(R.id.btnAlertDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }


    private <T extends IFilterModel> void setupDialogViews(@Nullable List<T> filters, AlertDialog.Builder builder) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_select_filter, null);
        builder.setView(view);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        setupDialogTitle(tvTitle);

        setupSelectFilterRecyclerView(view, filters);
    }


    private void setupDialogTitle(@NonNull TextView tvTitle) {
        switch (filterTypes) {
            case DEPARTMENT:
                tvTitle.setText(getString(R.string.tm_hr_search_activity_text_filter_by_department));
                break;

            case PROJECT:
                tvTitle.setText(getString(R.string.tm_hr_calendar_filters_activity_filter_by_project));
                break;
        }
    }


    private <T extends IFilterModel> void setupSelectFilterRecyclerView(View view, @Nullable List<T> results) {
        RecyclerView rvFilters = (RecyclerView) view.findViewById(R.id.rvFilters);
        rvFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FilterAdapter adapter = new FilterAdapter(this, false);
        rvFilters.setAdapter(adapter);
        adapter.refresh(results);
    }


    private void dismissDialogIfOpened() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    private void initUi() {
        setupActionBar();
        getData();
        swTeam.setChecked(isMyTeamChecked);
        swTeam.setOnCheckedChangeListener((buttonView, isChecked) -> isMyTeamChecked = isChecked);
        presenter.setupPage();
    }


    private void setupActionBar() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
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