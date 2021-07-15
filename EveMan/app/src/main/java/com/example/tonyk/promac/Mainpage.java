package com.example.tonyk.promac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Mainpage extends AppCompatActivity {

    GridLayout mainGrid;
    ImageButton logoutBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mainpage);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference("Benutzer/");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String userID = user.getUid();
            mRef.child(userID).child("Email").setValue(email);
            mRef.child(userID).child("UserID").setValue(userID);
        }

        mainGrid = findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
        logoutBtn = findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(Mainpage.this, Startapp.class));
            }
        });

        View dView = getWindow().getDecorView();
        dView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void setSingleEvent(GridLayout mainGrid){
        for(int i = 0; i < mainGrid.getChildCount(); i++){
            CardView cV = (CardView)mainGrid.getChildAt(i);
            final int var = i;
            cV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(var == 0){
                        Intent i = new Intent(Mainpage.this, Profile.class);
                        startActivity(i);
                    }else //noinspection StatementWithEmptyBody
                        if(var == 1){
                        //Intent i = new Intent(Mainpage.this, Messages.class);
                        //startActivity(i);
                    }else if(var == 2){
                        Intent i = new Intent (Mainpage.this, InviteMembers.class);
                        startActivity(i);
                    }else if(var == 3){
                        Intent i = new Intent(Mainpage.this, Search.class);
                        startActivity(i);
                    }else if(var == 4){
                        Intent i = new Intent (Mainpage.this, Events.class);
                        startActivity(i);
                    }else //noinspection StatementWithEmptyBody
                        if(var == 5){
                        //Intent i = new Intent(Mainpage.this, Calender.class);
                        //startActivity(i);
                    }
                }
            });
        }
    }
}