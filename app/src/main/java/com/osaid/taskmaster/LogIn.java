package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.util.prefs.Preferences;

public class LogIn extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        awsConfigure();

        Toast toast = Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_LONG);
        handler = new Handler(Looper.myLooper(), message -> {
            toast.show();
            return false;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void userSignIn(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref",MODE_PRIVATE);
        EditText userNameView = findViewById(R.id.usernameSignIn);
        String userName = userNameView.getText().toString();
        EditText passwordView = findViewById(R.id.passwordSignin);
        String password = passwordView.getText().toString();

        Amplify.Auth.signIn(
                userName,
                password,
                result -> {
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                    sharedPreferences.edit().putString("userInfo",userName).apply();
                    Intent intent = new Intent(LogIn.this, Home.class);
                    startActivity(intent);
                },
                error -> {
                    Log.e("AuthQuickstart", error.toString());
                    handler.sendEmptyMessage(1);
                }
        );
    }

    public void goToRegisterUser(View view){
        Intent intent = new Intent(LogIn.this, SignUp.class);
        startActivity(intent);
    }

    private void awsConfigure() {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

}