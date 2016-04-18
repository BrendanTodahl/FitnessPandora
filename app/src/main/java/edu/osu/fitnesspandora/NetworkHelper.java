package edu.osu.fitnesspandora;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.reflect.Method;

/**
 * Created by Don on 4/17/2016.
 */
public class NetworkHelper {

    public static int TYPE_CONNECTED = 1;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = cm.getActiveNetworkInfo();

        if(network != null) {
            return TYPE_CONNECTED;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getStatusString(Context context){
        int connection = NetworkHelper.getStatus(context);
        String statusString = null;
        if(connection == NetworkHelper.TYPE_CONNECTED){
            statusString = "Connected to network.  Data being logged.";
        }else if(connection == NetworkHelper.TYPE_NOT_CONNECTED){
            statusString = "No network connection!  Data will not be logged!";
        }
        return statusString;
    }
}
