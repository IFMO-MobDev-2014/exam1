package ru.ifmo.md.exam1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Svet on 15.01.2015.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    public static final String PROCESS_RESPONSE = "process_response";

    MainActivity main = null;
    public void setMainActivityHandler(MainActivity main) {
        this.main = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
