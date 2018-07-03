package com.freaky.id.moviecatalogue;

import retrofit2.Callback;
import retrofit2.http.GET;

public interface APIServices {
    @GET("/movie/popular")
    void getPopularMovies(Callback<Movie.MovieResult> cb);
}
