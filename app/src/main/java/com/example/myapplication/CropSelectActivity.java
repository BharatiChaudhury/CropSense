package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.views.CustomView2;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

public class CropSelectActivity extends AppCompatActivity {
    private Spinner cropType;
    private Spinner growthStage;
    private Button sendBtn;
    private String description;
    private String img_path;
    private ImageView imgView;
    private Bitmap bmp = RotateActivity.getBmp();
    FirebaseAuth fAuth;

    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_select);
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user=fAuth.getCurrentUser();
        String UserId=user.getUid();
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
        progressDialog=new ProgressDialog(CropSelectActivity.this);
        img_path = SelectImageActivity.getImg();
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmp = BitmapFactory.decodeFile(img_path,bmpOptions);
        cropType = findViewById(R.id.cropType);
        growthStage = findViewById(R.id.growthStage);
        sendBtn = findViewById(R.id.sendBtn);
        imgView=(ImageView)findViewById(R.id.imgView);
        imgView.setImageBitmap(bmp);
        ArrayAdapter<CharSequence> cropNames = ArrayAdapter.createFromResource(this, R.array.cropName, android.R.layout.simple_spinner_item);
        cropNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cropType.setAdapter(cropNames);
        ArrayAdapter<CharSequence> cropGrowth = ArrayAdapter.createFromResource(this, R.array.growthStage, android.R.layout.simple_spinner_item);
        cropGrowth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        growthStage.setAdapter(cropGrowth);



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // img_path = SelectImageActivity.getImg();
                //BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
                //bmp = BitmapFactory.decodeFile(img_path,bmpOptions);
                description = SelectParametersActivity.getDist() + " " + SelectParametersActivity.getDegrees() + " " + (CustomView2.getRadius()) + " ";
                List<LatLng> latLngList = PolygonActivity.getLatLngList();
                description += latLngList.size();
                for (int i = 0; i < latLngList.size(); i++)
                    description += " " + latLngList.get(i).latitude + " " + latLngList.get(i).longitude;
                description += " " + cropType.getSelectedItem().toString() + " " + growthStage.getSelectedItem().toString()+img_path;


                //sendEmail();
                upload();

            }
        });
    }


    private void upload(){
        Toast.makeText(this, ""+storageReference.child(img_path), Toast.LENGTH_SHORT).show();
        progressDialog.setTitle("Uploading the Data..");
        progressDialog.show();
        Uri file = Uri.fromFile(new File(img_path));
        StorageReference riversRef = storageReference.child("Pictures/"+file.getLastPathSegment());
        riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        riversRef.putFile(file).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                progressDialog.dismiss();
                Toast.makeText(CropSelectActivity.this, "Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                //upload imageUploadInfo=new upload()
                String ImageUploadId=databaseReference.push().getKey();
                databaseReference.child(ImageUploadId).child("Image_URL").setValue(taskSnapshot.getUploadSessionUri().toString());
                databaseReference.child(ImageUploadId).child("Dist").setValue(SelectParametersActivity.getDist());
                databaseReference.child(ImageUploadId).child("Degrees").setValue(SelectParametersActivity.getDegrees());
                databaseReference.child(ImageUploadId).child("Radius").setValue(CustomView2.getRadius());
                databaseReference.child(ImageUploadId).child("Latitute_and_Longitude").setValue(PolygonActivity.getLatLngList());
                databaseReference.child(ImageUploadId).child("CropType").setValue(cropType.getSelectedItem().toString());
                databaseReference.child(ImageUploadId).child("GrowthStage").setValue(growthStage.getSelectedItem().toString());

            }
        });

    }



    private void sendEmail(){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL,new String[]{"bharatichaudhury@gmail.com"});
        email.setType("*/*");
        email.putExtra(Intent.EXTRA_SUBJECT,"NEW DATA");
        email.putExtra(Intent.EXTRA_TEXT,description);
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }


}