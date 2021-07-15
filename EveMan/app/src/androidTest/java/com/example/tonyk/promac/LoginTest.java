package com.example.tonyk.promac;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class LoginTest {

    private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<Login> mLoginTestRule = new ActivityTestRule<>(Login.class);
    private Login loginActivity = null;
    private Instrumentation.ActivityMonitor monitorMainpage = getInstrumentation().addMonitor(Mainpage.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        loginActivity = mLoginTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void login(){
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword(onView(withId(R.id.loginEmail)).perform(typeText("test@dataport.de")).toString(), Espresso.onView(withId(R.id.loginPass)).perform(typeText("123456")).toString());
            closeSoftKeyboard();
            onView(withId(R.id.loginBtn)).perform(click());
            Activity mainpageActivity = getInstrumentation().waitForMonitorWithTimeout(monitorMainpage,2500);
            assertNotNull(mainpageActivity);
        }
    }

    @Test
    public void loginFailed(){
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword(onView(withId(R.id.loginEmail)).perform(typeText("test@dataport.de")).toString(), Espresso.onView(withId(R.id.loginPass)).perform(typeText("654321")).toString());
            closeSoftKeyboard();
            onView(withId(R.id.loginBtn)).perform(click());
            onView(withText(R.string.loginFailed)).inRoot(withDecorView(not(is(loginActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
        }
    }

    @Test
    public void passwordIsEmpty(){
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword(onView(withId(R.id.loginEmail)).perform(typeText("test@dataport.de")).toString(), Espresso.onView(withId(R.id.loginPass)).perform(typeText("")).toString());
            closeSoftKeyboard();
            onView(withId(R.id.loginBtn)).perform(click());
            onView(withText(R.string.passwordIsEmpty)).inRoot(withDecorView(not(is(loginActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
        }
    }

    @Test
    public void passwordIsTooSmall(){
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword(onView(withId(R.id.loginEmail)).perform(typeText("test@dataport.de")).toString(), Espresso.onView(withId(R.id.loginPass)).perform(typeText("1234")).toString());
            closeSoftKeyboard();
            onView(withId(R.id.loginBtn)).perform(click());
            onView(withText(R.string.passwordIsTooSmall)).inRoot(withDecorView(not(is(loginActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
        }
    }

    @Test
    public void emailIsRequired(){
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword(onView(withId(R.id.loginEmail)).perform(typeText("")).toString(), Espresso.onView(withId(R.id.loginPass)).perform(typeText("123456")).toString());
            closeSoftKeyboard();
            onView(withId(R.id.loginBtn)).perform(click());
            onView(withText(R.string.emailIsRequired)).inRoot(withDecorView(not(is(loginActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
        }
    }

    @Test
    public void emailIsInvalid(){
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword(onView(withId(R.id.loginEmail)).perform(typeText("blabla")).toString(), Espresso.onView(withId(R.id.loginPass)).perform(typeText("123456")).toString());
            closeSoftKeyboard();
            onView(withId(R.id.loginBtn)).perform(click());
            onView(withText(R.string.emailIsInvalid)).inRoot(withDecorView(not(is(loginActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
        }
    }

    @After
    public void tearDown() throws Exception {
        loginActivity = null;
        if(auth != null) {
            auth.signOut();
            auth = null;
        }
    }

}