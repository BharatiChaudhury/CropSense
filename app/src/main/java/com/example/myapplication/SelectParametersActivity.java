package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.myapplication.ParameterResetActivity.HEIGHT;
import static com.example.myapplication.ParameterResetActivity.RATIO;
import static com.example.myapplication.ParameterResetActivity.SHARED_PREFS;

public class SelectParametersActivity extends AppCompatActivity {
    public static float height,ratio;
    private static float dist=20f;
    private static float degrees=8f;
    private TextView calHeight;
    private TextView calRatio;
    private RadioButton dist20;
    private RadioButton dist50;
    private RadioButton dist100;
    private RadioButton deg8;
    private RadioButton deg10;
    private RadioButton deg25;
    private Button resBtn;
    private static float imgRad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_parameters);
        loadSettings();
        calHeight = (TextView)findViewById(R.id.calibHeight);
        calRatio = (TextView)findViewById(R.id.imgPix);
        dist20 = (RadioButton)findViewById(R.id.dist20);
        dist50 = (RadioButton)findViewById(R.id.dist50);
        dist100 = (RadioButton)findViewById(R.id.dist100);
        deg8 = (RadioButton)findViewById(R.id.ang1);
        deg10 = (RadioButton)findViewById(R.id.ang2);
        deg25 = (RadioButton)findViewById(R.id.ang3);
        resBtn = (Button)findViewById(R.id.resBtn);
        calHeight.setText("Calibration Height: "+ Float.toString(height) + " cm");
        calRatio.setText("Pixel-Distance Ratio: " + Float.toString(ratio) + " pixels/cm");
        dist20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dist=20f;
            }
        });
        dist50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dist=50f;
            }
        });
        dist100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dist=100f;
            }
        });
        deg8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees = 8f;
            }
        });
        deg10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees = 10f;
            }
        });
        deg25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees = 25f;
            }
        });
        resBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgRad=height*ratio*(float)Math.tan(Math.toRadians(degrees/2));
                Intent intent = new Intent(getApplicationContext(), DrawCircleActivity.class);
                startActivity(intent);
            }
        });
    }
    public void loadSettings(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        height = sharedPreferences.getFloat(HEIGHT,10f);
        ratio = sharedPreferences.getFloat(RATIO,70.5f);
    }
    public static float getImgRad(){
        return imgRad;
    }
    public static float getDist(){
        return dist;
    }
    public static float getDegrees(){
        return degrees;
    }
}