package com.example.testspractice

import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testpractice.main.MainActivity
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activity_AssertsNotNull() {
        scenario.onActivity {
            assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityTextView_AssertsNotNull() {
        scenario.onActivity {
            val totalCountTextView =
                it.findViewById<TextView>(R.id.totalCountTextView)
            assertNotNull(totalCountTextView)
        }
    }

    @Test
    fun activityTextView_NotIsDisplayed() {
        onView(withId(R.id.totalCountTextView))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun activityEditText_IsDisplayed() {
        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()))
    }

    @Test
    fun activityEditText_HasHint() {
        onView(withId(R.id.searchEditText)).check(matches(withHint(R.string.search_hint)))
    }

    @Test
    fun activityEditText_Focus() {
        onView(withId(R.id.searchEditText)).check(matches(not(hasFocus())))
        onView(withId(R.id.searchEditText))
            .perform(click())
            .check(matches(hasFocus()))
    }

    @Test
    fun activityEditText_isEnabled() {
        onView(withId(R.id.searchEditText)).check(matches(isEnabled()))
    }

    @Test
    fun activityEditText_WorkWithText() {
        onView(withId(R.id.searchEditText))
            .perform(typeText("ololo"))
            .check(matches(withText("ololo")))
        onView(withId(R.id.searchEditText))
            .perform(clearText())
            .check(matches(withText("")))
    }

    @Test
    fun activityButton_IsDisplayed() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(isDisplayed()))
    }

    @Test
    fun activityButton_Text() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withText(R.string.to_details)))
    }

    @Test
    fun activityButton_IsEnabled() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(isEnabled()))
    }

    @Test
    fun activityButton_isWorking() {
        onView(withId(R.id.toDetailsActivityButton)).perform(click())
        onView(withId(R.id.incrementButton)).check(matches(isDisplayed()))
    }

    @After
    fun close() {
        scenario.close()
    }
}