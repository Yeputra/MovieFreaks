package com.freaky.id.moviecatalogue.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.freaky.id.moviecatalogue.R;
import com.freaky.id.moviecatalogue.adapter.FavoriteAdapter;
import com.freaky.id.moviecatalogue.adapter.MovieAdapter;
import com.freaky.id.moviecatalogue.model.Result;

import java.util.ArrayList;
import java.util.List;

import static com.freaky.id.moviecatalogue.provider.DatabaseContract.CONTENT_URI;

public class FavoriteActivity extends AppCompatActivity {

    private ProgressBar pb;
    private RecyclerView rvFilm;
    private FavoriteAdapter favAdapter;
    private Cursor list;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        context = this;
        rvFilm = (findViewById(R.id.rv_film));
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        rvFilm.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this));
        favAdapter = new FavoriteAdapter(list);
        rvFilm.setAdapter(favAdapter);
        rvFilm.setHasFixedSize(true);

        showFilmFavorite();
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

    private void showFilmFavorite() {
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.favorite)));
        pb.setVisibility(View.INVISIBLE);
        new AsyncTask<Void, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Void... voids) {
                return context.getContentResolver().query(
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
