package ru.ifmo.md.exam1;

import android.app.IntentService;
import android.content.Intent;

public class MyIntentService extends IntentService {
    public static final String ACTION_READY = "ru.ifmo.md.exam1.READY";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
            Intent intentReady = new Intent();
            intentReady.setAction(ACTION_READY);
            intentReady.addCategory(Intent.CATEGORY_DEFAULT);
            sendBroadcast(intentReady);
    }

}
