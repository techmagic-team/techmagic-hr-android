package co.techmagic.hr.presentation.ui.activity;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.SearchPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.SearchViewImpl;
import co.techmagic.hr.presentation.util.KeyboardUtil;

public class SearchActivity extends BaseActivity<SearchViewImpl, SearchPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_search);
    }


    @Override
    protected SearchViewImpl initView() {
        return null;
    }


    @Override
    protected SearchPresenter initPresenter() {
        return new SearchPresenter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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


    @OnClick(R.id.btnCancel)
    public void onCancelClick() {
        handleOnCancelClick();
    }


    @OnClick(R.id.btnApply)
    public void onApplyClick() {
        handleOnApplyClick();
    }


    private void handleOnCancelClick() {
        KeyboardUtil.hideKeyboard(this, getCurrentFocus());
        startHomeScreen();
        finish();
    }


    private void handleOnApplyClick() {

    }


    private void initUi() {
        setupActionBar();
    }


    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }
}
