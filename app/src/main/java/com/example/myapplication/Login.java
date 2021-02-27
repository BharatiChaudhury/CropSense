package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText musername,mpwd;
    Button mloginBtn;
    ProgressBar mprog;
    FirebaseAuth fAuth;
    TextView mcreatebtn,mfpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        musername=findViewById(R.id.username);
        mpwd=findViewById(R.id.pwd);
        mloginBtn=findViewById(R.id.loginBtn);
        mprog=findViewById(R.id.prog);
        mfpwd=findViewById(R.id.fpwd);
        mcreatebtn=findViewById(R.id.reg);
        fAuth = FirebaseAuth.getInstance();

        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=musername.getText().toString().trim();
                String passwd=mpwd.getText().toString().trim();
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

                mprog.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();

                        }
                        else{
                            Toast.makeText(Login.this, "Username or Password is incorrect"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mprog.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });
        mfpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=musername.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    musername.setError("Enter your emailID");
                    return;
                }
                fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Login.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        mcreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
                finish();
            }
        });
    }
}
