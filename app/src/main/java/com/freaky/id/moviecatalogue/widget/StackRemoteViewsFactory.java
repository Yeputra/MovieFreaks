package com.freaky.id.moviecatalogue.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.freaky.id.moviecatalogue.API.RetrofitInterface;
import com.freaky.id.moviecatalogue.R;
import com.freaky.id.moviecatalogue.model.Result;

import java.util.concurrent.ExecutionException;

import static com.freaky.id.moviecatalogue.provider.DatabaseContract.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private int mAppWidgetId;

    private Cursor list;

    public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        list = context.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null
        );
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Result item = getItem(i);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.stack_widget_item);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(RetrofitInterface.BASE_IMAGE + item.getPosterPath())
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.imageView, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(StackWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private Result getItem(int position) {
        if (!list.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid!");
        }

        return new Result(list);
    }
}