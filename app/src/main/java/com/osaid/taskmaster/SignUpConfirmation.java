package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

public class SignUpConfirmation extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirmation);

        TextView wrongCode = findViewById(R.id.wrongCode);
        handler = new Handler(Looper.myLooper(), message -> {
            wrongCode.setVisibility(View.VISIBLE);
            return false;
        });
    }

    public void verifyUser(View view) {

        String userName = getIntent().getStringExtra("userName");
        EditText confirmCodeView = findViewById(R.id.confirmCodeText);
        String code = confirmCodeView.getText().toString();


        Amplify.Auth.confirmSignUp(
                userName,
                code,
                result -> {
                    Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    Intent intent = new Intent(SignUpConfirmation.this, LogIn.class);
                    startActivity(intent);
                },
                error -> {
                    Log.e("AuthQuickstart", error.toString());
                    handler.sendEmptyMessage(1);
                }
        );
    }
}