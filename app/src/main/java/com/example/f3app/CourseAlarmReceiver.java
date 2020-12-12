package com.example.f3app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class CourseAlarmReceiver extends BroadcastReceiver {

        private NotificationManager mNotificationManager;
        private int NOTIFICATION_ID = 0;
        private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
        private static final String TAG = "IgB:CourseAlarmReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive started ");
            Log.d(TAG, "onReceive got action: " + intent.getAction());

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            deliverNotification(context, intent);
        }

        private void deliverNotification(Context context, Intent intent) {

            Intent contentIntent;
            if (intent.getAction().startsWith("Assessment")) {
                NOTIFICATION_ID = 3;
                contentIntent = new Intent(context, AddModifyCourseActivity.class);
            }else{
                contentIntent = new Intent(context, AddModifyAssessmentActivity.class);
            }

            contentIntent.putExtras(intent);
                PendingIntent contentPendingIntent = PendingIntent.getActivity
                        (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_noitfy)
                    .setContentTitle(intent.getAction())
                    .setContentText(intent.getAction())
                    .setContentIntent(contentPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            mNotificationManager.notify(NOTIFICATION_ID, builder.build());

        }
}