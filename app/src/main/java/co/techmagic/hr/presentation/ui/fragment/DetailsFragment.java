package co.techmagic.hr.presentation.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.presentation.mvp.presenter.DetailsPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.DetailsViewImpl;
import co.techmagic.hr.presentation.ui.activity.HomeActivity;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;

public class DetailsFragment extends BaseFragment<DetailsViewImpl, DetailsPresenter> {

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
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
    @BindView(R.id.llTrialPeriod)
    View llTrialPeriod;
    @BindView(R.id.llLastDay)
    View llLastDay;
    @BindView(R.id.llEmergencyPhoneNumber)
    View llEmergencyPhoneNumber;
    @BindView(R.id.llEmergencyContact)
    View llEmergencyContact;
    @BindView(R.id.llAbout)
    View llAbout;
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
    @BindView(R.id.tvTrialPeriod)
    TextView tvTrialPeriod;
    @BindView(R.id.tvLastDay)
    TextView tvLastDay;
    @BindView(R.id.tvAbout)
    TextView tvAbout;
    @BindView(R.id.tvEmergPhoneNumber)
    TextView tvEmergPhoneNumber;
    @BindView(R.id.tvEmergContact)
    TextView tvEmergContact;

    private Docs data;
    private ProfileTypes profileTypes = ProfileTypes.NONE;
    private ActionBarChangeListener toolbarChangeListener;


    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        toolbarChangeListener = (ActionBarChangeListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        getData();
        initUi();
        return view;
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
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected DetailsViewImpl initView() {
        return new DetailsViewImpl(this) {
            @Override
            public void loadEmployeePhoto(@Nullable String photoUrl) {
                Glide.with(getActivity())
                        .load(photoUrl)
                        .placeholder(R.drawable.ic_user_placeholder)
                        .into(ivPhoto);
            }

            @Override
            public void showEmail(@NonNull String email) {
                llEmail.setVisibility(View.VISIBLE);
                tvEmail.setText(getString(R.string.fragment_employee_details_card_view_text_email) + email);
            }

            @Override
            public void showSkype(@NonNull String skype) {
                llSkype.setVisibility(View.VISIBLE);
                tvSkype.setText(getString(R.string.fragment_employee_details_card_view_text_skype) + skype);
            }

            @Override
            public void showPhone(@NonNull String phone) {
                llPhone.setVisibility(View.VISIBLE);
                tvPhone.setText(getString(R.string.fragment_employee_details_card_view_text_phone_number) + phone);
            }

            @Override
            public void showRoom(@NonNull String room) {
                llRoom.setVisibility(View.VISIBLE);
                tvRoom.setText(getString(R.string.fragment_employee_details_card_view_text_room) + room);
            }

            @Override
            public void showDepartment(@NonNull String department) {
                llDepartment.setVisibility(View.VISIBLE);
                tvDepartment.setText(getString(R.string.fragment_employee_details_card_view_text_department) + department);
            }

            @Override
            public void showLead(@NonNull String lead) {
                llLead.setVisibility(View.VISIBLE);
                tvLead.setText(getString(R.string.fragment_employee_details_card_view_text_lead) + lead);
            }

            @Override
            public void showBirthday(@NonNull String birthday) {
                llBirthday.setVisibility(View.VISIBLE);
                tvBirthday.setText(getString(R.string.fragment_employee_details_card_view_text_date_of_birth) + birthday);
            }

            @Override
            public void showRelocationCity(@NonNull String city) {
                llRelocationCity.setVisibility(View.VISIBLE);
                tvRelCity.setText(getString(R.string.fragment_employee_details_card_view_text_date_of_city_of_relocation) + city);
            }

            @Override
            public void showAbout(@NonNull String aboutText) {
                llAbout.setVisibility(View.VISIBLE);
                tvAbout.setText(getString(R.string.fragment_employee_details_card_view_text_about_me) + aboutText);
            }

            @Override
            public void showEmergencyPhoneNumber(@NonNull String phone) {
                llEmergencyPhoneNumber.setVisibility(View.VISIBLE);
                tvEmergPhoneNumber.setText(getString(R.string.fragment_employee_details_card_view_text_emergency_phone_number) + phone);
            }

            @Override
            public void showEmergencyContact(@NonNull String contact) {
                llEmergencyContact.setVisibility(View.VISIBLE);
                tvEmergContact.setText(getString(R.string.fragment_employee_details_card_view_text_emergency_contact) + contact);
            }

            @Override
            public void showFirstDay(@NonNull String date) {
                llFirstDay.setVisibility(View.VISIBLE);
                tvFirstDay.setText(getString(R.string.fragment_employee_details_card_view_text_first_working_day) + date);
            }

            @Override
            public void showTrialPeriodEndsDate(@NonNull String date) {
                llTrialPeriod.setVisibility(View.VISIBLE);
                tvTrialPeriod.setText(getString(R.string.fragment_employee_details_card_view_text_trial_period_ends) + date);
            }

            @Override
            public void showLastWorkingDay(@NonNull String date) {
                llLastDay.setVisibility(View.VISIBLE);
                tvLastDay.setText(getString(R.string.fragment_employee_details_card_view_text_last_working_day) + date);
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
        };
    }


    @Override
    protected DetailsPresenter initPresenter() {
        return new DetailsPresenter(getContext());
    }


    @OnClick(R.id.cvPhoto)
    public void onPhotoClick() {
        handleOnPhotoClick();
    }


    @OnClick(R.id.ivDownload)
    public void onDownloadClick() {
        handleOnDownloadClick();
    }


    @OnClick(R.id.tvMessage)
    public void onTapMessageClick() {
        presenter.onPhotoClick();
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
        presenter.onEmergencyPhoneNumberClick(getContext());
    }


    @OnClick(R.id.llEmergencyContact)
    public void onEmergencyContactClick() {
        presenter.onEmergencyContactClick(getContext());
    }


    private void initUi() {
        presenter.setupUiWithData(data, profileTypes);
    }


    private void handleOnPhotoClick() {
        presenter.onPhotoClick();
    }


    private void handleOnDownloadClick() {
        presenter.onDownloadClick(getContext());
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


    private void getData() {
        Bundle b = getArguments();
        if (b != null) {
            data = b.getParcelable(HomeActivity.DOCS_OBJECT_PARAM);
            profileTypes = (ProfileTypes) b.getSerializable(HomeActivity.PROFILE_TYPE_PARAM);
        }
    }


    private void showEmployeeName() {
        if (data.getFirstName() != null && data.getLastName() != null) {
            toolbarChangeListener.setActionBarText(data.getFirstName() + " " + data.getLastName());
        }
    }


    private void saveTextToClipboard(@NonNull String text) {
        ClipboardManager clipManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = android.content.ClipData.newPlainText("Text Label", text);
        clipManager.setPrimaryClip(clip);
        view.showMessage(R.string.message_copied_to_clipboard);
    }
}