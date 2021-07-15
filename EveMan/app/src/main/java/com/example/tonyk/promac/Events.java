package com.example.tonyk.promac;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Events extends AppCompatActivity {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDatabase.getReference("Events/");
    FirebaseAuth mAuth;

    String text;

    ListView listView;
    SearchView eventsSearch;
    FloatingActionButton fabOpenEvents, fabEventsToMenu;
    Animation fab_open, fab_close, rotate_btn_forward, rotate_btn_backward;
    boolean isOpen = false;

    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_events);

        mAuth = FirebaseAuth.getInstance();

        eventsSearch = findViewById(R.id.eventsSearch);
        listView = findViewById(R.id.listView);

        fabOpenEvents = findViewById(R.id.fabOpenEvents);
        fabEventsToMenu = findViewById(R.id.fabEventsToMenu);

        //Animations
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_btn_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_backward);
        rotate_btn_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_forward);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(1, true);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int nr = 0; nr < list.size(); nr++) {
                    text = listView.getItemAtPosition(i).toString();
                    if (nr == i) {
                        Intent intent = new Intent(Events.this, EditEvent.class);
                        intent.putExtra("Name", text);
                        intent.putExtra("listEvents", list);
                        startActivityForResult(intent, 24);
                        break;
                    }
                }
            }
        });

        eventsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String nameID = ds.child("Name").getValue(String.class);
                    final String creator = ds.child("Creator").getValue(String.class);
                    ArrayList al = (ArrayList) ds.child("ListMembers").getValue();
                    assert al != null;
                    for(Object member : al) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Object memb = member.toString();
                            assert creator != null;
                            if (ds.hasChildren() && !list.contains(nameID) && ((memb.equals(user.getEmail())) || (creator.equals(user.getEmail())))) {
                                list.add(nameID);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fabOpenEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabEventsToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Events.this, Mainpage.class);
                startActivity(i);
                animateFab();
            }
        });

        eventsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventsSearch.setIconified(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
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

    public int getCountAdapter(){
        return adapter.getCount();
    }

    private void animateFab() {
        if (isOpen) {
            fabOpenEvents.startAnimation(rotate_btn_forward);
            fabEventsToMenu.startAnimation(fab_close);
            fabEventsToMenu.setClickable(false);
            isOpen = false;
        } else {
            fabOpenEvents.startAnimation(rotate_btn_backward);
            fabEventsToMenu.startAnimation(fab_open);
            fabEventsToMenu.setClickable(true);
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
}
