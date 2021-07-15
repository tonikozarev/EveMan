package com.example.tonyk.promac;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

public class EventsTest {

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Rule
    public ActivityTestRule<Events> mEventsTestRule = new ActivityTestRule<>(Events.class);
    private Instrumentation.ActivityMonitor monitorMainpage = getInstrumentation().addMonitor(Mainpage.class.getName(), null, false);
    private Instrumentation.ActivityMonitor monitorEvents = getInstrumentation().addMonitor(Events.class.getName(), null, false);
    private Instrumentation.ActivityMonitor monitorEditEvent = getInstrumentation().addMonitor(EditEvent.class.getName(), null, false);
    private Events eventsActivity = null;

    @Before
    public void setUp() throws Exception {
        eventsActivity = mEventsTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Test
    public void testFAB(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.fabOpenEvents)).perform(click());
        }
    }

    @Test
    public void searchEvent(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.eventsSearch)).perform(typeText("Test"));
            onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
            Activity inviteMembersActivity = getInstrumentation().waitForMonitorWithTimeout(monitorEditEvent,2500);
            assertNotNull(inviteMembersActivity);
            inviteMembersActivity.finish();
        }
    }

    @Test
    public void adminToTheEvent(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.eventsSearch)).perform(typeText("Test"));
            onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
            Activity inviteMembersActivity = getInstrumentation().waitForMonitorWithTimeout(monitorEditEvent,2500);
            String email = user.getEmail();
            if(email != null) onView(withId(R.id.editEventCreator)).check(matches(withText(email)));
            assertNotNull(inviteMembersActivity);
            inviteMembersActivity.finish();
        }
    }

    @Test
    public void notAdminToTheEvent(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.eventsSearch)).perform(typeText("TestT"));
            onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
            Activity inviteMembersActivity = getInstrumentation().waitForMonitorWithTimeout(monitorEditEvent,2500);
            String email = user.getEmail();
            if(email != null) assertNotEquals(onView(withId(R.id.editEventCreator)).toString(), email);
            assertNotNull(inviteMembersActivity);
            inviteMembersActivity.finish();
        }
    }

    @Test
    public void editEvent(){
        onView(withId(R.id.eventsSearch)).perform(typeText("Test"));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
        Activity inviteMembersActivity = getInstrumentation().waitForMonitorWithTimeout(monitorEditEvent,2500);
        assertNotNull(inviteMembersActivity);
        onView(withId(R.id.editEventPlaceText)).perform(clearText(), typeText("TestEditEventPlace"));
        closeSoftKeyboard();
        onView(withId(R.id.editEventInfoText)).perform(clearText(), typeText("TestEditEventInfo"));
        closeSoftKeyboard();
        onView(withId(R.id.editEventSaveBtn)).perform(click());
        Activity editEventActivity = getInstrumentation().waitForMonitorWithTimeout(monitorMainpage,2500);
        assertNotNull(editEventActivity);
        editEventActivity.finish();
    }

    @Test
    public void removeEvent(){
        onView(withId(R.id.eventsSearch)).perform(typeText("TestRemoveMe"));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
        Activity inviteMembersActivity = getInstrumentation().waitForMonitorWithTimeout(monitorEditEvent,2500);
        assertNotNull(inviteMembersActivity);
        closeSoftKeyboard();
        onView(withId(R.id.editEventDeleteBtn)).perform(click());
        onView(withText("Yes")).perform(click());
        Activity editEventActivity = getInstrumentation().waitForMonitorWithTimeout(monitorEvents,2500);
        assertNotNull(editEventActivity);
        onView(withId(R.id.eventsSearch)).perform(typeText("TestRemoveMe"));
        if(eventsActivity.getCountAdapter() == 0){
            closeSoftKeyboard();
            pressBack();
        }
    }

    @After
    public void tearDown() throws Exception {
        eventsActivity = null;
    }

}