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
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.presentation.mvp.presenter.HomePresenter;
import co.techmagic.hr.presentation.mvp.view.impl.HomeViewImpl;
import co.techmagic.hr.presentation.ui.adapter.EmployeeAdapter;

public class HomeActivity extends BaseActivity<HomeViewImpl, HomePresenter> implements EmployeeAdapter.OnEmployeeItemClickListener {

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 1001;
    public static final int ITEMS_COUNT = 10;

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.rvEmployees)
    RecyclerView rvEmployees;
    @BindView(R.id.tvNoResults)
    TextView tvNoResults;

    private LinearLayoutManager linearLayoutManager;
    private EmployeeAdapter adapter;

    private String selDepId;
    private String selDepName;
    private String selLeadId;
    private String selLeadName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
        loadMoreEmployees(selDepId, selLeadId, 0, 0);
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_home);
    }


    @Override
    protected HomeViewImpl initView() {
        return new HomeViewImpl(this, findViewById(android.R.id.content)) {
            @Override
            public void addLoadingProgress() {
                adapter.addLoadingProgress();
            }

            @Override
            public void hideLoadingProgress() {
                adapter.removeLoadingProgress();
            }

            @Override
            public void showEmployeesList(List<Docs> docs, boolean shouldClearList) {
                tvNoResults.setVisibility(View.GONE);
                rvEmployees.setVisibility(View.VISIBLE);
                adapter.refresh(docs, shouldClearList);
            }

            @Override
            public void showNoResultsView() {
                rvEmployees.setVisibility(View.GONE);
                tvNoResults.setVisibility(View.VISIBLE);
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
                if (data.getStringExtra(SearchActivity.DEP_ID_EXTRA) != null && data.getStringExtra(SearchActivity.DEP_NAME_EXTRA) != null) {
                    selDepId = data.getStringExtra(SearchActivity.DEP_ID_EXTRA);
                    selDepName = data.getStringExtra(SearchActivity.DEP_NAME_EXTRA);

                } else if (data.getStringExtra(SearchActivity.LEAD_ID_EXTRA) != null && data.getStringExtra(SearchActivity.LEAD_NAME_EXTRA) != null) {
                    selLeadId = data.getStringExtra(SearchActivity.LEAD_ID_EXTRA);
                    selLeadName = data.getStringExtra(SearchActivity.LEAD_NAME_EXTRA);
                }

                loadMoreEmployees(selDepId, selLeadId, 0, 0);

            } else if (resultCode == RESULT_CANCELED) {
                selDepId = null;
                selDepName = null;
                selLeadId = null;
                selLeadName = null;
            }
        }
    }


    @Override
    public void onEmployeeItemClicked(@NonNull Docs docs) {
        // TODO show employee details
    }


    private void initUi() {
        setupBottomNavigation();
        setupRecyclerView();
    }


    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_ninjas:
                    break;
            }
            return true;
        });
    }


    private void startSearchScreen() {
        startActivityForResult(new Intent(this, SearchActivity.class), SEARCH_ACTIVITY_REQUEST_CODE);
    }


    private void setupRecyclerView() {
        adapter = new EmployeeAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvEmployees.setLayoutManager(linearLayoutManager);
        rvEmployees.setAdapter(adapter);
        rvEmployees.addOnScrollListener(getOnScrollListener());
    }


    /**
     * @param visibleItemsCount Used to show whether all items are already loaded.
     */

    private void loadMoreEmployees(@Nullable String selDepId, @Nullable String selLeadId, int offset, int visibleItemsCount) {
        presenter.loadEmployees(selDepId, selLeadId, offset, visibleItemsCount);
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
                    loadMoreEmployees(selDepId, selLeadId, totalItemCount, totalItemCount);
                }
            }
        };
    }
}