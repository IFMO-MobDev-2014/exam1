package ru.ifmo.md.exam1;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Svet on 15.01.2015.
 */
public class MyIntentService extends IntentService {
    public static final String REQUEST_TYPE = "request_type";
    public static final String REQUEST_ADDRESS = "request_address";

    public static final String RESPONSE_STRING = "json_result";
    public static final String ANSWER_TYPE = "answer_type";;


    public MyIntentService() {
        super("MyImageLoadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction(MyBroadcastReceiver.PROCESS_RESPONSE);
//        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
//        broadcastIntent.putExtra(ANSWER_TYPE, 11);
//        broadcastIntent.putExtra(RESPONSE_STRING, answer);
//        sendBroadcast(broadcastIntent);
}
