package edu.osu.fitnesspandora;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Don on 4/17/2016.
 */
public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent){
        String status = NetworkHelper.getStatusString(context);
        Toast.makeText(context,status, Toast.LENGTH_LONG).show();
    }
}
