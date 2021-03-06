package co.techmagic.hr.presentation.mvp.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.common.Role;
import co.techmagic.hr.data.entity.Department;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.data.entity.Reason;
import co.techmagic.hr.data.entity.Room;
import co.techmagic.hr.data.entity.UserProfile;
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
import co.techmagic.hr.presentation.ui.EditableFields;
import co.techmagic.hr.presentation.util.DateUtil;
import co.techmagic.hr.presentation.util.FileChooserUtil;
import co.techmagic.hr.presentation.util.ImagePickerUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;
import co.techmagic.hr.presentation.util.TextUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class EditProfilePresenter extends BasePresenter<EditProfileViewImpl> {

    private static final int GENDER_MALE = 0;
    private static final int GENDER_FEMALE = 1;
    private static final long IMAGE_SIZE_5MB = 5242880;

    private IUserRepository userRepository;
    private IEmployeeRepository employeeRepository;
    private GetUserProfile getUserProfile;
    private GetAllFilters getAllFilters;
    private UploadPhoto uploadPhoto;
    private SaveEditedUserProfile saveEditedUserProfile;

    private UserProfile data;
    private EditProfileFiltersDto profileFilters;
    private EmergencyContact emergencyContact;

    private String desc = null;
    private String skype = null;
    private String phone = null;
    private String city = null;
    private boolean hasChanges = false;


    public EditProfilePresenter() {
        super();
        userRepository = new UserRepositoryImpl();
        employeeRepository = new EmployeeRepositoryImpl();
        getUserProfile = new GetUserProfile(userRepository);
        uploadPhoto = new UploadPhoto(userRepository);
        getAllFilters = new GetAllFilters(employeeRepository);
        saveEditedUserProfile = new SaveEditedUserProfile(userRepository);
        emergencyContact = new EmergencyContact();
        data = new UserProfile();
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


    public void preparePhotoAndSend(Uri uri, Context context) {
        String imagePath = FileChooserUtil.getPath(context, uri);

        if (imagePath == null) {
            return;
        }

        File file = new File(imagePath);

        if (!file.exists()) {
            return;
        }

        if (file.length() > IMAGE_SIZE_5MB) {
            view.showImageSizeIsTooBigMessage();
            return;
        }

        Observable.OnSubscribe<MultipartBody.Part> onSubscribe = subscriber -> {
            MultipartBody.Part multipartBody = getPart(uri, context, file);

            if (multipartBody == null) {
                return;
            }

            subscriber.onNext(multipartBody);
            subscriber.onCompleted();
        };

        Subscriber<MultipartBody.Part> subscriber = new Subscriber<MultipartBody.Part>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MultipartBody.Part body) {
                performUploadPhotoRequestAndUpdateUser(body);
            }
        };

        Observable.create(onSubscribe)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
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
        // If Last Working Day Selected not selected
        if (data.getLastWorkingDay() == null) {
            view.showSelectLastWorkingDayFirstMessage();
        } else {
            final List<Filter> reasons = profileFilters.getReasons();
            if (reasons.isEmpty()) {
                view.showMessage(R.string.tm_hr_search_activity_text_empty_lead_filters);
            } else {
                view.showFiltersInDialog(reasons);
            }
        }
    }

    /**
     * Handling of content entered or selected by user.
     */

    public void handleEmailChange(String newEmail) {
        String email = data.getEmail();

        if (newEmail.equals(email)) {
            view.hideEmailError();
            data.setEmail(email);
            return;
        }

        // In case if user left field empty
        if (newEmail.isEmpty()) {
            view.showEmptyEmailError();
            hasChanges = true;
            return;
        }

        if (TextUtil.isValidEmail(newEmail)) {
            view.hideEmailError();
            data.setEmail(newEmail);
            hasChanges = true;
        } else {
            view.onEmailError();
        }
    }


    public void handlePasswordChange(String newPassword) {
        if (newPassword.isEmpty()) {
            view.setPasswordToggleEnabled(false);
            view.hidePasswordError();
            hasChanges = true;
            return;
        }

        if (newPassword.length() < TextUtil.PASSWORD_MINIMUM_LENGTH) {
            view.showShortPasswordMessage();
            hasChanges = true;
            return;
        }

        if (TextUtil.isValidPassword(newPassword)) {
            view.hidePasswordError();
            data.setPassword(newPassword);
            hasChanges = true;
        } else {
            view.onPasswordError();
        }
    }


    public void handleFirstNameChange(String newFirstName) {
        String firstName = data.getFirstName();

        if (newFirstName.equals(firstName)) {
            view.hideFirstNameError();
            data.setFirstName(firstName);
            return;
        }

        // In case if user left field empty
        if (newFirstName.isEmpty()) {
            view.showEmptyFirstNameError();
            hasChanges = true;
            return;
        }

        if (newFirstName.length() > TextUtil.NAME_MAXIMUM_LENGTH) {
            view.showLongFirstNameMessage();
            hasChanges = true;
            return;
        }

        if (TextUtil.isValidName(newFirstName)) {
            view.hideFirstNameError();
            data.setFirstName(newFirstName);
            hasChanges = true;
        } else {
            view.onFirstNameError();
        }
    }


    public void handleLastNameChange(String newLastName) {
        String lastName = data.getLastName();

        if (newLastName.equals(lastName)) {
            view.hideLastNameError();
            data.setLastName(lastName);
            return;
        }

        if (TextUtil.isValidName(newLastName)) {
            view.hideLastNameError();
            data.setLastName(newLastName);
            hasChanges = true;
        } else {
            view.onLastNameError();
        }

        // In case if user left field empty
        if (newLastName.isEmpty()) {
            view.showEmptyLastNameError();
            hasChanges = true;
        }
    }


    public void handleDateOfBirthChange(String date, String dateInUTC) {
        if (date != null) {
            hasChanges = true;
            data.setBirthday(dateInUTC);
            view.showBirthDate(date);
        }
    }


    public void handleGenderChange(boolean isMaleChecked) {
        if (!isGenderTheSame(isMaleChecked)) {
            data.setGender(isMaleChecked ? GENDER_MALE : GENDER_FEMALE);
            hasChanges = true;
        }
    }


    private boolean isGenderTheSame(boolean isMaleChecked) {
        return data.getGender() == GENDER_MALE && isMaleChecked || data.getGender() == GENDER_FEMALE && !isMaleChecked;
    }


    public void handleSkypeChange(String newSkype) {
        if (newSkype.equals(skype)) {
            data.setSkype(skype);
        } else {
            data.setSkype(newSkype);
            hasChanges = true;
        }

        // In case if user left field empty
        if (newSkype.isEmpty()) {
            data.setSkype("");
            hasChanges = true;
        }
    }


    public void handlePhoneChange(String newPhone) {
        if (newPhone.equals(phone)) {
            data.setPhone(phone);
        } else {
            data.setPhone(newPhone);
            hasChanges = true;
        }

        // In case if user left field empty
        if (newPhone.isEmpty()) {
            data.setPhone("");
            hasChanges = true;
        }
    }


    public void handleEmergencyContactNumberChange(String newNumber) {
        EmergencyContact emergency = data.getEmergencyContact();

        if (emergency == null) {
            if (newNumber == null || newNumber.isEmpty()) {
                // Should put empty string here
                emergencyContact.setPhone("");
                data.setEmergencyContact(emergencyContact);
            } else {
                emergencyContact.setPhone(newNumber);
                data.setEmergencyContact(emergencyContact);
                hasChanges = true;
            }
        } else {
            final String phone = emergency.getPhone();

            if (phone == null || newNumber.equals(phone)) {
                emergencyContact.setPhone(newNumber);
                data.setEmergencyContact(emergencyContact);
            } else {
                emergencyContact.setPhone(newNumber);
                data.setEmergencyContact(emergencyContact);
                hasChanges = true;
            }
        }
    }


    public void handleEmergencyContactChange(String newName) {
        EmergencyContact emergency = data.getEmergencyContact();

        if (emergency == null) {
            if (newName == null || newName.isEmpty()) {
                // Should put empty string here
                emergencyContact.setName("");
                data.setEmergencyContact(emergencyContact);
            } else {
                emergencyContact.setName(newName);
                data.setEmergencyContact(emergencyContact);
                hasChanges = true;
            }
        } else {
            final String name = emergency.getName();

            if (name == null || newName.equals(name)) {
                emergencyContact.setName(newName);
                data.setEmergencyContact(emergencyContact);
            } else {
                emergencyContact.setName(newName);
                data.setEmergencyContact(emergencyContact);
                hasChanges = true;
            }
        }
    }


    public void handleRoomChange(Room newRoom) {
        if (newRoom == null) {
            return;
        }

        Room room = new Room();

        if (data.getRoom() == null) {
            room.setId(newRoom.getId());
            room.setName(newRoom.getName());
            data.setRoom(room);
            hasChanges = true;
            return;
        }

        final String roomId = data.getRoom().getId();

        if (newRoom.getId().equals(roomId)) {
            room.setId(roomId);
        } else {
            room.setId(newRoom.getId());
            hasChanges = true;
        }

        room.setName(newRoom.getName());
        data.setRoom(room);
    }


    public void handleCityOfRelocationChange(String newCity) {
        if (newCity.equals(city)) {
            data.setRelocationCity(city);
        } else {
            data.setRelocationCity(newCity);
            hasChanges = true;
        }

        // In case if user left field empty
        if (newCity.isEmpty()) {
            data.setRelocationCity("");
            hasChanges = true;
        }
    }


    public void handlePresentationChange(String newDescription) {
        if (newDescription.equals(desc)) {
            data.setDescription(newDescription);
            return;
        }

        if (newDescription.isEmpty()) {
            data.setDescription(""); // instead of null
        } else {
            data.setDescription(newDescription);
        }

        hasChanges = true;
    }


    public void handleDepartmentChange(Department newDep) {
        if (newDep == null) {
            return;
        }

        Department dep = new Department();

        if (data.getDepartment() == null) {
            dep.setId(newDep.getId());
            dep.setName(newDep.getName());
            data.setDepartment(dep); //fixme: change to delivery?
            hasChanges = true;
            return;
        }

        final String depId = data.getDepartment().getId();

        if (newDep.getId().equals(depId)) {
            dep.setId(depId);
        } else {
            dep.setId(newDep.getId());
            hasChanges = true;
        }

        dep.setName(newDep.getName());
        data.setDepartment(dep); //fixme: change to delivery?
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
        } else {
            lead.setId(newLead.getId());
            hasChanges = true;
        }

        lead.setFirstName(newLead.getFirstName());
        lead.setLastName(newLead.getLastName());
        lead.setLastWorkingDay(newLead.getLastWorkingDay());
        data.setLead(lead);
    }


    public void handleFirstDayChange(String date, String dateInUTC) {
        if (date != null) {
            hasChanges = true;
            data.setFirstWorkingDay(dateInUTC);
            view.showFirstWorkingDay(date);
        }
    }


    public void handleFirstDayInItChange(String date, String dateInUTC) {
        if (date != null) {
            hasChanges = true;
            data.setGeneralFirstWorkingDay(dateInUTC);
            view.showFirstWorkingDayInIt(date);
        }
    }


    public void handleTrialPeriodChange(String date, String dateInUTC) {
        if (date != null) {
            hasChanges = true;
            data.setTrialPeriodEnds(dateInUTC);
            view.showTrialPeriodEnds(date);
        }
    }


    public void handlePdpChange(String newLink) {
        final String link = data.getPdpLink();

        if (TextUtil.containsValidUrl(newLink)) {
            view.hidePdpError();

            if (newLink.equals(link)) {
                data.setPdpLink(link);
            } else {
                data.setPdpLink(newLink);
                hasChanges = true;
            }
        } else {
            view.showPdpError();
        }

        // In case if user left field empty
        if (newLink.isEmpty()) {
            view.hidePdpError();
            data.setPdpLink(null);
            hasChanges = true;
        }
    }


    public void handleOneToOneChange(String newLink) {
        final String link = data.getOneToOneLink();

        if (TextUtil.containsValidUrl(newLink)) {
            view.hideOneToOneError();

            if (newLink.equals(link)) {
                data.setOneToOneLink(link);
            } else {
                data.setOneToOneLink(newLink);
                hasChanges = true;
            }
        } else {
            view.showOneToOneError();
        }

        // In case if user left field empty
        if (newLink.isEmpty()) {
            view.hideOneToOneError();
            data.setOneToOneLink(null);
            hasChanges = true;
        }
    }


    public void handleLastDayChange(String date, String dateInUTC) {
        if (date != null) {
            hasChanges = true;
            data.setLastWorkingDay(dateInUTC);
            view.showLastWorkingDay(date);
        }
    }


    public void handleReasonChange(Reason newReason) {
        if (newReason == null) {
            return;
        }

        Reason reason = new Reason();

        if (data.getReason() == null) {
            reason.setId(newReason.getId());
            reason.setName(newReason.getName());
            data.setReason(reason);
            hasChanges = true;
            return;
        }

        final String reasonId = data.getReason().getId();

        if (newReason.getId().equals(reasonId)) {
            reason.setId(reasonId);
        } else {
            reason.setId(newReason.getId());
            hasChanges = true;
        }

        reason.setName(newReason.getName());
        data.setReason(reason);
    }


    public void handleCommentsChange(String newComments) {
        String comment = data.getReasonComments();

        if (newComments.equals(comment)) {
            data.setReasonComments(newComments);
            return;
        }

        if (newComments.isEmpty()) {
            data.setReasonComments(""); // instead of null
        } else {
            data.setReasonComments(newComments);
        }

        hasChanges = true;
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
        Role role = Role.getRoleByCode(userRole);
        if (role == Role.ROLE_HR || role == Role.ROLE_ADMIN) {
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
        // This feature no longer works for admins, so button is temporarily hidden
        // TODO: uncomment when fixed or remove completely if not needed
        // view.allowPickUpPhoto();

        view.allowClickOnBirthDateView();
        view.showGenderView();
        view.enableEmail();
        view.enableFirstName();
        view.enableLastName();

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

        if (data.getReason() != null) {
            if (!profileFilters.getReasons().isEmpty()) {
                for (Filter f : profileFilters.getReasons()) {
                    if (data.getReason().getId().equals(f.getId())) {
                        view.showReason(f.getName());
                        break;
                    }
                }
            }
        }

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
                        view.showSelectedFilter(f, EditableFields.CHANGE_DEPARTMENT);
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
                        view.showSelectedLead(lead, EditableFields.CHANGE_LEAD);
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
                        view.showSelectedFilter(f, EditableFields.CHANGE_ROOM);
                        break;
                    }
                }
            }
        }

        if (data.getReason() != null) {
            if (!profileFilters.getReasons().isEmpty()) {
                for (Filter f : profileFilters.getReasons()) {
                    final String reasonId = data.getReason().getId();
                    if (reasonId.equals(f.getId())) {
                        view.showSelectedFilter(f, EditableFields.CHANGE_REASON);
                        break;
                    }
                }
            }
        }
    }


    @Nullable
    private MultipartBody.Part getPart(Uri uri, Context context, File file) {
        Bitmap bitmap = ImagePickerUtil.getDecodedBitmapFromFile(file);

        if (bitmap == null) {
            return null;
        }

        byte[] compressedImage = ImagePickerUtil.compressImage(bitmap);
        MultipartBody.Part multipartBody = prepareFilePart("photo", file.getName(), compressedImage, context, uri);

        if (multipartBody == null) {
            return null;
        }
        return multipartBody;
    }


    @Nullable
    private MultipartBody.Part prepareFilePart(String partName, String fileName, byte[] compressedImage, Context context, Uri uri) {
        String mimeType = ImagePickerUtil.getMimeType(context, uri);

        if (mimeType == null) {
            return null;
        }

        RequestBody requestFile = RequestBody.create(
                MediaType.parse(mimeType),
                compressedImage);

        return MultipartBody.Part.createFormData(partName, fileName, requestFile);
    }


    private void performGetMyProfileAndAllFiltersRequest() {
        view.showProgress();
        final String userId = SharedPreferencesUtil.readUser().getId();
        final GetMyProfileRequest request = new GetMyProfileRequest(userId);
        getUserProfile.execute(request, new DefaultSubscriber<UserProfile>() {
            @Override
            public void onNext(UserProfile userProfile) {
                super.onNext(userProfile);
                data = userProfile;
                getProfileInitialData();
                performGetAllFiltersRequest();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    private void getProfileInitialData() {
        desc = data.getDescription();
        skype = data.getSkype();
        phone = data.getPhone();
        city = data.getRelocationCity();
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


    private void performUploadPhotoRequestAndUpdateUser(@NonNull MultipartBody.Part multipartBody) {
        view.showProgress();
        final String userId = SharedPreferencesUtil.readUser().getId();
        final UploadPhotoRequest request = new UploadPhotoRequest(userId, multipartBody);
        uploadPhoto.execute(request, new DefaultSubscriber<Void>(view) {
            @Override
            public void onNext(Void aVoid) {
                super.onNext(aVoid);
                view.hideProgress();
                performGetMyProfileAndAllFiltersRequest();
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
        saveEditedUserProfile.execute(request, new DefaultSubscriber<UserProfile>(view) {
            @Override
            public void onNext(UserProfile profile) {
                super.onNext(profile);
                hasChanges = false;
                view.hideProgress();
                view.onBackClick();
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