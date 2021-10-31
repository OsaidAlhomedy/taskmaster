package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity2 extends AppCompatActivity {

    public int tasksNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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