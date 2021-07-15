package com.example.tonyk.promac;

import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class EditEventTest {

    private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<EditEvent> mEditEventTestRule = new ActivityTestRule<>(EditEvent.class);
    private EditEvent editEventActivity = null;

    @Before
    public void setUp() throws Exception {
        editEventActivity = mEditEventTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void testFAB(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.fabOpenEditEvent)).perform(click());
        }
    }

    @After
    public void tearDown() throws Exception {
        editEventActivity = null;
    }

}