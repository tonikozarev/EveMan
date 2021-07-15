package com.example.tonyk.promac;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/*
* Dieser Test wirkt analog für jedes Floating Animation Button überall in der App (nur Buttons vom Menu).
*/
public class ProfileTest {

    private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<Profile> mProfileTestRule = new ActivityTestRule<>(Profile.class);
    private Instrumentation.ActivityMonitor monitorMainpage = getInstrumentation().addMonitor(Mainpage.class.getName(), null, false);
    private Profile profileActivity = null;

    @Before
    public void setUp() throws Exception {
        profileActivity = mProfileTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void testFAB() {
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.fabOpenProfile)).perform(click());
        }
    }

    @Test
    public void etProfilePhoto(){
        if(auth.getCurrentUser() != null) {
            ImageView etPhoto = profileActivity.findViewById(R.id.userInfoPhoto);
            assertNotNull(etPhoto);
        }
    }

    @Test
    public void etProfileName(){
        if(auth.getCurrentUser() != null) {
            EditText etName = profileActivity.findViewById(R.id.userInfoName);
            assertNotNull(etName);
        }
    }

    @Test
    public void etProfileSignature(){
        if(auth.getCurrentUser() != null) {
            EditText etSignature = profileActivity.findViewById(R.id.userInfoLeitzeichenText);
            assertNotNull(etSignature);
        }
    }

    @Test
    public void etProfileDepartment(){
        if(auth.getCurrentUser() != null) {
            EditText etDepartment = profileActivity.findViewById(R.id.userInfoDepartmentText);
            assertNotNull(etDepartment);
        }
    }

    @Test
    public void etProfileRoom(){
        if(auth.getCurrentUser() != null) {
            EditText etRoom = profileActivity.findViewById(R.id.userInfoRoomText);
            assertNotNull(etRoom);
        }
    }

    @Test
    public void etProfilePhone(){
        if(auth.getCurrentUser() != null) {
            EditText etPhone = profileActivity.findViewById(R.id.userInfoTelephoneText);
            assertNotNull(etPhone);
        }
    }

    @Test
    public void etProfileExtraInfo(){
        if(auth.getCurrentUser() != null) {
            EditText etExtraInfo = profileActivity.findViewById(R.id.userInfoAdditionalText);
            assertNotNull(etExtraInfo);
        }
    }

    @Test
    public void etProfileSaveBtn(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.profileSaveBtn)).perform(click());
            Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitorMainpage,2500);
            assertNotNull(activity);
            activity.finish();
        }
    }

    @After
    public void tearDown() throws Exception {
        profileActivity = null;
    }
}

