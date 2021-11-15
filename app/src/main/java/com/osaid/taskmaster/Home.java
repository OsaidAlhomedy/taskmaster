package com.osaid.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Home extends AppCompatActivity {

    Handler handler;
    List<Task> allTasks = new ArrayList<>();


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


    private void awsConfigure() {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

    public void addTask(View view) {
        Intent intent = new Intent(this, AddTask.class);
        startActivity(intent);

    }

    public void allTask(View view) {
        Intent intent = new Intent(this, AllTasks.class);
        startActivity(intent);

    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void taskDetails(String title) {
        Intent intent = new Intent(Home.this, TaskDetails.class);
        intent.putExtra("taskTitle", title);
        startActivity(intent);
    }

}