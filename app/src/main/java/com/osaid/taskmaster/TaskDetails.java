package com.osaid.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

public class TaskDetails extends AppCompatActivity {


    Handler handler;

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

        String url = intent.getExtras().getString("taskURL");
        ImageView image = findViewById(R.id.imageTaskDetails);

        Picasso.get().load(url).into(image);

    }
}