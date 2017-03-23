package co.techmagic.hr;

import android.app.Application;

/**
 * Created by ruslankuziak on 3/22/17.
 */

public class TechMagicHrApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       // Fabric.with(this, new Crashlytics());
    }
}
