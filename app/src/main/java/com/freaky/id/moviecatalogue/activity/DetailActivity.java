package com.freaky.id.moviecatalogue.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.freaky.id.moviecatalogue.model.Result;
import com.freaky.id.moviecatalogue.API.RetrofitInterface;
import com.freaky.id.moviecatalogue.R;
import com.google.gson.GsonBuilder;

public class DetailActivity extends AppCompatActivity {
    public ImageView ivPoster;
    public TextView tvTitle;
    public TextView tvOverview;
    public TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Result movie = new GsonBuilder().create().fromJson(this.getIntent().getStringExtra("movie"), Result.class);

        ivPoster = findViewById(R.id.iv_poster);
        tvTitle = findViewById(R.id.tv_title);
        tvOverview = findViewById(R.id.tv_overview);
        tvDate = findViewById(R.id.tv_date);
        getSupportActionBar().setTitle(movie.getTitle());

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvDate.setText(movie.getReleaseDate());
        Glide.with(this)
                .load(RetrofitInterface.BASE_IMAGE + movie.getPosterPath())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(ivPoster);
    }
}
