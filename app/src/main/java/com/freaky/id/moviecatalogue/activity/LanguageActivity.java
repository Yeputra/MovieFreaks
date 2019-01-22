package com.freaky.id.moviecatalogue.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.freaky.id.moviecatalogue.R;
import com.freaky.id.moviecatalogue.data.MovieColumn;
import com.freaky.id.moviecatalogue.services.ReleaseReminderReceiver;
import com.freaky.id.moviecatalogue.services.ReminderPreference;
import com.freaky.id.moviecatalogue.services.ReminderReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class LanguageActivity extends AppCompatActivity {

    @BindView(R.id.sw_daily_reminder)
    Switch dailyReminder;
    @BindView(R.id.sw_release_reminder)
    Switch releaseReminder;
    Button btn_language;
    public ReminderReceiver reminderReceiverDaily;
    public ReminderPreference reminderPreference;
    public SharedPreferences sReleaseReminder, sDailyReminder;
    public SharedPreferences.Editor editorReleaseReminder, editorDailyReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        ButterKnife.bind(this);
        btn_language = findViewById(R.id.btn_language);
        reminderReceiverDaily = new ReminderReceiver();
        reminderPreference = new ReminderPreference(this);
        setPreference();

        btn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @OnCheckedChanged(R.id.sw_daily_reminder)
    public  void  setDailyRemind(boolean isChecked){
        editorDailyReminder = sDailyReminder.edit();
        if (isChecked) {
            editorDailyReminder.putBoolean(MovieColumn.KEY_FIELD_DAILY_REMINDER, true);
            editorDailyReminder.commit();
            dailyReminderOn();
        } else {
            editorDailyReminder.putBoolean(MovieColumn.KEY_FIELD_DAILY_REMINDER, false);
            editorDailyReminder.commit();
            dailyReminderOff();
        }
    }
    @OnCheckedChanged(R.id.sw_release_reminder)
    public  void setReleaseRemind(boolean isChecked){
        editorReleaseReminder = sReleaseReminder.edit();
        if (isChecked) {
            editorReleaseReminder.putBoolean(MovieColumn.KEY_FIELD_UPCOMING_REMINDER, true);
            editorReleaseReminder.commit();
            releaseReminderOn();
        } else {
            editorReleaseReminder.putBoolean(MovieColumn.KEY_FIELD_UPCOMING_REMINDER, false);
            editorReleaseReminder.commit();
            releaseReminderOff();
        }
    }


    private void  setPreference(){
        sReleaseReminder = getSharedPreferences(MovieColumn.KEY_HEADER_UPCOMING_REMINDER, MODE_PRIVATE);
        sDailyReminder = getSharedPreferences(MovieColumn.KEY_HEADER_DAILY_REMINDER, MODE_PRIVATE);
        boolean checkSwUpcomingReminder = sReleaseReminder.getBoolean(MovieColumn.KEY_FIELD_UPCOMING_REMINDER, false);
        releaseReminder.setChecked(checkSwUpcomingReminder);
        boolean checkSwDailyReminder = sDailyReminder.getBoolean(MovieColumn.KEY_FIELD_DAILY_REMINDER, false);
        dailyReminder.setChecked(checkSwDailyReminder);
    }

    private void releaseReminderOn() {
        String time = "08:00";
        String message = "New movie is released !";
        reminderPreference.setReminderReleaseTime(time);
        reminderPreference.setReminderReleaseMessage(message);
        ReleaseReminderReceiver.setReminder(LanguageActivity.this, MovieColumn.TYPE_REMINDER_PREF, time, message);
    }

    private void releaseReminderOff() {
        ReleaseReminderReceiver.cancelReminder(LanguageActivity.this);
    }
    private void dailyReminderOn() {
        String time = "07:00";
        String message = "We are missing you let's visit the app again!";
        reminderPreference.setReminderDailyTime(time);
        reminderPreference.setReminderDailyMessage(message);
        reminderReceiverDaily.setReminder(LanguageActivity.this, MovieColumn.TYPE_REMINDER_RECIEVE, time, message);
    }

    private void dailyReminderOff() {
        reminderReceiverDaily.cancelReminder(LanguageActivity.this);
    }

}
