package com.freaky.id.moviecatalogue.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.freaky.id.moviecatalogue.API.API;
import com.freaky.id.moviecatalogue.API.RetrofitInterface;
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
    Button mBtnFind, mBtnUpcoming;
    RecyclerView rvFilm;
    private List<Result> movieList = new ArrayList<>();
    final String API_KEY = "799bca1b436e938ef79b6d003aefa933";
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

    @SuppressLint("StaticFieldLeak")
    private void actionSearch(final String val){
        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... params) {
                RetrofitInterface retrofitInterface = API.getRetrofit().create(RetrofitInterface.class);
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

