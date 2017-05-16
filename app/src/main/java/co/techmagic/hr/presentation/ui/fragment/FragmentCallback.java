package co.techmagic.hr.presentation.ui.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import co.techmagic.hr.data.entity.Docs;

public interface FragmentCallback {

    void addDetailsFragment(@NonNull Docs docs, @NonNull ProfileTypes profileType, @Nullable String tag);

    void addCalendarFragment();
}