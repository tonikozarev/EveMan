package com.example.tonyk.promac;

import android.content.Intent;
import android.widget.ImageButton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class Startapp extends AppCompatActivity {

    private ImageButton goToLogin;
    private ImageButton goToRegistration;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_startapp);

        mAuth = FirebaseAuth.getInstance();

        goToLogin = findViewById(R.id.goToLogin);
        goToRegistration = findViewById(R.id.goToRegistration);


        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton a = (ImageButton) view;
                a.setEnabled(false);

                Runnable r = new Runnable(){
                    @Override
                    public void run(){
                        long future = System.currentTimeMillis() + 2500;
                        while(System.currentTimeMillis() < future){
                            synchronized (this){
                                try{
                                    wait(future - System.currentTimeMillis());
                                }catch(Exception e){
                                    //...
                                }
                            }
                        }
                        goToLogin.setEnabled(true);
                    }
                };
                Thread rThread = new Thread(r);
                rThread.start();

                Intent login = new Intent(Startapp.this, Login.class);
                startActivity(login);
            }
        });



        goToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton a = (ImageButton) view;
                a.setEnabled(false);

                Runnable r = new Runnable(){
                    @Override
                    public void run(){
                        long future = System.currentTimeMillis() + 2500;
                        while(System.currentTimeMillis() < future){
                            synchronized (this){
                                try{
                                    wait(future - System.currentTimeMillis());
                                }catch(Exception e){
                                    //...
                                }
                            }
                        }
                        goToRegistration.setEnabled(true);
                    }
                };
                Thread rThread = new Thread(r);
                rThread.start();

                Intent register = new Intent(Startapp.this, Register.class);
                startActivity(register);
            }
        });

    }

    public void refreshUI() {
        final View v = getWindow().getDecorView();
        v.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    v.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, Mainpage.class));
        }
    }

}
