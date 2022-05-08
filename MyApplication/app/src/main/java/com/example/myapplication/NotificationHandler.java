package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "form_notification_channel";
    private final int NOTIFICATIN_ID = 0;

    private NotificationManager mManager;
    private Context mContext;
    NotificationHandler(Context context){
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT <  Build.VERSION_CODES.O)return;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                                                            "Form Notification",
                                                            NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.RED);
        channel.setDescription("Notification from M.I.L.F.");
        this.mManager.createNotificationChannel(channel);
    }

    public void send(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("M.I.L.F Application")
                .setContentText(message).setSmallIcon(R.drawable.ic_launcher_foreground);
        this.mManager.notify(NOTIFICATIN_ID, builder.build());
    }
}
