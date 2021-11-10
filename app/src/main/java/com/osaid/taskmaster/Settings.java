package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Settings extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }

    public void saveUser(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        TextView username = findViewById(R.id.username);
        String userNameText = username.getText().toString();

        editor.putString("username", userNameText);
        editor.apply();
        getTeamName();

    }

    private void getTeamName() {

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);

        RadioGroup radioGroup = findViewById(R.id.radioGroup2);
        RadioButton radioButton1 = findViewById(R.id.radioButton6);
        RadioButton radioButton2 = findViewById(R.id.radioButton5);
        RadioButton radioButton3 = findViewById(R.id.radioButton4);
        int radioButtonId = radioGroup.getCheckedRadioButtonId();

        String teamName;
        if (radioButtonId == radioButton1.getId())
            teamName = "6733d5da-4581-455d-8425-ef87c95f434d";
        else if (radioButtonId == radioButton2.getId())
            teamName = "763c929c-f1df-48df-87f5-6a06907b5d99";
        else if (radioButtonId == radioButton3.getId())
            teamName = "b86a6c7f-1c51-4c06-9a09-000691d92bb8";
        else teamName = null;

        sharedPreferences.edit().putString("settingsTeamID",teamName).apply();

    }


}