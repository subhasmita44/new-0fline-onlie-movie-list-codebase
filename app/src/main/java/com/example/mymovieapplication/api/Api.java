package com.example.mymovieapplication.api;

import com.example.mymovieapplication.model.MovieDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("/")
    Call<MovieDetails> getMovieList(
            @Query("type") String type,
            @Query("apikey") String apikey,
            @Query("page") String page,
            @Query("s") String s);
}
