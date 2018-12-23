package com.example.alexdark.helthsteps;

import android.util.Log;

import com.google.android.gms.fitness.data.DataSet;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkManager {

    // наборы ддатасетов
    ArrayList<DataSet> stepSet = new ArrayList<>();
    ArrayList<DataSet> activitySet = new ArrayList<>();
//    ArrayList<DataSet> pulseSet = new ArrayList<>();
//    ArrayList<DataSet> nutritionsSet = new ArrayList<>();

    private static IFitnessApi service;
    public String googleId;

    public NetworkManager() {
        super();
        String baseUrl = "http://195.19.40.201:32098/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IFitnessApi.class);
    }

    // отправка шагов на сервер
    // по аналогии пишем функции для оставшихся типов данных
    void sendSteps() {
        Log.e("History", "##########################################################");
        ArrayList<SimpleItem> items = new ArrayList<>();
        // для каждого датасета в наборе собираем объекты нижнего уровня
        for (DataSet dataSet : stepSet) {
            ArrayList<SimpleItem> parsedDataList = StepParser.parseSteps(dataSet);
            for (int i = 0; i < parsedDataList.size(); i++) {
                SimpleItem item = parsedDataList.get(i);
                item.google_id = googleId;
                parsedDataList.set(i, item);
            }
            items.addAll(parsedDataList);
        }
        for (SimpleItem step : items) {
            createStep(step);
        }
        Log.e("History", "##########################################################");
    }

    void sendActivity() {
        Log.e("History", "##########################################################");
        ArrayList<ActivityItem> items = new ArrayList<>();

        // для каждого датасета в наборе собираем объекты нижнего уровня
        for (DataSet dataSet : activitySet) {
            ArrayList<ActivityItem> parsedDataList = ActivityParser.parseActivity(dataSet);
            for (int i = 0; i < parsedDataList.size(); i++) {
                ActivityItem item = parsedDataList.get(i);
                item.google_id = googleId;
                parsedDataList.set(i, item);
            }
            items.addAll(parsedDataList);
        }

        for (ActivityItem item : items) {
            createActivity(item);
        }
        Log.e("History", "##########################################################");
    }

//    void sendPulse() {
//        Log.e("History", "##########################################################");
//        ArrayList<SimpleItem> items = new ArrayList<>();
//
//        // для каждого датасета в наборе собираем объекты нижнего уровня
//        for (DataSet dataSet : pulseSet) {
//            items.addAll(StepParser.parseSteps(dataSet));
//        }
//
//        for (SimpleItem step : items) {
//            createPulse(step);
//        }
//        Log.e("History", "##########################################################");
//    }
//
//    void sendNutrition() {
//        Log.e("History", "##########################################################");
//        ArrayList<NutritionsItem> items = new ArrayList<>();
//
//        // для каждого датасета в наборе собираем объекты нижнего уровня
//        for (DataSet dataSet : nutritionsSet) {
//            items.addAll(NutritionsParser.parseNutrition(dataSet));
//        }
//
//        for (NutritionsItem item : items) {
//            createNutrition(item);
//        }
//        Log.e("History", "##########################################################");
//    }


    // обработка датасетов и отправка


    public void createStep(SimpleItem step) {
        Call<ResponseBody> fitCall = service.createStep(step);
        fitCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try (ResponseBody responseBody = response.body()) {
                    try {
                        Log.e("Response", responseBody.string());
                    } catch (IOException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void createActivity(ActivityItem item) {
        Call<ResponseBody> fitCall = service.createActivity(item);
        fitCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try (ResponseBody responseBody = response.body()) {
                    try {
                        Log.e("Response", responseBody.string());
                    } catch (IOException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

//        public void createPulse(SimpleItem pulse) {
//        Call<Void> fitCall = service.createPulse(pulse);
//        fitCall.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//
//            }
//        });
//    }
//
//    public void createNutrition(NutritionsItem item) {
//        Call<Void> fitCall = service.createNutrition(item);
//        fitCall.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//
//            }
//        });
//    }

}

