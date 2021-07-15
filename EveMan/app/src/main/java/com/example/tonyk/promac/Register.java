package com.example.tonyk.promac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText etEmail, etPass, etConfirmPass;
    Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.regEmail);
        etPass = findViewById(R.id.regPass);
        etConfirmPass = findViewById(R.id.regConfirmPass);
        regBtn = findViewById(R.id.regBtn);
        progressBar = findViewById(R.id.regProgressBar);

        regBtn.setOnClickListener(this);

        View dView = getWindow().getDecorView();
        dView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void registerUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String confirmPass = etConfirmPass.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email address is required!");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Enter a valid email!");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPass.setError("Password is required!");
            etPass.requestFocus();
            return;
        }

        if(password.length() < 6){
            etPass.setError("The password should be at least 6 characters!");
            etPass.requestFocus();
            return;
        }

        if(password.equals(confirmPass)){
            regBtn.setClickable(false);
            regBtn.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        finish();
                        Toast.makeText(getApplicationContext(), "Your registration was successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Mainpage.class));
                        regBtn.setClickable(true);
                        regBtn.setEnabled(true);
                    }else{
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(), "Email is already taken!", Toast.LENGTH_SHORT).show();
                            regBtn.setClickable(true);
                            regBtn.setEnabled(true);
                        }else {
                            Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                            regBtn.setClickable(true);
                            regBtn.setEnabled(true);
                        }
                    }
                }
            });
        }else{
            etPass.setError("The passwords do not match!");
            etPass.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.regBtn:
                registerUser();
                break;
        }
    }
}
