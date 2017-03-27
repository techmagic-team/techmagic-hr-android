package co.techmagic.hr.presentation.ui.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.HomePresenter;
import co.techmagic.hr.presentation.mvp.view.impl.HomeViewImpl;
import co.techmagic.hr.presentation.ui.adapter.EmployeeAdapter;

public class HomeActivity extends BaseActivity<HomeViewImpl, HomePresenter> {

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.rvEmployees)
    RecyclerView rvEmployees;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_home);
    }


    @Override
    protected HomeViewImpl initView() {
        return new HomeViewImpl(this, findViewById(android.R.id.content));
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
            case R.id.menu_item_logout:
                showLogOutDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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


    private void setupRecyclerView() {
        final EmployeeAdapter adapter = new EmployeeAdapter();
        rvEmployees.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvEmployees.setAdapter(adapter);
    }
}
