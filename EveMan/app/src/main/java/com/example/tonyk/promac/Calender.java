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
import android.widget.TextView;

public class Calender extends AppCompatActivity {

    TextView calenderWelcomeText;

    FloatingActionButton fabOpenCalender, fabCalenderToProfile,
            fabCalenderToEditEvent, fabCalenderToMessages, fabCalenderToCreateEvent,
            fabCalenderToSearch, fabCalenderToMenu;
    Animation fab_open,fab_close,rotate_btn_forward,rotate_btn_backward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_calender);

        fabOpenCalender = findViewById(R.id.fabOpenCalender);
        fabCalenderToProfile = findViewById(R.id.fabCalenderToProfile);
        fabCalenderToEditEvent = findViewById(R.id.fabCalenderToEditEvent);
        fabCalenderToMessages = findViewById(R.id.fabCalenderToMessages);
        fabCalenderToCreateEvent = findViewById(R.id.fabCalenderToCreateEvent);
        fabCalenderToSearch = findViewById(R.id.fabCalenderToSearch);
        fabCalenderToMenu = findViewById(R.id.fabCalenderToMenu);

        calenderWelcomeText = findViewById(R.id.calenderWelcomeText);

        fab_open = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotate_btn_backward = AnimationUtils.loadAnimation(this,R.anim.rotate_btn_backward);
        rotate_btn_forward = AnimationUtils.loadAnimation(this,R.anim.rotate_btn_forward);

        fabOpenCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabCalenderToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calender.this, Profile.class);
                startActivity(i);
                animateFab();
            }
        });

        fabCalenderToEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calender.this, Events.class);
                startActivity(i);
                animateFab();
            }
        });

        fabCalenderToMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calender.this, Messages.class);
                startActivity(i);
                animateFab();
            }
        });

        fabCalenderToCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calender.this, CreateEvent.class);
                startActivity(i);
                animateFab();
            }
        });

        fabCalenderToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calender.this, Search.class);
                startActivity(i);
                animateFab();
            }
        });

        fabCalenderToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calender.this, Mainpage.class);
                startActivity(i);
                animateFab();
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

    private void animateFab(){
        if(isOpen){
            fabOpenCalender.startAnimation(rotate_btn_forward);
            fabCalenderToProfile.startAnimation(fab_close);
            fabCalenderToEditEvent.startAnimation(fab_close);
            fabCalenderToMessages.startAnimation(fab_close);
            fabCalenderToCreateEvent.startAnimation(fab_close);
            fabCalenderToSearch.startAnimation(fab_close);
            fabCalenderToMenu.startAnimation(fab_close);
            fabCalenderToProfile.setClickable(false);
            fabCalenderToEditEvent.setClickable(false);
            fabCalenderToMessages.setClickable(false);
            fabCalenderToCreateEvent.setClickable(false);
            fabCalenderToSearch.setClickable(false);
            fabCalenderToMenu.setClickable(false);
            isOpen=false;
        }else{
            fabOpenCalender.startAnimation(rotate_btn_backward);
            fabCalenderToProfile.startAnimation(fab_open);
            fabCalenderToEditEvent.startAnimation(fab_open);
            fabCalenderToMessages.startAnimation(fab_open);
            fabCalenderToCreateEvent.startAnimation(fab_open);
            fabCalenderToSearch.startAnimation(fab_open);
            fabCalenderToMenu.startAnimation(fab_open);
            fabCalenderToProfile.setClickable(true);
            fabCalenderToEditEvent.setClickable(true);
            fabCalenderToMessages.setClickable(true);
            fabCalenderToCreateEvent.setClickable(true);
            fabCalenderToSearch.setClickable(true);
            fabCalenderToMenu.setClickable(true);
            isOpen=true;
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
}