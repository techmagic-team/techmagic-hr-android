package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface EmployeeDetailsView extends View {

    void showEmployeeName(@NonNull String name);

    void loadEmployeePhoto(@Nullable String photoUrl);

    void showEmail(@NonNull String email);

    void showSkype(@NonNull String skype);

    void showPhone(@NonNull String phone);

    void showRoom(@NonNull String room);

    void showDepartment(@NonNull String department);

    void showLead(@NonNull String lead);

    void showBirthday(@NonNull String birthday);

    void showRelocationCity(@NonNull String city);

    void showFirstDay(@NonNull String date);

    void showAbout(@NonNull String aboutText);

    void showEmergencyPhoneNumber(@NonNull String phone);

    void showEmergencyContact(@NonNull String contact);

    void showTrialPeriodEndsDate(@NonNull String date);

    void showLastWorkingDay(@NonNull String date);

    void onCopyEmailToClipboard(@NonNull String email);

    void onCopySkypeToClipboard(@NonNull String skype);

    void onCopyPhoneToClipboard(@NonNull String phone);
}
