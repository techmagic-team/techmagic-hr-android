package co.techmagic.hr.presentation.mvp.view;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import co.techmagic.hr.data.entity.IFilterModel;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.presentation.ui.EditProfileFields;

public interface EditProfileView {

    void pickUpPhoto();

    void loadEmployeePhoto(@Nullable String photoUrl);

    void showDatePickerDialog();

    void showSelectedFilter(@NonNull String id, @NonNull String name, EditProfileFields editProfileField);

    void showSelectedLead(@NonNull Lead lead, EditProfileFields editProfileField);

    <T extends IFilterModel> void showFiltersInDialog(@Nullable List<T> filters);

    void showConfirmationDialog();

    void onBackClick();

    /* Login section */

    void showLoginSection();

    void enableEmail();

    void showEmail(@NonNull String email);

    void enablePassword();

    void showPassword(@NonNull String password);

    void showCanSignInView(boolean canSignIn);

    void hideEmailError();

    void onEmailError();

    void onPasswordError();

    void hidePasswordError();

    /* Personal section */

    void showPersonalSection();

    void enableFirstName();

    void showFirstName(@NonNull String firstName);

    void enableLastName();

    void showLastName(@NonNull String lastName);

    void allowClickOnBirthDateView();

    void disAllowClickOnBirthDateView();

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

    void hidePdpError();

    void showPdpError();

    void hideOneToOneError();

    void showOneToOneError();

    /* Out of the company section */

    void showOutOfCompanySection();

    void showLastWorkingDay(@NonNull String date);

    void showReason(@NonNull String text);

    void showComments(@NonNull String text);
}