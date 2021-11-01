package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {

    public int tasksNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        TextView tasks = findViewById(R.id.textView6);
        tasksNumbers = Integer.parseInt(tasks.getText().toString());

    }

    public void taskAdd(View view) {
        TextView tasks = findViewById(R.id.textView6);
        tasksNumbers++;
        tasks.setText(String.valueOf(tasksNumbers));
        Toast toast = Toast.makeText(getApplicationContext(),"Submitted !" , Toast.LENGTH_SHORT);
        toast.show();
    }

    public int getTasksNumbers() {
        return tasksNumbers;
    }

    public void setTasksNumbers(int tasksNumbers) {
        this.tasksNumbers = tasksNumbers;
    }
}