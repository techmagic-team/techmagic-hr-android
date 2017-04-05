package co.techmagic.hr.presentation.ui.fragment;

import android.support.annotation.NonNull;
import co.techmagic.hr.data.entity.Docs;

public interface FragmentCallback {

    void addEmployeeDetailsFragment(@NonNull Docs docs);

    void addMyProfileFragment();
}
