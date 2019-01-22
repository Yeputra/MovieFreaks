package com.freaky.id.moviecatalogue.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.freaky.id.moviecatalogue.data.MovieColumn;

public class ReminderPreference {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public ReminderPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(MovieColumn.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void setReminderReleaseTime(String time){
        editor.putString(MovieColumn.KEY_REMINDER_Daily,time);
        editor.commit();
    }
    public void setReminderReleaseMessage (String message){
        editor.putString(MovieColumn.KEY_REMINDER_MESSAGE_Release,message);
    }
    public void setReminderDailyTime(String time){
        editor.putString(MovieColumn.KEY_REMINDER_Daily,time);
        editor.commit();
    }
    public void setReminderDailyMessage(String message){
        editor.putString(MovieColumn.KEY_REMINDER_MESSAGE_Daily,message);
    }}
