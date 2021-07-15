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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
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


public class InviteMembers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRefB = mDatabase.getReference("Benutzer/");

    private boolean exec = true;
    private String members = "";
    ImageButton inviteMembersBtn;
    ListView inviteMembersListView;
    SearchView inviteMembersSearch;
    FloatingActionButton fabOpenInviteMembers, fabInviteMembersToMenu;
    Animation fab_open, fab_close, rotate_btn_forward, rotate_btn_backward;
    boolean isOpen = false;

    private ArrayAdapter<String> adapter;
    FirebaseAuth mAuth;

    private ArrayList<String> selectedUsers = new ArrayList<>();
    private ArrayList<String> allUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_invite_members);

        fabOpenInviteMembers = findViewById(R.id.fabOpenInviteMembers);
        fabInviteMembersToMenu = findViewById(R.id.fabInviteMembersToMenu);
        inviteMembersSearch = findViewById(R.id.inviteMembersSearch);
        inviteMembersListView = findViewById(R.id.inviteMembersListView);
        inviteMembersBtn = findViewById(R.id.inviteMembersBtn);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_btn_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_backward);
        rotate_btn_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_forward);

        adapter = new ArrayAdapter<>(this, R.layout.checkbox_listview, R.id.checkTextView, allUsers);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mRefB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String email = ds.child("Email").getValue(String.class);
                        String uid = ds.child("UserID").getValue(String.class);
                        assert uid != null;
                        if (!uid.equals(user.getUid()))
                            allUsers.add(email);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        inviteMembersListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        inviteMembersListView.setAdapter(adapter);
        inviteMembersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedUser = ((TextView) view).getText().toString();
                if (selectedUsers.contains(selectedUser)) {
                    selectedUsers.remove(selectedUser);
                } else {
                    selectedUsers.add(selectedUser);
                }
            }
        });

        inviteMembersSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        inviteMembersSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteMembersSearch.setIconified(false);
            }
        });

        fabOpenInviteMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabInviteMembersToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InviteMembers.this, Mainpage.class);
                startActivity(i);
                animateFab();
            }
        });

        inviteMembersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitedUsers();
                if (exec) {
                    Intent i = new Intent(InviteMembers.this, CreateEvent.class);
                    i.putExtra("members", members);
                    i.putExtra("selectedUsers", selectedUsers);
                    startActivity(i);
                }
            }
        });

    }

    private void invitedUsers() {
        if (!selectedUsers.isEmpty()) {
            for (String user : selectedUsers) {
                //noinspection StringConcatenationInLoop
                members += user + ", ";
            }
            members = members.substring(0, members.length() - 2);
        } else {
            Toast.makeText(getApplicationContext(), "Nobody invited!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(InviteMembers.this, Mainpage.class);
            startActivity(i);
            exec = false;
        }
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
            fabOpenInviteMembers.startAnimation(rotate_btn_forward);
            fabInviteMembersToMenu.startAnimation(fab_close);
            fabInviteMembersToMenu.setClickable(false);
            isOpen = false;
        } else {
            fabOpenInviteMembers.startAnimation(rotate_btn_backward);
            fabInviteMembersToMenu.startAnimation(fab_open);
            fabInviteMembersToMenu.setClickable(true);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //...
    }


}
