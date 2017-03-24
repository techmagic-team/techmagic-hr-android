package co.techmagic.hr.data.manager;

import android.content.Context;

import co.techmagic.hr.data.entity.User;


/**
 * Created by asap on 9/19/2016.
 */
public interface IAuthenticationManager {

    boolean isUserSignedIn(Context context);

    void saveUser(Context context, User user);

    void deleteUser(Context context);

    User readUser(Context context);

    void saveAccessToken(Context context, String accessToken);

    String readAccessToken(Context context);

    void deleteAccessToken(Context context);
}
