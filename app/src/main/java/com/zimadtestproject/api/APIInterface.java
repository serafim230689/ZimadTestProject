package com.zimadtestproject.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("api.php?")
    Call<AnimalResponse> getAnimal(@Query("query") String text);

}
