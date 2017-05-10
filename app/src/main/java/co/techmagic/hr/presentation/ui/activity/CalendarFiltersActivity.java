package co.techmagic.hr.presentation.ui.activity;

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
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.presentation.mvp.presenter.CalendarFiltersPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.CalendarFiltersViewImpl;
import co.techmagic.hr.presentation.ui.adapter.FilterAdapter;
import co.techmagic.hr.presentation.ui.fragment.DatePickerFragment;

public class CalendarFiltersActivity extends BaseActivity<CalendarFiltersViewImpl, CalendarFiltersPresenter> implements DatePickerFragment.OnDatePickerFragmentListener,
        FilterAdapter.OnFilterSelectionListener {

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

    private ActionBar actionBar;
    private AlertDialog dialog;


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
            public void showFilterByDepartmentDialog(@NonNull List<FilterDepartment> departments) {
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
        };
    }


    @Override
    protected CalendarFiltersPresenter initPresenter() {
        return new CalendarFiltersPresenter();
    }


    @Override
    public void displaySelectedFromDate(@NonNull String date, @Nullable Calendar from, @Nullable Calendar to) {
        view.updateSelectedFromButtonText(date);
    }


    @Override
    public void displaySelectedToDate(@NonNull String date, @Nullable Calendar from, @Nullable Calendar to) {
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


    @OnClick(R.id.btnCalClear)
    public void onClearClick() {
        clearAllFilters();
    }


    @OnClick(R.id.btnCalApply)
    public void onApplyClick() {
        applyFilters();
    }


    private void clearAllFilters() {
        presenter.setDefaultDates();
        tvSelDep.setText("");
    }


    private void applyFilters() {

    }


    private void handleSelection(String id, String name) {
        dismissDialogIfOpened();
        tvSelDep.setText(name);
    }


    private void showSelectFilterAlertDialog(@Nullable List<FilterDepartment> departments) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setupDialogViews(departments, builder);
        dialog = builder.show();
        dialog.findViewById(R.id.btnAlertDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }


    private void setupDialogViews(@Nullable List<FilterDepartment> departments, AlertDialog.Builder builder) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_select_filter, null);
        builder.setView(view);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.tm_hr_search_activity_text_filter_by_department));

        setupSelectFilterRecyclerView(view, departments);
    }


    private void setupSelectFilterRecyclerView(View view, @Nullable List<FilterDepartment> results) {
        RecyclerView rvFilters = (RecyclerView) view.findViewById(R.id.rvFilters);
        rvFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FilterAdapter adapter = new FilterAdapter(this);
        rvFilters.setAdapter(adapter);
        adapter.refresh(results);
    }


    private void dismissDialogIfOpened() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    private void initUi() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            presenter.setupPage();
        }
    }
}