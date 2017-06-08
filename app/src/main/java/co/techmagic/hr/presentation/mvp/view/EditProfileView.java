package co.techmagic.hr.presentation.mvp.view;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface EditProfileView {

    void loadEmployeePhoto(@Nullable String photoUrl);

    void showDatePickerDialog();

    /* Login section */

    void showLoginSection();

    void showEmail(@NonNull String email);

    void showPassword(@NonNull String password);

    void showCanSignInView(boolean canSignIn);

    /* Personal section */

    void showPersonalSection();

    void showFirstName(@NonNull String firstName);

    void showLastName(@NonNull String lastName);

    void showBirthDate(@NonNull String date);

    void showGenderView();

    void hideGenderView();

    void showGenderMale();

    void showGenderFemale();

    /* Contacts section */

    void showContactsSection();

    void showSkype(@NonNull String skype);

    void showPhoneNumber(@NonNull String phone);

    void showEmergencyNumber(@NonNull String phone);

    void showEmergencyContact(@NonNull String phone);

    void showRoom(@NonNull String room);

    /* Additional section */

    void showAdditionalSection();

    void showCityOfRelocation(@NonNull String city);

    void showPresentationText(@NonNull String text);

    /* Professional section */

    void showProfessionalSection();

    void showDepartment(@NonNull String department);

    void showLead(@NonNull String lead);

    void showFirstWorkingDay(@NonNull String date);

    void showFirstWorkingDayInIt(@NonNull String date);

    void showTrialPeriodEnds(@NonNull String date);

    /* PDP section */

    void showPdpSection();

    void showPdpLink(@NonNull String link);

    void showOneToOneLink(@NonNull String link);

    /* Out of the company section */

    void showOutOfCompanySection();

    void showLastWorkingDay(@NonNull String date);

    void showReason(@NonNull String text);

    void showComments(@NonNull String text);
}
