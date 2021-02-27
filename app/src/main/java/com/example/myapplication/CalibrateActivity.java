package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.views.CustomView;

import static com.example.myapplication.ParameterResetActivity.HEIGHT;
import static com.example.myapplication.ParameterResetActivity.RATIO;
import static com.example.myapplication.ParameterResetActivity.SHARED_PREFS;

public class CalibrateActivity extends AppCompatActivity {
    private EditText heightData;
    private EditText radData;
    private Button calibrateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
        heightData=(EditText)findViewById(R.id.editHeight);
        radData=(EditText)findViewById(R.id.editRadius);
        calibrateBtn=(Button)findViewById(R.id.calibrateBtn);
        calibrateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ht=Float.parseFloat(heightData.getText().toString());
                float radRadius = CustomView.getRadRadius();
                float radius = Float.parseFloat(radData.getText().toString());
                float radRatio =  radRadius/radius;
                saveSettings(ht,radRatio);
                Intent intent = new Intent(CalibrateActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void saveSettings(float height, float ratio){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(HEIGHT,height);
        editor.putFloat(RATIO,ratio);
        editor.apply();
        Toast.makeText(this,"Camera Calibration Done!", Toast.LENGTH_SHORT).show();
    }
}
