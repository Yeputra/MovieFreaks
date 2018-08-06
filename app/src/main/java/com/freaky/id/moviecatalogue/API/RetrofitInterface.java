package com.freaky.id.moviecatalogue.API;

import com.freaky.id.moviecatalogue.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    public static final String BASE_IMAGE = "https://image.tmdb.org/t/p/w185";

    @GET("search/movie")
    Call<Movies> getMovieSearch(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query
    );

    @GET("movie/upcoming")
    Call<Movies> getMovieUpcoming(
            @Query("api_key") String apikey,
            @Query("language") String langauge
    );

    @GET("movie/now_playing")
    Call<Movies> getMovieNowPlaying(
            @Query("api_key") String apikey,
            @Query("language") String langauge
    );

}
