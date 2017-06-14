package co.techmagic.hr;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import co.techmagic.hr.data.manager.impl.NetworkManagerImpl;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;
import io.fabric.sdk.android.Fabric;


public class TechMagicHrApplication extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManagerImpl.initNetworkManager(getApplicationContext());
        SharedPreferencesUtil.init(this);
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}