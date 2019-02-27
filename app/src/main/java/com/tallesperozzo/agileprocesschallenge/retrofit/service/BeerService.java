package com.tallesperozzo.agileprocesschallenge.retrofit.service;

import com.tallesperozzo.agileprocesschallenge.model.Beer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BeerService {
    String BASE_URL = "https://api.punkapi.com/v2/";

    @GET("beers")
    Call<List<Beer>> getBeers(@Query("page") int page, @Query("per_page") int per_page);

    @GET("beers/{id}")
    Call<List<Beer>> getBeerById(@Path("id") int id);
}
