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
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

public class SearchTest {

    private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<Search> mSearchTestRule = new ActivityTestRule<>(Search.class);
    private Instrumentation.ActivityMonitor monitorMainpage = getInstrumentation().addMonitor(Mainpage.class.getName(), null, false);
    private Instrumentation.ActivityMonitor monitorUserInfo = getInstrumentation().addMonitor(UserInfo.class.getName(), null, false);
    private Search searchActivity = null;

    @Before
    public void setUp() throws Exception {
        searchActivity = mSearchTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void testFAB(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.fabOpenSearch)).perform(click());
        }
    }

    @Test
    public void searchUser(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.searchUser)).perform(typeText("bob"));
            onData(anything()).inAdapterView(withId(R.id.searchUserListView)).atPosition(0).perform(click());
            Activity userInfoActivity = getInstrumentation().waitForMonitorWithTimeout(monitorUserInfo,2500);
            assertNotNull(userInfoActivity);
            userInfoActivity.finish();
        }
    }

    @Test
    public void searchNotFoundUser(){
        if(auth.getCurrentUser() != null) {
            onView(withId(R.id.searchUser)).perform(typeText("bobby"));
            if(searchActivity.getSizeAdapter() == 0){
                closeSoftKeyboard();
                pressBack();
            }
        }
    }


    @After
    public void tearDown() throws Exception {
        searchActivity = null;
    }

}