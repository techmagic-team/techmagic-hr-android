package co.techmagic.hr;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import co.techmagic.hr.data.store.client.ApiClient;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;
import io.fabric.sdk.android.Fabric;


public class TechMagicHrApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.initApiClient();
        SharedPreferencesUtil.init(this);
        Fabric.with(this, new Crashlytics());
    }
}
