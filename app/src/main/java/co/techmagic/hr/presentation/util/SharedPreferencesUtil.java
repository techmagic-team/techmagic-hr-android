package co.techmagic.hr.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class SharedPreferencesUtil {

    private static SharedPreferences prefs;

    private SharedPreferencesUtil() {}

    public static void init(@NonNull Context appContext) {
        if (prefs == null) {
            prefs = appContext.getSharedPreferences(SharedPreferencesKeys.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        }
    }

    interface SharedPreferencesKeys {
        String SHARED_PREFS_NAME = "appPrefs";
    }
}
