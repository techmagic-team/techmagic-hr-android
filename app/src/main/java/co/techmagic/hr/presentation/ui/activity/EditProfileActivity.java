package co.techmagic.hr.presentation.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.IFilterModel;
import co.techmagic.hr.presentation.mvp.presenter.EditProfilePresenter;
import co.techmagic.hr.presentation.mvp.view.impl.EditProfileViewImpl;
import co.techmagic.hr.presentation.ui.FilterTypes;
import co.techmagic.hr.presentation.ui.adapter.FilterAdapter;
import co.techmagic.hr.presentation.ui.fragment.DatePickerFragment;

public class EditProfileActivity extends BaseActivity<EditProfileViewImpl, EditProfilePresenter> implements FilterAdapter.OnFilterSelectionListener {

    public static final String DATE_PICKER_FRAGMENT_TAG = "date_picker_fragment_tag";

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
    @BindView(R.id.cvPersonalInfo)
    View cvPersonalInfo;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
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
    @BindView(R.id.etChangePdpLink)
    EditText etChangePdpLink;
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

    private AlertDialog dialog;
    private FilterTypes filterType = FilterTypes.ROOM;


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
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed(); // todo
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_edit_profile);
    }


    @Override
    protected EditProfileViewImpl initView() {
        return new EditProfileViewImpl(this, findViewById(android.R.id.content)) {
            @Override
            public void loadEmployeePhoto(@Nullable String photoUrl) {
                presenter.loadPhoto(photoUrl, ivPhoto);
            }

            @Override
            public void showDatePickerDialog() {
                DatePickerFragment datePicker = DatePickerFragment.newInstance();
                datePicker.show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
            }

            @Override
            public void showSelectedFilter(@NonNull String id, @NonNull String name, FilterTypes type) {
                filterType = type;
                onFilterSelected(id, name);
            }

            @Override
            public <T extends IFilterModel> void showFiltersInDialog(@Nullable List<T> filters) {
                dismissDialogIfOpened();
                showSelectFilterAlertDialog(filters);
            }

            @Override
            public void showLoginSection() {
                cvLoginInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void showEmail(@NonNull String email) {
                etChangeEmail.setText(email);
            }

            @Override
            public void showPassword(@NonNull String password) {
                etChangePassword.setText(password);
            }

            @Override
            public void showCanSignInView(boolean canSignIn) {
                cbCanSignIn.setVisibility(View.VISIBLE);
                cbCanSignIn.setChecked(canSignIn);
            }

            @Override
            public void showPersonalSection() {
                cvPersonalInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void showFirstName(@NonNull String firstName) {
                etFirstName.setText(firstName);
            }

            @Override
            public void showLastName(@NonNull String lastName) {
                etLastName.setText(lastName);
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
    public void onFilterSelected(@NonNull String id, @NonNull String name) {
        displaySelectedFilter(id, name);
    }


    @OnClick(R.id.btnUploadPhoto)
    public void uploadPhotoClick() {
        presenter.onUploadPhotoClick();
    }


    @OnClick(R.id.rlEditDateOfBirth)
    public void onDateOfBirthClick() {
        presenter.showDatePickerDialog();
    }


    @OnClick(R.id.rlEditRoom)
    public void onRoomClick() {
        filterType = FilterTypes.ROOM;
        presenter.onRoomClick();
    }


    @OnClick(R.id.rlEditDepartment)
    public void onDepartmentClick() {
        filterType = FilterTypes.DEPARTMENT;
        presenter.onDepartmentClick();
    }


    @OnClick(R.id.rlEditLead)
    public void onLeadClick() {
        filterType = FilterTypes.LEAD;
        presenter.onLeadClick();
    }


    @OnClick(R.id.rlEditFirstDay)
    public void onFirstDayClick() {
        presenter.showDatePickerDialog();
    }


    @OnClick(R.id.rlEditFirstDayInIt)
    public void onFirstDayInItClick() {
        presenter.showDatePickerDialog();
    }


    @OnClick(R.id.rlEditTrialEnd)
    public void onTrialEndClick() {
        presenter.showDatePickerDialog();
    }


    @OnClick(R.id.rlEditLastWorkingDay)
    public void onLastDayClick() {
        presenter.showDatePickerDialog();
    }


    @OnClick(R.id.rlEditSelectReason)
    public void onReasonClick() {
        filterType = FilterTypes.REASON;
        presenter.onReasonClick();
    }


    @OnClick(R.id.btnCancel)
    public void onCancelClick() {
        presenter.onCancelClick();
    }


    @OnClick(R.id.btnSave)
    public void onSaveClick() {
        presenter.onSaveClick();
    }


    private void displaySelectedFilter(String id, String filterName) {
        switch (filterType) {
            case DEPARTMENT:
                tvSelectedDep.setText(filterName);
                break;

            case LEAD:
                tvSelectedLead.setText(filterName);
                break;

            case ROOM:
                tvSelectedRoom.setText(filterName);
                break;

            case REASON:
                tvSelectedReason.setText(filterName);
                break;
        }
    }


    private <T extends IFilterModel> void showSelectFilterAlertDialog(@Nullable List<T> filters) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setupDialogViews(filters, builder);
        dialog = builder.show();
        dialog.findViewById(R.id.btnAlertDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }


    private <T extends IFilterModel> void setupDialogViews(@Nullable List<T> filters, AlertDialog.Builder builder) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_select_filter, null);
        builder.setView(view);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        setupDialogTitle(tvTitle);

        setupSelectFilterRecyclerView(view, filters);
    }


    private void setupDialogTitle(@NonNull TextView tvTitle) {
        switch (filterType) {
            case DEPARTMENT:
                tvTitle.setText(getString(R.string.tm_hr_search_activity_text_filter_by_department));
                break;

            case LEAD:
                tvTitle.setText(getString(R.string.tm_hr_search_activity_text_filter_by_lead));
                break;

            case ROOM:
                tvTitle.setText(getString(R.string.tm_hr_search_activity_text_filter_by_room));
                break;

            case REASON:
                tvTitle.setText(getString(R.string.tm_hr_search_activity_text_filter_by_reason));
                break;
        }
    }


    private <T extends IFilterModel> void setupSelectFilterRecyclerView(View view, @Nullable List<T> results) {
        RecyclerView rvFilters = (RecyclerView) view.findViewById(R.id.rvFilters);
        rvFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FilterAdapter adapter = new FilterAdapter(this, false);
        rvFilters.setAdapter(adapter);
        adapter.refresh(results);
    }


    private void dismissDialogIfOpened() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    private void initUi() {
        setupActionBar();
        ivDownload.setVisibility(View.GONE);
        presenter.setupPage();
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.tm_hr_edit_profile_activity_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}