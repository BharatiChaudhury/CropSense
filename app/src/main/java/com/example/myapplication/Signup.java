package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    EditText mname,musername,mpwd,mcpwd,minstname,mdesignation,mexpert,mexpno,mphno;
    Button mregisterBtn;
    TextView mregstd;
    FirebaseAuth fAuth;
    RadioButton madvanced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mname = findViewById(R.id.name);
        musername = findViewById(R.id.username);
        mpwd = findViewById(R.id.pwd);
        mcpwd = findViewById(R.id.cpwd);
        minstname = findViewById(R.id.instname);
        mdesignation = findViewById(R.id.designation);
        mexpert = findViewById(R.id.expert);
        mexpno = findViewById(R.id.expno);
        mphno = findViewById(R.id.phno);
        mregisterBtn = findViewById(R.id.regstrBtn);
        mregstd = findViewById(R.id.regstd);
        madvanced = findViewById(R.id.advanced);
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
//        mname = findViewById(R.id.name);
//        musername = findViewById(R.id.username);
//        mpwd = findViewById(R.id.pwd);
//        mcpwd = findViewById(R.id.cpwd);
//        minstname = findViewById(R.id.instname);
//        mdesignation = findViewById(R.id.designation);
//        mexpert = findViewById(R.id.expert);
//        mexpno = findViewById(R.id.expno);
//        mphno = findViewById(R.id.phno);
//        mregisterBtn = findViewById(R.id.regstrBtn);
//        mregstd = findViewById(R.id.regstd);
//        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=musername.getText().toString().trim();
                String passwd=mpwd.getText().toString().trim();
                String confirmpassword=mcpwd.getText().toString().trim();
                final String Institute=minstname.getText().toString().trim();
                final String Designation=mdesignation.getText().toString().trim();
                final String Expert=mexpert.getText().toString().trim();
                final String YOE=mexpno.getText().toString().trim();
                final String Mobile=mphno.getText().toString().trim();
                final boolean advanced = madvanced.isSelected();
                if(TextUtils.isEmpty(email)){
                    musername.setError("EmailId is required");
                    return;
                }

                if(TextUtils.isEmpty(passwd)){
                    mpwd.setError("Password is required");
                    return;
                }
                if (passwd.length()<6){
                    mpwd.setError("Length of Password should be >=6");
                    return;
                }

                else if(TextUtils.isEmpty(confirmpassword)){
                    mcpwd.setError("Enter Password Again");
                    return;
                }
                else if(TextUtils.isEmpty(Institute)){
                    minstname.setError("Institute Name is required");
                    return;
                }

                else if(TextUtils.isEmpty(Designation)){
                    mdesignation.setError("Designation is required");
                    return;
                }
                else if(TextUtils.isEmpty(Expert)){
                    mexpert.setError("Area of Expertise is required");
                    return;
                }
                else if(TextUtils.isEmpty(YOE)){
                    mexpno.setError("Years of Experience is required");
                    return;
                }
                else if(TextUtils.isEmpty(Mobile)){
                    mphno.setError("Mobile Number is required");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            User user = new User(
                                    email,
                                    Institute,
                                    Designation,
                                    Expert,
                                    YOE,
                                    Mobile,
                                    advanced
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Signup.this, "Registration Successful ", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            });
                        }
                        else{
                            Toast.makeText(Signup.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });
        mregstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

    }
}
