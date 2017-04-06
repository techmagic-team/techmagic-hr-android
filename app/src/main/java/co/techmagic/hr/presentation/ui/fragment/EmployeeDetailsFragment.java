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
import co.techmagic.hr.presentation.mvp.presenter.EmployeeDetailsPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.EmployeeDetailsViewImpl;
import co.techmagic.hr.presentation.ui.activity.HomeActivity;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;

public class EmployeeDetailsFragment extends BaseFragment<EmployeeDetailsViewImpl, EmployeeDetailsPresenter> {

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
    @BindView(R.id.tvAbout)
    TextView tvAbout;
    @BindView(R.id.tvEmergPhoneNumber)
    TextView tvEmergPhoneNumber;
    @BindView(R.id.tvEmergContact)
    TextView tvEmergContact;

    private Docs data;
    private ActionBarChangeListener toolbarChangeListener;


    public static EmployeeDetailsFragment newInstance() {
        return new EmployeeDetailsFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        toolbarChangeListener = (ActionBarChangeListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_details, container, false);
        ButterKnife.bind(this, view);
        getData();
        initUi();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        toolbarChangeListener.showBackButton();
        if (data.getFirstName() != null && data.getLastName() != null) {
            toolbarChangeListener.showEmployeeDetailsActionBar(data.getFirstName() + " " + data.getLastName());
        }
        inflater.inflate(R.menu.menu_employee_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toolbarChangeListener.showHomeActionBar();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected EmployeeDetailsViewImpl initView() {
        return new EmployeeDetailsViewImpl(this) {
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
            public void showFirstDay(@NonNull String date) {
                llFirstDay.setVisibility(View.VISIBLE);
                tvFirstDay.setText(getString(R.string.fragment_employee_details_card_view_text_first_working_day) + date);
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
            public void showTrialPeriodEndsDate(@NonNull String date) {

            }

            @Override
            public void showLastWorkingDay(@NonNull String date) {

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
    protected EmployeeDetailsPresenter initPresenter() {
        return new EmployeeDetailsPresenter();
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


    private void initUi() {
        presenter.setupUiWithData(data);
    }


    private void getData() {
        Bundle b = getArguments();
        if (b != null) {
            data = b.getParcelable(HomeActivity.DOCS_OBJECT_PARAM);
        }
    }


    private void saveTextToClipboard(@NonNull String text) {
        ClipboardManager clipManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = android.content.ClipData.newPlainText("Text Label", text);
        clipManager.setPrimaryClip(clip);
        view.showMessage(R.string.message_copied_to_clipboard);
    }
}
