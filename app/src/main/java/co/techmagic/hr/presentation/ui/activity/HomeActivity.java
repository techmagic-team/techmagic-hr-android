package co.techmagic.hr.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
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
import co.techmagic.hr.presentation.ui.fragment.CalendarFragment;
import co.techmagic.hr.presentation.ui.fragment.DetailsFragment;
import co.techmagic.hr.presentation.ui.fragment.FragmentCallback;
import co.techmagic.hr.presentation.ui.fragment.ProfileTypes;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;
import co.techmagic.hr.presentation.ui.view.ChangeBottomTabListener;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class HomeActivity extends BaseActivity<HomeViewImpl, HomePresenter> implements ActionBarChangeListener, FragmentCallback,
        EmployeeAdapter.OnEmployeeItemClickListener, ChangeBottomTabListener {

    public static final String DOCS_OBJECT_PARAM = "docs_object_param";
    public static final String PROFILE_TYPE_PARAM = "profile_type_param";
    public static final String SEARCH_QUERY_EXTRAS = "search_query_extras";
    private static final String FRAGMENT_DETAILS_TAG = "fragment_details_tag";
    private static final String FRAGMENT_CALENDAR_TAG = "fragment_calendar_tag";
    private static final String FRAGMENT_MY_PROFILE_TAG = "fragment_my_profile_tag";

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 1001;
    public static final int CALENDAR_FILTERS_ACTIVITY_REQUEST_CODE = 1002;
    public static final int ITEMS_COUNT = 10;

    @BindView(R.id.flFilters)
    View flFilters;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.rvEmployees)
    RecyclerView rvEmployees;
    @BindView(R.id.tvNoResults)
    TextView tvNoResults;

    private ActionBar actionBar;
    private LinearLayoutManager linearLayoutManager;
    private EmployeeAdapter adapter;
    private ProfileTypes profileType = ProfileTypes.NONE;

    private String selDepId;
    private String selLeadId;
    private String searchQuery = null;
    private boolean allowChangeTab = true;


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

            @Override
            public void showEmployeeDetails(@NonNull Docs data) {
                profileType = ProfileTypes.EMPLOYEE;
                allowChangeTab = true;
                addDetailsFragment(data, FRAGMENT_DETAILS_TAG);
            }

            @Override
            public void showMyProfile(@NonNull Docs data) {
                profileType = ProfileTypes.MY_PROFILE;
                addDetailsFragment(data, FRAGMENT_MY_PROFILE_TAG);
            }

            @Override
            public void allowChangeTabClick() {
                allowChangeTab = true;
            }

            @Override
            public void disallowChangeTabClick() {
                allowChangeTab = false;
            }
        };
    }


    @Override
    protected HomePresenter initPresenter() {
        return new HomePresenter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(getString(R.string.app_name));
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                removeFragmentFromBackStack();
                return true;

            case R.id.search:
                startSearchScreen();
                return true;

            case R.id.menu_item_logout:
                showLogOutDialog();
                return true;

            case R.id.action_calendar_filters:
                startCalendarFiltersScreen();
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

        } else if (requestCode == CALENDAR_FILTERS_ACTIVITY_REQUEST_CODE) {

        }
    }


    /**
     * Methods to update actionbar should be called only in Fragment's onCreateOptionsMenu.
     * Otherwise they won't be work.
     */

    @Override
    public void showBackButton() {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void setActionBarTitle(@NonNull String title) {
        actionBar.setTitle(title);
    }


    @Override
    public void onEmployeeItemClicked(@NonNull Docs docs) {
        presenter.handleEmployeeItemClick(docs);
    }


    @Override
    public void addDetailsFragment(@NonNull Docs docs, @Nullable String tag) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PROFILE_TYPE_PARAM, profileType);
        bundle.putParcelable(DOCS_OBJECT_PARAM, docs);

        DetailsFragment fragment = DetailsFragment.newInstance();
        fragment.setArguments(bundle);
        replaceFragment(fragment, tag);
    }


    @Override
    public void addCalendarFragment() {
        CalendarFragment fragment = CalendarFragment.newInstance();
        replaceFragment(fragment, FRAGMENT_CALENDAR_TAG);
    }


    @OnClick(R.id.btnClearFilters)
    public void onClearFiltersClick() {
        handleClearFiltersClick();
    }


    @Override
    public void allowBottomTabClick() {
        allowChangeTab = true;
    }


    @Override
    public void disableBottomTabClick() {
        allowChangeTab = false;
    }


    private void initUi() {
        actionBar = getSupportActionBar();
        setupBottomNavigation();
        setupRecyclerView();
        selDepId = SharedPreferencesUtil.getSelectedDepartmentId();
        selLeadId = SharedPreferencesUtil.getSelectedLeadId();
        presenter.setupFiltersView(selDepId, selLeadId, searchQuery);
    }


    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_ninjas:
                    if (allowChangeTab) {
                        profileType = ProfileTypes.NONE;
                        clearFragmentsBackStack(this);
                    }
                    break;

                case R.id.action_calendar:
                    addCalendarFragment();
                    break;

                case R.id.action_my_profile:
                    presenter.handleMyProfileClick();
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


    private void startCalendarFiltersScreen() {
        Intent i = new Intent(this, CalendarFiltersActivity.class);
       // i.putExtra(SEARCH_QUERY_EXTRAS, searchQuery);
        startActivityForResult(i, CALENDAR_FILTERS_ACTIVITY_REQUEST_CODE);
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
        flFilters.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.GONE);
        adapter.clear();
        searchQuery = null;
        selDepId = null;
        selLeadId = null;
        SharedPreferencesUtil.saveSelectedDepartmentId(null);
        SharedPreferencesUtil.saveSelectedLeadId(null);
        loadMoreEmployees(null, selDepId, selLeadId, 0, 0, false);
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