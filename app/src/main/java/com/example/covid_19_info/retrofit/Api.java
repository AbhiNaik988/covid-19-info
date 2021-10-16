package com.example.covid_19_info.retrofit;

import com.example.covid_19_info.model.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "https://disease.sh/v3/";

    @GET("covid-19/countries")
    Call<List<Root>> getCountries();
}

