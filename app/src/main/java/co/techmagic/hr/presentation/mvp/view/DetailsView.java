package co.techmagic.hr.presentation.mvp.view;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface DetailsView extends View {

    void loadEmployeePhoto(@Nullable String photoUrl);

    void showEmail(@NonNull String email);

    void hideEmail();

    void showSkype(@NonNull String skype);

    void hideSkype();

    void showPhone(@NonNull String phone);

    void hidePhone();

    void showRoom(@NonNull String room);

    void hideRoom();

    void showDepartment(@NonNull String department);

    void hideDepartment();

    void showLead(@NonNull String lead);

    void hideLead();

    void showBirthday(@NonNull String birthday);

    void hideBirthday();

    void showRelocationCity(@NonNull String city);

    void hideRelocationCity();

    void showFirstDay(@NonNull String date);

    void hideFirstDay();

    void showFirstDayInIt(@NonNull String date);

    void hideFirstDayInIt();

    void showAbout(@NonNull String aboutText);

    void hideAbout();

    void showEmergencyPhoneNumber(@NonNull String phone);

    void hideEmergencyPhoneNumber();

    void showEmergencyContact(@NonNull String contact);

    void hideEmergencyContact();

    void showTrialPeriodEndsDate(@NonNull String date);

    void hideTrialPeriodEndsDate();

    void showLastWorkingDay(@NonNull String date);

    void hideLastWorkingDay();

    void showReason(@NonNull String reason);

    void hideReason();

    void showComment(@NonNull String comment);

    void hideComment();

    void showVacationDays(@NonNull String dates);

    void hideVacations();

    void showDayOff(@NonNull String dates);

    void hideDayOff();

    void showIllnessDays(@NonNull String dates);

    void hideIllnessDays();

    void onCopyEmailToClipboard(@NonNull String email);

    void onCopySkypeToClipboard(@NonNull String skype);

    void onCopyPhoneToClipboard(@NonNull String phone);

    void showConfirmationDialog();

    void saveImage(@NonNull final Bitmap image);

    void allowChangeBottomTab();

    void disallowChangeBottomTab();
}
