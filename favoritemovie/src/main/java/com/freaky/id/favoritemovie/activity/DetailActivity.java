package com.freaky.id.favoritemovie.activity;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.freaky.id.favoritemovie.R;
import com.freaky.id.favoritemovie.model.Result;
import com.google.gson.GsonBuilder;


public class DetailActivity extends AppCompatActivity {
    public ImageView ivPoster, ivBackdrop;
    public TextView tvTitle;
    public TextView tvOverview;
    public TextView tvDate;
    private TextView voteAvg;
    private RatingBar rb;
    private Result movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = new GsonBuilder().create().fromJson(this.getIntent().getStringExtra("movie"), Result.class);

        rb = findViewById(R.id.ratingBar);
        voteAvg = findViewById(R.id.tv_vote_avg);
        ivPoster = findViewById(R.id.iv_poster);
        tvTitle = findViewById(R.id.tv_title);
        tvOverview = findViewById(R.id.tv_overview);
        tvDate = findViewById(R.id.tv_date);
        ivBackdrop = findViewById(R.id.iv_backdrop);
        getSupportActionBar().setTitle(movie.getTitle());

        loadDataSQLite();
        rb.setRating((float) (movie.getVoteAverage() / 2));
        voteAvg.setText(String.valueOf(movie.getVoteAverage()));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvDate.setText(movie.getReleaseDate());
        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w342/" + movie.getBackdropPath())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(ivBackdrop);
        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w92" + movie.getPosterPath())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(ivPoster);

    }

    private void loadDataSQLite() {
        Uri uri = getIntent().getData();
        if (uri == null) return;
        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) movie = new Result(cursor);
            cursor.close();
        }
    }
}
