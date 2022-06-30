package com.example.foregroundservice29032022;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    boolean isPause;
    Notification notification;
    int count = 0;
    Thread thread;
    Handler handler;
    NotificationManager manager;
    private OnListenerCounter onListenerCounter;
    private final IBinder binder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isPause = false;
        Log.d("BBB", "onCreate");
        notification = createNotification(count + "");
        handler = new Handler(Looper.getMainLooper());
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BBB", "onStartCommand");
        if (intent != null) {
            isPause = intent.getBooleanExtra("isPause", false);
        }
        thread = new Thread(() -> {
            Log.d("BBB",isPause + "");
            for (int i = 0; i < 1000000;) {
                if (!isPause) {
                    try {
                        count = i;
                        Thread.sleep(1000);
                        if (thread.isAlive()) {
                            handler.post(() -> {
                                if (onListenerCounter != null) {
                                    onListenerCounter.onCountChange(count);
                                }

                                notification = createNotification(count + "");
                                manager.notify(1, notification);
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                } else {
                    break;
                }
            }
        });
        thread.start();
        return START_NOT_STICKY;
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

        Intent intentPause = new Intent(MyService.this, MyService.class);
        intentPause.putExtra("isPause", true);
        PendingIntent pendingPause = PendingIntent.getService(MyService.this, 0, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingOpenActivity);
        builder.addAction(android.R.drawable.ic_media_pause, "Pause", pendingPause);
        return builder.build();
    }

    public void setOnListenerCounter(OnListenerCounter onListenerCounter) {
        this.onListenerCounter = onListenerCounter;
    }

    interface OnListenerCounter {
        void onCountChange(int count);
    }
}
