package com.example.tonyk.promac;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;

public class StartappTest {

    @Rule
    public ActivityTestRule<Startapp> mStartAppTestRule = new ActivityTestRule<>(Startapp.class);
    private Startapp startAppActivity = null;
    private Instrumentation.ActivityMonitor monitorLogin = getInstrumentation().addMonitor(Login.class.getName(), null, false);
    private Instrumentation.ActivityMonitor monitorRegister = getInstrumentation().addMonitor(Register.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        startAppActivity = mStartAppTestRule.getActivity();
    }

    @Test
    public void testGoToLoginBtn(){
        assertNotNull(startAppActivity.findViewById(R.id.goToLogin));
        onView(withId(R.id.goToLogin)).perform(click());
        Activity loginActivity = getInstrumentation().waitForMonitorWithTimeout(monitorLogin,2500);
        assertNotNull(loginActivity);
        loginActivity.finish();
    }

    @Test
    public void testGoToRegisterBtn(){
        assertNotNull(startAppActivity.findViewById(R.id.goToRegistration));
        onView(withId(R.id.goToRegistration)).perform(click());
        Activity registerActivity = getInstrumentation().waitForMonitorWithTimeout(monitorRegister,2500);
        assertNotNull(registerActivity);
        registerActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        startAppActivity = null;
    }

}