package co.techmagic.hr.presentation.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.DetailsPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.DetailsViewImpl;
import co.techmagic.hr.presentation.ui.ProfileTypes;
import co.techmagic.hr.presentation.ui.activity.HomeActivity;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;
import co.techmagic.hr.presentation.ui.view.ChangeBottomTabListener;
import co.techmagic.hr.presentation.ui.view.FullPhotoActionListener;
import co.techmagic.hr.presentation.ui.view.FullSizeImageDialog;
import co.techmagic.hr.presentation.ui.view.RequestPermissionListener;

public class DetailsFragment extends BaseFragment<DetailsViewImpl, DetailsPresenter> implements RequestPermissionListener, FullPhotoActionListener {

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.ivDownload)
    ImageView ivDownload;
    @BindView(R.id.llEmail)
    View llEmail;
    @BindView(R.id.llSkype)
    View llSkype;
    @BindView(R.id.llPhone)
    View llPhone;
    @BindView(R.id.llRoom)
    View llRoom;
    @BindView(R.id.llDepartment)
    View llDepartment;
    @BindView(R.id.llLead)
    View llLead;
    @BindView(R.id.llBirthday)
    View llBirthday;
    @BindView(R.id.llRelocationCity)
    View llRelocationCity;
    @BindView(R.id.llFirstDay)
    View llFirstDay;
    @BindView(R.id.llFirstDayInIt)
    View llFirstDayInIt;
    @BindView(R.id.llTrialPeriod)
    View llTrialPeriod;
    @BindView(R.id.llLastDay)
    View llLastDay;
    @BindView(R.id.llReason)
    View llReason;
    @BindView(R.id.llComment)
    View llComment;
    @BindView(R.id.llEmergencyPhoneNumber)
    View llEmergencyPhoneNumber;
    @BindView(R.id.llEmergencyContact)
    View llEmergencyContact;
    @BindView(R.id.llVacation)
    View llVacation;
    @BindView(R.id.llDayOff)
    View llDayOff;
    @BindView(R.id.llIllness)
    View llIllness;
    @BindView(R.id.llAbout)
    View llAbout;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvSkype)
    TextView tvSkype;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvRoom)
    TextView tvRoom;
    @BindView(R.id.tvDepartment)
    TextView tvDepartment;
    @BindView(R.id.tvLead)
    TextView tvLead;
    @BindView(R.id.tvBirthday)
    TextView tvBirthday;
    @BindView(R.id.tvRelCity)
    TextView tvRelCity;
    @BindView(R.id.tvFirstDay)
    TextView tvFirstDay;
    @BindView(R.id.tvFirstDayInIt)
    TextView tvFirstDayInIt;
    @BindView(R.id.tvTrialPeriod)
    TextView tvTrialPeriod;
    @BindView(R.id.tvLastDay)
    TextView tvLastDay;
    @BindView(R.id.tvReason)
    TextView tvReason;
    @BindView(R.id.tvComment)
    TextView tvComment;
    @BindView(R.id.tvVacation)
    TextView tvVacation;
    @BindView(R.id.tvDayOff)
    TextView tvDayOff;
    @BindView(R.id.tvIllness)
    TextView tvIllness;
    @BindView(R.id.tvAbout)
    TextView tvAbout;
    @BindView(R.id.tvEmergPhoneNumber)
    TextView tvEmergPhoneNumber;
    @BindView(R.id.tvEmergContact)
    TextView tvEmergContact;

    private String userId;
    private String userName;
    private String photoUrl;
    private ProfileTypes profileTypes = ProfileTypes.NONE;
    private ActionBarChangeListener toolbarChangeListener;
    private ChangeBottomTabListener changeBottomTabListener;
    private FullSizeImageDialog fullSizeImageDialog;


    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        toolbarChangeListener = (ActionBarChangeListener) context;
        changeBottomTabListener = (ChangeBottomTabListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        presenter.performRequests(userId, profileTypes);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        setupActionBar(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                removeFragmentFromBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_WRITE_EXTERNAL_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onDownloadPhotoWithGrantedPermissionClick();
                } else {
                    view.showMessage(getString(R.string.message_permission_denied));
                }
                break;
            }
        }
    }


    @Override
    protected DetailsViewImpl initView() {
        return new DetailsViewImpl(this, getActivity().findViewById(android.R.id.content)) {
            @Override
            public void loadEmployeePhoto(@Nullable String url) {
                photoUrl = url;
                presenter.loadPhoto(photoUrl, ivPhoto);
                if (photoUrl == null) {
                    setupNoPhotoLayout();
                } else {
                    setupPhotoLayout();
                }
            }

            @Override
            public void showEmail(@NonNull String email) {
                tvEmail.setPaintFlags(tvEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                llEmail.setVisibility(View.VISIBLE);
                tvEmail.setText(getString(R.string.fragment_employee_details_card_view_text_email) + email);
            }

            @Override
            public void hideEmail() {
                llEmail.setVisibility(View.GONE);
            }

            @Override
            public void showSkype(@NonNull String skype) {
                tvSkype.setPaintFlags(tvSkype.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                llSkype.setVisibility(View.VISIBLE);
                tvSkype.setText(getString(R.string.fragment_employee_details_card_view_text_skype) + skype);
            }

            @Override
            public void hideSkype() {
                llSkype.setVisibility(View.GONE);
            }

            @Override
            public void showPhone(@NonNull String phone) {
                tvPhone.setPaintFlags(tvPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                llPhone.setVisibility(View.VISIBLE);
                tvPhone.setText(getString(R.string.fragment_employee_details_card_view_text_phone_number) + phone);
            }

            @Override
            public void hidePhone() {
                llPhone.setVisibility(View.GONE);
            }

            @Override
            public void showRoom(@NonNull String room) {
                llRoom.setVisibility(View.VISIBLE);
                tvRoom.setText(getString(R.string.fragment_employee_details_card_view_text_room) + room);
            }

            @Override
            public void hideRoom() {
                llRoom.setVisibility(View.GONE);
            }

            @Override
            public void showDepartment(@NonNull String department) {
                llDepartment.setVisibility(View.VISIBLE);
                tvDepartment.setText(getString(R.string.fragment_employee_details_card_view_text_department) + department);
            }

            @Override
            public void hideDepartment() {
                llDepartment.setVisibility(View.GONE);
            }

            @Override
            public void showLead(@NonNull String lead) {
                llLead.setVisibility(View.VISIBLE);
                tvLead.setText(getString(R.string.fragment_employee_details_card_view_text_lead) + lead);
            }

            @Override
            public void hideLead() {
                llLead.setVisibility(View.GONE);
            }

            @Override
            public void showBirthday(@NonNull String birthday) {
                llBirthday.setVisibility(View.VISIBLE);
                tvBirthday.setText(getString(R.string.fragment_employee_details_card_view_text_date_of_birth) + birthday);
            }

            @Override
            public void hideBirthday() {
                llBirthday.setVisibility(View.GONE);
            }

            @Override
            public void showRelocationCity(@NonNull String city) {
                llRelocationCity.setVisibility(View.VISIBLE);
                tvRelCity.setText(getString(R.string.fragment_employee_details_card_view_text_date_of_city_of_relocation) + city);
            }

            @Override
            public void hideRelocationCity() {
                llRelocationCity.setVisibility(View.GONE);
            }

            @Override
            public void showAbout(@NonNull String aboutText) {
                llAbout.setVisibility(View.VISIBLE);
                tvAbout.setText(getString(R.string.fragment_employee_details_card_view_text_about_me) + aboutText);
            }

            @Override
            public void hideAbout() {
                llAbout.setVisibility(View.GONE);
            }

            @Override
            public void showEmergencyPhoneNumber(@NonNull String phone) {
                llEmergencyPhoneNumber.setVisibility(View.VISIBLE);
                tvEmergPhoneNumber.setText(getString(R.string.fragment_employee_details_card_view_text_emergency_phone_number) + phone);
            }

            @Override
            public void hideEmergencyPhoneNumber() {
                llEmergencyPhoneNumber.setVisibility(View.GONE);
            }

            @Override
            public void showEmergencyContact(@NonNull String contact) {
                llEmergencyContact.setVisibility(View.VISIBLE);
                tvEmergContact.setText(getString(R.string.fragment_employee_details_card_view_text_emergency_contact) + contact);
            }

            @Override
            public void hideEmergencyContact() {
                llEmergencyContact.setVisibility(View.GONE);
            }

            @Override
            public void showFirstDay(@NonNull String date) {
                llFirstDay.setVisibility(View.VISIBLE);
                tvFirstDay.setText(getString(R.string.fragment_employee_details_card_view_text_first_working_day) + date);
            }

            @Override
            public void hideFirstDay() {
                llFirstDay.setVisibility(View.GONE);
            }

            @Override
            public void showFirstDayInIt(@NonNull String date) {
                llFirstDayInIt.setVisibility(View.VISIBLE);
                tvFirstDayInIt.setText(getString(R.string.fragment_employee_details_card_view_text_first_working_day_in_it) + date);
            }

            @Override
            public void hideFirstDayInIt() {
                llFirstDayInIt.setVisibility(View.GONE);
            }

            @Override
            public void showTrialPeriodEndsDate(@NonNull String date) {
                llTrialPeriod.setVisibility(View.VISIBLE);
                tvTrialPeriod.setText(getString(R.string.fragment_employee_details_card_view_text_trial_period_ends) + date);
            }

            @Override
            public void hideTrialPeriodEndsDate() {
                llTrialPeriod.setVisibility(View.GONE);
            }

            @Override
            public void showLastWorkingDay(@NonNull String date) {
                llLastDay.setVisibility(View.VISIBLE);
                tvLastDay.setText(getString(R.string.fragment_employee_details_card_view_text_last_working_day) + date);
            }

            @Override
            public void hideLastWorkingDay() {
                llLastDay.setVisibility(View.GONE);
            }

            @Override
            public void showReason(@NonNull String reason) {
                llReason.setVisibility(View.VISIBLE);
                tvReason.setText(getString(R.string.fragment_employee_details_card_view_text_reason) + reason);
            }

            @Override
            public void hideReason() {
                llReason.setVisibility(View.GONE);
            }

            @Override
            public void showComment(@NonNull String comment) {
                llComment.setVisibility(View.VISIBLE);
                tvComment.setText(getString(R.string.fragment_employee_details_card_view_text_comment) + comment);
            }

            @Override
            public void hideComment() {
                llComment.setVisibility(View.GONE);
            }

            @Override
            public void showVacationDays(@NonNull String dates) {
                llVacation.setVisibility(View.VISIBLE);
                tvVacation.setText(dates);
            }

            @Override
            public void hideVacations() {
                llVacation.setVisibility(View.GONE);
            }

            @Override
            public void showDayOff(@NonNull String dates) {
                llDayOff.setVisibility(View.VISIBLE);
                tvDayOff.setText(dates);
            }

            @Override
            public void hideDayOff() {
                llDayOff.setVisibility(View.GONE);
            }

            @Override
            public void showIllnessDays(@NonNull String dates) {
                llIllness.setVisibility(View.VISIBLE);
                tvIllness.setText(dates);
            }

            @Override
            public void hideIllnessDays() {
                llIllness.setVisibility(View.GONE);
            }

            @Override
            public void onCopyEmailToClipboard(@NonNull String email) {
                saveTextToClipboard(email);
            }

            @Override
            public void onCopySkypeToClipboard(@NonNull String skype) {
                saveTextToClipboard(skype);
            }

            @Override
            public void onCopyPhoneToClipboard(@NonNull String phone) {
                saveTextToClipboard(phone);
            }

            @Override
            public void showConfirmationDialog() {
                showDialog();
            }

            @Override
            public void saveImage(@NonNull Bitmap image) {
                saveImageIntoGallery(image);
            }

            @Override
            public void allowChangeBottomTab() {
                changeBottomTabListener.allowBottomTabClick();
            }

            @Override
            public void disallowChangeBottomTab() {
                changeBottomTabListener.disableBottomTabClick();
            }
        };
    }


    @Override
    protected DetailsPresenter initPresenter() {
        return new DetailsPresenter();
    }


    @Override
    public void checkForWriteExternalStoragePermission() {
        if (isWriteExternalStoragePermissionGranted()) {
            onDownloadPhotoWithGrantedPermissionClick();
        } else {
            requestWriteExternalStoragePermission();
        }
    }


    @Override
    public void onCloseImage() {
        fullSizeImageDialog.dismiss();
    }


    @Override
    public void onDownloadImage() {
        checkForWriteExternalStoragePermission();
    }


    @OnClick(R.id.ivDownload)
    public void onDownloadClick() {
        checkForWriteExternalStoragePermission();
    }


    @OnClick(R.id.tvMessage)
    public void onTapMessageClick() {
        handleOnPhotoClick();
    }


    @OnClick(R.id.ivEmailClipboard)
    public void onEmailClipBoardClick() {
        presenter.onCopyEmailToClipboardClick();
    }


    @OnClick(R.id.ivSkypeClipboard)
    public void onSkypeClipBoardClick() {
        presenter.onCopySkypeToClipboardClick();
    }


    @OnClick(R.id.ivPhoneClipboard)
    public void onPhoneClipBoardClick() {
        presenter.onCopyPhoneToClipboardClick();
    }


    @OnClick(R.id.llEmail)
    public void onEmailClick() {
        presenter.onEmailClick(getContext());
    }


    @OnClick(R.id.llSkype)
    public void onSkypeClick() {
        presenter.onSkypeClick(getContext());
    }


    @OnClick(R.id.llPhone)
    public void onPhoneClick() {
        presenter.onPhoneClick(getContext());
    }


    @OnClick(R.id.llEmergencyPhoneNumber)
    public void onEmergencyNumberClick() {
        presenter.onEmergencyPhoneNumberClick();
    }


    private void init() {
        getData();
        fullSizeImageDialog = new FullSizeImageDialog(getContext(), R.style.DialogThemeNoBarDimmed, this);
    }


    private void handleOnPhotoClick() {
        fullSizeImageDialog.show();
        fullSizeImageDialog.loadImage(photoUrl);
    }


    private void onDownloadPhotoWithGrantedPermissionClick() {
        presenter.onDownloadPhotoWithGrantedPermissionClick(getContext());
    }


    private void setupActionBar(Menu menu, MenuInflater inflater) {
        switch (profileTypes) {
            case EMPLOYEE:
                toolbarChangeListener.showBackButton();
                showEmployeeName();
                break;

            case MY_PROFILE:
                inflater.inflate(R.menu.menu_details, menu);
                showEmployeeName();
                break;
        }
    }


    private void setupPhotoLayout() {
        ivDownload.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        ivPhoto.setOnClickListener(v -> handleOnPhotoClick());
    }


    private void setupNoPhotoLayout() {
        ivDownload.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        ivPhoto.setOnClickListener(null);
    }


    private void getData() {
        Bundle b = getArguments();
        if (b != null) {
            userId = b.getString(HomeActivity.USER_ID_PARAM);
            userName = b.getString(HomeActivity.FULL_NAME_PARAM, "");
            photoUrl = b.getString(HomeActivity.PHOTO_URL_PARAM);
            profileTypes = (ProfileTypes) b.getSerializable(HomeActivity.PROFILE_TYPE_PARAM);
        }
    }


    private void showEmployeeName() {
        toolbarChangeListener.setActionBarTitle(userName);
    }


    protected void showDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.message_do_you_want_to_call_emergency_contact))
                .setPositiveButton(R.string.message_text_yes, (dialog, which) -> presenter.onEmergencyPhoneNumberClick(getContext()))
                .setNegativeButton(R.string.message_text_no, (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }


    private void saveTextToClipboard(@NonNull String text) {
        ClipboardManager clipManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Text Label", text);
        clipManager.setPrimaryClip(clip);
        view.showMessage(R.string.message_copied_to_clipboard);
    }


    private void saveImageIntoGallery(@NonNull final Bitmap bitmap) {
        MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                bitmap,
                String.valueOf(System.currentTimeMillis()),
                "Description");

        view.hideProgress();
        view.showMessage(R.string.message_image_downloaded);
    }
}