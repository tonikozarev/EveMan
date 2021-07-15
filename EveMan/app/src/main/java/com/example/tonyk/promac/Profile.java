package com.example.tonyk.promac;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    private static final int imageNr = 24;
    Uri uriProfileImage;
    String imageUrl;
    FirebaseAuth mAuth;
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDatabase.getReference("Benutzer/");

    FloatingActionButton fabOpenProfile, fabProfileToCreateEvent,
            fabProfileToEditEvent, fabProfileToSearch, fabProfileToMenu;
    Animation fab_open, fab_close, rotate_btn_forward, rotate_btn_backward;
    boolean isOpen = false;

    ProgressBar progressBar;
    ImageView profileInfoPhoto;
    TextView profileInfoEmail, uploadPhoto;
    EditText profileInfoName, profileInfoTelephoneText, profileInfoLeitzeichenText,
            profileInfoDepartmentText, profileInfoRoomText, profileInfoAdditionalText;
    ImageButton profileSaveBtn, uploadBtn;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUI();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        //Other objects
        progressBar = findViewById(R.id.profileProgressBar);
        profileInfoPhoto = findViewById(R.id.userInfoPhoto);
        profileInfoEmail = findViewById(R.id.userInfoEmail);
        profileInfoName = findViewById(R.id.userInfoName);
        profileInfoTelephoneText = findViewById(R.id.userInfoTelephoneText);
        profileInfoLeitzeichenText = findViewById(R.id.userInfoLeitzeichenText);
        profileInfoDepartmentText = findViewById(R.id.userInfoDepartmentText);
        profileInfoRoomText = findViewById(R.id.userInfoRoomText);
        profileInfoAdditionalText = findViewById(R.id.userInfoAdditionalText);
        profileSaveBtn = findViewById(R.id.profileSaveBtn);
        uploadPhoto = findViewById(R.id.uploadPhoto);
        uploadBtn = findViewById(R.id.uploadBtn);

        //FAB Buttons
        fabOpenProfile = findViewById(R.id.fabOpenProfile);
        fabProfileToCreateEvent = findViewById(R.id.fabProfileToCreateEvent);
        fabProfileToEditEvent = findViewById(R.id.fabProfileToEditEvent);
        fabProfileToSearch = findViewById(R.id.fabProfileToSearch);
        fabProfileToMenu = findViewById(R.id.fabProfileToMenu);

        //Animations
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_btn_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_backward);
        rotate_btn_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_btn_forward);

        loadUserInfo();
        loadUserPhoto();

        fabOpenProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabProfileToCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this, CreateEvent.class);
                startActivity(i);
                animateFab();
            }
        });

        fabProfileToEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this, Events.class);
                startActivity(i);
                animateFab();
            }
        });

        fabProfileToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this, Search.class);
                startActivity(i);
                animateFab();
            }
        });


        fabProfileToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this, Mainpage.class);
                startActivity(i);
                animateFab();
            }
        });

        profileInfoPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveUserPhoto()) {
                    saveUserPhoto();
                    uploadBtn.setEnabled(false);
                    uploadPhoto.setVisibility(View.GONE);
                    uploadBtn.setVisibility(View.GONE);
                    profileSaveBtn.setVisibility(View.VISIBLE);
                    profileInfoPhoto.setEnabled(false);
                    profileInfoPhoto.setClickable(false);
                }
            }
        });

        profileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveUserInfo()) {
                    saveUserInfo();
                    Toast.makeText(Profile.this, "Info was updated", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Profile.this, Mainpage.class);
                    startActivity(i);
                    return;
                }
                Toast.makeText(Profile.this, "Nothing was updated", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Profile.this, Mainpage.class);
                startActivity(i);
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

    private boolean saveUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            String name = profileInfoName.getText().toString();
            mRef.child(userID).child("Name").setValue(name);
            String leitzeichen = profileInfoLeitzeichenText.getText().toString();
            mRef.child(userID).child("Leitzeichen").setValue(leitzeichen);
            String department = profileInfoDepartmentText.getText().toString();
            mRef.child(userID).child("Abteilung").setValue(department);
            String room = profileInfoRoomText.getText().toString();
            mRef.child(userID).child("Raum").setValue(room);
            String phone = profileInfoTelephoneText.getText().toString();
            mRef.child(userID).child("Telefon").setValue(phone);
            String info = profileInfoAdditionalText.getText().toString();
            mRef.child(userID).child("Additional Info").setValue(info);

            return true;
        }
        return false;
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            DatabaseReference mUserRef = mRef.child(userID);
            mUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    profileInfoName.setText(name);
                    String leitzeichen = dataSnapshot.child("Leitzeichen").getValue(String.class);
                    profileInfoLeitzeichenText.setText(leitzeichen);
                    String room = dataSnapshot.child("Raum").getValue(String.class);
                    profileInfoRoomText.setText(room);
                    String tel = dataSnapshot.child("Telefon").getValue(String.class);
                    profileInfoTelephoneText.setText(tel);
                    String department = dataSnapshot.child("Abteilung").getValue(String.class);
                    profileInfoDepartmentText.setText(department);
                    String info = dataSnapshot.child("Additional Info").getValue(String.class);
                    profileInfoAdditionalText.setText(info);
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
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            profileInfoEmail.setText(mAuth.getCurrentUser().getEmail());
            if (mAuth.getCurrentUser() == null) {
                finish();
                startActivity(new Intent(Profile.this, Startapp.class));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    private void loadUserPhoto() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profileInfoPhoto);
            }
        }
    }

    private boolean saveUserPhoto() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(imageUrl))
                    .build();

            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(Profile.this, "Photo was uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            return true;
        }
        return false;
    }

    private void chooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Image is selected"), imageNr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == imageNr && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                profileInfoPhoto.setImageBitmap(bm);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        StorageReference imageRef = FirebaseStorage.getInstance()
                .getReference("profilePics/" + System.currentTimeMillis() + ".jpg");
        if (uriProfileImage != null) {
            imageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //noinspection ConstantConditions
                    imageUrl = taskSnapshot.getDownloadUrl().toString();
                    uploadPhoto.setVisibility(View.VISIBLE);
                    uploadBtn.setVisibility(View.VISIBLE);
                    profileSaveBtn.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void animateFab() {
        if (isOpen) {
            fabOpenProfile.startAnimation(rotate_btn_forward);
            fabProfileToCreateEvent.startAnimation(fab_close);
            fabProfileToEditEvent.startAnimation(fab_close);
            fabProfileToSearch.startAnimation(fab_close);
            fabProfileToMenu.startAnimation(fab_close);
            fabProfileToCreateEvent.setClickable(false);
            fabProfileToEditEvent.setClickable(false);
            fabProfileToSearch.setClickable(false);
            fabProfileToMenu.setClickable(false);
            isOpen = false;
        } else {
            fabOpenProfile.startAnimation(rotate_btn_backward);
            fabProfileToCreateEvent.startAnimation(fab_open);
            fabProfileToEditEvent.startAnimation(fab_open);
            fabProfileToSearch.startAnimation(fab_open);
            fabProfileToMenu.startAnimation(fab_open);
            fabProfileToCreateEvent.setClickable(true);
            fabProfileToEditEvent.setClickable(true);
            fabProfileToSearch.setClickable(true);
            fabProfileToMenu.setClickable(true);
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
