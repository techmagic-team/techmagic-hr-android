package co.techmagic.hr.presentation.ui.manager;


import android.content.Context;
import android.support.annotation.NonNull;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;


public class MixpanelManager {

    private MixpanelAPI mixpanelAPI;


    public MixpanelManager(@NonNull Context context) {
        mixpanelAPI = MixpanelAPI.getInstance(context, context.getString(R.string.mixpanel_hr_app_project_token));
    }


    /**
     * Sends messages to Mixpanel servers.
     * */

    public void flush() {
        mixpanelAPI.flush();
    }


    public void sendLoggedInUserToMixpanel() {
        JSONObject props = new JSONObject();

        try {
            props.put("platform", "Android");
            final User user = SharedPreferencesUtil.readUser();

            if (user == null) {
                mixpanelAPI.getPeople().set("Anonymous", null);
            } else {
                sendUserToMixpanel(props, user);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void trackArrivedAtScreenEventIfUserExists(@NonNull String screenName) {
        final User user = SharedPreferencesUtil.readUser();

        if (user != null) {
            JSONObject props = new JSONObject();

            try {
                props.put("distinct_id", user.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mixpanelAPI.track("Arrived at " + screenName + " screen", props);
            mixpanelAPI.getPeople().set("last_seen", new Date());
        }
    }


    private void sendUserToMixpanel(JSONObject props, User user) throws JSONException {
        mixpanelAPI.getPeople().identify(user.getId());

        if (user.getCompany() != null) {
            props.put("Company", user.getCompany().getName());
        }

        props.put("distinct_id", user.getId());
        props.put("username", user.getUsername());
        props.put("token", user.getAccessToken());
        props.put("firstName", user.getFirstName());
        props.put("lastName", user.getLastName());
        props.put("email", user.getEmail());
        props.put("phone", user.getPhone());
        props.put("created", new Date());

        mixpanelAPI.getPeople().set("Arrived at Home screen", props);
    }
}