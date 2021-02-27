package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ParameterResetActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String HEIGHT = "height";
    public static final String RATIO = "ratio";
    private float height;
    private float ratio;
    private TextView calHeight;
    private TextView pixRatio;
    private Button resetParamBtn;
    private static final int REQUEST_PICTURE_CAPTURE = 1;
    private String pictureFile;
    private static Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter_reset);
        loadSettings();
        calHeight=findViewById(R.id.calHeight);
        pixRatio=findViewById(R.id.pixRatio);
        resetParamBtn=findViewById(R.id.resetBtn);
        calHeight.setText("Calibration Height: "+ Float.toString(height) + " cm");
        pixRatio.setText("Pixel-Distance Ratio: " + Float.toString(ratio) + " pixels/cm");
        resetParamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent,REQUEST_PICTURE_CAPTURE);
            }
        });
    }
    public void loadSettings(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        height = sharedPreferences.getFloat(HEIGHT,10f);
        ratio = sharedPreferences.getFloat(RATIO,70.5f);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        bmp=(Bitmap)data.getExtras().get("data");
        Intent intent = new Intent(ParameterResetActivity.this,CircleActivity.class);
        startActivity(intent);
    }
    public static Bitmap getBmp(){
        return bmp;
    }
}