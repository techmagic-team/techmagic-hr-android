package co.techmagic.hr.presentation.mvp.presenter;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.data.repository.UserRepositoryImpl;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.domain.interactor.employee.GetUserDayOffs;
import co.techmagic.hr.domain.interactor.employee.GetUserIllness;
import co.techmagic.hr.domain.interactor.employee.GetUserVacations;
import co.techmagic.hr.domain.interactor.user.GetUserProfile;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.domain.repository.IUserRepository;
import co.techmagic.hr.presentation.DefaultSubscriber;
import co.techmagic.hr.presentation.mvp.view.DetailsView;
import co.techmagic.hr.presentation.ui.ProfileTypes;
import co.techmagic.hr.presentation.util.DateUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class DetailsPresenter extends BasePresenter<DetailsView> {

    static final int ROLE_USER = 0;
    static final int ROLE_HR = 1;
    static final int ROLE_ADMIN = 2;

    private UserProfile data;
    private IUserRepository userRepository;
    private IEmployeeRepository employeeRepository;
    private GetUserProfile getUserProfile;
    private GetUserVacations getUserVacations;
    private GetUserDayOffs getUserDayOffs;
    private GetUserIllness getUserIllness;

    private ProfileTypes profileType = ProfileTypes.NONE;


    public DetailsPresenter() {
        super();
        userRepository = new UserRepositoryImpl();
        employeeRepository = new EmployeeRepositoryImpl();
        getUserProfile = new GetUserProfile(userRepository);
        getUserVacations = new GetUserVacations(employeeRepository);
        getUserDayOffs = new GetUserDayOffs(employeeRepository);
        getUserIllness = new GetUserIllness(employeeRepository);
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getUserVacations.unsubscribe();
        getUserIllness.unsubscribe();
    }


    public void performRequests(@NonNull String userId , ProfileTypes profileType) {
        this.profileType = profileType;
        performGetUserProfileAndTimeOffRequests(userId);
    }


    private void performGetTimeOffRequestsIfNeeded() {
        String firstDate = data.getFirstWorkingDay();
        if (firstDate == null) {
            view.allowChangeBottomTab();
            return;
        }
        view.disallowChangeBottomTab();
        String userId = data.getId();
        performGetUserVacationsRequest(userId, firstDate);
        performGetUserDayOffsRequest(userId, firstDate);
        performGetIllnessesRequest(userId, firstDate);
    }


    private void showData() {
        view.loadEmployeePhoto(data.getPhotoOrigin() == null ? data.getPhoto() : data.getPhotoOrigin());

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

        if (data.getRelocationCity() != null) {
            view.showRelocationCity(data.getRelocationCity());
        }

        if (data.getDescription() != null) {
            view.showAbout(data.getDescription());
        }

        final String firstDate = DateUtil.getFormattedFullDate(data.getFirstWorkingDay());
        if (firstDate != null) {
            view.showFirstDay(firstDate);
        }

        /* Show next info only for User, HR or Admin */
        final int userRole = SharedPreferencesUtil.readUser().getRole();
        if (profileType == ProfileTypes.MY_PROFILE || userRole == ROLE_HR || userRole == ROLE_ADMIN) {
            showFullDetailsIfAvailable(data);
        } else {
            final String birthdayDate = getCorrectDateFormat(data, false);
            if (birthdayDate != null) {
                view.showBirthday(birthdayDate);
            }
        }
    }


    private void showFullDetailsIfAvailable(@NonNull UserProfile data) {
        final String birthdayDate = getCorrectDateFormat(data, true);
        if (birthdayDate != null) {
            view.showBirthday(birthdayDate);
        }

        final String firstDayInItDate = DateUtil.getFormattedFullDate(data.getGeneralFirstWorkingDay());
        if (firstDayInItDate != null) {
            view.showFirstDayInIt(firstDayInItDate);
        }

        final String trialPeriodDate = DateUtil.getFormattedFullDate(data.getTrialPeriodEnds());
        if (trialPeriodDate != null) {
            view.showTrialPeriodEndsDate(trialPeriodDate);
        }

        final String lastDayDate = DateUtil.getFormattedFullDate(data.getLastWorkingDay());
        if (lastDayDate != null) {
            view.showLastWorkingDay(lastDayDate);
        }

        final EmergencyContact emergencyContact = data.getEmergencyContact();
        if (emergencyContact != null && emergencyContact.getPhone() != null) {
            view.showEmergencyPhoneNumber(emergencyContact.getPhone());
        }

        if (emergencyContact != null && emergencyContact.getName() != null) {
            view.showEmergencyContact(emergencyContact.getName());
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

            try {
                context.startActivity(i);
            } catch (ActivityNotFoundException ex) {
                view.showMessage(R.string.message_no_installed_application);
            }
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


    public void loadPhoto(String photoUrl, @NonNull ImageView ivPhoto) {
        Glide.with(ivPhoto.getContext())
                .load(photoUrl)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(ivPhoto);
    }


    private void performGetUserProfileAndTimeOffRequests(@NonNull String userId) {
        view.showProgress();
        final GetMyProfileRequest request = new GetMyProfileRequest(userId);
        getUserProfile.execute(request, new DefaultSubscriber<UserProfile>() {
            @Override
            public void onNext(UserProfile userProfile) {
                super.onNext(userProfile);
                data = userProfile;
                view.hideProgress();
                showData();
                performGetTimeOffRequestsIfNeeded();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void performDownloadImageRequest(Context context) {
        view.showProgress();
        Glide.with(context)
                .load(data.getPhotoOrigin() == null ? data.getPhoto() : data.getPhotoOrigin())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        view.saveImage(resource);
                    }
                });
    }


    private void performGetUserVacationsRequest(@NonNull String userId, @NonNull String firstDate) {
        view.showProgress();

        long firstDay = DateUtil.getFirstWorkingDayInMillis(firstDate);
        long dateAfterYear = DateUtil.getDateAfterYearInMillis();

        final TimeOffRequest request = new TimeOffRequest(userId, true, firstDay, dateAfterYear);
        getUserVacations.execute(request, new DefaultSubscriber<List<RequestedTimeOff>>(view) {
            @Override
            public void onNext(List<RequestedTimeOff> response) {
                super.onNext(response);
                view.hideProgress();
                handleRetrievedVacations(response);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleRetrievedVacations(List<RequestedTimeOff> vacations) {
        String formattedText = "";

        if (vacations != null && !vacations.isEmpty()) {
            for (RequestedTimeOff item : vacations) {
                if (item.isAccepted() && item.getDateFrom() != null && item.getDateTo() != null) {
                    formattedText = buildFormattedString(formattedText, item);
                }
            }
        }

        if (!TextUtils.isEmpty(formattedText)) {
            view.showVacationDays(formattedText);
        }
    }


    private void performGetUserDayOffsRequest(@NonNull String userId, @NonNull String firstDate) {
        view.showProgress();

        long firstDay = DateUtil.getFirstWorkingDayInMillis(firstDate);
        long dateAfterYear = DateUtil.getDateAfterYearInMillis();

        final TimeOffRequest request = new TimeOffRequest(userId, false, firstDay, dateAfterYear);
        getUserDayOffs.execute(request, new DefaultSubscriber<List<RequestedTimeOff>>(view) {
            @Override
            public void onNext(List<RequestedTimeOff> response) {
                super.onNext(response);
                view.hideProgress();
                handleRetrievedDayOffs(response);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgress();
            }
        });
    }


    private void handleRetrievedDayOffs(List<RequestedTimeOff> dayOffs) {
        String formattedText = "";

        if (dayOffs != null && !dayOffs.isEmpty()) {
            for (RequestedTimeOff item : dayOffs) {
                if (item.isAccepted() && item.getDateFrom() != null && item.getDateTo() != null) {
                    formattedText = buildFormattedString(formattedText, item);
                }
            }
        }

        if (!TextUtils.isEmpty(formattedText)) {
            view.showDayOff(formattedText);
        }
    }



    private void performGetIllnessesRequest(@NonNull String userId, @NonNull String firstDate) {
        view.showProgress();

        long firstDay = DateUtil.getFirstWorkingDayInMillis(firstDate);
        long dateAfterYear = DateUtil.getDateAfterYearInMillis();

        final GetIllnessRequest request = new GetIllnessRequest(userId, firstDay, dateAfterYear);
        getUserIllness.execute(request, new DefaultSubscriber<List<RequestedTimeOff>>(view) {
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
                view.allowChangeBottomTab();
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

        /* Should allow user to change the tab */

        if (TextUtils.isEmpty(formattedText)) {
            view.allowChangeBottomTab();
        } else {
            view.showIllnessDays(formattedText);
            view.allowChangeBottomTab();
        }
    }


    @NonNull
    private String buildFormattedString(String formattedText, @NonNull RequestedTimeOff item) {
        if (TextUtils.isEmpty(formattedText)) {
            formattedText += DateUtil.getFormattedFullDate(item.getDateFrom()) + " - " + DateUtil.getFormattedFullDate(item.getDateTo());
        } else {
            formattedText += "\n" + DateUtil.getFormattedFullDate(item.getDateFrom()) + " - " + DateUtil.getFormattedFullDate(item.getDateTo());
        }
        return formattedText;
    }


    @Nullable
    private String getCorrectDateFormat(UserProfile data, boolean fullDate) {
        return fullDate ? DateUtil.getFormattedFullDate(data.getBirthday()) : DateUtil.getFormattedMonthAndDay(data.getBirthday());
    }
}