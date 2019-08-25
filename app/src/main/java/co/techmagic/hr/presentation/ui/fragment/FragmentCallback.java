package co.techmagic.hr.presentation.ui.fragment;

import android.support.annotation.NonNull;

import co.techmagic.hr.data.entity.UserProfile;

public interface FragmentCallback {

    void addDetailsFragment(@NonNull UserProfile userProfile);

    void addCalendarFragment();
}