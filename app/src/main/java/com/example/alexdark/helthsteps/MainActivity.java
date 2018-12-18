package com.example.alexdark.helthsteps;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static int RC_SIGN_IN = 100;

    private Button mButtonViewToday;
    private Button mConBut;
    //    private Button mButtonSignIn;
    private TextView mCoefTextView;

    private GoogleSignInAccount account;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private NetworkManager networkManager;

    private static KEK service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mCoefTextView = findViewById(R.id.textCoefView);
        String coefText = getResources().getString(R.string.coefText);

        String mCoefString = String.format(coefText, "0.5");
        mCoefTextView.setText(mCoefString);

        initViews();

        //Немного магический код, но он нужен)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //создание клиента (логин) из важного, вы тут указываете скоупы данных которые вам нужны
        //если нужно что-то еще смотрите в инструкции на типы данных и в каких скоупах они лежат
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SESSIONS_API)
                .addApi(Fitness.CONFIG_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_NUTRITION_READ))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();

        networkManager = new NetworkManager();
    }

    @Override
    protected void onStart() {
        super.onStart();

        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            hideSignInButton();
        }
    }

    private void initViews() {
        //настройка кнопок
        mButtonViewToday = findViewById(R.id.btn_view_today);
        mButtonViewToday.setOnClickListener(this);
        mButtonViewToday.setText("GetData");

        mConBut = findViewById(R.id.resig);
        mConBut.setText("wait for con");

        // google sign in and out buttons
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
    }

    public void onConnected(@Nullable Bundle bundle) {
        //обработка события "успешное подключение к облаку"
        Log.e("HistoryAPI", "onConnected");
        mConBut.setBackgroundColor(10);
        mConBut.setText("Con OK");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("HistoryAPI", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HistoryAPI", "onConnectionFailed");
    }

    @Override
    public void onClick(View v) {
        // обработка гажатия на кнопку
        switch (v.getId()) {
            case R.id.btn_view_today: {
                //запросы данных
//                new WeekStepTask(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA).execute();
//                new WeekNutritionTask(DataType.TYPE_NUTRITION, DataType.AGGREGATE_NUTRITION_SUMMARY).execute();
//                new WeekPulseTask(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY).execute();
//                new WeekActivityTask(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY).execute();
//
//                String notifyTitle = getResources().getString(R.string.notifyTitle);
//                String notifyText = getResources().getString(R.string.notifyText);
//                showNotification(notifyTitle, notifyText);
//
//                // запись данныъх в облако
//                new WriteActivityTask().execute();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.95.64:32802")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                service = retrofit.create(KEK.class);
                foo();
                break;
            }
            case R.id.sign_in_button: {
                signIn();
                break;
            }
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    public interface KEK {
        @GET("/")
        Call<Void> foo();
    }

    public void foo() {
        Call<Void> call = service.foo();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void showNotification(String nTitle, String nTest) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(nTitle)
                        .setContentText(nTest);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            hideSignInButton();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("signInResult: ", "failed code=" + e.getStatusCode());

        }
    }

    public void signOut() {
        mGoogleSignInClient.signOut();
        mGoogleSignInClient.revokeAccess();
        showSignInButton();
    }

    public void hideSignInButton() {
        Log.e("GoogleID", account.getId());
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
    }

    public void showSignInButton() {
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
    }

    // запрос данных типа д1 д2 за последнюю неделю
    //возвращает набор дадасетов которые пришли
    private ArrayList<DataSet> displayLastWeekData(GoogleApiClient client, DataType d1, DataType d2) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        ArrayList<DataSet> sets = new ArrayList<DataSet>();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));

        //Check how many steps were walked and recorded in the last 7 days
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(d1, d2)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, MILLISECONDS)
                .build();

        DataReadResult dataReadResult = Fitness.HistoryApi.readData(client, readRequest).await(1, TimeUnit.MINUTES);

        //Used for aggregated data
        if (dataReadResult.getBuckets().size() > 0) {
            Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                sets.addAll(dataSets);
            }
        }
        //Used for non-aggregated data
        else if (dataReadResult.getDataSets().size() > 0) {
            Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
            sets.addAll(dataReadResult.getDataSets());
        }
        return sets;
    }

    // пример записи данных в облако гугл фита
    private void writeActivity(GoogleApiClient client) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        // Set a range of the run, using a start time of 10 minutes before this moment,
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.MINUTE, -10);
        long startTime = cal.getTimeInMillis();

        // Create a data source
        DataSource runningDataSource = new DataSource.Builder()
                .setAppPackageName(this.getPackageName())
                .setDataType(DataType.TYPE_SPEED)
                .setName("-running speed")
                .setType(DataSource.TYPE_RAW)
                .build();

        float runSpeedMps = 10;
        // Create a data set of the running speeds to include in the session.
        DataSet runningDataSet = DataSet.create(runningDataSource);
        runningDataSet.add(
                runningDataSet.createDataPoint()
                        .setTimeInterval(startTime, startTime, TimeUnit.MILLISECONDS)
                        .setFloatValues(runSpeedMps)
        );
        Fitness.HistoryApi.insertData(client, runningDataSet);
    }

    //общий класс для асинхронной задачи
    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        DataType d1;
        DataType d2;

        public MyAsyncTask(DataType d1, DataType d2) {
            this.d1 = d1;
            this.d2 = d2;
        }

        protected Void doInBackground(Void... params) {
            return null;
        }
    }

    //асинхронная реализация запроса шагов
    private class WeekStepTask extends MyAsyncTask {

        public WeekStepTask(DataType d1, DataType d2) {
            super(d1, d2);
        }

        protected Void doInBackground(Void... params) {

            //обновляем набор датасетов в нетворк менеджере
            networkManager.stepSet = displayLastWeekData(mGoogleApiClient, d1, d2);
            //запускаем функцию отправки шагов
            networkManager.sendSteps();
            return null;
        }
    }

    private class WeekPulseTask extends MyAsyncTask {

        public WeekPulseTask(DataType d1, DataType d2) {
            super(d1, d2);
        }

        protected Void doInBackground(Void... params) {
            networkManager.pulseSet = displayLastWeekData(mGoogleApiClient, d1, d2);
            networkManager.sendPulse();
            return null;
        }
    }

    private class WeekActivityTask extends MyAsyncTask {

        public WeekActivityTask(DataType d1, DataType d2) {
            super(d1, d2);
        }

        protected Void doInBackground(Void... params) {

            //обновляем набор датасетов в нетворк менеджере
            networkManager.activitySet = displayLastWeekData(mGoogleApiClient, d1, d2);
            //запускаем функцию отправки шагов
            networkManager.sendActivity();
            return null;
        }
    }

    private class WeekNutritionTask extends MyAsyncTask {

        public WeekNutritionTask(DataType d1, DataType d2) {
            super(d1, d2);
        }

        protected Void doInBackground(Void... params) {

            //обновляем набор датасетов в нетворк менеджере
            networkManager.nutritionsSet = displayLastWeekData(mGoogleApiClient, d1, d2);
            //запускаем функцию отправки шагов
            networkManager.sendNutrition();
            return null;
        }
    }

    //Асинхронная таска для записи данных в облако
    private class WriteActivityTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            writeActivity(mGoogleApiClient);
            return null;
        }
    }

}







