package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

public class AddTask extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        TextView tasks = findViewById(R.id.textView6);
        int tasksNumbers = AppDB.getInstance(this).taskDAO().getAllTasks().size();
        tasks.setText(String.valueOf(tasksNumbers));

    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tasks = findViewById(R.id.textView6);
        int tasksNumbers = AppDB.getInstance(this).taskDAO().getAllTasks().size();
        tasks.setText(String.valueOf(tasksNumbers));
    }

    public void taskAdd(View view) {
        String TAG = "AddTaskActivity";


        EditText taskTitleEditText = findViewById(R.id.taskTitleEditText);
        String title = taskTitleEditText.getText().toString();

        EditText taskBodyEditText = findViewById(R.id.taskBodyEditText);
        String body = taskBodyEditText.getText().toString();

        TaskOG newTaskOG = new TaskOG(title, body, "new");

        Log.d(TAG, "taskAdd: " + title + " , " + body);

        AppDB.getInstance(AddTask.this).taskDAO().insertTask(newTaskOG);

        Task newTask = Task.builder()
                .title(title)
                .body(body)
                .state("new")
                .build();

        Amplify.API.mutate(
                ModelMutation.create(newTask),
                response -> Log.i("MyAmplifyApp", "Task Added Successfully: " + response.getData().getId()),
                error -> Log.e("MyAmplifyApp", "Task failed Successfully", error)
        );


        TextView tasks = findViewById(R.id.textView6);
        int tasksNumbers = AppDB.getInstance(this).taskDAO().getAllTasks().size();
        tasks.setText(String.valueOf(tasksNumbers));

        Toast toast = Toast.makeText(getApplicationContext(), "Submitted !", Toast.LENGTH_SHORT);

        toast.show();

    }

}