package co.techmagic.hr.data.manager.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import co.techmagic.hr.data.manager.NetworkManager;


/**
 * Created by techmagic on 4/6/17.
 */

public class NetworkManagerImpl implements NetworkManager {

    private static NetworkManagerImpl networkManager;

    private Context context;


    public static synchronized NetworkManagerImpl initNetworkManager (Context context) {
        if (networkManager == null) {
            networkManager = new NetworkManagerImpl(context.getApplicationContext());
        }

        return networkManager;
    }


    private NetworkManagerImpl(Context context) {
        this.context = context;
    }


    public static NetworkManager getNetworkManager() {
        return networkManager;
    }


    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }

        return false;
    }
}
