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

public class MainpageTest {

    private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<Mainpage> mMainpageTestRule = new ActivityTestRule<>(Mainpage.class);
    private Mainpage mainpageActivity = null;
    private Instrumentation.ActivityMonitor monitorProfile = getInstrumentation().addMonitor(Profile.class.getName(), null, false);
    private Instrumentation.ActivityMonitor monitorCreateEvent = getInstrumentation().addMonitor(InviteMembers.class.getName(), null, false);
    private Instrumentation.ActivityMonitor monitorEvents = getInstrumentation().addMonitor(Events.class.getName(), null, false);
    private Instrumentation.ActivityMonitor monitorSearch = getInstrumentation().addMonitor(Search.class.getName(), null, false);
    private Instrumentation.ActivityMonitor monitorStartapp = getInstrumentation().addMonitor(Startapp.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mainpageActivity = mMainpageTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void goToProfile(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.cardViewProfile)).perform(click());
            Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitorProfile,2500);
            assertNotNull(activity);
            activity.finish();
        }
    }

    @Test
    public void goToCreateEvent(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.cardViewCreateEvent)).perform(click());
            Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitorCreateEvent,2500);
            assertNotNull(activity);
            activity.finish();
        }
    }

    @Test
    public void goToEvents(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.cardViewEvents)).perform(click());
            Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitorEvents,2500);
            assertNotNull(activity);
            activity.finish();
        }
    }

    @Test
    public void goToSearch(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.cardViewSearch)).perform(click());
            Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitorSearch,2500);
            assertNotNull(activity);
            activity.finish();
        }
    }

    @Test
    public void logOut(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.logoutBtn)).perform(click());
            Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitorStartapp,2500);
            assertNotNull(activity);
            activity.finish();
        }
    }

    @After
    public void tearDown() throws Exception {
        mainpageActivity = null;
    }

}