package co.techmagic.hr.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.manager.HrCryptoManager;

public class SharedPreferencesUtil {

    private static SharedPreferences prefs;
    private static HrCryptoManager hrCryptoManager;

    private SharedPreferencesUtil() {}


    public static void init(@NonNull Context appContext) {
        if (prefs == null && hrCryptoManager == null) {
            prefs = appContext.getSharedPreferences(SharedPreferencesKeys.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            hrCryptoManager = new HrCryptoManager();
        }
    }


    public static void saveAccessToken(String token) {
        saveAccessTokenLength(token.length());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.ACCESS_TOKEN_KEY, hrCryptoManager.encrypt(token));
        editor.apply();
    }


    public static String getAccessToken() {
        String encodedAuthToken = prefs.getString(SharedPreferencesKeys.ACCESS_TOKEN_KEY, null);
        return encodedAuthToken == null ? null : hrCryptoManager.decrypt(encodedAuthToken, getAccessTokenLength());
    }


    public static void saveUser(final User user) {
        String u = new Gson().toJson(user);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.LOGGED_ID_USER_KEY, u);
        editor.apply();
    }


    public static User readUser() {
        final String u = prefs.getString(SharedPreferencesKeys.LOGGED_ID_USER_KEY, null);
        return new Gson().fromJson(u, User.class);
    }


    public static void clearPreferences() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(SharedPreferencesKeys.ACCESS_TOKEN_KEY);
        editor.remove(SharedPreferencesKeys.LOGGED_ID_USER_KEY);
        editor.apply();
    }


    private static void saveAccessTokenLength(int tokenLength) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SharedPreferencesKeys.ACCESS_TOKEN_KEY_LENGTH, tokenLength);
        editor.apply();
    }


    private static int getAccessTokenLength() {
        return prefs.getInt(SharedPreferencesKeys.ACCESS_TOKEN_KEY_LENGTH, 0);
    }


    interface SharedPreferencesKeys {
        String SHARED_PREFS_NAME = "appPrefs";
        String ACCESS_TOKEN_KEY = "access_token_key";
        String ACCESS_TOKEN_KEY_LENGTH = "access_token_key_length";
        String LOGGED_ID_USER_KEY = "logged_in_user_key";
    }
}
