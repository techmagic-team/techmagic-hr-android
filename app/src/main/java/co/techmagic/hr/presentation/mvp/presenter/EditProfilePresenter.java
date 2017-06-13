package co.techmagic.hr.presentation.mvp.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Department;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.EditProfile;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.data.entity.Room;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.data.repository.UserRepositoryImpl;
import co.techmagic.hr.data.request.EditProfileRequest;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.data.request.UploadPhotoRequest;
import co.techmagic.hr.domain.interactor.employee.GetAllFilters;
import co.techmagic.hr.domain.interactor.user.GetUserProfile;
import co.techmagic.hr.domain.interactor.user.SaveEditedUserProfile;
import co.techmagic.hr.domain.interactor.user.UploadPhoto;
import co.techmagic.hr.domain.pojo.EditProfileFiltersDto;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.domain.repository.IUserRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.impl.EditProfileViewImpl;
import co.techmagic.hr.presentation.ui.EditProfileFields;
import co.techmagic.hr.presentation.util.DateUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;
import co.techmagic.hr.presentation.util.TextUtil;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class EditProfilePresenter extends BasePresenter<EditProfileViewImpl> {

    private static final int GENDER_MALE = 0;
    private static final int GENDER_FEMALE = 1;

    private IUserRepository userRepository;
    private IEmployeeRepository employeeRepository;
    private GetUserProfile getUserProfile;
    private GetAllFilters getAllFilters;
    private UploadPhoto uploadPhoto;
    private SaveEditedUserProfile saveEditedUserProfile;

    private Docs data;
    private EditProfileFiltersDto profileFilters;
    private EmergencyContact emergencyContact;

    private boolean hasChanges = false;


    public EditProfilePresenter() {
        super();
        userRepository = new UserRepositoryImpl();
        employeeRepository = new EmployeeRepositoryImpl();
        getUserProfile = new GetUserProfile(userRepository);
        uploadPhoto = new UploadPhoto(userRepository);
        getAllFilters = new GetAllFilters(employeeRepository);
        saveEditedUserProfile = new SaveEditedUserProfile(userRepository);
        data = new Docs();
        emergencyContact = new EmergencyContact();
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getUserProfile.unsubscribe();
        getAllFilters.unsubscribe();
        uploadPhoto.unsubscribe();
        saveEditedUserProfile.unsubscribe();
    }


    public void setupPage() {
        performGetMyProfileAndAllFiltersRequest();
    }


    public void onUploadPhotoClick() {
        view.pickUpPhoto();
    }


    public void sendPhoto(Uri uri) {
        File file = new File(uri.getPath());
        RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        performUploadPhotoRequest(multipartBody);
    }


    public void onRoomClick() {
        final List<Filter> rooms = profileFilters.getRooms();
        if (rooms.isEmpty()) {
            view.showMessage(R.string.tm_hr_search_activity_text_empty_lead_filters);
        } else {
            view.showFiltersInDialog(rooms);
        }
    }


    public void onDepartmentClick() {
        final List<Filter> departments = profileFilters.getDepartments();
        if (departments.isEmpty()) {
            view.showMessage(R.string.tm_hr_search_activity_text_empty_lead_filters);
        } else {
            view.showFiltersInDialog(departments);
        }
    }


    public void onLeadClick() {
        final List<FilterLead> leads = profileFilters.getLeads();
        if (leads.isEmpty()) {
            view.showMessage(R.string.tm_hr_search_activity_text_empty_lead_filters);
        } else {
            view.showFiltersInDialog(leads);
        }
    }


    public void onReasonClick() {
        final List<Filter> reasons = profileFilters.getReasons();
        if (reasons.isEmpty()) {
            view.showMessage(R.string.tm_hr_search_activity_text_empty_lead_filters);
        } else {
            view.showFiltersInDialog(reasons);
        }
    }


    public void handleEmailChange(String newEmail) {
        final String email = data.getEmail();

        if (TextUtil.isValidEmail(newEmail)) {
            view.hideEmailError();

            if (newEmail.equals(email)) {
                data.setEmail(email);
                hasChanges = false;
            } else {
                data.setEmail(newEmail);
                hasChanges = true;
            }
        } else {
            view.onEmailError();
        }
    }


    public void handlePasswordChange(String newPassword) {
        if (TextUtil.isValidPassword(newPassword)) {
            view.hidePasswordError();
            data.setPassword(newPassword);
        } else {
            view.onPasswordError();
            data.setPassword(null);
        }
    }


    public void handleFirstNameChange(String newFirstName) {
        final String firstName = data.getFirstName();

        if (newFirstName.equals(firstName)) {
            data.setFirstName(firstName);
            hasChanges = false;
        } else {
            data.setFirstName(newFirstName);
            hasChanges = true;
        }
    }


    public void handleLastNameChange(String newLastName) {
        final String lastName = data.getLastName();

        if (newLastName.equals(lastName)) {
            data.setLastName(lastName);
            hasChanges = false;
        } else {
            data.setLastName(newLastName);
            hasChanges = true;
        }
    }


    public void handleDateOfBirthChange(String date) {
        if (date != null) {
            hasChanges = true;
            data.setBirthday(date);
            view.showBirthDate(date);
        }
    }


    public void handleGenderChange(boolean isMaleChecked) {
        final int gender = data.getGender();

        if (isMaleChecked && gender == GENDER_MALE || !isMaleChecked && gender == GENDER_FEMALE) {
            hasChanges = false;
        } else {
            hasChanges = true;
        }

        if (hasChanges) {
            data.setGender(isMaleChecked ? GENDER_MALE : GENDER_FEMALE);
        }
    }


    public void handleSkypeChange(String newSkype) {
        final String skype = data.getSkype();

        if (newSkype.equals(skype)) {
            data.setSkype(skype);
            hasChanges = false;
        } else {
            data.setSkype(newSkype);
            hasChanges = true;
        }
    }


    public void handlePhoneChange(String newPhone) {
        final String phone = data.getPhone();

        if (newPhone.equals(phone)) {
            data.setPhone(phone);
            hasChanges = false;
        } else {
            data.setPhone(newPhone);
            hasChanges = true;
        }
    }


    public void handleEmergencyContactNumberChange(String newNumber) {
        if (newNumber == null) {
            return;
        }

        if (data.getEmergencyContact() == null) {
            emergencyContact.setPhone(newNumber);
            data.setEmergencyContact(emergencyContact);
            hasChanges = true;
            return;
        }

        final String phone = data.getEmergencyContact().getPhone();

        if (newNumber.equals(phone)) {
            emergencyContact.setPhone(phone);
            data.setEmergencyContact(emergencyContact);
            hasChanges = false;
        } else {
            emergencyContact.setPhone(newNumber);
            data.setEmergencyContact(emergencyContact);
            hasChanges = true;
        }
    }


    public void handleEmergencyContactChange(String newName) {
        if (newName == null) {
            return;
        }

        if (data.getEmergencyContact() == null) {
            emergencyContact.setName(newName);
            data.setEmergencyContact(emergencyContact);
            hasChanges = true;
            return;
        }

        final String name = data.getEmergencyContact().getName();

        if (newName.equals(name)) {
            emergencyContact.setName(newName);
            data.setEmergencyContact(emergencyContact);
            hasChanges = false;
        } else {
            emergencyContact.setName(newName);
            data.setEmergencyContact(emergencyContact);
            hasChanges = true;
        }
    }


    public void handleRoomChange(String id, String name) {
        if (id == null || name == null) {
            return;
        }

        Room room = new Room();

        if (data.getRoom() == null) {
            room.setId(id);
            room.setName(name);
            data.setRoom(room);
            hasChanges = true;
            return;
        }

        final String roomId = data.getRoom().getId();

        if (id.equals(roomId)) {
            room.setId(roomId);
            hasChanges = false;
        } else {
            room.setId(id);
            hasChanges = true;
        }

        room.setName(name);
        data.setRoom(room);
    }


    public void handleCityOfRelocationChange(String newCity) {
        final String city = data.getRelocationCity();

        if (newCity.equals(city)) {
            data.setRelocationCity(city);
            hasChanges = false;
        } else {
            data.setRelocationCity(newCity);
            hasChanges = true;
        }
    }


    public void handlePresentationChange(String newDescription) {
        final String desc = data.getDescription();
        final String fmtDesc = TextUtil.getFormattedText(newDescription);

        if (fmtDesc.equals(desc)) {
            data.setDescription(desc);
            hasChanges = false;
        } else {
            data.setDescription(fmtDesc);
            hasChanges = true;
        }
    }


    public void handleDepartmentChange(String newDepId, String name) {
        if (newDepId == null || name == null) {
            return;
        }

        Department dep = new Department();

        if (data.getDepartment() == null) {
            dep.setId(newDepId);
            dep.setName(name);
            data.setDepartment(dep);
            hasChanges = true;
            return;
        }

        final String depId = data.getDepartment().getId();

        if (newDepId.equals(depId)) {
            dep.setId(depId);
            hasChanges = false;
        } else {
            dep.setId(newDepId);
            hasChanges = true;
        }

        dep.setName(name);
        data.setDepartment(dep);
    }


    public void handleLeadChange(@NonNull Lead newLead) {
        Lead lead = new Lead();

        if (data.getLead() == null) {
            lead.setId(newLead.getId());
            lead.setFirstName(newLead.getFirstName());
            lead.setLastName(newLead.getLastName());
            lead.setLastWorkingDay(newLead.getLastWorkingDay());
            data.setLead(lead);
            hasChanges = true;
            return;
        }

        final String leadId = data.getLead().getId();

        if (newLead.getId().equals(leadId)) {
            lead.setId(leadId);
            hasChanges = false;
        } else {
            lead.setId(newLead.getId());
            hasChanges = true;
        }

        lead.setFirstName(newLead.getFirstName());
        lead.setLastName(newLead.getLastName());
        lead.setLastWorkingDay(newLead.getLastWorkingDay());
        data.setLead(lead);
    }


    public void handleFirstDayChange(String date) {
        if (date != null) {
            hasChanges = true;
            data.setFirstWorkingDay(date);
            view.showFirstWorkingDay(date);
        }
    }


    public void handleFirstDayInItChange(String date) {
        if (date != null) {
            hasChanges = true;
            data.setGeneralFirstWorkingDay(date);
            view.showFirstWorkingDayInIt(date);
        }
    }


    public void handleTrialPeriodChange(String date) {
        if (date != null) {
            hasChanges = true;
            data.setTrialPeriodEnds(date);
            view.showTrialPeriodEnds(date);
        }
    }


    public void handlePdpChange(String newLink) {
        final String link = data.getPdpLink();

        if (TextUtil.containsValidUrl(newLink)) {
            view.hidePdpError();

            if (newLink.equals(link)) {
                data.setPdpLink(link);
                hasChanges = false;
            } else {
                data.setPdpLink(newLink);
                hasChanges = true;
            }
        } else {
            view.showPdpError();
        }
    }


    public void handleOneToOneChange(String newLink) {
        final String link = data.getOneToOneLink();

        if (TextUtil.containsValidUrl(newLink)) {
            view.hideOneToOneError();

            if (newLink.equals(link)) {
                data.setOneToOneLink(link);
                hasChanges = false;
            } else {
                data.setOneToOneLink(newLink);
                hasChanges = true;
            }
        } else {
            view.showOneToOneError();
        }
    }


    public void handleLastDayChange(String date) {
        if (date != null) {
            hasChanges = true;
            data.setLastWorkingDay(date);
            view.showLastWorkingDay(date);
        }
    }

    // todo

    public void handleReasonChange(String newReasonId, String name) {
        /*Reason reason = new Reason();

        if (data.getReason() == null) {
            reason.setId(newReasonId);
            reason.setName(name);
            data.setReason(reason);
            hasChanges = true;
            return;
        }

        final String reasonId = data.getReason().getId();

        if (reasonId.equals(newReasonId)) {
            reason.setId(reasonId);
            hasChanges = false;
        } else {
            reason.setId(newReasonId);
            hasChanges = true;
        }

        reason.setName(name);
        data.setReason(reason);*/
    }


    public void handleCommentsChange(String newComments) {
        final String comments = data.getReasonComments();
        final String fmtDesc = TextUtil.getFormattedText(newComments);

        if (fmtDesc.equals(comments)) {
            data.setReasonComments(comments);
            hasChanges = false;
        } else {
            data.setReasonComments(fmtDesc);
            hasChanges = true;
        }
    }


    public void showDatePickerDialog() {
        view.showDatePickerDialog();
    }


    public void onBackClick() {
        if (hasChanges) {
            view.showConfirmationDialog();
        } else {
            view.onBackClick();
        }
    }


    public void onSaveClick() {
        performSaveUserRequest();
    }


    public void loadPhoto(String photoUrl, @NonNull ImageView ivPhoto) {
        Glide.with(ivPhoto.getContext())
                .load(photoUrl)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(ivPhoto);
    }


    private void showData() {
        view.loadEmployeePhoto(data.getPhotoOrigin() == null ? data.getPhoto() : data.getPhotoOrigin());

        handleLoginSection();
        handlePersonalSection();
        handleContactsSection();
        handleAdditionalSection();

        handleFilters();

        /* Show next info only for HR or Admin */
        final int userRole = data.getRole();
        if (userRole == DetailsPresenter.ROLE_HR || userRole == DetailsPresenter.ROLE_ADMIN) {
            showFullDetailsIfAvailable();
        }
    }


    private void handleLoginSection() {
        view.disAllowClickOnBirthDateView();
        view.showLoginSection();

        if (data.getEmail() != null) {
            view.showEmail(data.getEmail());
        }
    }


    private void handlePersonalSection() {
        view.showPersonalSection();

        if (data.getFirstName() != null) {
            view.showFirstName(data.getFirstName());
        }

        if (data.getLastName() != null) {
            view.showLastName(data.getLastName());
        }

        final String birthDate = DateUtil.getFormattedFullDate(data.getBirthday());
        if (birthDate != null) {
            view.showBirthDate(birthDate);
        }
    }


    private void handleContactsSection() {
        view.showContactsSection();

        if (data.getSkype() != null) {
            view.showSkype(data.getSkype());
        }

        if (data.getPhone() != null) {
            view.showPhoneNumber(data.getPhone());
        }

        if (data.getEmergencyContact() != null) {
            view.showEmergencyContact(data.getEmergencyContact().getPhone());
        }

        final EmergencyContact emergencyContact = data.getEmergencyContact();
        if (emergencyContact != null && emergencyContact.getPhone() != null) {
            view.showEmergencyNumber(emergencyContact.getPhone());
        }

        if (emergencyContact != null && emergencyContact.getName() != null) {
            view.showEmergencyContact(emergencyContact.getName());
        }

        if (data.getRoom() != null) {
            view.showRoom(data.getRoom().getName());
        }
    }


    private void handleAdditionalSection() {
        view.showAdditionalSection();

        if (data.getRelocationCity() != null) {
            view.showCityOfRelocation(data.getRelocationCity());
        }

        if (data.getDescription() != null) {
            view.showPresentationText(data.getDescription());
        }
    }


    private void showFullDetailsIfAvailable() {
        view.allowClickOnBirthDateView();
        view.showCanSignInView(data.isActive());
        view.showGenderView();

        if (data.getGender() == GENDER_MALE) {
            view.showGenderMale();
        } else if (data.getGender() == GENDER_FEMALE) {
            view.showGenderFemale();
        } else {
            view.hideGenderView();
        }

        handleProfessionalSection();
        handlePdpSection();
        handleOutOfCompanySection();
    }


    private void handleProfessionalSection() {
        view.showProfessionalSection();

        if (data.getDepartment() != null) {
            view.showDepartment(data.getDepartment().getName());
        }

        final Lead lead = data.getLead();
        if (lead != null) {
            view.showLead(lead.getFirstName() + " " + lead.getLastName());
        }

        if (data.getFirstWorkingDay() != null) {
            final String firstDate = DateUtil.getFormattedFullDate(data.getFirstWorkingDay());
            if (firstDate != null) {
                view.showFirstWorkingDay(firstDate);
            }
        }

        if (data.getGeneralFirstWorkingDay() != null) {
            final String firstDayInItDate = DateUtil.getFormattedFullDate(data.getGeneralFirstWorkingDay());
            if (firstDayInItDate != null) {
                view.showFirstWorkingDayInIt(firstDayInItDate);
            }
        }

        if (data.getTrialPeriodEnds() != null) {
            final String trialPeriodDate = DateUtil.getFormattedFullDate(data.getTrialPeriodEnds());
            if (trialPeriodDate != null) {
                view.showTrialPeriodEnds(trialPeriodDate);
            }
        }
    }


    private void handlePdpSection() {
        view.showPdpSection();

        if (data.getPdpLink() != null) {
            view.showPdpLink(data.getPdpLink());
        }

        if (data.getOneToOneLink() != null) {
            view.showOneToOneLink(data.getOneToOneLink());
        }
    }


    private void handleOutOfCompanySection() {
        view.showOutOfCompanySection();

        final String lastDayDate = DateUtil.getFormattedFullDate(data.getLastWorkingDay());
        if (lastDayDate != null) {
            view.showLastWorkingDay(lastDayDate);
        }

        // TODO

        /*if (data.getReason() != null && data.getReason().getId()) {
            if (!profileFilters.getReasons().isEmpty()) {
                for (Filter f : profileFilters.getReasons()) {
                    if (data.getReason().getId().equals(f.getId())) {
                        view.showReason(f.getName());
                        break;
                    }
                }
            }
        }*/

        if (data.getReasonComments() != null) {
            view.showComments(data.getReasonComments());
        }
    }


    private void handleFilters() {
        if (!profileFilters.getDepartments().isEmpty()) {
            for (Filter f : profileFilters.getDepartments()) {
                Department dep = data.getDepartment();
                if (dep != null) {
                    if (dep.getId().equals(f.getId())) {
                        view.showSelectedFilter(dep.getId(), dep.getName(), EditProfileFields.CHANGE_DEPARTMENT);
                        break;
                    }
                }
            }
        }

        if (!profileFilters.getLeads().isEmpty()) {
            for (FilterLead f : profileFilters.getLeads()) {
                Lead lead = data.getLead();
                if (lead != null) {
                    if (lead.getId().equals(f.getId())) {
                        view.showSelectedLead(lead, EditProfileFields.CHANGE_LEAD);
                        break;
                    }
                }
            }
        }

        if (!profileFilters.getRooms().isEmpty()) {
            for (Filter f : profileFilters.getRooms()) {
                Room room = data.getRoom();
                if (room != null) {
                    if (room.getId().equals(f.getId())) {
                        view.showSelectedFilter(room.getId(), room.getName(), EditProfileFields.CHANGE_ROOM);
                        break;
                    }
                }
            }
        }

        // TODO

        /*if (data.getReason() != null && data.getReason().getId()) {
            if (!profileFilters.getReasons().isEmpty()) {
                for (Filter f : profileFilters.getReasons()) {
                    final String reasonId = data.getReasonId();
                    if (reasonId.equals(f.getId())) {
                        view.showSelectedFilter(reasonId, f.getName(), EditProfileFields.CHANGE_REASON);
                        break;
                    }
                }
            }
        }*/
    }


    private void performGetMyProfileAndAllFiltersRequest() {
        view.showProgress();
        final String userId = SharedPreferencesUtil.readUser().getId();
        final GetMyProfileRequest request = new GetMyProfileRequest(userId);
        getUserProfile.execute(request, new DefaultSubscriber<Docs>() {
            @Override
            public void onNext(Docs docs) {
                super.onNext(docs);
                data = docs;
                performGetAllFiltersRequest();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    private void performGetAllFiltersRequest() {
        getAllFilters.execute(new DefaultSubscriber<EditProfileFiltersDto>(view) {
            @Override
            public void onNext(EditProfileFiltersDto editProfileFiltersDto) {
                super.onNext(editProfileFiltersDto);
                handleSuccessAllFiltersResponse(editProfileFiltersDto);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleSuccessAllFiltersResponse(EditProfileFiltersDto editProfileFiltersDto) {
        view.hideProgress();
        profileFilters = editProfileFiltersDto;
        showData();
    }


    private void performUploadPhotoRequest(@NonNull MultipartBody.Part multipartBody) {
        view.showProgress();
        final String userId = SharedPreferencesUtil.readUser().getId();
        final UploadPhotoRequest request = new UploadPhotoRequest(userId, multipartBody);
        uploadPhoto.execute(request, new DefaultSubscriber<Void>(view) {
            @Override
            public void onNext(Void aVoid) {
                super.onNext(aVoid);
                view.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void performSaveUserRequest() {
        view.showProgress();
        final EditProfileRequest request = new EditProfileRequest(data);
        saveEditedUserProfile.execute(request, new DefaultSubscriber<EditProfile>(view) {
            @Override
            public void onNext(EditProfile profile) {
                super.onNext(profile);
                hasChanges = false;
                view.hideProgress();
                view.showSnackBarMessage(R.string.message_successfully_saved);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
                view.showSnackBarMessage(R.string.message_error);
            }
        });
    }
}