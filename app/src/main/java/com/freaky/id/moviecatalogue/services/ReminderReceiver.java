package com.freaky.id.moviecatalogue.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.freaky.id.moviecatalogue.R;
import com.freaky.id.moviecatalogue.activity.MainActivity;
import com.freaky.id.moviecatalogue.data.MovieColumn;

import java.util.Calendar;

public class ReminderReceiver extends BroadcastReceiver {
    private final int NOTIF_ID_REMINDER = 1;

    public ReminderReceiver() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(MovieColumn.EXTRA_MESSAGE_PREF);
        String title = "Daily Reminder";
        int notifId = NOTIF_ID_REMINDER;  //type.equal

        showReminderNotification(context,title,message,notifId);
    }

    private void showReminderNotification(Context context, String title, String message, int notifId) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntent(intent)
                .getPendingIntent(NOTIF_ID_REMINDER, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,message)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId,builder.build());

    }

    public void setReminder(Context context, String type, String time, String message){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(MovieColumn.EXTRA_MESSAGE_PREF,message);
        intent.putExtra(MovieColumn.EXTRA_TYPE_PREF,type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,0);

        int requestCode = NOTIF_ID_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, "Daily Reminder Set!", Toast.LENGTH_SHORT).show();
    }
    public void cancelReminder(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,ReminderReceiver.class);
        int requestCode = NOTIF_ID_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context,"Reminder Canceled", Toast.LENGTH_SHORT).show();
    }

}
