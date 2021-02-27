package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

public class SelectImageActivity extends AppCompatActivity {
    private String path = Environment.getExternalStorageDirectory().getPath()+"/Android/data/com.example.myapplication/files/Pictures/";
    private String fileNames[];
    private ListView imageList;
    private static String selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        obtainImageList();
        imageList=findViewById(R.id.imageList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_row, R.id.imgText, fileNames);
        imageList.setAdapter(arrayAdapter);
        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(SelectImageActivity.this, RotateActivity.class);
                selected = path+((TextView) view.findViewById(R.id.imgText)).getText().toString();
                intent.putExtra("imagePath",selected);
                startActivity(intent);
            }
        });
    }
    public static String getImg(){return selected;}
    private void obtainImageList(){
        File files[] = new File(path).listFiles();
        if(files!=null){
            fileNames = new String[files.length];
            for(int i=0;i<files.length;i++) fileNames[i]=files[i].getName();
        }
    }
}
