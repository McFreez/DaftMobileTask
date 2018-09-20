package com.daftmobile.daftmobiletask

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityScreenTest{

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun testButtonStart(){
        Espresso.onView(ViewMatchers.withId(R.id.btn_start)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_stop)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.btn_start)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.btn_stop)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_start)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.btn_stop)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.btn_start)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_stop)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }
}