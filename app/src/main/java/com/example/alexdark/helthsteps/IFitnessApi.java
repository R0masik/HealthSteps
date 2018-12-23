package com.example.alexdark.helthsteps;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import okhttp3.ResponseBody;

public interface IFitnessApi {

//    @POST("/data")
//    Call<SimpleItem> createContainer(@Body NetworkManager.FitnessContainer user);

    @GET("/")
    Call<Void> rootGet();

    @POST("/login")
    Call<ResponseBody> accLogin(@Body HealthStepsAcc acc);

    @POST("/calculate_coefficient")
    Call<ResponseBody> calculateCoefficient(@Body TestData data);

    @POST("/step")
    Call<ResponseBody> createStep(@Body SimpleItem step);

    @POST("/activity")
    Call<ResponseBody> createActivity(@Body ActivityItem activity);

//    @POST("/pulse")
//    Call<Void> createPulse(@Body SimpleItem pulse);
//
//    @POST("/nutrition")
//    Call<Void> createNutrition(@Body NutritionsItem nutr);

}
