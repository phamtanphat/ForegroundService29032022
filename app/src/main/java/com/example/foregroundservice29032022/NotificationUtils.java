package com.example.foregroundservice29032022;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationUtils {
    private static String channel_id = "my_channel";

    public static Notification createNotification(Context context, String title, String text, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_id);
        builder.setSmallIcon(android.R.drawable.star_big_on);
        builder.setContentTitle(title);
        builder.setContentText(text);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }
}
