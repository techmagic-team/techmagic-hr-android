package co.techmagic.hr.presentation.mvp.presenter;

import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.presentation.mvp.view.EmployeeDetailsView;
import co.techmagic.hr.presentation.util.DateUtil;

public class EmployeeDetailsPresenter extends BasePresenter<EmployeeDetailsView> {


    public EmployeeDetailsPresenter() {
        super();
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
    }


    public void setupUiWithData(Docs data) {
        view.showProgress();
        showData(data);
        view.hideProgress();
    }


    private void showData(Docs data) {
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

        /* TODO next fields should be visible only for HR */
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
}
