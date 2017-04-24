package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;

import java.util.List;

import co.techmagic.hr.data.entity.Docs;

public interface HomeView extends View {

    void showFiltersView();

    void hideFiltersView();

    void addLoadingProgress();

    void hideLoadingProgress();

    void clearAdapter();

    void showEmployeesList(List<Docs> docs);

    void showNoResultsView(int resId);

    void showEmployeeDetails(@NonNull Docs data);

    void showMyProfile(@NonNull Docs data);

    void allowChangeTabClick();

    void disallowChangeTabClick();
}
