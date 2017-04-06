package co.techmagic.hr;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import co.techmagic.hr.data.manager.impl.NetworkManagerImpl;

import co.techmagic.hr.presentation.util.SharedPreferencesUtil;


public class TechMagicHrApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManagerImpl.initNetworkManager(this);
        SharedPreferencesUtil.init(this);
       // Fabric.with(this, new Crashlytics());
    }
}
