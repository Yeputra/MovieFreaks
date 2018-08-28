package com.freaky.id.moviecatalogue.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.freaky.id.moviecatalogue.data.FavoriteHelper;
import com.freaky.id.moviecatalogue.data.MovieColumn;
import com.freaky.id.moviecatalogue.model.Result;
import com.freaky.id.moviecatalogue.API.RetrofitInterface;
import com.freaky.id.moviecatalogue.R;
import com.google.gson.GsonBuilder;

import static com.freaky.id.moviecatalogue.provider.DatabaseContract.CONTENT_URI;

public class DetailActivity extends AppCompatActivity {
    public ImageView ivPoster, ivBackdrop;
    public TextView tvTitle;
    public TextView tvOverview;
    public TextView tvDate;
    private ImageView imgFav;
    private TextView voteAvg;
    private RatingBar rb;
    private Result movie;
    private Boolean isFav = false;
    private FavoriteHelper favoriteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = new GsonBuilder().create().fromJson(this.getIntent().getStringExtra("movie"), Result.class);

        rb = findViewById(R.id.ratingBar);
        voteAvg = findViewById(R.id.tv_vote_avg);
        imgFav = findViewById(R.id.img_favorite);
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
                if (isFav == false) {
                    imgFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                    FavoriteSave();
                    isFav = true;

                } else {
                    imgFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    FavoriteRemove();
                    isFav = false;

                }
            }
        });
    }

    private void FavoriteSave() {
        ContentValues cv = new ContentValues();
        cv.put(MovieColumn.COLUMN_ID, movie.getId());
        cv.put(MovieColumn.COLUMN_TITLE, movie.getTitle());
        cv.put(MovieColumn.COLUMN_BACKDROP, movie.getBackdropPath());
        cv.put(MovieColumn.COLUMN_POSTER, movie.getPosterPath());
        cv.put(MovieColumn.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MovieColumn.COLUMN_VOTE, movie.getVoteAverage());
        cv.put(MovieColumn.COLUMN_OVERVIEW, movie.getOverview());

        getContentResolver().insert(CONTENT_URI, cv);
        Toast.makeText(DetailActivity.this, R.string.isfav,
                Toast.LENGTH_SHORT).show();
    }

    private void FavoriteRemove() {
        getContentResolver().delete(
                Uri.parse(CONTENT_URI + "/" + movie.getId()),
                null,
                null
        );
        Toast.makeText(DetailActivity.this, R.string.isNotfav,
                Toast.LENGTH_SHORT).show();
    }

    private void loadDataSQLite() {
        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();

        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI + "/" + movie.getId()),
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) isFav = true;
            cursor.close();
        }
        if (isFav) {
            imgFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            imgFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

    }
}
