package co.techmagic.hr.presentation.mvp.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.presentation.mvp.view.DetailsView;
import co.techmagic.hr.presentation.ui.fragment.ProfileTypes;
import co.techmagic.hr.presentation.ui.view.FullSizeImageDialog;
import co.techmagic.hr.presentation.ui.view.UserPhotoActionListener;
import co.techmagic.hr.presentation.util.DateUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

public class DetailsPresenter extends BasePresenter<DetailsView> implements UserPhotoActionListener {

    private static final int ROLE_USER = 0;
    private static final int ROLE_HR = 1;
    private static final int ROLE_ADMIN = 2;

    private Docs data;
    private FullSizeImageDialog fullSizeImageDialog;


    public DetailsPresenter(Context context) {
        super();
        fullSizeImageDialog = new FullSizeImageDialog(context, R.style.DialogThemeNoBarDimmed, this);
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
    }


    @Override
    public void onCloseImage() {
        fullSizeImageDialog.hide();
    }


    @Override
    public void onDownloadImage(@NonNull Context context) {
        performDownloadImageRequest(context);
    }


    public void setupUiWithData(Docs data, ProfileTypes profileType) {
        this.data = data;
        view.showProgress();
        showData(data, profileType);
        view.hideProgress();
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


    public void onPhotoClick() {
        fullSizeImageDialog.show();
        fullSizeImageDialog.loadImage(data.getPhoto());
    }


    public void onDownloadClick(@NonNull Context context) {
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
                        saveImageIntoGallery(context, resource);
                    }
                });
    }


    private void saveImageIntoGallery(@NonNull Context context, @NonNull final Bitmap bitmap) {
        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap,
                String.valueOf(System.currentTimeMillis()),
                "Description");

        view.hideProgress();
        view.showMessage(R.string.message_image_downloaded);
    }
}