package com.example.priya.servicetutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by priya on 1/15/2017.
 */

public class ConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isAvailable() && networkInfo.isConnected()){
            Log.v("Receiver","Network Started");
            Intent i = new Intent(context,MessagingService.class);
            context.startService(i);
        }else{
            Log.v("Network","Network Stopped");
            context.stopService(new Intent(context,MessagingService.class));
        }
    }
}
