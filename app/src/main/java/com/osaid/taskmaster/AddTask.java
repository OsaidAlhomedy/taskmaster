package com.osaid.taskmaster;

import static android.content.Intent.ACTION_PICK;
import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.commons.io.FilenameUtils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.aws.AWSApiPluginConfiguration;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.onimur.handlepathoz.HandlePathOz;
import br.com.onimur.handlepathoz.HandlePathOzListener;
import br.com.onimur.handlepathoz.model.PathOz;

public class AddTask extends AppCompatActivity implements HandlePathOzListener.SingleUri {

    private static final int REQUEST_OPEN_GALLERY = 920;
    private static final int REQUEST_PERMISSION = 123;
    private static final String TAG = "AddTask";
    private HandlePathOz handlePathOz;
    private Uri uriGlobal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        awsConfigure();

        TextView tasks = findViewById(R.id.textView6);
        int tasksNumbers = AppDB.getInstance(this).taskDAO().getAllTasks().size();
        tasks.setText(String.valueOf(tasksNumbers));
        handlePathOz = new HandlePathOz(this, this);

        Button button = findViewById(R.id.attachFileBtn);
        button.setOnClickListener((view) -> {
            openFile();
        });

        if (getIntent().getClipData() != null) {
            uriGlobal = getIntent().getClipData().getItemAt(0).getUri();
            handlePathOz.getRealPath(uriGlobal);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tasks = findViewById(R.id.textView6);
        int tasksNumbers = AppDB.getInstance(this).taskDAO().getAllTasks().size();
        tasks.setText(String.valueOf(tasksNumbers));
    }

    public void showToast(String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView uploadedFileView = findViewById(R.id.uploadedFileName);
                uploadedFileView.setText(name);
                uploadedFileView.setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), "The file has been added to the task", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    ////////////////////////////////////////////////////////////

    public void openFile() {
        if (checkSelfPermission()) {
            Intent intent;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                intent = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
            } else {
                intent = new Intent(ACTION_PICK, INTERNAL_CONTENT_URI);
            }

            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra("return-data", true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(intent, REQUEST_OPEN_GALLERY);
        }
    }

    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                //set Uri to handle
                handlePathOz.getRealPath(uri);
                //show Progress Loading
            }
        }
    }

    @Override
    public void onRequestHandlePathOz(@NonNull PathOz pathOz, @Nullable Throwable throwable) {
        if (throwable != null) {
            Log.e(TAG, "onRequestHandlePathOz: ERRRRROR");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);

        Log.i(TAG, "onRequestHandlePathOz: ====> URI === " + pathOz.getPath());

        String fileName = FilenameUtils.getName(pathOz.getPath());
        Log.i(TAG, "onRequestHandlePathOz: ===============> " + fileName);
        File file = new File(pathOz.getPath());

        Amplify.Storage.uploadFile(
                fileName,
                file,
                uploadFileResult -> {
                    Log.i("MyAmplifyApp", "Successfully uploaded: " + uploadFileResult.getKey());
                    Amplify.Storage.getUrl(
                            uploadFileResult.getKey(),
                            result -> {
                                Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                                sharedPreferences.edit().putString("fileUrl", result.getUrl().toString()).apply();
                                showToast(fileName);
                                uriGlobal = null;
                            },
                            error -> Log.e("MyAmplifyApp", "URL generation failure", error)
                    );
                },
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFile();
            } else {
                //TODO("show Message to the user")
                Log.i(TAG, "onRequestPermissionsResult: ERRRORRRRR =>>>>> PERMISSION FIELD");
            }
        }
    }

    /////////////////////////////////////////////////////////////////

    public void taskAdd(View view) {
        String TAG = "AddTaskActivity";
        EditText taskTitleEditText = findViewById(R.id.taskTitleEditText);
        String title = taskTitleEditText.getText().toString();

        EditText taskBodyEditText = findViewById(R.id.taskBodyEditText);
        String body = taskBodyEditText.getText().toString();

        String teamID = getTeamName();

        Log.i(TAG, "taskAdd: " + title + " , " + body + " , " + teamID);


//        TaskOG newTaskOG = new TaskOG(title, body, "new");
//        AppDB.getInstance(AddTask.this).taskDAO().insertTask(newTaskOG);

        String url = getSharedPreferences("myPref", MODE_PRIVATE).getString("fileUrl", "No File");
        Task newTask = Task.builder().teamId(teamID)
                .title(title)
                .body(body)
                .state("new")
                .fileUrl(url)
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

    private void awsConfigure() {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin(getApplication()));
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }


}