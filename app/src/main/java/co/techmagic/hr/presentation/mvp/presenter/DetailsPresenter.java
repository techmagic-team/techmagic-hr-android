package co.techmagic.hr.presentation.mvp.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.domain.interactor.employee.GetIllness;
import co.techmagic.hr.domain.interactor.employee.GetTimeOff;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.DetailsView;
import co.techmagic.hr.presentation.ui.fragment.ProfileTypes;
import co.techmagic.hr.presentation.util.DateUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class DetailsPresenter extends BasePresenter<DetailsView> {

    private static final int ROLE_USER = 0;
    private static final int ROLE_HR = 1;
    private static final int ROLE_ADMIN = 2;

    private Docs data;
    private IEmployeeRepository employeeRepository;
    private GetTimeOff getTimeOff;
    private GetIllness getIllness;


    public DetailsPresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
        getTimeOff = new GetTimeOff(employeeRepository);
        getIllness = new GetIllness(employeeRepository);
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getTimeOff.unsubscribe();
        getIllness.unsubscribe();
    }


    public void performGetTimeOffRequestsIfNeeded() {
        String firstDate = data.getFirstWorkingDay();
        if (firstDate == null) {
            return;
        }
        String userId = data.getId();
        performGetTimeOffRequest(userId, true, firstDate);
        performGetTimeOffRequest(userId, false, firstDate);
        performGetIllnessesRequest(userId, firstDate);
    }


    public void setupUiWithData(Docs data, ProfileTypes profileType) {
        this.data = data;
        showData(data, profileType);
    }


    private void showData(@NonNull Docs data, ProfileTypes profileType) {
        view.loadEmployeePhoto(data.getPhoto());

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

        /* Show next info only for User, HR or Admin */
        final int userRole = SharedPreferencesUtil.readUser().getRole();
        if (profileType == ProfileTypes.MY_PROFILE || userRole == ROLE_HR || userRole == ROLE_ADMIN) {
            showFullDetailsIfAvailable(data);
        }
    }


    private void showFullDetailsIfAvailable(@NonNull Docs data) {
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


    public void onEmailClick(@NonNull Context context) {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + data.getEmail()));

        if (isIntentAvailable(context, i)) {
            context.startActivity(Intent.createChooser(i, "Email via..."));
        }
    }


    public void onSkypeClick(@NonNull Context context) {
        Intent i = new Intent("android.intent.action.VIEW");
        final String skypeId = data.getSkype();

        if (isSkypeInstalled(context)) {
            i.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
            i.setData(Uri.parse("skype:" + skypeId + "?chat"));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }


    public void onPhoneClick(@NonNull Context context) {
        onPhoneClick(context, data.getPhone());
    }


    public void onEmergencyPhoneNumberClick() {
        view.showConfirmationDialog();
    }


    public void onEmergencyPhoneNumberClick(@NonNull Context context) {
        onPhoneClick(context, data.getEmergencyContact().getPhone());
    }


    public void onEmergencyContactClick(@NonNull Context context) {
        onPhoneClick(context, data.getEmergencyContact().getName());
    }


    private void onPhoneClick(@NonNull Context context, @NonNull String phone) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("tel:" + phone));

        if (isIntentAvailable(context, i)) {
            context.startActivity(i);
        }
    }


    /**
     * @return true if intent is available.
     * Otherwise - show error message and @return false.
     */

    private boolean isIntentAvailable(@NonNull Context context, Intent i) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            return true;
        } else {
            view.showMessage(R.string.message_no_installed_application);
            return false;
        }
    }


    private boolean isSkypeInstalled(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            view.showMessage(R.string.message_no_installed_application);
            return false;
        }
        return true;
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


    public void onDownloadPhotoWithGrantedPermissionClick(@NonNull Context context) {
        performDownloadImageRequest(context);
    }


    private void performDownloadImageRequest(Context context) {
        view.showProgress();
        Glide.with(context)
                .load(data.getPhoto())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        view.saveImage(resource);
                    }
                });
    }


    /**
     * @param isPaid == true (Vacation Request)
     *               else (Day-off Request)
     */

    private void performGetTimeOffRequest(@NonNull String userId, boolean isPaid, @NonNull String firstDate) {
        view.showProgress();

        long firstDay = DateUtil.getFirstWorkingDayInMillis(firstDate);
        long dateAfterYear = DateUtil.getDateAfterYearInMillis(firstDay);

        final TimeOffRequest request = new TimeOffRequest(userId, firstDay, dateAfterYear, isPaid);
        getTimeOff.execute(request, new DefaultSubscriber<List<RequestedTimeOff>>(view) {
            @Override
            public void onNext(List<RequestedTimeOff> response) {
                super.onNext(response);
                view.hideProgress();
                handleRetrievedTimeOffs(response, isPaid);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleRetrievedTimeOffs(List<RequestedTimeOff> requestedTimeOffs, boolean isPaid) {
        String formattedText = "";

        if (requestedTimeOffs != null && !requestedTimeOffs.isEmpty()) {
            for (RequestedTimeOff item : requestedTimeOffs) {
                if (item.isAccepted() && item.getDateFrom() != null && item.getDateTo() != null) {
                    formattedText = buildFormattedString(formattedText, item);
                }
            }
        }

        if (!TextUtils.isEmpty(formattedText)) {
            if (isPaid) {
                view.showVacationDays(formattedText);
            } else {
                view.showDayOff(formattedText);
            }
        }
    }


    private void performGetIllnessesRequest(@NonNull String userId, @NonNull String firstDate) {
        view.showProgress();

        long firstDay = DateUtil.getFirstWorkingDayInMillis(firstDate);
        long dateAfterYear = DateUtil.getDateAfterYearInMillis(firstDay);

        final GetIllnessRequest request = new GetIllnessRequest(userId, firstDay, dateAfterYear);
        getIllness.execute(request, new DefaultSubscriber<List<RequestedTimeOff>>(view) {
            @Override
            public void onNext(List<RequestedTimeOff> response) {
                super.onNext(response);
                view.hideProgress();
                handleRetrievedIllnesses(response);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleRetrievedIllnesses(List<RequestedTimeOff> requestedTimeOffs) {
        String formattedText = "";

        if (requestedTimeOffs != null && !requestedTimeOffs.isEmpty()) {
            for (RequestedTimeOff item : requestedTimeOffs) {
                if (item.isAccepted() && item.getDateFrom() != null && item.getDateTo() != null) {
                    formattedText = buildFormattedString(formattedText, item);
                }
            }
        }

        if (!TextUtils.isEmpty(formattedText)) {
            view.showIllnessDays(formattedText);
        }
    }


    @NonNull
    private String buildFormattedString(String formattedText, @NonNull RequestedTimeOff item) {
        if (TextUtils.isEmpty(formattedText)) {
            formattedText += DateUtil.getFormattedDate(item.getDateFrom()) + " - " + DateUtil.getFormattedDate(item.getDateTo());
        } else {
            formattedText += "\n" + DateUtil.getFormattedDate(item.getDateFrom()) + " - " + DateUtil.getFormattedDate(item.getDateTo());
        }
        return formattedText;
    }
}