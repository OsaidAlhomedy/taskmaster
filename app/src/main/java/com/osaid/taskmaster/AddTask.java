package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

public class AddTask extends AppCompatActivity {

    Handler handler;


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
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);

        EditText taskTitleEditText = findViewById(R.id.taskTitleEditText);
        String title = taskTitleEditText.getText().toString();

        EditText taskBodyEditText = findViewById(R.id.taskBodyEditText);
        String body = taskBodyEditText.getText().toString();

        String teamID = getTeamName();

        Log.i(TAG, "taskAdd: " + title + " , " + body + " , " + teamID);


//        TaskOG newTaskOG = new TaskOG(title, body, "new");



//        AppDB.getInstance(AddTask.this).taskDAO().insertTask(newTaskOG);

        handler = new Handler(Looper.myLooper(), message -> {

            return false;
        });


        Task newTask = Task.builder().teamId(teamID)
                .title(title)
                .body(body)
                .state("new")
                .build();


        Amplify.API.mutate(
                ModelMutation.create(newTask),
                response -> Log.i("MyAmplifyApp", "Task Added Successfully: " + response.getData().getId()),
                error -> Log.e("MyAmplifyApp", "Task failed Successfully", error)
        );


//        TextView tasks = findViewById(R.id.textView6);
//        int tasksNumbers = AppDB.getInstance(this).taskDAO().getAllTasks().size();
//        tasks.setText(String.valueOf(tasksNumbers));
//
        Toast toast = Toast.makeText(getApplicationContext(), "Task Added !", Toast.LENGTH_SHORT);

        toast.show();

    }

    private String getTeamName() {

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButton1 = findViewById(R.id.radioButton);
        RadioButton radioButton2 = findViewById(R.id.radioButton3);
        RadioButton radioButton3 = findViewById(R.id.radioButton2);
        int radioButtonId = radioGroup.getCheckedRadioButtonId();

        String teamName;
        if (radioButtonId == radioButton1.getId())
            teamName = "6733d5da-4581-455d-8425-ef87c95f434d";
        else if (radioButtonId == radioButton2.getId())
            teamName = "763c929c-f1df-48df-87f5-6a06907b5d99";
        else if (radioButtonId == radioButton3.getId())
            teamName = "b86a6c7f-1c51-4c06-9a09-000691d92bb8";
        else teamName = null;

        return teamName;
    }

}