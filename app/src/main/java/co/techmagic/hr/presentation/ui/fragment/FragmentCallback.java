package co.techmagic.hr.presentation.ui.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.presentation.ui.ProfileTypes;

public interface FragmentCallback {

    void addDetailsFragment(@NonNull UserProfile userProfile, @NonNull ProfileTypes profileType, @Nullable String tag);

    void addCalendarFragment();
}