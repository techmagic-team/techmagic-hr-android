package co.techmagic.hr.presentation.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.EditProfilePresenter;
import co.techmagic.hr.presentation.mvp.view.impl.EditProfileViewImpl;
import co.techmagic.hr.presentation.ui.fragment.DatePickerFragment;

public class EditProfileActivity extends BaseActivity<EditProfileViewImpl, EditProfilePresenter> {

    public static final String DATE_PICKER_FRAGMENT_TAG = "date_picker_fragment_tag";

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.ivDownload)
    ImageView ivDownload;

    private ActionBar actionBar;


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
            public void showDatePickerDialog() {
                DatePickerFragment datePicker = DatePickerFragment.newInstance();
                datePicker.show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
            }

            @Override
            public void showEmail(@NonNull String email) {

            }

            @Override
            public void showPassword(@NonNull String password) {

            }

            @Override
            public void showCanSignInView() {

            }

            @Override
            public void showFirstName(@NonNull String firstName) {

            }

            @Override
            public void showLastName(@NonNull String lastName) {

            }

            @Override
            public void showDayOfBirth(@NonNull String date) {

            }

            @Override
            public void showGenderView() {

            }

            @Override
            public void showGender() {

            }

            @Override
            public void showSkype(@NonNull String skype) {

            }

            @Override
            public void showPhoneNumber(@NonNull String phone) {

            }

            @Override
            public void showEmergencyNumber(@NonNull String phone) {

            }

            @Override
            public void showEmergencyContact(@NonNull String phone) {

            }

            @Override
            public void showRoom(@NonNull String room) {

            }

            @Override
            public void showCityOfRelocation(@NonNull String city) {

            }

            @Override
            public void showPresentationText(@NonNull String text) {

            }

            @Override
            public void showDepartment(@NonNull String department) {

            }

            @Override
            public void showLead(@NonNull String lead) {

            }

            @Override
            public void showFirstWorkingDay(@NonNull String date) {

            }

            @Override
            public void showFirstWorkingDayInIt(@NonNull String date) {

            }

            @Override
            public void showTrialPeriodEnds(@NonNull String date) {

            }

            @Override
            public void showPdpLink(@NonNull String link) {

            }

            @Override
            public void showOneToOneLink(@NonNull String link) {

            }

            @Override
            public void showLastWorkingDay(@NonNull String date) {

            }

            @Override
            public void showReason(@NonNull String text) {

            }

            @Override
            public void showComments(@NonNull String text) {

            }
        };
    }


    @Override
    protected EditProfilePresenter initPresenter() {
        return new EditProfilePresenter();
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
        presenter.onRoomClick();
    }


    @OnClick(R.id.rlEditDepartment)
    public void onDepartmentClick() {
        presenter.onRoomClick();
    }


    @OnClick(R.id.rlEditLead)
    public void onLeadClick() {
        presenter.onRoomClick();
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


    private void initUi() {
        setupActionBar();
        ivDownload.setVisibility(View.GONE);
    }


    private void setupActionBar() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.tm_hr_edit_profile_activity_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}