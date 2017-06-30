package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;

import java.util.List;

import co.techmagic.hr.data.entity.UserProfile;

public interface HomeView extends View {

    void showFiltersView();

    void hideFiltersView();

    void addLoadingProgress();

    void hideLoadingProgress();

    void clearAdapter();

    void showEmployeesList(List<UserProfile> docs);

    void showNoResultsView(int resId);

    void showEmployeeDetails(@NonNull UserProfile data);

    void showMyProfile(@NonNull UserProfile data);

    void allowChangeTabClick();

    void disallowChangeTabClick();
}
