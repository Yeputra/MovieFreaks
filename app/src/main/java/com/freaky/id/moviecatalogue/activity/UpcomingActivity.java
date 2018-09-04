package com.freaky.id.moviecatalogue.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.freaky.id.moviecatalogue.API.API;
import com.freaky.id.moviecatalogue.API.RetrofitInterface;
import com.freaky.id.moviecatalogue.R;
import com.freaky.id.moviecatalogue.adapter.FavoriteAdapter;
import com.freaky.id.moviecatalogue.adapter.MovieAdapter;
import com.freaky.id.moviecatalogue.model.Movies;
import com.freaky.id.moviecatalogue.model.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingActivity extends AppCompatActivity {

    final String lang = "en-US";
    ProgressBar pb;
    RecyclerView rvFilm;
    private List<Result> movieList = new ArrayList<>();
    final String API_KEY = "799bca1b436e938ef79b6d003aefa933";
    private MovieAdapter movieAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        context = this;
        rvFilm = (findViewById(R.id.rv_film));
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        rvFilm.setLayoutManager(new LinearLayoutManager(UpcomingActivity.this));
        movieAdapter = new MovieAdapter(movieList);
        rvFilm.setAdapter(movieAdapter);
        rvFilm.setHasFixedSize(true);

        showFilmUpcoming();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nowplaying:
                Intent nowplaying = new Intent(this, MainActivity.class);
                startActivity(nowplaying);
                break;
            case R.id.favorite:
                Intent favorite = new Intent(this, FavoriteActivity.class);
                startActivity(favorite);
                break;
            case R.id.search:
                Intent search = new Intent(this, SearchActivity.class);
                startActivity(search);
                break;
            case R.id.language:
                Intent language = new Intent(this, LanguageActivity.class);
                startActivity(language);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilmUpcoming() {
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.upcoming)));
        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... params) {
                RetrofitInterface retrofitInterface = API.getRetrofit().create(RetrofitInterface.class);
                Call<Movies> movie = retrofitInterface.getMovieUpcoming(API_KEY, lang);
                movie.enqueue(new Callback<Movies>() {
                    @Override
                    public void onResponse(Call<Movies> call, Response<Movies> response) {
                        pb.setVisibility(View.INVISIBLE);
                        rvFilm.setVisibility(View.VISIBLE);
                        movieList.clear();
                        movieList.addAll(response.body().getResults());
                        movieAdapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onFailure(Call<Movies> call, Throwable t) {
                        Log.e(MainActivity.class.getSimpleName(), "onFailure: ");
                    }
                });
                return "";
            }
        }.execute();
    }
}
