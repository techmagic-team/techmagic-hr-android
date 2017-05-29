package co.techmagic.hr.presentation.ui.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.presentation.mvp.presenter.SearchPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.SearchViewImpl;
import co.techmagic.hr.presentation.ui.adapter.FilterAdapter;
import co.techmagic.hr.presentation.util.KeyboardUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class SearchActivity extends BaseActivity<SearchViewImpl, SearchPresenter> implements FilterAdapter.OnFilterSelectionListener {

    public static final String DEP_ID_EXTRA = "dep_id_extra";
    public static final String LEAD_ID_EXTRA = "lead_id_extra";
    public static final String SEARCH_QUERY_EXTRA = "search_query_extra";

    @BindView(R.id.tvSelectedDep)
    TextView tvDepartment;
    @BindView(R.id.tvSelectedLead)
    TextView tvLead;
    SearchView searchView;

    private FilterTypes filterTypes = FilterTypes.NONE;
    private AlertDialog dialog;

    private String selDepId;
    private String selLeadId;
    private String searchQuery = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
        presenter.performGetFiltersRequests();
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_search);
    }


    @Override
    protected SearchViewImpl initView() {
        return new SearchViewImpl(this, findViewById(android.R.id.content)) {
            @Override
            public void showFilterByDepartmentDialog(@NonNull List<FilterDepartment> departments) {
                filterTypes = FilterTypes.DEPARTMENT;
                dismissDialogIfOpened();
                showSelectFilterAlertDialog(departments, null);
            }

            @Override
            public void showSelectedDepartmentFilter(@NonNull String id, @NonNull String filterName) {
                selDepId = id;
                tvDepartment.setText(filterName);
            }

            @Override
            public void showEmptyDepartmentFiltersErrorMessage(int resId) {
                showSnackBarMessage(getString(resId));
            }

            @Override
            public void showFilterByLeadDialog(@NonNull List<FilterLead> leads) {
                filterTypes = FilterTypes.LEAD;
                dismissDialogIfOpened();
                showSelectFilterAlertDialog(null, leads);
            }

            @Override
            public void showSelectedLeadFilter(@NonNull String id, @NonNull String filterName) {
                selLeadId = id;
                tvLead.setText(filterName);
                requestSearchViewFocus();
            }

            @Override
            public void showEmptyLeadFiltersErrorMessage(int resId) {
                showSnackBarMessage(getString(resId));
            }

            @Override
            public void requestSearchViewFocus() {
                searchView.onActionViewExpanded();
                searchView.requestFocus();
                searchView.setQuery(searchQuery, false);
            }
        };
    }


    @Override
    protected SearchPresenter initPresenter() {
        return new SearchPresenter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        setupSearchView(menu);
        return true;
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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else {
            KeyboardUtil.hideKeyboard(this, getCurrentFocus());
            Intent i = new Intent();
            setResult(RESULT_CANCELED, i);
            finish();
        }
    }


    @OnClick(R.id.rlFilterByDepartment)
    public void onFilterByDepartmentClick() {
        presenter.onDepartmentFilterClick();
    }


    @OnClick(R.id.rlFilterByLead)
    public void onFilterByLeadClick() {
        presenter.onLeadFilterClick();
    }


    @OnClick(R.id.btnClear)
    public void onClearClick() {
        clearAllFilters();
    }


    @OnClick(R.id.btnApply)
    public void onApplyClick() {
        String query = searchView.getQuery().toString().trim();
        searchQuery = query.isEmpty() ? null : query;
        applyFilters();
    }


    @Override
    public void onFilterSelected(@NonNull String id, @NonNull String name) {
        handleSelection(id, name);
    }


    private void handleSelection(String id, String name) {
        dismissDialogIfOpened();
        switch (filterTypes) {
            case DEPARTMENT:
                selDepId = id;
                tvDepartment.setText(name);
                break;

            case LEAD:
                selLeadId = id;
                tvLead.setText(name);
                break;
        }
    }


    private void clearAllFilters() {
        searchQuery = null;
        selDepId = null;
        selLeadId = null;
        searchView.setQuery("", false);
        tvDepartment.setText("");
        tvLead.setText("");
        SharedPreferencesUtil.saveSelectedDepartmentId(null);
        SharedPreferencesUtil.saveSelectedLeadId(null);
        filterTypes = FilterTypes.NONE;
    }


    private void applyFilters() {
        SharedPreferencesUtil.saveSelectedDepartmentId(selDepId);
        SharedPreferencesUtil.saveSelectedLeadId(selLeadId);
        Intent i = new Intent();
        i.putExtra(SEARCH_QUERY_EXTRA, searchQuery);
        i.putExtra(DEP_ID_EXTRA, selDepId);
        i.putExtra(LEAD_ID_EXTRA, selLeadId);

        setResult(Activity.RESULT_OK, i);
        finish();
    }


    private void initUi() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchQuery = bundle.getString(HomeActivity.SEARCH_QUERY_EXTRAS);
        }
        setupActionBar();
    }


    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }


    private void setupSearchView(@NonNull Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query.trim();
                applyFilters();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void showSelectFilterAlertDialog(@Nullable List<FilterDepartment> departments, @Nullable List<FilterLead> leads) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setupDialogViews(departments, leads, builder);
        dialog = builder.show();
        dialog.findViewById(R.id.btnAlertDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }


    private void setupDialogViews(@Nullable List<FilterDepartment> departments, @Nullable List<FilterLead> leads, AlertDialog.Builder builder) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_select_filter, null);
        builder.setView(view);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        setupSelectFilterRecyclerView(view, departments, leads);
        setupDialogTitle(tvTitle);
    }


    private void setupDialogTitle(@NonNull TextView tvTitle) {
        switch (filterTypes) {
            case DEPARTMENT:
                tvTitle.setText(getString(R.string.tm_hr_search_activity_text_filter_by_department));
                break;

            case LEAD:
                tvTitle.setText(getString(R.string.tm_hr_search_activity_text_filter_by_lead));
                break;
        }
    }


    private void setupSelectFilterRecyclerView(View view, @Nullable List<FilterDepartment> results, @Nullable List<FilterLead> leads) {
        RecyclerView rvFilters = (RecyclerView) view.findViewById(R.id.rvFilters);
        rvFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FilterAdapter adapter = new FilterAdapter(this, false);
        rvFilters.setAdapter(adapter);

        if (results == null) {
            adapter.refresh(leads);
        } else {
            adapter.refresh(results);
        }
    }


    private void dismissDialogIfOpened() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    private enum FilterTypes {
        NONE,
        DEPARTMENT,
        LEAD
    }
}