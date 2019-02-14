package com.freaky.id.moviecatalogue.activity;

import android.content.Context;
import android.content.Intent;
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
import com.freaky.id.moviecatalogue.BuildConfig;
import com.freaky.id.moviecatalogue.adapter.MovieAdapter;
import com.freaky.id.moviecatalogue.model.Movies;
import com.freaky.id.moviecatalogue.model.Result;
import com.freaky.id.moviecatalogue.API.RetrofitInterface;
import com.freaky.id.moviecatalogue.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    final String lang = "en-US";
    ProgressBar pb;
    RecyclerView rvFilm;
    private List<Result> movieList = new ArrayList<>();
    final String API_KEY = BuildConfig.your_api_key;
    private MovieAdapter movieAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        rvFilm = (findViewById(R.id.rv_film));
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        rvFilm.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        movieAdapter = new MovieAdapter(movieList);
        rvFilm.setAdapter(movieAdapter);
        rvFilm.setHasFixedSize(true);

        showFilmNowplaying();

        if(savedInstanceState!=null){
            ArrayList<Result> list;
            list = savedInstanceState.getParcelableArrayList("movie_list");
            movieAdapter = new MovieAdapter(list);
            rvFilm.setAdapter(movieAdapter);
        }else{
            showFilmNowplaying();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie_list", new ArrayList<>(movieList));
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
                rvFilm.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
                showFilmNowplaying();
                break;
            case R.id.upcoming:
                Intent upcoming = new Intent(this, UpcomingActivity.class);
                startActivity(upcoming);
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

    private void showFilmNowplaying() {
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.nowplaying)));
        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... params) {
                RetrofitInterface retrofitInterface = API.getRetrofit(API.BASE_URL).create(RetrofitInterface.class);
                Call<Movies> movie = retrofitInterface.getMovieNowPlaying(API_KEY, lang);
                movie.enqueue(new Callback<Movies>() {
                    @Override
                    public void onResponse(Call<Movies> call, Response<Movies> response) {
                        pb.setVisibility(View.INVISIBLE);
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
