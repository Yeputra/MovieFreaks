package com.freaky.id.favoritemovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "movie_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_MOVIE = "create table " + MovieColumn.TABLE_NAME + " (" +
                MovieColumn.COLUMN_ID + " integer primary key autoincrement, " +
                MovieColumn.COLUMN_TITLE + " text not null, " +
                MovieColumn.COLUMN_BACKDROP + " text not null, " +
                MovieColumn.COLUMN_POSTER + " text not null, " +
                MovieColumn.COLUMN_RELEASE_DATE + " text not null, " +
                MovieColumn.COLUMN_VOTE + " text not null, " +
                MovieColumn.COLUMN_OVERVIEW + " text not null " +
                ");";

        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieColumn.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
