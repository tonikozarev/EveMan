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
import static org.junit.Assert.assertNotNull;

public class RegisterTest {

    private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<Register> mRegisterTestRule = new ActivityTestRule<>(Register.class);
    private Register registerActivity = null;
    private Instrumentation.ActivityMonitor monitorMainpage = getInstrumentation().addMonitor(Mainpage.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        registerActivity = mRegisterTestRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void registration(){
        if(auth.getCurrentUser() == null) {
            auth.createUserWithEmailAndPassword(onView(withId(R.id.regEmail)).perform(typeText("test2@dataport.de")).toString(), onView(withId(R.id.regPass)).perform(typeText("123456")).toString());
            closeSoftKeyboard();
            onView(withId(R.id.regConfirmPass)).perform(typeText("123456"));
            closeSoftKeyboard();
            onView(withId(R.id.regBtn)).perform(click());
            onView(withText(R.string.registrationSuccessful)).inRoot(withDecorView(not(is(registerActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
            Activity mainpageActivity = getInstrumentation().waitForMonitorWithTimeout(monitorMainpage,2500);
            assertNotNull(mainpageActivity);
            mainpageActivity.finish();
        }
    }

    @Test
    public void emailExistsRegistrationFailed(){
        if(auth.getCurrentUser() == null) {
            auth.createUserWithEmailAndPassword(onView(withId(R.id.regEmail)).perform(typeText("test@dataport.de")).toString(), onView(withId(R.id.regPass)).perform(typeText("123456")).toString());
            closeSoftKeyboard();
            onView(withId(R.id.regConfirmPass)).perform(typeText("123456"));
            closeSoftKeyboard();
            onView(withId(R.id.regBtn)).perform(click());
            onView(withText(R.string.emailExistsRegistrationFailed)).inRoot(withDecorView(not(is(registerActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
        }
    }

    @After
    public void tearDown() throws Exception {
        registerActivity = null;
        if(auth != null) {
            auth.signOut();
            auth = null;
        }
    }

}