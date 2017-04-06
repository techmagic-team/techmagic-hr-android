package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;

import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.presentation.mvp.view.EmployeeDetailsView;
import co.techmagic.hr.presentation.util.DateUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class EmployeeDetailsPresenter extends BasePresenter<EmployeeDetailsView> {

    private static final int ROLE_USER = 0;
    private static final int ROLE_HR = 1;
    private static final int ROLE_ADMIN = 2;

    private Docs data;

    public EmployeeDetailsPresenter() {
        super();
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
    }


    public void setupUiWithData(Docs data) {
        this.data = data;
        view.showProgress();
        showData(data);
        view.hideProgress();
    }


    private void showData(@NonNull Docs data) {
        view.loadEmployeePhoto(data.getPhoto());

        if (data.getFirstName() != null && data.getLastName() != null) {
            view.showEmployeeName(data.getFirstName() + " " + data.getLastName());
        }

        if (data.getEmail() != null) {
            view.showEmail(data.getEmail());
        }

        if (data.getSkype() != null) {
            view.showSkype(data.getSkype());
        }

        if (data.getPhone() != null) {
            view.showPhone(data.getPhone());
        }

        if (data.getRoom() != null) {
            view.showRoom(data.getRoom().getName());
        }

        if (data.getDepartment() != null) {
            view.showDepartment(data.getDepartment().getName());
        }

        final Lead lead = data.getLead();
        if (lead != null) {
            view.showLead(lead.getFirstName() + " " + lead.getLastName());
        }

        final String birthdayDate = DateUtil.getFormattedDate(data.getBirthday());
        if (birthdayDate != null) {
            view.showBirthday(birthdayDate);
        }

        if (data.getRelocationCity() != null) {
            view.showRelocationCity(data.getRelocationCity());
        }

        final EmergencyContact emergencyContact = data.getEmergencyContact();
        if (emergencyContact != null && emergencyContact.getPhone() != null) {
            view.showEmergencyPhoneNumber(emergencyContact.getPhone());
        }

        if (emergencyContact != null && emergencyContact.getName() != null) {
            view.showEmergencyContact(emergencyContact.getName());
        }

        if (data.getDescription() != null) {
            view.showAbout(data.getDescription());
        }

        final int userRole = SharedPreferencesUtil.readUser().getRole();
        if (userRole == ROLE_HR) {
            setupHrRoleViews(data);
        }
    }


    private void setupHrRoleViews(@NonNull Docs data) {
        final String firstDayDate = DateUtil.getFormattedDate(data.getFirstWorkingDay());
        if (firstDayDate != null) {
            view.showFirstDay(firstDayDate);
        }

        final String trialPeriodDate = DateUtil.getFormattedDate(data.getTrialPeriodEnds());
        if (trialPeriodDate != null) {
            view.showTrialPeriodEndsDate(trialPeriodDate);
        }

        final String lastDayDate = DateUtil.getFormattedDate(data.getLastWorkingDay());
        if (lastDayDate != null) {
            view.showLastWorkingDay(lastDayDate);
        }
    }


    public void onCopyEmailToClipboardClick() {
        view.onCopyEmailToClipboard(data.getEmail());
    }


    public void onCopySkypeToClipboardClick() {
        view.onCopySkypeToClipboard(data.getSkype());
    }


    public void onCopyPhoneToClipboardClick() {
        view.onCopyPhoneToClipboard(data.getPhone());
    }
}