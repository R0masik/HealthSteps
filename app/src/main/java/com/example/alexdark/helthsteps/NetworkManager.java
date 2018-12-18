package com.example.alexdark.helthsteps;

import android.util.Log;

import com.google.android.gms.fitness.data.DataSet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkManager {

    // наборы ддатасетов
    ArrayList<DataSet> nutritionsSet = new ArrayList<>();
    ArrayList<DataSet> stepSet = new ArrayList<>();
    ArrayList<DataSet> activitySet = new ArrayList<>();
    ArrayList<DataSet> pulseSet = new ArrayList<>();

    private static IFitnessApi service;

    public NetworkManager() {
        super();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.4.1:5005")
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
            items.addAll(StepParser.parseSteps(dataSet));
        }

        for (SimpleItem step : items) {
            Log.e("===== Step start", String.valueOf(step.start));
            Log.e("===== Step start", String.valueOf(step.value));

            createStep(step);
        }
        Log.e("History", "##########################################################");
    }

    void sendPulse() {
        Log.e("History", "##########################################################");
        ArrayList<SimpleItem> items = new ArrayList<>();

        // для каждого датасета в наборе собираем объекты нижнего уровня
        for (DataSet dataSet : pulseSet) {
            items.addAll(StepParser.parseSteps(dataSet));
        }

        for (SimpleItem step : items) {
            createPulse(step);
        }
        Log.e("History", "##########################################################");
    }

    void sendActivity() {
        Log.e("History", "##########################################################");
        ArrayList<ActivityItem> items = new ArrayList<>();

        // для каждого датасета в наборе собираем объекты нижнего уровня
        for (DataSet dataSet : activitySet) {
            items.addAll(ActivityParser.parseActivity(dataSet));
        }

        for (ActivityItem item : items) {
            createActivity(item);
        }
        Log.e("History", "##########################################################");
    }

    void sendNutrition() {
        Log.e("History", "##########################################################");
        ArrayList<NutritionsItem> items = new ArrayList<>();

        // для каждого датасета в наборе собираем объекты нижнего уровня
        for (DataSet dataSet : nutritionsSet) {
            items.addAll(NutritionsParser.parseNutrition(dataSet));
        }

        for (NutritionsItem item : items) {
            createNutrition(item);
        }
        Log.e("History", "##########################################################");
    }


    // обработка датасетов и отправка


    public void createStep(SimpleItem step) {
        Call<Void> fitCall = service.createStep(step);
        fitCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void createPulse(SimpleItem pulse) {
        Call<Void> fitCall = service.createPulse(pulse);
        fitCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void createActivity(ActivityItem item) {
        Call<Void> fitCall = service.createActivity(item);
        fitCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


    public void createNutrition(NutritionsItem item) {
        Call<Void> fitCall = service.createNutrition(item);
        fitCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

}

