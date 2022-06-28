package com.example.foregroundservice29032022;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    boolean isPause = false;
    Notification notification;
    int count = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BBB", "onCreate");
        notification = createNotification(count + "");
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BBB", "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BBB", "onDestroy");
    }

    private Notification createNotification(String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, "my_channel");
        builder.setSmallIcon(android.R.drawable.star_big_on);
        builder.setContentTitle("My service is running");
        builder.setContentText(text);

        Intent intentOpenActivity = new Intent(MyService.this, MainActivity.class);
        PendingIntent pendingOpenActivity = PendingIntent.getActivity(MyService.this, 0, intentOpenActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPause = new Intent(MyService.this, MainActivity.class);
        intentPause.putExtra("isPause", true);
        PendingIntent pendingPause = PendingIntent.getActivity(MyService.this, 0, intentOpenActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingOpenActivity);
        builder.addAction(android.R.drawable.ic_media_pause, "Pause", pendingPause);
        return builder.build();
    }
}
