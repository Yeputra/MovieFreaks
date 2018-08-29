package com.freaky.id.favoritemovie.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.freaky.id.favoritemovie.R;
import com.freaky.id.favoritemovie.adapter.FavoriteAdapter;

import static com.freaky.id.favoritemovie.provider.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb;
    RecyclerView rvFilm;
    private FavoriteAdapter favAdapter;
    private Cursor list;
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
        favAdapter = new FavoriteAdapter(list);
        rvFilm.setAdapter(favAdapter);
        rvFilm.setHasFixedSize(true);

        showFilmFavorite();

    }

    private void showFilmFavorite() {
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.favorite)));
        pb.setVisibility(View.INVISIBLE);
        new AsyncTask<Void, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Void... voids) {
                return getContentResolver().query(
                        CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);

                list = cursor;
                favAdapter.replaceAll(list);
            }
        }.execute();
    }

}

