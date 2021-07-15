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

public class Messages extends AppCompatActivity {

    TextView messagesWelcomeText;

    FloatingActionButton fabOpenMessages, fabMessagesToProfile,
            fabMessagesToSearch, fabMessagesToCreateEvent, fabMessagesToEditEvent,
            fabMessagesToCalender,fabMessagesToMenu;
    Animation fab_open,fab_close,rotate_btn_forward,rotate_btn_backward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_messages);

        fabOpenMessages = findViewById(R.id.fabOpenMessages);
        fabMessagesToProfile = findViewById(R.id.fabMessagesToProfile);
        fabMessagesToSearch = findViewById(R.id.fabMessagesToSearch);
        fabMessagesToCreateEvent = findViewById(R.id.fabMessagesToCreateEvent);
        fabMessagesToEditEvent = findViewById(R.id.fabMessagesToEditEvent);
        fabMessagesToCalender = findViewById(R.id.fabMessagesToCalender);
        fabMessagesToMenu = findViewById(R.id.fabMessagesToMenu);

        messagesWelcomeText = findViewById(R.id.messagesWelcomeText);

        fab_open = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotate_btn_backward = AnimationUtils.loadAnimation(this,R.anim.rotate_btn_backward);
        rotate_btn_forward = AnimationUtils.loadAnimation(this,R.anim.rotate_btn_forward);

        fabOpenMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabMessagesToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Messages.this, Profile.class);
                startActivity(i);
                animateFab();
            }
        });

        fabMessagesToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Messages.this, Search.class);
                startActivity(i);
                animateFab();
            }
        });

        fabMessagesToCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Messages.this, CreateEvent.class);
                startActivity(i);
                animateFab();
            }
        });

        fabMessagesToEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Messages.this, Events.class);
                startActivity(i);
                animateFab();
            }
        });

        fabMessagesToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Messages.this, Calender.class);
                startActivity(i);
                animateFab();
            }
        });

        fabMessagesToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Messages.this, Mainpage.class);
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
            fabOpenMessages.startAnimation(rotate_btn_forward);
            fabMessagesToProfile.startAnimation(fab_close);
            fabMessagesToSearch.startAnimation(fab_close);
            fabMessagesToCreateEvent.startAnimation(fab_close);
            fabMessagesToEditEvent.startAnimation(fab_close);
            fabMessagesToCalender.startAnimation(fab_close);
            fabMessagesToMenu.startAnimation(fab_close);
            fabMessagesToProfile.setClickable(false);
            fabMessagesToSearch.setClickable(false);
            fabMessagesToCreateEvent.setClickable(false);
            fabMessagesToEditEvent.setClickable(false);
            fabMessagesToCalender.setClickable(false);
            fabMessagesToMenu.setClickable(false);
            isOpen=false;
        }else{
            fabOpenMessages.startAnimation(rotate_btn_backward);
            fabMessagesToProfile.startAnimation(fab_open);
            fabMessagesToSearch.startAnimation(fab_open);
            fabMessagesToCreateEvent.startAnimation(fab_open);
            fabMessagesToEditEvent.startAnimation(fab_open);
            fabMessagesToCalender.startAnimation(fab_open);
            fabMessagesToMenu.startAnimation(fab_open);
            fabMessagesToProfile.setClickable(true);
            fabMessagesToSearch.setClickable(true);
            fabMessagesToCreateEvent.setClickable(true);
            fabMessagesToEditEvent.setClickable(true);
            fabMessagesToCalender.setClickable(true);
            fabMessagesToMenu.setClickable(true);
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