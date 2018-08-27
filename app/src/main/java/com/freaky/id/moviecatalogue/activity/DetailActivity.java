package com.freaky.id.moviecatalogue.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.freaky.id.moviecatalogue.model.Result;
import com.freaky.id.moviecatalogue.API.RetrofitInterface;
import com.freaky.id.moviecatalogue.R;
import com.google.gson.GsonBuilder;

public class DetailActivity extends AppCompatActivity {
    public ImageView ivPoster, ivBackdrop;
    public TextView tvTitle;
    public TextView tvOverview;
    public TextView tvDate;
    private ImageView imgFav;
    private boolean isFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Result movie = new GsonBuilder().create().fromJson(this.getIntent().getStringExtra("movie"), Result.class);

        imgFav = findViewById(R.id.img_favorite);
        ivPoster = findViewById(R.id.iv_poster);
        tvTitle = findViewById(R.id.tv_title);
        tvOverview = findViewById(R.id.tv_overview);
        tvDate = findViewById(R.id.tv_date);
        ivBackdrop = findViewById(R.id.iv_backdrop);
        getSupportActionBar().setTitle(movie.getTitle());

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvDate.setText(movie.getReleaseDate());
        Glide.with(this)
                .load(RetrofitInterface.BASE_IMAGE_DETAIL + movie.getBackdropPath())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(ivBackdrop);
        Glide.with(this)
                .load(RetrofitInterface.BASE_IMAGE + movie.getPosterPath())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(ivPoster);

        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFav==false) {
                    imgFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                    isFav=true;
                    Toast.makeText(DetailActivity.this, "Movie Favorited!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    imgFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    isFav=false;
                    Toast.makeText(DetailActivity.this, "Movie Deleted from Favorite!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
