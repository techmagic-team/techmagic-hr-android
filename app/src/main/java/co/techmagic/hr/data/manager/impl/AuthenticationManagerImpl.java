package co.techmagic.hr.data.manager.impl;


import android.content.Context;

import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.manager.IAuthenticationManager;

/**
 * Created by asap on 9/19/2016.
 */
public class AuthenticationManagerImpl implements IAuthenticationManager {

    private static final String SHARED_PREFS_NAME = "com.techmagic.yealie.appPrefs";

    private static final String PREFERENCE_USER_ID = "PREFERENCE_USER_ID";
    private static final String PREFERENCE_ACCESS_TOKEN = "PREFERENCE_ACCESS_TOKEN";


    public AuthenticationManagerImpl() {

    }


    @Override
    public boolean isUserSignedIn(Context context) {
        return false;
    }


    @Override
    public void saveUser(Context context, User user) {

    }


    @Override
    public void deleteUser(Context context) {

    }


    @Override
    public User readUser(Context context) {
        return null;
    }


    @Override
    public void saveAccessToken(Context context, String accessToken) {

    }


    @Override
    public String readAccessToken(Context context) {
        return null;
    }


    @Override
    public void deleteAccessToken(Context context) {

    }
}