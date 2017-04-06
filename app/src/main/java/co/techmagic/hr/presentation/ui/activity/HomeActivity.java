package co.techmagic.hr.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.presentation.mvp.presenter.HomePresenter;
import co.techmagic.hr.presentation.mvp.view.impl.HomeViewImpl;
import co.techmagic.hr.presentation.ui.adapter.EmployeeAdapter;
import co.techmagic.hr.presentation.ui.fragment.EmployeeDetailsFragment;
import co.techmagic.hr.presentation.ui.fragment.FragmentCallback;
import co.techmagic.hr.presentation.ui.fragment.MyProfileFragment;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class HomeActivity extends BaseActivity<HomeViewImpl, HomePresenter> implements FragmentCallback, EmployeeAdapter.OnEmployeeItemClickListener {

    public static final String DOCS_OBJECT_PARAM = "docs_object_param";
    public static final String SEARCH_QUERY_EXTRAS = "search_query_extras";
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 1001;
    public static final int ITEMS_COUNT = 10;

    @BindView(R.id.flFilters)
    View flFilters;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.rvEmployees)
    RecyclerView rvEmployees;
    @BindView(R.id.tvNoResults)
    TextView tvNoResults;

    private LinearLayoutManager linearLayoutManager;
    private EmployeeAdapter adapter;

    private String selDepId;
    private String selLeadId;
    private String searchQuery = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
        loadMoreEmployees(null, selDepId, selLeadId, 0, 0, false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.setupFiltersView(selDepId, selLeadId, searchQuery);
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_home);
    }


    @Override
    protected HomeViewImpl initView() {
        return new HomeViewImpl(this, findViewById(android.R.id.content)) {
            @Override
            public void showFiltersView() {
                flFilters.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideFiltersView() {
                flFilters.setVisibility(View.GONE);
            }

            @Override
            public void addLoadingProgress() {
                adapter.addLoadingProgress();
            }

            @Override
            public void hideLoadingProgress() {
                adapter.removeLoadingProgress();
            }

            @Override
            public void clearAdapter() {
                adapter.clear();
            }

            @Override
            public void showEmployeesList(List<Docs> docs) {
                tvNoResults.setVisibility(View.GONE);
                adapter.refresh(docs);
                rvEmployees.setVisibility(View.VISIBLE);
            }

            @Override
            public void showNoResultsView(int resId) {
                showNoResults(resId);
            }
        };
    }


    @Override
    protected HomePresenter initPresenter() {
        return new HomePresenter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                startSearchScreen();
                return true;

            case R.id.menu_item_logout:
                showLogOutDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                searchQuery = data.getStringExtra(SearchActivity.SEARCH_QUERY_EXTRA);
                selDepId = data.getStringExtra(SearchActivity.DEP_ID_EXTRA);
                selLeadId = data.getStringExtra(SearchActivity.LEAD_ID_EXTRA);
            }

            loadMoreEmployees(searchQuery, selDepId, selLeadId, 0, 0, true);
        }
    }


    @Override
    public void onEmployeeItemClicked(@NonNull Docs docs) {
        addEmployeeDetailsFragment(docs);
    }


    @Override
    public void addEmployeeDetailsFragment(@NonNull Docs docs) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DOCS_OBJECT_PARAM, docs);

        EmployeeDetailsFragment fragment = EmployeeDetailsFragment.newInstance();
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }


    @Override
    public void addMyProfileFragment() {
        MyProfileFragment fragment = MyProfileFragment.newInstance();
        replaceFragment(fragment);
    }


    @OnClick(R.id.btnClearFilters)
    public void onClearFiltersClick() {
        handleClearFiltersClick();
    }


    private void initUi() {
        selDepId = SharedPreferencesUtil.getSelectedDepartmentId();
        selLeadId = SharedPreferencesUtil.getSelectedLeadId();
        setupBottomNavigation();
        setupRecyclerView();
        presenter.setupFiltersView(selDepId, selLeadId, searchQuery);
    }


    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_ninjas:
                    clearFragmentsBackStack(this);
                    break;

                case R.id.action_my_profile:
                    addMyProfileFragment();
                    break;
            }
            return true;
        });
    }


    private void startSearchScreen() {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(SEARCH_QUERY_EXTRAS, searchQuery);
        startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
    }


    private void setupRecyclerView() {
        adapter = new EmployeeAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvEmployees.setLayoutManager(linearLayoutManager);
        rvEmployees.setAdapter(adapter);
        rvEmployees.addOnScrollListener(getOnScrollListener());
    }


    private void showNoResults(int resId) {
        rvEmployees.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.VISIBLE);
        tvNoResults.setText(getString(resId));
    }


    private void handleClearFiltersClick() {
        clearFilterIds();
        flFilters.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.GONE);
        adapter.clear();
        SharedPreferencesUtil.saveSelectedDepartmentId(null);
        SharedPreferencesUtil.saveSelectedLeadId(null);
        loadMoreEmployees(null, selDepId, selLeadId, 0, 0, false);
    }


    private void clearFilterIds() {
        selDepId = null;
        selLeadId = null;
    }


    /**
     * @param visibleItemsCount Used to show whether all items are already loaded.
     */

    private void loadMoreEmployees(@Nullable String searchQuery, @Nullable String selDepId, @Nullable String selLeadId, int offset, int visibleItemsCount, boolean isAfterFilters) {
        if (isAfterFilters) {
            presenter.loadEmployeesAfterFilters(searchQuery, selDepId, selLeadId, offset, visibleItemsCount);
        } else {
            presenter.loadEmployees(searchQuery, selDepId, selLeadId, offset, visibleItemsCount);
        }
    }


    private RecyclerView.OnScrollListener getOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= ITEMS_COUNT) {
                    loadMoreEmployees(searchQuery, selDepId, selLeadId, totalItemCount, totalItemCount, false);
                }
            }
        };
    }
}