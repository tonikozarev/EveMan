package com.example.tonyk.promac;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

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

public class UserInfoTest {

    private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<UserInfo> mUserInfoTestRule = new ActivityTestRule<>(UserInfo.class);
    private Instrumentation.ActivityMonitor monitorSearch = getInstrumentation().addMonitor(Search.class.getName(), null, false);
    private UserInfo userInfoActivity = null;

    @Before
    public void setUp() throws Exception {
        userInfoActivity = mUserInfoTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void testFAB(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.fabOpenUserInfo)).perform(click());
        }
    }

    @Test
    public void goBack(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.userInfoCloseBtn)).perform(click());
            Activity userInfoActivity = getInstrumentation().waitForMonitorWithTimeout(monitorSearch,2500);
            assertNotNull(userInfoActivity);
            userInfoActivity.finish();
        }
    }

    @After
    public void tearDown() throws Exception {
        userInfoActivity = null;
    }

}