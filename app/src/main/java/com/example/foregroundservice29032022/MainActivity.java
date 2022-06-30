package com.example.foregroundservice29032022;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnCreateNotification, btnStartForeground, btnStopForeground;
    NotificationManager notificationManager;
    TextView tvCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateNotification = findViewById(R.id.button_create_notification);
        btnStartForeground = findViewById(R.id.button_start_foreground_service);
        btnStopForeground = findViewById(R.id.button_stop_foreground_service);
        tvCount = findViewById(R.id.textView);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        btnCreateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                Notification notification = NotificationUtils.createNotification(
                                                            MainActivity.this,
                                                            "Thông báo",
                                                            "Bạn có tin nhắn mới",
                                                            intent);
                notificationManager.notify(1, notification);
            }
        });

        btnStartForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.putExtra("text", "Hello");
                startService(intent);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        });

        btnStopForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                stopService(intent);
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
            MyService myService = myBinder.getService();
            myService.setOnListenerCounter(new MyService.OnListenerCounter() {
                @Override
                public void onCountChange(int count) {
                    tvCount.setText("Count: " + count);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (isMyServiceRunning(MyService.class)) {
            Intent intent = new Intent(MainActivity.this, MyService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
