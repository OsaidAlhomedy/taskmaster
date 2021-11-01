package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void taskDetails(View view){
        int id = view.getId();
        Button button = findViewById(id);
        String text = button.getText().toString();
        Intent intent = new Intent(this,TaskDetails.class);
        intent.putExtra("taskTitle",text);
        startActivity(intent);
    }

}