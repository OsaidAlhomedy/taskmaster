package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        TextView userName = findViewById(R.id.userNameSettings);
        userName.setText(sharedPreferences.getString("userInfo", ""));

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

    public void signOutUser(View view) {

        Amplify.Auth.signOut(
                () -> {
                    Log.i("AuthQuickstart", "Signed out successfully");
                    Intent intent = new Intent(Settings.this, LogIn.class);
                    startActivity(intent);
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );

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

        sharedPreferences.edit().putString("settingsTeamID", teamName).apply();

    }


}