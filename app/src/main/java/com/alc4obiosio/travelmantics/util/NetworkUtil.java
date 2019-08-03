package com.alc4obiosio.travelmantics.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.TYPE_ETHERNET;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * Created by Mbuodile Obiosio on Aug 02,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class NetworkUtil {

    private NetworkUtil() {
        // no instances
    }

    @SuppressWarnings("deprecation")
    // in the future see: http://developer.android.com/intl/pt-br/reference/android/support/v4/net/ConnectivityManagerCompat.html
    public static boolean isDeviceConnectedToInternet(@NonNull Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(
                CONNECTIVITY_SERVICE);

        if (CommonUtils.isLollipop()) {
            for (Network network : connManager.getAllNetworks()) {
                if (network != null) {
                    final NetworkInfo networkInfo = connManager.getNetworkInfo(network);

                    if (networkInfo != null && networkInfo.isConnected()) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            NetworkInfo mWifi = connManager.getNetworkInfo(TYPE_WIFI);
            if (mWifi != null && mWifi.isConnected()) {
                return true;
            }

            NetworkInfo m3G = connManager.getNetworkInfo(TYPE_MOBILE);
            if (m3G != null && m3G.isConnected()) {
                return true;
            }

            if (CommonUtils.isJellyBeanMR2()) {
                NetworkInfo mEthernet = connManager.getNetworkInfo(TYPE_ETHERNET);
                return mEthernet != null && mEthernet.isConnected();
            } else {
                return false;
            }
        }
    }
}