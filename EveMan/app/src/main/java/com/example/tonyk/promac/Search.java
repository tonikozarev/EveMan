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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRefB = mDatabase.getReference("Benutzer/");

    SearchView searchUser;
    ListView searchUserListView;

    String userEmail;
    ArrayAdapter<String> adapter;
    ArrayList<String> allUsersList = new ArrayList<>();

    FloatingActionButton fabOpenSearch, fabSearchToProfile,
            fabSearchToEditEvent, fabSearchToCreateEvent, fabSearchToMenu;
    Animation fab_open,fab_close,rotate_btn_forward,rotate_btn_backward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_search);

        fabOpenSearch = findViewById(R.id.fabOpenSearch);
        fabSearchToProfile = findViewById(R.id.fabSearchToProfile);
        fabSearchToEditEvent = findViewById(R.id.fabSearchToEditEvent);
        fabSearchToCreateEvent = findViewById(R.id.fabSearchToCreateEvent);
        fabSearchToMenu = findViewById(R.id.fabSearchToMenu);

        searchUser = findViewById(R.id.searchUser);
        searchUserListView = findViewById(R.id.searchUserListView);

        fab_open = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotate_btn_backward = AnimationUtils.loadAnimation(this,R.anim.rotate_btn_backward);
        rotate_btn_forward = AnimationUtils.loadAnimation(this,R.anim.rotate_btn_forward);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, allUsersList);
        searchUserListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        searchUserListView.setItemChecked(1, true);
        searchUserListView.setAdapter(adapter);

        searchUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int nr = 0; nr < allUsersList.size(); nr++) {
                    userEmail = searchUserListView.getItemAtPosition(i).toString();
                    if (nr == i) {
                        Intent intent = new Intent(Search.this, UserInfo.class);
                        intent.putExtra("UserEmail", userEmail);
                        startActivityForResult(intent, 9624);
                        break;
                    }
                }
            }
        });

        mRefB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.child("Email").getValue(String.class);
                    allUsersList.add(email);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nText) {
                adapter.getFilter().filter(nText);
                return false;
            }
        });

        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUser.setIconified(false);
            }
        });

        fabOpenSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabSearchToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Search.this, Profile.class);
                startActivity(i);
                animateFab();
            }
        });

        fabSearchToEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Search.this, Events.class);
                startActivity(i);
                animateFab();
            }
        });

        fabSearchToCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Search.this, CreateEvent.class);
                startActivity(i);
                animateFab();
            }
        });

        fabSearchToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Search.this, Mainpage.class);
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

    //used for android tests
    public int getSizeAdapter(){
        return adapter.getCount();
    }

    private void animateFab(){
        if(isOpen){
            fabOpenSearch.startAnimation(rotate_btn_forward);
            fabSearchToProfile.startAnimation(fab_close);
            fabSearchToEditEvent.startAnimation(fab_close);
            fabSearchToCreateEvent.startAnimation(fab_close);
            fabSearchToMenu.startAnimation(fab_close);
            fabSearchToProfile.setClickable(false);
            fabSearchToEditEvent.setClickable(false);
            fabSearchToCreateEvent.setClickable(false);
            fabSearchToMenu.setClickable(false);
            isOpen=false;
        }else{
            fabOpenSearch.startAnimation(rotate_btn_backward);
            fabSearchToProfile.startAnimation(fab_open);
            fabSearchToEditEvent.startAnimation(fab_open);
            fabSearchToCreateEvent.startAnimation(fab_open);
            fabSearchToMenu.startAnimation(fab_open);
            fabSearchToProfile.setClickable(true);
            fabSearchToEditEvent.setClickable(true);
            fabSearchToCreateEvent.setClickable(true);
            fabSearchToMenu.setClickable(true);
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