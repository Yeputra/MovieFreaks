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
import android.util.Log;
import android.widget.Toast;

import com.freaky.id.moviecatalogue.API.API;
import com.freaky.id.moviecatalogue.API.RetrofitInterface;
import com.freaky.id.moviecatalogue.R;
import com.freaky.id.moviecatalogue.activity.DetailActivity;
import com.freaky.id.moviecatalogue.data.MovieColumn;
import com.freaky.id.moviecatalogue.model.Movies;
import com.freaky.id.moviecatalogue.model.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseReminderReceiver extends BroadcastReceiver {

    final String lang = "en-US";
    final String API_KEY = "799bca1b436e938ef79b6d003aefa933";
    public List<Result> listMovie = new ArrayList<>();

    public ReleaseReminderReceiver() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        getUpCommingMovie(context);
    }

    public static void setReminder(Context context, String type, String time, String message){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminderReceiver.class);
        intent.putExtra(MovieColumn.EXTRA_MESSAGE_RECIEVE,message);
        intent.putExtra(MovieColumn.EXTRA_TYPE_RECIEVE,type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,0);

        int requestCode = 21;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context,"Reminder Set!", Toast.LENGTH_SHORT).show();
    }

    public static void cancelReminder(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,ReleaseReminderReceiver.class);
        int requestCode = 21;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context,"Reminder Canceled", Toast.LENGTH_SHORT).show();
    }



    private void getUpCommingMovie(final Context context){
        RetrofitInterface service = API.getRetrofit(API.BASE_URL).create(RetrofitInterface.class);
        Call<Movies> call = service.getMovieUpcoming(API_KEY, lang);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {

                listMovie = response.body().getResults();

                List<Result> items = response.body().getResults();
                int index = new Random().nextInt(items.size());

                Result item = items.get(index);

                int notifId = 200;

                String title = items.get(index).getTitle();
                String message = items.get(index).getOverview();
                showNotification(context, title, message, notifId, item);

            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d("getUpComingMovie", "onFailure: " + t.toString());
            }

        });
    }


    private void showNotification(Context context, String title, String message, int notifId, Result item) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra("title", item.getTitle());
        i.putExtra("poster_path", item.getPosterPath());
        i.putExtra("overview", item.getOverview());
        i.putExtra("release_date", item.getReleaseDate());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, message)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId, builder.build());
    }

}
