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
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class InviteMembersTest {

    private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<InviteMembers> mInviteMembersTestRule = new ActivityTestRule<>(InviteMembers.class);
    private Instrumentation.ActivityMonitor monitorMainpage = getInstrumentation().addMonitor(Mainpage.class.getName(), null, false);
    private Instrumentation.ActivityMonitor monitorCreateEvent = getInstrumentation().addMonitor(CreateEvent.class.getName(), null, false);
    private InviteMembers InviteMembersActivity = null;

    @Before
    public void setUp() throws Exception {
        InviteMembersActivity = mInviteMembersTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void testFAB(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.fabOpenInviteMembers)).perform(click());
        }
    }

    @Test
    public void nobodyInvited(){
        if(auth.getCurrentUser() != null) {
            closeSoftKeyboard();
            onView(withId(R.id.inviteMembersBtn)).perform(click());
            onView(withText(R.string.nobodyInvited)).inRoot(withDecorView(not(is(InviteMembersActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
            Activity inviteMembersActivity = getInstrumentation().waitForMonitorWithTimeout(monitorMainpage,2500);
            assertNotNull(inviteMembersActivity);
            inviteMembersActivity.finish();
        }
    }

    @Test
    public void inviteSomeone(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.inviteMembersSearch)).perform(typeText("Joh"));
            onData(anything()).inAdapterView(withId(R.id.inviteMembersListView)).atPosition(0).perform(click());
            onData(anything()).inAdapterView(withId(R.id.inviteMembersListView)).atPosition(1).perform(click());
            closeSoftKeyboard();
            onView(withId(R.id.inviteMembersBtn)).perform(click());
            Activity inviteMembersActivity = getInstrumentation().waitForMonitorWithTimeout(monitorCreateEvent,2500);
            assertNotNull(inviteMembersActivity);
            inviteMembersActivity.finish();
        }
    }

    @After
    public void tearDown() throws Exception {
        InviteMembersActivity = null;
    }

}