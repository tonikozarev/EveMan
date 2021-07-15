package com.example.tonyk.promac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInfo extends AppCompatActivity {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRefB = mDatabase.getReference("Benutzer/");

    String userEmail;
    TextView userInfoEmail, userInfoName, userInfoTelephoneText, userInfoLeitzeichenText,
            userInfoDepartmentText, userInfoRoomText, userInfoAdditionalText;
    ImageButton userInfoCloseBtn;

    FloatingActionButton fabOpenUserInfo, fabUserInfoToMenu;
    Animation fab_open, fab_close, rotate_btn_forward, rotate_btn_backward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_user_info);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) userEmail = bundle.getString("UserEmail");

        fabUserInfoToMenu = findViewById(R.id.fabUserInfoToMenu);
        fabOpenUserInfo = findViewById(R.id.fabOpenUserInfo);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_btn_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_backward);
        rotate_btn_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_forward);

        //Photo is an example. No update to the photo is possible yet!

        userInfoEmail = findViewById(R.id.userInfoEmail);
        userInfoName = findViewById(R.id.userInfoName);
        userInfoTelephoneText = findViewById(R.id.userInfoTelephoneText);
        userInfoLeitzeichenText = findViewById(R.id.userInfoLeitzeichenText);
        userInfoDepartmentText = findViewById(R.id.userInfoDepartmentText);
        userInfoRoomText = findViewById(R.id.userInfoRoomText);
        userInfoAdditionalText = findViewById(R.id.userInfoAdditionalText);
        userInfoCloseBtn = findViewById(R.id.userInfoCloseBtn);

        loadUserInfo();

        userInfoCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInfo.this, Search.class);
                startActivity(i);
            }
        });

        fabOpenUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabUserInfoToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInfo.this, Mainpage.class);
                startActivity(i);
                animateFab();
            }
        });
    }

    private void loadUserInfo() {
        mRefB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    String emailUser = ds.child("Email").getValue(String.class);
                    assert emailUser != null;
                    if(emailUser.equals(userEmail)){

                        String email = ds.child("Email").getValue(String.class);
                        userInfoEmail.setText(email);
                        String name = ds.child("Name").getValue(String.class);
                        userInfoName.setText(name);
                        String leitzeichen = ds.child("Leitzeichen").getValue(String.class);
                        userInfoLeitzeichenText.setText(leitzeichen);
                        String room = ds.child("Raum").getValue(String.class);
                        userInfoRoomText.setText(room);
                        String tel = ds.child("Telefon").getValue(String.class);
                        userInfoTelephoneText.setText(tel);
                        String department = ds.child("Abteilung").getValue(String.class);
                        userInfoDepartmentText.setText(department);
                        String info = ds.child("Additional Info").getValue(String.class);
                        userInfoAdditionalText.setText(info);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void refreshUI() {
        final View v = getWindow().getDecorView();
        v.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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

    private void animateFab() {
        if (isOpen) {
            fabOpenUserInfo.startAnimation(rotate_btn_forward);
            fabUserInfoToMenu.startAnimation(fab_close);
            fabUserInfoToMenu.setClickable(false);
            isOpen = false;
        } else {
            fabOpenUserInfo.startAnimation(rotate_btn_backward);
            fabUserInfoToMenu.startAnimation(fab_open);
            fabUserInfoToMenu.setClickable(true);
            isOpen = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return (id == R.id.action_settings) || super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        // user can only go back with the additional back button or use the menu to go back.
    }
}