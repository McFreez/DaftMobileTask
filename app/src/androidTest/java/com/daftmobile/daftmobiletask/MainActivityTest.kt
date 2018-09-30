package com.daftmobile.daftmobiletask

import android.content.pm.ActivityInfo
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun testButtonStart(){
        onView(withId(R.id.btn_start)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_stop)).check(matches(not(isDisplayed())))

        onView(withId(R.id.btn_start)).perform(click())

        onView(withId(R.id.btn_stop)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_start)).check(matches(not(isDisplayed())))

        onView(withId(R.id.btn_stop)).perform(click())

        onView(withId(R.id.btn_start)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_stop)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testItemsCount(){
        onView(withId(R.id.btn_start)).perform(click())

        Thread.sleep(10000)

        onView(withId(R.id.rv)).check { view, noViewFoundException ->
            if (view !is RecyclerView)
                throw Exception("Not recycler view")
            Assert.assertTrue(view.adapter?.run { itemCount <= 5 } ?: false)
        }
    }

    @Test
    fun rotationSaveElements(){
        onView(withId(R.id.btn_start)).perform(click())

        Thread.sleep(3000)

        activityTestRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onView(withId(R.id.rv)).check { view, noViewFoundException ->
            if (view !is RecyclerView)
                throw Exception("Not recycler view")
            Assert.assertTrue(view.adapter?.run { itemCount > 0 } ?: false)
        }

        Thread.sleep(1000)

        activityTestRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        onView(withId(R.id.rv)).check { view, noViewFoundException ->
            if (view !is RecyclerView)
                throw Exception("Not recycler view")
            Assert.assertTrue(view.adapter?.run { itemCount > 0 } ?: false)
        }
    }

    @Test
    fun rotationSaveTimerState(){
        onView(withId(R.id.btn_start)).perform(click())

        activityTestRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        Assert.assertTrue(activityTestRule.activity.isTimerRunning)

        onView(withId(R.id.btn_stop)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_start)).check(matches(not(isDisplayed())))

        activityTestRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        Assert.assertTrue(activityTestRule.activity.isTimerRunning)

        onView(withId(R.id.btn_stop)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_start)).check(matches(not(isDisplayed())))

        onView(withId(R.id.btn_stop)).perform(click())

        Assert.assertFalse(activityTestRule.activity.isTimerRunning)

    }

    @Test
    fun clickOnItemAddsValue(){
        onView(withId(R.id.btn_start)).perform(click())

        Thread.sleep(3000)

        onView(withId(R.id.btn_stop)).perform(click())

        val itemNumber = { position: Int, view: RecyclerView ->
            view.findViewHolderForAdapterPosition(position)?.run {
                if (itemView.findViewById<CircleImageView>(R.id.item_color).tag as Color == Color.RED)
                    itemView.findViewById<TextView>(R.id.item_number).text.toString().toInt() / 3
                else
                    itemView.findViewById<TextView>(R.id.item_number).text.toString().toInt()
            }
        }

        val recyclerView = activityTestRule.activity.findViewById<RecyclerView>(R.id.rv)

        val number : Int? = itemNumber(1, recyclerView)
        val additionalNumber : Int? = itemNumber(0, recyclerView)

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition<ItemsAdapter.ViewHolder>(1, click()))

        val resultNumber : Int? = itemNumber(1, recyclerView)

        Assert.assertEquals(resultNumber!!, number!!.plus(additionalNumber!!))
    }

}