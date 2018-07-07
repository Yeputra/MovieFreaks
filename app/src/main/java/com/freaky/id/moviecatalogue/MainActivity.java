package com.freaky.id.moviecatalogue;

import android.net.Network;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String value;
    String lang = "EN";
    EditText mEtFind;
    Button mBtnFind;
    RecyclerView rvFilm;
    private List<Result> movieList = new ArrayList<>();
    final String API_KEY = "799bca1b436e938ef79b6d003aefa933";
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            mEtFind = findViewById(R.id.et_title);
            mBtnFind = findViewById(R.id.btn_cari);
            rvFilm = findViewById(R.id.rv_film);

        rvFilm.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        movieAdapter = new MovieAdapter(movieList);
        rvFilm.setAdapter(movieAdapter);
        rvFilm.setHasFixedSize(true);

        mBtnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                value = mEtFind.getText().toString();

                actionSearch(value);

            }
        });


    }

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
                        movieList.clear();
                        movieList.addAll(response.body().getResults());
                        movieAdapter.notifyDataSetChanged();
                        // Toast.makeText(MainActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Movies> call, Throwable t) {
                        Log.e(MainActivity.class.getSimpleName(), "onFailure: ");
                        // Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
                return "";
            }
        }.execute();
    }

}
