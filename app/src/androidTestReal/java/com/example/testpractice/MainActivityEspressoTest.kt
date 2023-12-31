package com.example.testpractice

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.testpractice.main.MainActivity
import com.example.testspractice.R
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MainActivityEspressoTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activity_AssertsNotNull() {
        scenario.onActivity {
            Assert.assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        Assert.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activitySearch_IsWorking() {
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText)).perform(
            ViewActions.replaceText("algol"),
            ViewActions.closeSoftKeyboard()
        )
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText))
            .perform(ViewActions.pressImeActionButton())

        Espresso.onView(ViewMatchers.isRoot()).perform(delay())
        Espresso.onView(ViewMatchers.withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText("Number of results: 3965")))
    }

    @After
    fun close() {
        scenario.close()
    }

    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
            override fun getDescription(): String = "wait for $3 seconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(3000)
            }
        }
    }
}