package co.techmagic.hr.presentation.mvp.view;

import java.util.List;

import co.techmagic.hr.data.entity.Docs;

public interface HomeView extends View {

    void showFiltersView();

    void hideFiltersView();

    void addLoadingProgress();

    void hideLoadingProgress();

    void showEmployeesList(List<Docs> docs, boolean shouldClearList);

    void showNoResultsView(int resId);
}
