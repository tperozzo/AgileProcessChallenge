package com.tallesperozzo.agileprocesschallenge.retrofit;

import com.tallesperozzo.agileprocesschallenge.retrofit.service.BeerService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Retrofit Class
 * Created by Talles Perozzo
 */

public class RetrofitInitializer {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BeerService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
