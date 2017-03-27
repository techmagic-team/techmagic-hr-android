package co.techmagic.hr.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.manager.YealieCryptoManager;

public class SharedPreferencesUtil {

    private static SharedPreferences prefs;
    private static YealieCryptoManager yealieCryptoManager;

    private SharedPreferencesUtil() {}


    public static void init(@NonNull Context appContext) {
        if (prefs == null && yealieCryptoManager == null) {
            prefs = appContext.getSharedPreferences(SharedPreferencesKeys.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            yealieCryptoManager = new YealieCryptoManager();
        }
    }


    public static void saveAccessToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.ACCESS_TOKEN_KEY, yealieCryptoManager.encrypt(token));
        editor.apply();
    }


    public static String getAccessToken() {
        String encodedAuthToken = prefs.getString(SharedPreferencesKeys.ACCESS_TOKEN_KEY, null);
        return encodedAuthToken == null ? null : yealieCryptoManager.decrypt(encodedAuthToken, encodedAuthToken.length());
    }


    public static void saveUser(final User user) {
        String u  = new Gson().toJson(user);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.LOGGED_ID_USER_KEY, u);
        editor.apply();
    }


    public static User readUser() {
        final String u = prefs.getString(SharedPreferencesKeys.LOGGED_ID_USER_KEY, null);
        return new Gson().fromJson(u, User.class);
    }


    interface SharedPreferencesKeys {
        String SHARED_PREFS_NAME = "appPrefs";
        String ACCESS_TOKEN_KEY = "access_token_key";
        String LOGGED_ID_USER_KEY = "logged_in_user_key";
    }
}
