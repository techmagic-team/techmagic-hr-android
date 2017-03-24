package co.techmagic.hr;

import android.app.Application;

import co.techmagic.hr.data.store.client.ApiClient;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;

/**
 * Created by ruslankuziak on 3/22/17.
 */

public class TechMagicHrApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.initApiClient(this);
        SharedPreferencesUtil.init(this);
       // Fabric.with(this, new Crashlytics());
    }
}
