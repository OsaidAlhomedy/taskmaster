package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {


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


//        List<Task> taskArrayList = new ArrayList<>();
        List<Task> taskArrayList = AppDB.getInstance(this).taskDAO().getAllTasks();

//        taskArrayList.add(new Task("Wake Up", "Grab a brush and put a little make-up", "complete"));
//        taskArrayList.add(new Task("Turn Pc On", "Hide the scars to fade away the shake-up", "complete"));
//        taskArrayList.add(new Task("Study All Day", "Why'd you leave the keys upon the table?", "new"));
//        taskArrayList.add(new Task("Study All Night", "Here you go create another fable", "new"));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new TasksAdapter(this,taskArrayList));

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