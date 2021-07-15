package com.example.tonyk.promac;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

public class EditEvent extends AppCompatActivity {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDatabase.getReference("Events/");
    DatabaseReference mRefBE = mDatabase.getReference("Benutzer-Events/");

    boolean exec = false;
    String nameID, eventID, invitedUsers;
    ArrayList listEvents;

    FirebaseAuth mAuth;

    TextView editEventName, editEventCreator, editEventMembers, editEventDeadline;
    EditText editEventPurposeText, editEventPlanText, editEventPlaceText,
            editEventPriceText, editEventInfoText;
    ImageButton editEventInviteMembersBtn, editEventDeleteBtn, editEventLeaveBtn, editEventSaveBtn;

    FloatingActionButton fabOpenEditEvent, fabEditEventToMenu,
            fabEditEventToCreateEvent, fabEditEventToProfile, fabEditEventToSearch;
    Animation fab_open, fab_close, rotate_btn_forward, rotate_btn_backward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_edit_event);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) nameID = bundle.getString("Name");
        if (bundle != null) listEvents = bundle.getStringArrayList("listEvents");
        if (bundle != null) invitedUsers = bundle.getString("members");

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        editEventCreator = findViewById(R.id.editEventCreator);
        editEventName = findViewById(R.id.editEventName);
        editEventMembers = findViewById(R.id.editEventMembers);
        editEventDeadline = findViewById(R.id.editEventDeadline);
        editEventPurposeText = findViewById(R.id.editEventPurposeText);
        editEventPlanText = findViewById(R.id.editEventPlanText);
        editEventPlaceText = findViewById(R.id.editEventPlaceText);
        editEventPriceText = findViewById(R.id.editEventPriceText);
        editEventInfoText = findViewById(R.id.editEventInfoText);
        editEventInviteMembersBtn = findViewById(R.id.editEventInviteMembersBtn);
        editEventSaveBtn = findViewById(R.id.editEventSaveBtn);
        editEventDeleteBtn = findViewById(R.id.editEventDeleteBtn);
        editEventLeaveBtn = findViewById(R.id.editEventLeaveBtn);

        fabOpenEditEvent = findViewById(R.id.fabOpenEditEvent);
        fabEditEventToMenu = findViewById(R.id.fabEditEventToMenu);
        fabEditEventToCreateEvent = findViewById(R.id.fabEditEventToCreateEvent);
        fabEditEventToSearch = findViewById(R.id.fabEditEventToSearch);
        fabEditEventToProfile = findViewById(R.id.fabEditEventToProfile);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_btn_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_backward);
        rotate_btn_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_forward);

        editEventMembers.setMovementMethod(new ScrollingMovementMethod());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    String eventName = ds.child("Name").getValue(String.class);
                    final String eventID = ds.child("EventID").getValue(String.class);

                    assert eventID != null;
                    if (eventName != null) {
                        if (eventName.equals(nameID)) {
                            if (user != null) {
                                final String email = user.getEmail();
                                assert email != null;

                                if (email.equals(ds.child("Creator").getValue(String.class))) {
                                    // Add/Remove users from edit mode is not possible. Button has no functionality yet and it is disabled!
                                    // Button add/remove users shows on only if the admin is editing the event.
                                    editEventInviteMembersBtn.setVisibility(View.VISIBLE);
                                    editEventInviteMembersBtn.setEnabled(false);

                                    editEventDeleteBtn.setVisibility(View.VISIBLE);

                                    editEventDeadline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean hasFocus) {
                                            if (hasFocus) {
                                                DateDialog dialog = new DateDialog(view);
                                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                dialog.show(ft, "Pick a date");
                                            }
                                        }
                                    });
                                }else{
                                    editEventLeaveBtn.setVisibility(View.VISIBLE);
                                }

                                editEventLeaveBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditEvent.this);
                                        alertDialogBuilder.setTitle("Do you want to leave this event?\n");
                                        alertDialogBuilder.setCancelable(false);

                                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {

                                                mRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        ArrayList al = (ArrayList) ds.child("ListMembers").getValue();
                                                        assert al != null;
                                                        for(int i = 0; i < al.size(); i++){
                                                            if(al.get(i).equals(email)){
                                                                al.remove(i);
                                                                mRef.child(eventID).child("ListMembers").setValue(al);
                                                                mRefBE.child(eventID).child(user.getUid()).removeValue();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                Intent i = new Intent(EditEvent.this, Events.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });

                                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });

                                        final AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                            @Override
                                            public void onShow(DialogInterface arg0) {
                                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                                            }
                                        });

                                        alertDialog.show();
                                    }
                                });

                                editEventDeleteBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditEvent.this);
                                        alertDialogBuilder.setTitle("Do you want to delete this event?\n");
                                        alertDialogBuilder.setCancelable(false);

                                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                mRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        mRef.child(eventID).removeValue();

                                                        mRefBE.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                mRefBE.child(eventID).removeValue();
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                                Intent i = new Intent(EditEvent.this, Events.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });

                                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });

                                        final AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                            @Override
                                            public void onShow(DialogInterface arg0) {
                                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                                            }
                                        });

                                        alertDialog.show();
                                    }
                                });

                                String name = ds.child("Name").getValue(String.class);
                                editEventName.setText(name);
                                String creator = ds.child("Creator").getValue(String.class);
                                editEventCreator.setText(creator);
                                String members = ds.child("Members").getValue(String.class);
                                editEventMembers.setText(members);
                                String deadline = ds.child("Deadline").getValue(String.class);
                                editEventDeadline.setText(deadline);
                                String purpose = ds.child("Purpose").getValue(String.class);
                                editEventPurposeText.setText(purpose);
                                String plan = ds.child("Plan").getValue(String.class);
                                editEventPlanText.setText(plan);
                                String place = ds.child("Place").getValue(String.class);
                                editEventPlaceText.setText(place);
                                String price = ds.child("Price").getValue(String.class);
                                editEventPriceText.setText(price);
                                String moreInfo = ds.child("Information").getValue(String.class);
                                editEventInfoText.setText(moreInfo);
                                EditEvent.this.eventID = ds.child("EventID").getValue(String.class);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editEventInviteMembersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditEvent.this, InviteMembers.class);
                startActivity(i);
            }
        });

        fabOpenEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabEditEventToCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditEvent.this, InviteMembers.class);
                startActivity(i);
                animateFab();
            }
        });

        fabEditEventToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditEvent.this, Profile.class);
                startActivity(i);
                animateFab();
            }
        });

        fabEditEventToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditEvent.this, Search.class);
                startActivity(i);
                animateFab();
            }
        });

        fabEditEventToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditEvent.this, Mainpage.class);
                startActivity(i);
                animateFab();
            }
        });

        editEventSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEditEvent();
                Toast.makeText(EditEvent.this, "Event was edited", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditEvent.this, Mainpage.class);
                startActivity(i);
            }
        });
    }

    private void saveEditEvent() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nameKey = ds.child("Name").getValue(String.class);
                    if (nameKey != null) {
                        if ((nameKey.equals(nameID)) && !exec) {
                            String eventDeadline = editEventDeadline.getText().toString();
                            mRef.child(eventID).child("Deadline").setValue(eventDeadline);
                            String eventPurpose = editEventPurposeText.getText().toString();
                            mRef.child(eventID).child("Purpose").setValue(eventPurpose);
                            String eventPlan = editEventPlanText.getText().toString();
                            mRef.child(eventID).child("Plan").setValue(eventPlan);
                            String eventPlace = editEventPlaceText.getText().toString();
                            mRef.child(eventID).child("Place").setValue(eventPlace);
                            String eventPrice = editEventPriceText.getText().toString();
                            mRef.child(eventID).child("Price").setValue(eventPrice);
                            String eventInfo = editEventInfoText.getText().toString();
                            mRef.child(eventID).child("Information").setValue(eventInfo);
                            exec = true;
                        }
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
            fabOpenEditEvent.startAnimation(rotate_btn_forward);
            fabEditEventToMenu.startAnimation(fab_close);
            fabEditEventToProfile.startAnimation(fab_close);
            fabEditEventToCreateEvent.startAnimation(fab_close);
            fabEditEventToSearch.startAnimation(fab_close);
            fabEditEventToMenu.setClickable(false);
            fabEditEventToProfile.setClickable(false);
            fabEditEventToCreateEvent.setClickable(false);
            fabEditEventToSearch.setClickable(false);
            isOpen = false;

        } else {
            fabOpenEditEvent.startAnimation(rotate_btn_backward);
            fabEditEventToMenu.startAnimation(fab_open);
            fabEditEventToProfile.startAnimation(fab_open);
            fabEditEventToCreateEvent.startAnimation(fab_open);
            fabEditEventToSearch.startAnimation(fab_open);
            fabEditEventToMenu.setClickable(true);
            fabEditEventToProfile.setClickable(true);
            fabEditEventToCreateEvent.setClickable(true);
            fabEditEventToSearch.setClickable(true);
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
        // user can only save the changes or use the menu to go back.
    }
}
