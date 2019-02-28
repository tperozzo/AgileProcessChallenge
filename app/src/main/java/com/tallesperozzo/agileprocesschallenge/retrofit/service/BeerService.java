package com.tallesperozzo.agileprocesschallenge.retrofit.service;

import com.tallesperozzo.agileprocesschallenge.model.Beer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
 * BeerService Interface
 * Created by Talles Perozzo
 */

public interface BeerService {
    String BASE_URL = "https://api.punkapi.com/v2/";

    //Get beers by page and per_page
    @GET("beers")
    Call<List<Beer>> getBeers(@Query("page") int page, @Query("per_page") int per_page);

    //Get beers by id
    @GET("beers/{id}")
    Call<List<Beer>> getBeerById(@Path("id") int id);
}
