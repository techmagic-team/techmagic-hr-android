package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.data.repository.UserRepositoryImpl;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.domain.interactor.employee.GetAllFilters;
import co.techmagic.hr.domain.interactor.user.GetMyProfile;
import co.techmagic.hr.domain.pojo.EditProfileFiltersDto;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.domain.repository.IUserRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.impl.EditProfileViewImpl;
import co.techmagic.hr.presentation.util.DateUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;


public class EditProfilePresenter extends BasePresenter<EditProfileViewImpl> {

    private static final int GENDER_MALE = 0;
    private static final int GENDER_FEMALE = 1;

    private IUserRepository userRepository;
    private IEmployeeRepository employeeRepository;
    private GetMyProfile getMyProfile;
    private GetAllFilters getAllFilters;

    private Docs data;
    private EditProfileFiltersDto editProfileFiltersDto;


    public EditProfilePresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        getMyProfile = new GetMyProfile(userRepository);
        getAllFilters = new GetAllFilters(employeeRepository);
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
    }


    public void setupPage() {
        performGetMyProfileAndAllFiltersRequest();
    }


    public void onUploadPhotoClick() {

    }


    public void onRoomClick() {

    }


    public void onReasonClick() {

    }


    public void showDatePickerDialog() {
        view.showDatePickerDialog();
    }


    public void onCancelClick() {

    }


    public void onSaveClick() {

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

        /* Show next info only for HR or Admin */
        final int userRole = data.getRole();
        if (userRole == DetailsPresenter.ROLE_HR || userRole == DetailsPresenter.ROLE_ADMIN) {
            showFullDetailsIfAvailable();
        }
    }


    private void handleLoginSection() {
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

        final String firstDate = DateUtil.getFormattedFullDate(data.getFirstWorkingDay());
        if (firstDate != null) {
            view.showFirstWorkingDay(firstDate);
        }

        final String firstDayInItDate = DateUtil.getFormattedFullDate(data.getGeneralFirstWorkingDay());
        if (firstDayInItDate != null) {
            view.showFirstWorkingDayInIt(firstDayInItDate);
        }

        final String trialPeriodDate = DateUtil.getFormattedFullDate(data.getTrialPeriodEnds());
        if (trialPeriodDate != null) {
            view.showTrialPeriodEnds(trialPeriodDate);
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
            view.showReason(data.getReason());
        }

        if (data.getReasonComments() != null) {
            view.showComments(data.getReasonComments());
        }
    }


    private void performGetMyProfileAndAllFiltersRequest() {
        view.showProgress();
        final String userId = SharedPreferencesUtil.readUser().getId();
        final GetMyProfileRequest request = new GetMyProfileRequest(userId);
        getMyProfile.execute(request, new DefaultSubscriber<Docs>() {
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
        this.editProfileFiltersDto = editProfileFiltersDto;
        showData();
    }
}