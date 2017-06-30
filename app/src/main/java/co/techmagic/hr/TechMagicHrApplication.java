package co.techmagic.hr;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import co.techmagic.hr.data.manager.impl.NetworkManagerImpl;
import co.techmagic.hr.presentation.util.LocaleUtil;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;
import io.fabric.sdk.android.Fabric;


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
    }
}