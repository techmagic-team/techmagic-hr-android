package co.techmagic.hr.presentation.mvp.view;


import android.support.annotation.NonNull;

public interface EditProfileView {

    void showDatePickerDialog();

    /* Login section */

    void showEmail(@NonNull String email);

    void showPassword(@NonNull String password);

    void showCanSignInView();

    /* Personal section */

    void showFirstName(@NonNull String firstName);

    void showLastName(@NonNull String lastName);

    void showDayOfBirth(@NonNull String date);

    void showGenderView();

    void showGender();

    /* Contacts section */

    void showSkype(@NonNull String skype);

    void showPhoneNumber(@NonNull String phone);

    void showEmergencyNumber(@NonNull String phone);

    void showEmergencyContact(@NonNull String phone);

    void showRoom(@NonNull String room);

    /* Additional section */

    void showCityOfRelocation(@NonNull String city);

    void showPresentationText(@NonNull String text);

    /* Professional section */

    void showDepartment(@NonNull String department);

    void showLead(@NonNull String lead);

    void showFirstWorkingDay(@NonNull String date);

    void showFirstWorkingDayInIt(@NonNull String date);

    void showTrialPeriodEnds(@NonNull String date);

    /* PDP section */

    void showPdpLink(@NonNull String link);

    void showOneToOneLink(@NonNull String link);

    /* Out of the company section */

    void showLastWorkingDay(@NonNull String date);

    void showReason(@NonNull String text);

    void showComments(@NonNull String text);
}
