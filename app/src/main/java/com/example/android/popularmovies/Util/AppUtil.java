package com.example.android.popularmovies.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class AppUtil {

    /**
     * source https://medium.com/code-better/check-network-connectivity-on-android-in-10-lines-60d10361b73
     * @param context
     * @return
     */
    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }
}
