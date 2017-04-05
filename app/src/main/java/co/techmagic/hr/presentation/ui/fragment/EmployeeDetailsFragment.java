package co.techmagic.hr.presentation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.presentation.mvp.presenter.EmployeeDetailsPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.EmployeeDetailsViewImpl;
import co.techmagic.hr.presentation.ui.activity.HomeActivity;

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


    public static EmployeeDetailsFragment newInstance() {
        return new EmployeeDetailsFragment();
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
    protected EmployeeDetailsViewImpl initView() {
        return new EmployeeDetailsViewImpl(this) {
            @Override
            public void showEmployeeName(@NonNull String name) {

            }

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
                tvEmail.setText(email);
            }

            @Override
            public void showSkype(@NonNull String skype) {
                llSkype.setVisibility(View.VISIBLE);
                tvSkype.setText(skype);
            }

            @Override
            public void showPhone(@NonNull String phone) {
                llPhone.setVisibility(View.VISIBLE);
                tvPhone.setText(phone);
            }

            @Override
            public void showRoom(@NonNull String room) {
                llRoom.setVisibility(View.VISIBLE);
                tvRoom.setText(room);
            }

            @Override
            public void showDepartment(@NonNull String department) {
                llDepartment.setVisibility(View.VISIBLE);
                tvDepartment.setText(department);
            }

            @Override
            public void showLead(@NonNull String lead) {
                llLead.setVisibility(View.VISIBLE);
                tvLead.setText(lead);
            }

            @Override
            public void showBirthday(@NonNull String birthday) {
                llBirthday.setVisibility(View.VISIBLE);
                tvBirthday.setText(birthday);
            }

            @Override
            public void showRelocationCity(@NonNull String city) {
                llRelocationCity.setVisibility(View.VISIBLE);
                tvRelCity.setText(city);
            }

            @Override
            public void showFirstDay(@NonNull String date) {
                llFirstDay.setVisibility(View.VISIBLE);
                tvFirstDay.setText(date);
            }

            @Override
            public void showAbout(@NonNull String aboutText) {
                llAbout.setVisibility(View.VISIBLE);
                tvAbout.setText(aboutText);
            }

            @Override
            public void showEmergencyPhoneNumber(@NonNull String phone) {
                llEmergencyPhoneNumber.setVisibility(View.VISIBLE);
                tvEmergPhoneNumber.setText(phone);
            }

            @Override
            public void showEmergencyContact(@NonNull String contact) {
                llEmergencyContact.setVisibility(View.VISIBLE);
                tvEmergContact.setText(contact);
            }

            @Override
            public void showTrialPeriodEndsDate(@NonNull String date) {

            }

            @Override
            public void showLastWorkingDay(@NonNull String date) {

            }
        };
    }


    @Override
    protected EmployeeDetailsPresenter initPresenter() {
        return new EmployeeDetailsPresenter();
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
}
