package co.techmagic.hr.presentation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Department;
import co.techmagic.hr.data.entity.IFilterModel;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.data.entity.Reason;
import co.techmagic.hr.data.entity.Room;
import co.techmagic.hr.presentation.mvp.presenter.EditProfilePresenter;
import co.techmagic.hr.presentation.mvp.view.impl.EditProfileViewImpl;
import co.techmagic.hr.presentation.ui.EditableFields;
import co.techmagic.hr.presentation.ui.FilterTypes;
import co.techmagic.hr.presentation.ui.adapter.FilterAdapter;
import co.techmagic.hr.presentation.ui.fragment.DatePickerFragment;
import co.techmagic.hr.presentation.ui.manager.FilterDialogManager;
import co.techmagic.hr.presentation.util.ImagePickerUtil;

public class EditProfileActivity extends BaseActivity<EditProfileViewImpl, EditProfilePresenter> implements FilterAdapter.OnFilterSelectionListener, DatePickerFragment.onDatePickerSelectionListener {

    public static final String DATE_PICKER_FRAGMENT_TAG = "date_picker_fragment_tag";
    private static final int RC_GET_IMAGE = 1005;

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.ivDownload)
    ImageView ivDownload;
    @BindView(R.id.btnUploadPhoto)
    Button btnUploadPhoto;
    @BindView(R.id.cvLoginInfo)
    View cvLoginInfo;
    @BindView(R.id.etChangeEmail)
    EditText etChangeEmail;
    @BindView(R.id.etChangePassword)
    EditText etChangePassword;
    @BindView(R.id.cbCanSignIn)
    CheckBox cbCanSignIn;
    @BindView(R.id.tilChangeEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilChangePassword)
    TextInputLayout tilPassword;
    @BindView(R.id.cvPersonalInfo)
    View cvPersonalInfo;
    @BindView(R.id.tilFirstName)
    TextInputLayout tilFirstName;
    @BindView(R.id.tilLastName)
    TextInputLayout tilLastName;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.rlEditDateOfBirth)
    RelativeLayout rlEditDateOfBirth;
    @BindView(R.id.tvDateOfBirth)
    TextView tvSelectedDateOfBirth;
    @BindView(R.id.rgGender)
    RadioGroup rgGender;
    @BindView(R.id.rbMale)
    RadioButton rbMale;
    @BindView(R.id.rbFemale)
    RadioButton rbFemale;
    @BindView(R.id.cvContacts)
    View cvContacts;
    @BindView(R.id.etChangeSkype)
    EditText etChangeSkype;
    @BindView(R.id.etChangePhone)
    EditText etChangePhone;
    @BindView(R.id.etChangeEmergencyNumber)
    EditText etChangeEmergencyNumber;
    @BindView(R.id.etChangeEmergencyContact)
    EditText etChangeEmergencyContact;
    @BindView(R.id.rlEditRoom)
    View rlEditRoom;
    @BindView(R.id.tvSelectedRoom)
    TextView tvSelectedRoom;
    @BindView(R.id.cvAdditional)
    View cvAdditional;
    @BindView(R.id.etChangeCity)
    EditText etChangeCity;
    @BindView(R.id.etChangePresentation)
    EditText etChangePresentation;
    @BindView(R.id.cvProfessional)
    View cvProfessional;
    @BindView(R.id.tvSelectedDep)
    TextView tvSelectedDep;
    @BindView(R.id.tvSelectedLead)
    TextView tvSelectedLead;
    @BindView(R.id.tvSelectedFirstDay)
    TextView tvSelectedFirstDay;
    @BindView(R.id.tvSelectedFirstDayInIt)
    TextView tvSelectedFirstDayInIt;
    @BindView(R.id.tvSelectedTrialEnd)
    TextView tvSelectedTrialEnd;
    @BindView(R.id.cvPdp)
    View cvPdp;
    @BindView(R.id.tilPdpLink)
    TextInputLayout tilPdpLink;
    @BindView(R.id.etChangePdpLink)
    EditText etChangePdpLink;
    @BindView(R.id.tilOneToOneLink)
    TextInputLayout tilOneToOneLink;
    @BindView(R.id.etChangeOneToOneLink)
    EditText etChangeOneToOneLink;
    @BindView(R.id.cvOutOfTheCompany)
    View cvOutOfTheCompany;
    @BindView(R.id.tvLastWorkingDay)
    TextView tvLastWorkingDay;
    @BindView(R.id.tvReason)
    TextView tvSelectedReason;
    @BindView(R.id.etComments)
    EditText etComments;

    private FilterDialogManager dialogManager;

    private EditableFields editProfileField = EditableFields.NONE;
    private Department department;
    private Lead selectedLead;
    private Room room;
    private Reason reason;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUi();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.onBackClick();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case RC_READ_EXTERNAL_STORAGE_PERMISSION: {
                    startActivityForResult(ImagePickerUtil.getPickImageChooserIntent(this), RC_GET_IMAGE);
                    break;
                }
            }
        } else {
            view.showMessage(getString(R.string.message_permission_denied));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            handlePickedImageResult(data);
        }
    }


    @Override
    public void onBackPressed() {
        if (dialogManager.isDialogActive()) {
            dialogManager.dismissDialogIfOpened();
        } else {
            presenter.onBackClick();
        }
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_edit_profile);
    }


    @Override
    protected EditProfileViewImpl initView() {
        return new EditProfileViewImpl(this, findViewById(android.R.id.content)) {
            @Override
            public void allowPickUpPhoto() {
                btnUploadPhoto.setVisibility(View.VISIBLE);
            }

            @Override
            public void pickUpPhoto() {
                startChooserIntentIfPermissionGranted();
            }

            @Override
            public void showImageSizeIsTooBigMessage() {
                view.showMessage(getString(R.string.message_image_too_big));
            }

            @Override
            public void loadEmployeePhoto(@Nullable String photoUrl) {
                presenter.loadPhoto(photoUrl, ivPhoto);
            }

            @Override
            public void showDatePickerDialog() {
                DatePickerFragment datePicker;
                if (editProfileField == EditableFields.CHANGE_TRIAL_PERIOD || editProfileField == EditableFields.CHANGE_LAST_WORKING_DAY) {
                    datePicker = DatePickerFragment.newInstance(true);
                } else {
                    datePicker = DatePickerFragment.newInstance(false);
                }
                datePicker.show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
            }

            @Override
            public void showSelectedFilter(@NonNull IFilterModel filter, EditableFields field) {
                editProfileField = field;
                onFilterSelected(filter);
            }

            @Override
            public void showSelectedLead(@NonNull Lead lead, EditableFields field) {
                editProfileField = field;
                selectedLead = lead;
                onFilterSelected(lead);
            }

            @Override
            public <T extends IFilterModel> void showFiltersInDialog(@Nullable List<T> filters) {
                dialogManager.dismissDialogIfOpened();
                setupCorrectFilterType(filters);
            }

            @Override
            public void showConfirmationDialog() {
                showSaveChangesDialog();
            }

            @Override
            public void onBackClick() {
                finish();
                overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out);
            }

            @Override
            public void showLoginSection() {
                cvLoginInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void enableEmail() {
                etChangeEmail.setEnabled(true);
            }

            @Override
            public void showEmail(@NonNull String email) {
                etChangeEmail.setText(email);
            }

            @Override
            public void showPassword(@NonNull String password) {
                tilPassword.setPasswordVisibilityToggleEnabled(true);
                etChangePassword.setText(password);
            }

            @Override
            public void showCanSignInView(boolean canSignIn) {
                cbCanSignIn.setVisibility(View.VISIBLE);
                cbCanSignIn.setChecked(canSignIn);
            }

            @Override
            public void hideEmailError() {
                tilEmail.setErrorEnabled(false);
            }

            @Override
            public void onEmailError() {
                tilEmail.setError(getString(R.string.message_invalid_email));
            }

            @Override
            public void showEmptyEmailError() {
                tilEmail.setError(getString(R.string.message_you_can_not_leave_this_empty));
            }

            @Override
            public void onPasswordError() {
                setPasswordToggleEnabled(true);
                tilPassword.setError(getString(R.string.message_invalid_password));
            }

            @Override
            public void hidePasswordError() {
                setPasswordToggleEnabled(true);
                tilPassword.setErrorEnabled(false);
            }

            @Override
            public void setPasswordToggleEnabled(boolean enabled) {
                tilPassword.setPasswordVisibilityToggleEnabled(enabled);
            }

            @Override
            public void showShortPasswordMessage() {
                setPasswordToggleEnabled(true);
                tilPassword.setError(getString(R.string.message_short_password));
            }

            @Override
            public void showPersonalSection() {
                cvPersonalInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void enableFirstName() {
                etFirstName.setEnabled(true);
            }

            @Override
            public void showFirstName(@NonNull String firstName) {
                etFirstName.setText(firstName);
            }

            @Override
            public void onFirstNameError() {
                tilFirstName.setError(getString(R.string.message_name_contains_not_allowed_characters));
            }

            @Override
            public void showEmptyFirstNameError() {
                tilFirstName.setError(getString(R.string.message_you_can_not_leave_this_empty));
            }

            @Override
            public void showLongFirstNameMessage() {
                tilFirstName.setError(getString(R.string.message_long_name));
            }

            @Override
            public void hideFirstNameError() {
                tilFirstName.setErrorEnabled(false);
            }

            @Override
            public void enableLastName() {
                etLastName.setEnabled(true);
            }

            @Override
            public void showLastName(@NonNull String lastName) {
                etLastName.setText(lastName);
            }

            @Override
            public void onLastNameError() {
                tilLastName.setError(getString(R.string.message_name_contains_not_allowed_characters));
            }

            @Override
            public void showEmptyLastNameError() {
                tilLastName.setError(getString(R.string.message_you_can_not_leave_this_empty));
            }

            @Override
            public void showLongLastNameMessage() {
                tilLastName.setError(getString(R.string.message_long_name));
            }

            @Override
            public void hideLastNameError() {
                tilLastName.setErrorEnabled(false);
            }

            @Override
            public void allowClickOnBirthDateView() {
                rlEditDateOfBirth.setOnClickListener(getOnClickListener());
            }

            @Override
            public void disAllowClickOnBirthDateView() {
                rlEditDateOfBirth.setOnClickListener(null);
            }

            @Override
            public void showBirthDate(@NonNull String date) {
                tvSelectedDateOfBirth.setText(date);
            }

            @Override
            public void showGenderView() {
                rgGender.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideGenderView() {
                rgGender.setVisibility(View.GONE);
            }

            @Override
            public void showGenderMale() {
                rbMale.setChecked(true);
            }

            @Override
            public void showGenderFemale() {
                rbFemale.setChecked(true);
            }

            @Override
            public void showContactsSection() {
                cvContacts.setVisibility(View.VISIBLE);
            }

            @Override
            public void showSkype(@NonNull String skype) {
                etChangeSkype.setText(skype);
            }

            @Override
            public void showPhoneNumber(@NonNull String phone) {
                etChangePhone.setText(phone);
            }

            @Override
            public void showEmergencyNumber(@NonNull String phone) {
                etChangeEmergencyNumber.setText(phone);
            }

            @Override
            public void showEmergencyContact(@NonNull String phone) {
                etChangeEmergencyContact.setText(phone);
            }

            @Override
            public void showRoom(@NonNull String room) {
                tvSelectedRoom.setText(room);
            }

            @Override
            public void showAdditionalSection() {
                cvAdditional.setVisibility(View.VISIBLE);
            }

            @Override
            public void showCityOfRelocation(@NonNull String city) {
                etChangeCity.setText(city);
            }

            @Override
            public void showPresentationText(@NonNull String text) {
                etChangePresentation.setText(text);
            }

            @Override
            public void showProfessionalSection() {
                cvProfessional.setVisibility(View.VISIBLE);
            }

            @Override
            public void showDepartment(@NonNull String department) {
                tvSelectedDep.setText(department);
            }

            @Override
            public void showLead(@NonNull String lead) {
                tvSelectedLead.setText(lead);
            }

            @Override
            public void showFirstWorkingDay(@NonNull String date) {
                tvSelectedFirstDay.setText(date);
            }

            @Override
            public void showFirstWorkingDayInIt(@NonNull String date) {
                tvSelectedFirstDayInIt.setText(date);
            }

            @Override
            public void showTrialPeriodEnds(@NonNull String date) {
                tvSelectedTrialEnd.setText(date);
            }

            @Override
            public void showPdpSection() {
                cvPdp.setVisibility(View.VISIBLE);
            }

            @Override
            public void showPdpLink(@NonNull String link) {
                etChangePdpLink.setText(link);
            }

            @Override
            public void showOneToOneLink(@NonNull String link) {
                etChangeOneToOneLink.setText(link);
            }

            @Override
            public void hidePdpError() {
                tilPdpLink.setErrorEnabled(false);
            }

            @Override
            public void showPdpError() {
                tilPdpLink.setError(getString(R.string.message_wrong_url));
            }

            @Override
            public void hideOneToOneError() {
                tilOneToOneLink.setErrorEnabled(false);
            }

            @Override
            public void showOneToOneError() {
                tilOneToOneLink.setError(getString(R.string.message_wrong_url));
            }

            @Override
            public void showOutOfCompanySection() {
                cvOutOfTheCompany.setVisibility(View.VISIBLE);
            }

            @Override
            public void showLastWorkingDay(@NonNull String date) {
                tvLastWorkingDay.setText(date);
            }

            @Override
            public void showReason(@NonNull String text) {
                tvSelectedReason.setText(text);
            }

            @Override
            public void showSelectLastWorkingDayFirstMessage() {
                view.showMessage(getString(R.string.tm_hr_edit_profile_activity_please_select_last_working_day_first_message));
            }

            @Override
            public void showComments(@NonNull String text) {
                etComments.setText(text);
            }
        };
    }


    @Override
    protected EditProfilePresenter initPresenter() {
        return new EditProfilePresenter();
    }


    @Override
    public void onFilterSelected(@NonNull IFilterModel model) {
        dialogManager.dismissDialogIfOpened();
        displaySelectedFilter(model);
    }


    @Override
    public void onDateSelected(@NonNull String formattedDate, @NonNull String dateInUTC) {
        handleSelectedDate(formattedDate, dateInUTC);
    }


    @OnClick(R.id.btnUploadPhoto)
    public void uploadPhotoClick() {
        presenter.onUploadPhotoClick();
    }


    @OnClick(R.id.rlEditRoom)
    public void onRoomClick() {
        editProfileField = EditableFields.CHANGE_ROOM;
        presenter.onRoomClick();
    }


    @OnClick(R.id.rlEditDepartment)
    public void onDepartmentClick() {
        editProfileField = EditableFields.CHANGE_DEPARTMENT;
        presenter.onDepartmentClick();
    }


    @OnClick(R.id.rlEditLead)
    public void onLeadClick() {
        editProfileField = EditableFields.CHANGE_LEAD;
        presenter.onLeadClick();
    }


    @OnClick(R.id.rlEditFirstDay)
    public void onFirstDayClick() {
        editProfileField = EditableFields.CHANGE_FIRST_DAY;
        presenter.showDatePickerDialog();
    }


    @OnClick(R.id.rlEditFirstDayInIt)
    public void onFirstDayInItClick() {
        editProfileField = EditableFields.CHANGE_FIRST_DAY_IN_IT;
        presenter.showDatePickerDialog();
    }


    @OnClick(R.id.rlEditTrialEnd)
    public void onTrialEndClick() {
        editProfileField = EditableFields.CHANGE_TRIAL_PERIOD;
        presenter.showDatePickerDialog();
    }


    @OnClick(R.id.rlEditSelectReason)
    public void onReasonClick() {
        editProfileField = EditableFields.CHANGE_REASON;
        presenter.onReasonClick();
    }


    @OnClick(R.id.rlEditLastWorkingDay)
    public void onLastDayClick() {
        editProfileField = EditableFields.CHANGE_LAST_WORKING_DAY;
        presenter.showDatePickerDialog();
    }


    @OnClick(R.id.btnCancel)
    public void onCancelClick() {
        presenter.onBackClick();
    }


    @OnClick(R.id.btnSave)
    public void onSaveClick() {
        if (!fieldContainsError()) {
            presenter.onSaveClick();
        }
    }


    private void startChooserIntentIfPermissionGranted() {
        if (isReadExternalStoragePermissionGranted()) {
            startActivityForResult(ImagePickerUtil.getPickImageChooserIntent(this), RC_GET_IMAGE);
        } else {
            requestReadExternalStoragePermission();
        }
    }


    private void handlePickedImageResult(Intent data) {
        Uri imageUri = ImagePickerUtil.getPickImageResultUri(data, this);
        if (imageUri == null) {
            return;
        }

        if (isValidUri(imageUri)) {
            presenter.preparePhotoAndSend(imageUri, this);
        } else {
            view.showMessage(getString(R.string.message_invalid_file_extension));
        }
    }


    private boolean isValidUri(Uri uriImg) {
        String mimeType = ImagePickerUtil.getMimeType(this, uriImg);
        return ImagePickerUtil.EXTENSION_JPEG.equals(mimeType) || ImagePickerUtil.EXTENSION_JPG.equals(mimeType) || ImagePickerUtil.EXTENSION_PNG.equals(mimeType);
    }


    private <T extends IFilterModel> void setupCorrectFilterType(List<T> filters) {
        switch (editProfileField) {
            case CHANGE_DEPARTMENT:
                dialogManager.showSelectFilterAlertDialog(filters, FilterTypes.DEPARTMENT);
                break;

            case CHANGE_LEAD:
                dialogManager.showSelectFilterAlertDialog(filters, FilterTypes.LEAD);
                break;

            case CHANGE_ROOM:
                dialogManager.showSelectFilterAlertDialog(filters, FilterTypes.ROOM);
                break;

            case CHANGE_REASON:
                dialogManager.showSelectFilterAlertDialog(filters, FilterTypes.REASON);
                break;

            case CHANGE_DATE_OF_BIRTH:
                dialogManager.showSelectFilterAlertDialog(filters, FilterTypes.DATE_OF_BIRTH);
                break;

            case CHANGE_FIRST_DAY:
                dialogManager.showSelectFilterAlertDialog(filters, FilterTypes.FIRST_WORKING_DAY);
                break;

            case CHANGE_FIRST_DAY_IN_IT:
                dialogManager.showSelectFilterAlertDialog(filters, FilterTypes.FIRST_WORKING_DAY_IN_IT);
                break;

            case CHANGE_TRIAL_PERIOD:
                dialogManager.showSelectFilterAlertDialog(filters, FilterTypes.TRIAL_PERIOD_ENDS);
                break;

            case CHANGE_LAST_WORKING_DAY:
                dialogManager.showSelectFilterAlertDialog(filters, FilterTypes.LAST_WORKING_DAY);
                break;
        }
    }


    private void displaySelectedFilter(IFilterModel model) {
        switch (editProfileField) {
            case CHANGE_DEPARTMENT:
                department = new Department(model.getId(), model.getName());
                tvSelectedDep.setText(model.getName());
                break;

            case CHANGE_LEAD:
                selectedLead = new Lead(model.getId(), model.getLastWorkingDay(), model.getFirstName(), model.getLastName());
                tvSelectedLead.setText(model.getName());
                break;

            case CHANGE_ROOM:
                room = new Room(model.getId(), model.getName());
                tvSelectedRoom.setText(model.getName());
                break;

            case CHANGE_REASON:
                reason = new Reason(model.getId(), model.getName());
                tvSelectedReason.setText(model.getName());
                break;
        }
    }


    private void handleSelectedDate(String formattedDate, String dateInUTC) {
        switch (editProfileField) {
            case CHANGE_DATE_OF_BIRTH:
                presenter.handleDateOfBirthChange(formattedDate, dateInUTC);
                break;

            case CHANGE_FIRST_DAY:
                presenter.handleFirstDayChange(formattedDate, dateInUTC);
                break;

            case CHANGE_FIRST_DAY_IN_IT:
                presenter.handleFirstDayInItChange(formattedDate, dateInUTC);
                break;

            case CHANGE_TRIAL_PERIOD:
                presenter.handleTrialPeriodChange(formattedDate, dateInUTC);
                break;

            case CHANGE_LAST_WORKING_DAY:
                presenter.handleLastDayChange(formattedDate, dateInUTC);
                break;
        }
    }


    private void initUi() {
        dialogManager = new FilterDialogManager(this, this);
        setupActionBar();
        ivDownload.setVisibility(View.GONE);
        presenter.setupPage();
        setListeners();
    }


    private void setListeners() {
        tilEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                tilEmail.setErrorEnabled(false);
        });

        tilPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                tilPassword.setErrorEnabled(false);
        });

        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbMale:
                    presenter.handleGenderChange(true);
                    break;

                case R.id.rbFemale:
                    presenter.handleGenderChange(false);
                    break;
            }
        });

        etChangePresentation.setOnTouchListener(getOnTouchListener());
        etComments.setOnTouchListener(getOnTouchListener());

        etChangeEmail.addTextChangedListener(getTextChangeListener(etChangeEmail, EditableFields.CHANGE_EMAIL));
        etChangePassword.addTextChangedListener(getTextChangeListener(etChangePassword, EditableFields.CHANGE_PASSWORD));
        etFirstName.addTextChangedListener(getTextChangeListener(etFirstName, EditableFields.CHANGE_FIRST_NAME));
        etLastName.addTextChangedListener(getTextChangeListener(etLastName, EditableFields.CHANGE_LAST_NAME));
        tvSelectedDateOfBirth.addTextChangedListener(getTextChangeListener(tvSelectedDateOfBirth, EditableFields.CHANGE_DATE_OF_BIRTH));

        etChangeSkype.addTextChangedListener(getTextChangeListener(etChangeSkype, EditableFields.CHANGE_SKYPE));
        etChangePhone.addTextChangedListener(getTextChangeListener(etChangePhone, EditableFields.CHANGE_PHONE));
        etChangeEmergencyNumber.addTextChangedListener(getTextChangeListener(etChangeEmergencyNumber, EditableFields.CHANGE_EMERGENCY_NUMBER));
        etChangeEmergencyContact.addTextChangedListener(getTextChangeListener(etChangeEmergencyContact, EditableFields.CHANGE_EMERGENCY_CONTACT));
        tvSelectedRoom.addTextChangedListener(getTextChangeListener(tvSelectedRoom, EditableFields.CHANGE_ROOM));

        etChangeCity.addTextChangedListener(getTextChangeListener(etChangeCity, EditableFields.CHANGE_CITY_OF_RELOCATION));
        etChangePresentation.addTextChangedListener(getTextChangeListener(etChangePresentation, EditableFields.CHANGE_PRESENTATION));

        tvSelectedDep.addTextChangedListener(getTextChangeListener(tvSelectedDep, EditableFields.CHANGE_DEPARTMENT));
        tvSelectedLead.addTextChangedListener(getTextChangeListener(tvSelectedLead, EditableFields.CHANGE_LEAD));
        tvSelectedFirstDay.addTextChangedListener(getTextChangeListener(tvSelectedFirstDay, EditableFields.CHANGE_FIRST_DAY));
        tvSelectedFirstDayInIt.addTextChangedListener(getTextChangeListener(tvSelectedFirstDayInIt, EditableFields.CHANGE_FIRST_DAY_IN_IT));
        tvSelectedTrialEnd.addTextChangedListener(getTextChangeListener(tvSelectedTrialEnd, EditableFields.CHANGE_TRIAL_PERIOD));

        etChangePdpLink.addTextChangedListener(getTextChangeListener(etChangePdpLink, EditableFields.CHANGE_PDP_LINK));
        etChangeOneToOneLink.addTextChangedListener(getTextChangeListener(etChangeOneToOneLink, EditableFields.CHANGE_ONE_TO_ONE_LINK));

        tvLastWorkingDay.addTextChangedListener(getTextChangeListener(tvLastWorkingDay, EditableFields.CHANGE_LAST_WORKING_DAY));
        tvSelectedReason.addTextChangedListener(getTextChangeListener(tvSelectedReason, EditableFields.CHANGE_REASON));
        etComments.addTextChangedListener(getTextChangeListener(etComments, EditableFields.CHANGE_COMMENTS));
    }


    private View.OnClickListener getOnClickListener() {
        return v -> {
            switch (v.getId()) {
                case R.id.rlEditDateOfBirth:
                    editProfileField = EditableFields.CHANGE_DATE_OF_BIRTH;
                    presenter.showDatePickerDialog();
                    break;
            }
        };
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.tm_hr_edit_profile_activity_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private boolean fieldContainsError() {
        return tilEmail.isErrorEnabled() || tilPassword.isErrorEnabled() || tilFirstName.isErrorEnabled() || tilLastName.isErrorEnabled()
                || tilPdpLink.isErrorEnabled() || tilOneToOneLink.isErrorEnabled();
    }


    private void showSaveChangesDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.tm_hr_edit_profile_activity_alert_dialog_save_changes))
                .setMessage(getString(R.string.tm_hr_edit_profile_activity_alert_dialog_message))
                .setPositiveButton(R.string.message_text_yes, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                    overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out);
                })
                .setNegativeButton(R.string.message_text_no, (dialog, which) -> dialog.dismiss())
                .show();
    }


    @NonNull
    private View.OnTouchListener getOnTouchListener() {
        return (v, event) -> {
            v.getParent().getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    v.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        };
    }


    private TextWatcher getTextChangeListener(final TextView textView, final EditableFields field) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView.removeTextChangedListener(this);
                // Do not cut spaces for Password field
                handleSelectedField(field == EditableFields.CHANGE_PASSWORD ? s.toString() : s.toString().trim(), field);
                textView.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }


    private void handleSelectedField(String selectedContent, EditableFields field) {
        switch (field) {
            case CHANGE_EMAIL:
                presenter.handleEmailChange(selectedContent);
                break;

            case CHANGE_PASSWORD:
                presenter.handlePasswordChange(selectedContent);
                break;

            case CHANGE_FIRST_NAME:
                presenter.handleFirstNameChange(selectedContent);
                break;

            case CHANGE_LAST_NAME:
                presenter.handleLastNameChange(selectedContent);
                break;

            case CHANGE_SKYPE:
                presenter.handleSkypeChange(selectedContent);
                break;

            case CHANGE_PHONE:
                presenter.handlePhoneChange(selectedContent);
                break;

            case CHANGE_EMERGENCY_NUMBER:
                presenter.handleEmergencyContactNumberChange(selectedContent);
                break;

            case CHANGE_EMERGENCY_CONTACT:
                presenter.handleEmergencyContactChange(selectedContent);
                break;

            case CHANGE_ROOM:
                presenter.handleRoomChange(room);
                room = null;
                break;

            case CHANGE_CITY_OF_RELOCATION:
                presenter.handleCityOfRelocationChange(selectedContent);
                break;

            case CHANGE_PRESENTATION:
                presenter.handlePresentationChange(selectedContent);
                break;

            case CHANGE_DEPARTMENT:
                presenter.handleDepartmentChange(department);
                department = null;
                break;

            case CHANGE_LEAD:
                presenter.handleLeadChange(selectedLead);
                break;

            case CHANGE_PDP_LINK:
                presenter.handlePdpChange(selectedContent);
                break;

            case CHANGE_ONE_TO_ONE_LINK:
                presenter.handleOneToOneChange(selectedContent);
                break;

            case CHANGE_REASON:
                presenter.handleReasonChange(reason);
                reason = null;
                break;

            case CHANGE_COMMENTS:
                presenter.handleCommentsChange(selectedContent);
                break;
        }
    }
}