package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

public class CircleActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bmp;
    private RadioButton centerBtn;
    private RadioButton radiusBtn;
    private Button nextBtn;
    private static int ClickOpt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        imageView=findViewById(R.id.imageView);
        centerBtn=(RadioButton)findViewById(R.id.radBtn1);
        radiusBtn=(RadioButton)findViewById(R.id.radBtn2);
        nextBtn=(Button)findViewById(R.id.nextBtn);
        bmp = ParameterResetActivity.getBmp();
        imageView.setImageBitmap(bmp);
        ClickOpt=0;
        centerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickOpt=0;
            }
        });
        radiusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickOpt=1;
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CircleActivity.this,CalibrateActivity.class);
                startActivity(intent);
            }
        });
    }
    public static int getClickOpt(){
        return ClickOpt;
    }

}