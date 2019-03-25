package co.techmagic.hr;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import co.techmagic.hr.data.manager.impl.NetworkManagerImpl;
import co.techmagic.hr.presentation.util.LocaleUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;
import io.fabric.sdk.android.Fabric;
import io.github.eterverda.sntp.SNTP;
import io.github.eterverda.sntp.android.AndroidSNTPCacheFactory;
import io.github.eterverda.sntp.android.AndroidSNTPClientFactory;


public class TechMagicHrApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        // Always English language for the app
        super.attachBaseContext(LocaleUtil.onAttach(base, "en"));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManagerImpl.initNetworkManager(getApplicationContext());
        SharedPreferencesUtil.init(this);

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        FirebaseApp.initializeApp(this);

        SNTP.setClient(AndroidSNTPClientFactory.create());
        SNTP.setCache(AndroidSNTPCacheFactory.create(this));
    }
}