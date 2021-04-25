package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RotateActivity extends AppCompatActivity {
    private static Bitmap bmp;
    private CheckBox rot180;
    private CheckBox rot90;
    private CheckBox transpose;
    private ImageView imgView;
    private Button nextBtn;
    private static String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate);
        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");
        Toast.makeText(this,imagePath,Toast.LENGTH_SHORT).show();
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmp = BitmapFactory.decodeFile(imagePath,bmpOptions);
        rot90=(CheckBox)findViewById(R.id.rot90);
        rot180=(CheckBox)findViewById(R.id.rot180);
        transpose=(CheckBox)findViewById(R.id.rottrans);
        imgView=(ImageView)findViewById(R.id.imgView);
        nextBtn=(Button)findViewById(R.id.nextBtn);
        imgView.setImageBitmap(bmp);
        rot90.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) Rotate(90f);
                else Rotate(-90f);
                imgView.setImageBitmap(bmp);
            }
        });
        rot180.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) Rotate(180f);
                else Rotate(-180f);
                imgView.setImageBitmap(bmp);
            }
        });
        transpose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Rotate(90f);
                    Flip();
                }
                else{
                    Flip();
                    Rotate(-90f);
                }
                imgView.setImageBitmap(bmp);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RotateActivity.this,SelectParametersActivity.class);
                startActivity(intent);
            }
        });
    }
    private void Rotate(float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }
    private void Flip(){
        Matrix matrix = new Matrix();
        float cx = bmp.getWidth()/2;
        float cy = bmp.getHeight()/2;
        matrix.postScale(-1f,1f,cx,cy);
        bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
    }
    public static Bitmap getBmp(){
        return bmp;
    }
    public static String getImagePath() {return imagePath;}
}
