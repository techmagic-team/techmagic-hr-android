package co.techmagic.hr.presentation.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Responsible for available actions with user photo.
 * */

public interface UserPhotoActionListener {

    void onCloseImage();

    void onDownloadImage(@NonNull Context context);
}
