package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TaskDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        Intent intent = getIntent();

        TextView title = findViewById(R.id.taskTitle);
        title.setText(intent.getExtras().getString("taskTitle"));

        TextView body = findViewById(R.id.taskBody);
        body.setText(intent.getExtras().getString("taskBody"));

        TextView state = findViewById(R.id.taskState);
        state.setText(intent.getExtras().getString("taskState"));
    }
}