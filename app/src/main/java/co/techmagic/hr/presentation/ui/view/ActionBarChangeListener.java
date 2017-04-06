package co.techmagic.hr.presentation.ui.view;

import android.support.annotation.NonNull;

/**
 * Methods to update actionbar should be called only in Fragment's onCreateOptionsMenu.
 * Otherwise they won't be work.
 * */

public interface ActionBarChangeListener {

    void showHomeActionBar();

    void showBackButton();

    void showEmployeeDetailsActionBar(@NonNull String title);

    void showMyProfileActionBar();
}
