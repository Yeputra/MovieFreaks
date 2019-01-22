package com.freaky.id.moviecatalogue.data;

import android.provider.BaseColumns;

public class MovieColumn implements BaseColumns {
    public static String TABLE_NAME = "favorite_movie";

    public static String COLUMN_ID = "_id";
    public static String COLUMN_TITLE = "title";
    public static String COLUMN_BACKDROP = "backdrop";
    public static String COLUMN_POSTER = "poster";
    public static String COLUMN_RELEASE_DATE = "release_date";
    public static String COLUMN_VOTE = "vote";
    public static String COLUMN_OVERVIEW = "overview";
    public static final String KEY_HEADER_UPCOMING_REMINDER = "upcomingReminder";
    public static final String KEY_HEADER_DAILY_REMINDER = "dailyReminder";
    public static final String KEY_FIELD_UPCOMING_REMINDER = "checkedUpcoming";
    public static final String KEY_FIELD_DAILY_REMINDER = "checkedDaily";
    public final static String PREF_NAME = "reminderPreferences";
    public static final String LINK_IMAGE = "http://image.tmdb.org/t/p/w185/";
    public static final String LANG = "en-US";
    public final static String KEY_REMINDER_Daily = "DailyTime";
    public final static String KEY_REMINDER_MESSAGE_Release = "reminderMessageRelease";
    public final static String KEY_REMINDER_MESSAGE_Daily = "reminderMessageDaily";
    public static final String EXTRA_MESSAGE_PREF = "message";
    public static final String EXTRA_TYPE_PREF = "type";
    public static final String TYPE_REMINDER_PREF = "reminderAlarm";
    public static final String EXTRA_MESSAGE_RECIEVE = "messageRelease";
    public static final String EXTRA_TYPE_RECIEVE = "typeRelease";
    public static final String TYPE_REMINDER_RECIEVE = "reminderAlarmRelease";
}
