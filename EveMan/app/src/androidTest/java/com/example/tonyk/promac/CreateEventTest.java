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
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

public class CreateEventTest {

    private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<CreateEvent> mCreateEventTestRule = new ActivityTestRule<>(CreateEvent.class);
    private CreateEvent createEventActivity = null;

    @Before
    public void setUp() throws Exception {
        createEventActivity = mCreateEventTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void testFAB(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.fabOpenCreateEvent)).perform(click());
        }
    }

    @After
    public void tearDown() throws Exception {
        createEventActivity = null;
    }

}