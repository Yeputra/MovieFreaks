package com.freaky.id.moviecatalogue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitInterface {
    public static final String BASE_IMAGE = "https://image.tmdb.org/t/p/w185";

    @GET("movie/popular")
    Call<Movies> getMovieSearch(@Query("api_key") String apikey, String v);

    /*@GET("movie/top_rated")
    Call<Movies> getMovieTopRated(@Query("api_key") String apikey);*/

}
