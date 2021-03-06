package co.techmagic.hr.presentation.ui.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.IFilterModel;
import co.techmagic.hr.presentation.mvp.presenter.SearchPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.SearchViewImpl;
import co.techmagic.hr.presentation.ui.manager.FilterDialogManager;
import co.techmagic.hr.presentation.ui.FilterTypes;
import co.techmagic.hr.presentation.ui.adapter.FilterAdapter;
import co.techmagic.hr.presentation.util.KeyboardUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class SearchActivity extends BaseActivity<SearchViewImpl, SearchPresenter> implements FilterAdapter.OnFilterSelectionListener {

    public static final String DEP_ID_EXTRA = "dep_id_extra";
    public static final String LEAD_ID_EXTRA = "lead_id_extra";
    public static final String PROJECT_ID_EXTRA = "project_id_extra";
    public static final String SEARCH_QUERY_EXTRA = "search_query_extra";

    @BindView(R.id.tvSelectedDep)
    TextView tvDepartment;
    @BindView(R.id.tvSelectedLead)
    TextView tvLead;
    @BindView(R.id.tvSelProject)
    TextView tvSelProject;
    SearchView searchView;

    private FilterDialogManager dialogManager;
    private FilterTypes filterTypes = FilterTypes.NONE;

    private String selDepId;
    private String selLeadId;
    private String selProjectId;
    private String searchQuery = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
        dialogManager = new FilterDialogManager(this, this);
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
            public void showFilterByDepartmentDialog(@NonNull List<Filter> departments) {
                filterTypes = FilterTypes.DEPARTMENT;
                dialogManager.dismissDialogIfOpened();
                dialogManager.showSelectFilterAlertDialog(departments, filterTypes);
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
                dialogManager.dismissDialogIfOpened();
                dialogManager.showSelectFilterAlertDialog(leads, filterTypes);
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
            public void showFilterByProjectDialog(@NonNull List<Filter> projects) {
                filterTypes = FilterTypes.PROJECT;
                dialogManager.dismissDialogIfOpened();
                dialogManager.showSelectFilterAlertDialog(projects, filterTypes);
            }

            @Override
            public void showSelectedProjectFilter(@NonNull String id, @NonNull String filterName) {
                selProjectId = id;
                tvSelProject.setText(filterName);
            }

            @Override
            public void showEmptyProjectFiltersErrorMessage(int resId) {
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
        if (dialogManager.isDialogActive()) {
            dialogManager.dismissDialogIfOpened();
        } else {
            KeyboardUtil.hideKeyboard(this, getCurrentFocus());
            Intent i = new Intent();
            setResult(RESULT_CANCELED, i);
            finish();
            overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out);
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


    @OnClick(R.id.rlFilterByProject)
    public void onFilterByProjectClick() {
        presenter.onFilterByProjectClick();
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
    public void onFilterSelected(@NonNull IFilterModel model) {
        handleSelection(model.getId(), model.getName());
    }


    private void handleSelection(String id, String name) {
        dialogManager.dismissDialogIfOpened();

        switch (filterTypes) {
            case DEPARTMENT:
                selDepId = id;
                tvDepartment.setText(name);
                break;

            case LEAD:
                selLeadId = id;
                tvLead.setText(name);
                break;

            case PROJECT:
                selProjectId = id;
                tvSelProject.setText(name);
                break;
        }
    }


    private void clearAllFilters() {
        searchQuery = null;
        selDepId = null;
        selLeadId = null;
        selProjectId = null;
        searchView.setQuery("", false);
        tvDepartment.setText("");
        tvLead.setText("");
        tvSelProject.setText("");
        SharedPreferencesUtil.saveSelectedDepartmentId(null);
        SharedPreferencesUtil.saveSelectedLeadId(null);
        SharedPreferencesUtil.saveSelectedProjectId(null);
        filterTypes = FilterTypes.NONE;
    }


    private void applyFilters() {
        SharedPreferencesUtil.saveSelectedDepartmentId(selDepId);
        SharedPreferencesUtil.saveSelectedLeadId(selLeadId);
        SharedPreferencesUtil.saveSelectedProjectId(selProjectId);

        Intent i = new Intent();
        i.putExtra(SEARCH_QUERY_EXTRA, searchQuery);
        i.putExtra(DEP_ID_EXTRA, selDepId);
        i.putExtra(LEAD_ID_EXTRA, selLeadId);
        i.putExtra(PROJECT_ID_EXTRA, selProjectId);

        setResult(Activity.RESULT_OK, i);
        finish();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out);
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
        searchView.setQueryHint(getString(R.string.tm_hr_home_activity_searchview_text_search));
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
}