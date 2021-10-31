package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void taskAdd(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),"Submitted !" , Toast.LENGTH_SHORT);
        toast.show();
    }
}