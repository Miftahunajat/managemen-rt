package com.pens.managementmasyrakat.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.pens.managementmasyrakat.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by miftahun on 8/6/18.
 */


public class NotificationService {

    private static final String AUTH_KEY = "key=AAAAVUCqQmY:APA91bH7Pz58g9LjzSstbKUJmHqlILP6T4YzUAeUVH2vCYkoSwUk5lxp7C8mGuHC_TcueL9jnF_w2dKSSGgFWyk2ks3Bu-QjH8SZVdb9ELJ3dR5f-fvVKP0bDV_i0Y4mtPG3XVGqLF5o";
    private final Context mContext;
    private final NotificationManager mNotificationManager;
    private static NotificationService instance;

    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";

    public static NotificationService getInstance(Context context){
        if (instance == null ){
            instance = new NotificationService(context);
        }
        return instance;
    }

    // TODO : COmplete dependency Injection

    private NotificationService(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    CHANNEL_NAME,
                    "MANAGEMENT_RT_MASYRAKAT",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public void clearAllNotifications() {
        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
    }

    public void sendNotifToWargas(final String title, final String body) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(title, body);
            }
        }).start();
    }

    private void pushNotification(String title,String body) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Pengunguman untuk Warga RT 02");
            jNotification.put("body",  title);
            jNotification.put("sound", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
//            jNotification.put("icon", "ic_notification");

            jPayload.put("to", "/topics/pengunguman" );
            jPayload.put("collapse_key", "type_a");
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Debug : ",resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}