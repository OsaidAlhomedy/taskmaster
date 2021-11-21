package com.osaid.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Home extends AppCompatActivity {

    private static final String TAG = Home.class.getName();
    Handler handler;
    List<Task> allTasks = new ArrayList<>();
    private static PinpointManager pinpointManager;

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i(TAG, userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        final String token = task.getResult();
                        Log.d(TAG, "Registering push notifications token =========>>>>>>>>>>>>>>>> " + token);
                        pinpointManager.getNotificationClient().registerDeviceToken(token);
                    });
        }
        return pinpointManager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);

        TextView textTitle = findViewById(R.id.title);
        String string = sharedPreferences.getString("username", "Hello Boys");
        string = string + "'s" + " tasks";
        if (string.equals("'s tasks")) {
            textTitle.setTextSize(22);
            textTitle.setText("Change username in the settings");
        } else {
            textTitle.setText(string);
        }

        Log.i("HOME START", "ON CREATE");

//        awsConfigure();
        createNotificationChannel();
        getPinpointManager(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        TasksAdapter newAdapter = new TasksAdapter(this, null);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(newAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);


        TextView textTitle = findViewById(R.id.title);
        String string = sharedPreferences.getString("username", "Hello Boys");
        string = string + "'s" + " tasks";
        if (string.equals("'s tasks") || string.equals("Hello Boys's tasks")) {
            textTitle.setTextSize(22);
            textTitle.setText("Change username in the settings");
        } else {
            textTitle.setText(string);
        }

        getTasks();



        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        TasksAdapter newAdapter = new TasksAdapter(this, allTasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(newAdapter);

        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                newAdapter.setTaskOGList(allTasks);
                Log.i("Async", allTasks.toString());
                recyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "osaid";
            String description = "nothing to describe";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void teamSeeder(SharedPreferences sharedPreferences) {
        String TAG = "teamSeeder";
        List<Team> teamList = new ArrayList<>();
        teamList.add(Team.builder().name("team1").build());
        teamList.add(Team.builder().name("team2").build());
        teamList.add(Team.builder().name("team3").build());


        Amplify.API.mutate(
                ModelMutation.create(teamList.get(0))
                , success -> {
                    if (success.getData().getId() != null) {
                        Log.i(TAG, "team0 id ====> " + success.getData().getId());
                        sharedPreferences.edit().putString(teamList.get(0).getName(), success.getData().getId()).apply();
                    }
                }, error -> {
                    Log.i(TAG, "team1 id ====> " + error.toString());
                }
        );
        Amplify.API.mutate(
                ModelMutation.create(teamList.get(1))
                , success -> {
                    if (success.getData().getId() != null) {
                        Log.i(TAG, "team1 id ====> " + success.getData().getId());
                        sharedPreferences.edit().putString(teamList.get(1).getName(), success.getData().getId()).apply();
                    }
                }, error -> {
                    Log.i(TAG, "team2 id ====> " + error.toString());
                }
        );
        Amplify.API.mutate(
                ModelMutation.create(teamList.get(2))
                , success -> {
                    if (success.getData().getId() != null) {
                        Log.i(TAG, "team2 id ====> " + success.getData().getId());
                        sharedPreferences.edit().putString(teamList.get(2).getName(), success.getData().getId()).apply();
                    }
                }, error -> {
                    Log.i(TAG, "team3 id ====> " + error.toString());
                }
        );


    }


    private void getTasks() {
        allTasks = new ArrayList<>();
        List<Task> listOfTasks = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        String settingsTeamID = sharedPreferences.getString("settingsTeamID", null);
        Log.i("TeamID", "Settings Team ID ===> " + settingsTeamID);

        if (settingsTeamID == null) {
            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    response -> {
                        for (Task task : response.getData()) {
                            listOfTasks.add(task);
                        }
                        Collections.sort(listOfTasks, new Comparator<Task>() {
                            @Override
                            public int compare(Task task, Task t1) {
                                return Long.compare(task.getCreatedAt().toDate().getTime(), t1.getCreatedAt().toDate().getTime());
                            }
                        });

                        allTasks = listOfTasks;
                        handler.sendEmptyMessage(1);
                    },
                    error -> Log.e("MyAmplifyApp", "Query failure", error)
            );
        } else {
            Amplify.API.query(
                    ModelQuery.list(Task.class, Task.TEAM_ID.contains(settingsTeamID)),
                    response -> {
                        for (Task task : response.getData()) {
                            listOfTasks.add(task);
                        }
                        Collections.sort(listOfTasks, new Comparator<Task>() {
                            @Override
                            public int compare(Task task, Task t1) {
                                return Long.compare(task.getCreatedAt().toDate().getTime(), t1.getCreatedAt().toDate().getTime());
                            }
                        });

                        allTasks = listOfTasks;
                        handler.sendEmptyMessage(1);
                    },
                    error -> Log.e("MyAmplifyApp", "Query failure", error)
            );


        }


    }

    private void eventRecord1() {
        String userName = getSharedPreferences("pref", MODE_PRIVATE).getString("userInfo", "no user info");
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("User Launched Settings")
                .addProperty("UserName", userName)
                .build();

        Amplify.Analytics.recordEvent(event);

    }

    private void eventRecord2() {
        String userName = getSharedPreferences("pref", MODE_PRIVATE).getString("userInfo", "no user info");
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("User Launched Add task activity")
                .addProperty("UserName", userName)
                .build();

        Amplify.Analytics.recordEvent(event);

    }

    private void eventRecord3() {
        String userName = getSharedPreferences("pref", MODE_PRIVATE).getString("userInfo", "no user info");
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("User Launched All tasks activity")
                .addProperty("UserName", userName)
                .build();

        Amplify.Analytics.recordEvent(event);

    }


    public void addTask(View view) {
        Intent intent = new Intent(this, AddTask.class);
        eventRecord2();
        startActivity(intent);

    }

    public void allTask(View view) {
        Intent intent = new Intent(this, AllTasks.class);
        eventRecord3();
        startActivity(intent);

    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        eventRecord1();
        startActivity(intent);
    }

}