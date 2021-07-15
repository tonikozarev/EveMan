package com.example.tonyk.promac;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateEvent extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRefE = mDatabase.getReference("Events/");
    DatabaseReference mRefB = mDatabase.getReference("Benutzer/");
    DatabaseReference mRefBE = mDatabase.getReference("Benutzer-Events/");

    String eventKey, members;
    ArrayList<String> selectedUsers;

    CoordinatorLayout createEventLayout;
    TextView createEventMembers;
    EditText createEventDeadline, createEventName, createEventPurposeText, createEventPlanText,
            createEventPlaceText, createEventPriceText, createEventInfoText;
    ImageButton createEventInviteMembersBtn, createEventSaveBtn;

    FloatingActionButton fabOpenCreateEvent, fabCreateEventToProfile, fabCreateEventToSearch,
            fabCreateEventToEditEvent, fabCreateEventToMenu;
    Animation fab_open, fab_close, rotate_btn_forward, rotate_btn_backward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_create_event);

        mAuth = FirebaseAuth.getInstance();

        createEventLayout = findViewById(R.id.createEventLayout);
        createEventName = findViewById(R.id.createEventName);
        createEventMembers = findViewById(R.id.createEventMembers);
        createEventDeadline = findViewById(R.id.createEventDeadline);
        createEventPurposeText = findViewById(R.id.createEventPurposeText);
        createEventPlanText = findViewById(R.id.createEventPlanText);
        createEventPlaceText = findViewById(R.id.createEventPlaceText);
        createEventPriceText = findViewById(R.id.createEventPriceText);
        createEventInfoText = findViewById(R.id.createEventInfoText);
        createEventInviteMembersBtn = findViewById(R.id.createEventInviteMembersBtn);
        createEventSaveBtn = findViewById(R.id.createEventSaveBtn);

        fabOpenCreateEvent = findViewById(R.id.fabOpenCreateEvent);
        fabCreateEventToProfile = findViewById(R.id.fabCreateEventToProfile);
        fabCreateEventToSearch = findViewById(R.id.fabCreateEventToSearch);
        fabCreateEventToEditEvent = findViewById(R.id.fabCreateEventToEditEvent);
        fabCreateEventToMenu = findViewById(R.id.fabCreateEventToMenu);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_btn_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_backward);
        rotate_btn_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_forward);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            members = bundle.getString("members");
        }

        if (bundle != null) {
            selectedUsers = bundle.getStringArrayList("selectedUsers");
        }

        createEventMembers.setText(members);
        createEventMembers.setMovementMethod(new ScrollingMovementMethod());

        fabOpenCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabCreateEventToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateEvent.this, Profile.class);
                startActivity(i);
                animateFab();
            }
        });

        fabCreateEventToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateEvent.this, Search.class);
                startActivity(i);
                animateFab();
            }
        });

        fabCreateEventToEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateEvent.this, Events.class);
                startActivity(i);
                animateFab();
            }
        });

        fabCreateEventToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateEvent.this, Mainpage.class);
                startActivity(i);
                animateFab();
            }
        });

        createEventInviteMembersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateEvent.this, InviteMembers.class);
                startActivity(i);
            }
        });

        createEventSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performClick();
                createEvent();
            }
        });
    }

    private void performClick(){
        createEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void createEvent() {
        final FirebaseUser user = mAuth.getCurrentUser();
        final DatabaseReference mRefDB = mRefE.push();

        if (user != null) {
            final String eventName = createEventName.getText().toString();
            if (eventName.trim().isEmpty()) {
                createEventName.setError("Name field is empty!");
                createEventName.requestFocus();
                return;
            }

            mRefE.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String valueEditBox = createEventName.getText().toString(); //same as eventName, just for better understanding of the code
                        String valueDB = ds.child("Name").getValue(String.class);
                        assert valueDB != null;
                        if (valueDB.equals(valueEditBox)) {
                            createEventName.setError("Name is taken!");
                            createEventName.requestFocus();
                            return;
                        }
                    }

                    mRefDB.child("Name").setValue(eventName);
                    String eventCreator = user.getEmail();
                    mRefDB.child("Creator").setValue(eventCreator);

                    String eventDeadline = createEventDeadline.getText().toString();
                    mRefDB.child("Deadline").setValue(eventDeadline);

                    mRefBE.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String UID = user.getUid();
                            mRefBE.child(eventKey).child(UID).setValue("Admin");
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mRefDB.child("ListMembers").setValue(selectedUsers);
                    String eventMembers = createEventMembers.getText().toString();
                    mRefDB.child("Members").setValue(eventMembers);

                    String eventPurpose = createEventPurposeText.getText().toString();
                    mRefDB.child("Purpose").setValue(eventPurpose);
                    String eventPlan = createEventPlanText.getText().toString();
                    mRefDB.child("Plan").setValue(eventPlan);
                    String eventPlace = createEventPlaceText.getText().toString();
                    mRefDB.child("Place").setValue(eventPlace);
                    String eventPrice = createEventPriceText.getText().toString();
                    mRefDB.child("Price").setValue(eventPrice);
                    String eventInfo = createEventInfoText.getText().toString();
                    mRefDB.child("Information").setValue(eventInfo);
                    eventKey = mRefDB.getKey();
                    mRefDB.child("EventID").setValue(eventKey);

                    mRefB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                String uid = ds.child("UserID").getValue(String.class);
                                assert uid != null;
                                String email = ds.child("Email").getValue(String.class);
                                assert email != null;
                                for(String emailInvite : selectedUsers){
                                    if(emailInvite.equals(email)){
                                        mRefBE.child(eventKey).child(uid).setValue(email);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(CreateEvent.this, "Event was created", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CreateEvent.this, Mainpage.class);
                    startActivity(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        createEventDeadline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(view);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "Choose a deadline");
                }
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

    private void animateFab() {
        if (isOpen) {
            fabOpenCreateEvent.startAnimation(rotate_btn_forward);
            fabCreateEventToProfile.startAnimation(fab_close);
            fabCreateEventToSearch.startAnimation(fab_close);
            fabCreateEventToEditEvent.startAnimation(fab_close);
            fabCreateEventToMenu.startAnimation(fab_close);
            fabCreateEventToProfile.setClickable(false);
            fabCreateEventToSearch.setClickable(false);
            fabCreateEventToEditEvent.setClickable(false);
            fabCreateEventToMenu.setClickable(false);
            isOpen = false;
        } else {
            fabOpenCreateEvent.startAnimation(rotate_btn_backward);
            fabCreateEventToProfile.startAnimation(fab_open);
            fabCreateEventToSearch.startAnimation(fab_open);
            fabCreateEventToEditEvent.startAnimation(fab_open);
            fabCreateEventToMenu.startAnimation(fab_open);
            fabCreateEventToProfile.setClickable(true);
            fabCreateEventToSearch.setClickable(true);
            fabCreateEventToEditEvent.setClickable(true);
            fabCreateEventToMenu.setClickable(true);
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
