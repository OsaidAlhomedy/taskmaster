package com.osaid.taskmaster;

import androidx.annotation.NonNull;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.regex.Pattern;

public class TaskDetails extends AppCompatActivity implements OnMapReadyCallback {



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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Intent intent = getIntent();
        Log.i("LOCATION DATA", "LOCATION DATA ====> : " + intent.getExtras().getDouble("longitude") + "   " + intent.getExtras().getDouble("latitude"));
        LatLng loc = new LatLng( intent.getExtras().getDouble("latitude"),intent.getExtras().getDouble("longitude"));
        googleMap.addMarker(new MarkerOptions()
                .position(loc)
                .title("The task location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }
}