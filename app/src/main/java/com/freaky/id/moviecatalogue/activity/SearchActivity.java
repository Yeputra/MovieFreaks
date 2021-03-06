package com.freaky.id.moviecatalogue.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.freaky.id.moviecatalogue.API.API;
import com.freaky.id.moviecatalogue.API.RetrofitInterface;
import com.freaky.id.moviecatalogue.BuildConfig;
import com.freaky.id.moviecatalogue.R;
import com.freaky.id.moviecatalogue.adapter.MovieAdapter;
import com.freaky.id.moviecatalogue.model.Movies;
import com.freaky.id.moviecatalogue.model.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    String value;
    ProgressBar pb;
    final String lang = "en-US";
    EditText mEtFind;
    Button mBtnFind;
    RecyclerView rvFilm;
    private List<Result> movieList = new ArrayList<>();
    final String API_KEY = BuildConfig.your_api_key;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEtFind = findViewById(R.id.et_title);
        mBtnFind = findViewById(R.id.btn_cari);
        rvFilm = findViewById(R.id.rv_film);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        rvFilm.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        movieAdapter = new MovieAdapter(movieList);
        rvFilm.setAdapter(movieAdapter);
        rvFilm.setHasFixedSize(true);

        mBtnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                value = mEtFind.getText().toString();
                if(TextUtils.isEmpty(value)){
                    Toast.makeText(SearchActivity.this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                else {
                    actionSearch(mEtFind.getText().toString());
                }
            }
        });


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
            case R.id.upcoming:
                Intent upcoming = new Intent(this, UpcomingActivity.class);
                startActivity(upcoming);
                break;
            case R.id.favorite:
                Intent favorite = new Intent(this, FavoriteActivity.class);
                startActivity(favorite);
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

    @SuppressLint("StaticFieldLeak")
    private void actionSearch(final String val){
        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... params) {
                RetrofitInterface retrofitInterface = API.getRetrofit(API.BASE_URL).create(RetrofitInterface.class);
                Call<Movies> movie = retrofitInterface.getMovieSearch(API_KEY, lang, val);
                movie.enqueue(new Callback<Movies>() {
                    @Override
                    public void onResponse(Call<Movies> call, Response<Movies> response) {
                        Log.d(MainActivity.class.getSimpleName(), "onResponse: ");
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

