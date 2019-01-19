package co.techmagic.hr.presentation.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
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
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.presentation.mvp.presenter.HomePresenter;
import co.techmagic.hr.presentation.mvp.view.impl.HomeViewImpl;
import co.techmagic.hr.presentation.ui.ProfileTypes;
import co.techmagic.hr.presentation.ui.adapter.EmployeeAdapter;
import co.techmagic.hr.presentation.ui.fragment.CalendarFragment;
import co.techmagic.hr.presentation.ui.fragment.DetailsFragment;
import co.techmagic.hr.presentation.ui.fragment.FragmentCallback;
import co.techmagic.hr.presentation.ui.fragment.TimeTrackerFragment;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;
import co.techmagic.hr.presentation.ui.view.ChangeBottomTabListener;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class HomeActivity extends BaseActivity<HomeViewImpl, HomePresenter> implements ActionBarChangeListener, FragmentCallback,
        EmployeeAdapter.OnEmployeeItemClickListener, ChangeBottomTabListener {

    public static final String USER_ID_PARAM = "user_id_param";
    public static final String PROFILE_TYPE_PARAM = "profile_type_param";
    public static final String SEARCH_QUERY_EXTRAS = "search_query_extras";
    public static final String FRAGMENT_DETAILS_TAG = "fragment_details_tag";
    public static final String FRAGMENT_MY_PROFILE_TAG = "fragment_my_profile_tag";
    private static final String FRAGMENT_CALENDAR_TAG = "fragment_calendar_tag";
    private static final String FRAGMENT_TIME_TRACKER_TAG = "fragment_time_tracker_tag";

    private static final String MIXPANEL_HOME_TAG = "Home";
    private static final String MIXPANEL_SEARCH_EMPLOYEES_TAG = "Search Employees";
    private static final String MIXPANEL_USER_DETAILS_TAG = "User Details";
    private static final String MIXPANEL_MY_PROFILE_TAG = "My Profile";
    private static final String MIXPANEL_EDIT_PROFILE_TAG = "Edit Profile";
    private static final String MIXPANEL_CALENDAR_TAG = "Calendar";

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

    private ActionBar actionBar;
    private LinearLayoutManager linearLayoutManager;
    private EmployeeAdapter adapter;

    private String selDepId;
    private String selLeadId;
    private String selProjectId;
    private String searchQuery = null;
    private boolean allowChangeTab = true;
    private boolean isOnActivityResultCalled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.setupFiltersView(selDepId, selLeadId, selProjectId, searchQuery);

        if (!isOnActivityResultCalled) {
            isOnActivityResultCalled = false;
            loadMoreEmployees(null, selDepId, selLeadId, selProjectId, 0, 0, false);
        }
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
            public void showEmployeesList(List<UserProfile> docs) {
                tvNoResults.setVisibility(View.GONE);
                adapter.refresh(docs);
                rvEmployees.setVisibility(View.VISIBLE);
            }

            @Override
            public void showNoResultsView(int resId) {
                showNoResults(resId);
            }

            @Override
            public void showEmployeeDetails(@NonNull UserProfile data) {
                allowChangeTab = true;
                addDetailsFragment(data, ProfileTypes.EMPLOYEE, FRAGMENT_DETAILS_TAG);
            }

            @Override
            public void showMyProfile(@NonNull UserProfile data) {
                addDetailsFragment(data, ProfileTypes.MY_PROFILE, FRAGMENT_MY_PROFILE_TAG);
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

            case R.id.menu_item_edit_profile:
                startEditProfileScreen();
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
                selProjectId = data.getStringExtra(SearchActivity.PROJECT_ID_EXTRA);
            }

            isOnActivityResultCalled = true;
            loadMoreEmployees(searchQuery, selDepId, selLeadId, selProjectId, 0, 0, true);
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
    public void onEmployeeItemClicked(@NonNull UserProfile userProfile) {
        presenter.handleEmployeeItemClick(userProfile);
    }


    @Override
    public void addDetailsFragment(@NonNull UserProfile user, @NonNull ProfileTypes profileType, @Nullable String tag) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PROFILE_TYPE_PARAM, profileType);
        bundle.putString(USER_ID_PARAM, user.getId());

        DetailsFragment fragment = DetailsFragment.newInstance();
        fragment.setArguments(bundle);
        replaceFragment(fragment, tag);

        if (profileType == ProfileTypes.MY_PROFILE) {
            mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_MY_PROFILE_TAG);
        } else if (profileType == ProfileTypes.EMPLOYEE) {
            mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_USER_DETAILS_TAG);
        }
    }


    @Override
    public void addCalendarFragment() {
        CalendarFragment fragment = CalendarFragment.newInstance();
        replaceFragment(fragment, FRAGMENT_CALENDAR_TAG);
        mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_CALENDAR_TAG);
    }

    private void addTimeTrackerFragment() {
        TimeTrackerFragment fragment = TimeTrackerFragment.Companion.newInstance();
        replaceFragment(fragment, FRAGMENT_TIME_TRACKER_TAG);
//        mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_TIME_TRACKER_TAG);
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
        selProjectId = SharedPreferencesUtil.getSelectedProjectId();
    }


    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_tracker: {
                    addTimeTrackerFragment();
                    break;
                }

                case R.id.action_ninjas:
                    if (allowChangeTab) {
                        clearFragmentsBackStack(this);
                        mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_HOME_TAG);
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

        // TODO: 1/19/19 refactor screen structure as ninjas list is not a fragment and always is the first visible view
        bottomNavigation.setSelectedItemId(R.id.action_tracker);
    }


    @SuppressLint("RestrictedApi")
    private void startSearchScreen() {
        Bundle animation = ActivityOptions.makeCustomAnimation(this, R.anim.anim_slide_in, R.anim.anim_not_move).toBundle();
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(SEARCH_QUERY_EXTRAS, searchQuery);
        startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE, animation);
        mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_SEARCH_EMPLOYEES_TAG);
    }


    private void startEditProfileScreen() {
        Bundle animation = ActivityOptions.makeCustomAnimation(this, R.anim.anim_slide_in, R.anim.anim_not_move).toBundle();
        startActivity(new Intent(this, EditProfileActivity.class), animation);
        mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_EDIT_PROFILE_TAG);
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
        selProjectId = null;
        SharedPreferencesUtil.saveSelectedDepartmentId(null);
        SharedPreferencesUtil.saveSelectedLeadId(null);
        SharedPreferencesUtil.saveSelectedProjectId(null);
        loadMoreEmployees(null, selDepId, selLeadId, selProjectId,  0, 0, false);
    }


    /**
     * @param visibleItemsCount Used to show whether all items are already loaded.
     */

    private void loadMoreEmployees(@Nullable String searchQuery, @Nullable String selDepId, @Nullable String selLeadId, @Nullable String selProjectId, int offset, int visibleItemsCount, boolean isAfterFilters) {
        if (isAfterFilters) {
            presenter.loadEmployeesAfterFilters(searchQuery, selDepId, selLeadId, selProjectId, offset, visibleItemsCount);
        } else {
            presenter.loadEmployees(searchQuery, selDepId, selLeadId, selProjectId, offset, visibleItemsCount);
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
                    loadMoreEmployees(searchQuery, selDepId, selLeadId, selProjectId, totalItemCount, totalItemCount, false);
                }
            }
        };
    }
}