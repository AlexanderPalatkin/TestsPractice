package com.example.testpractice.main


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.testspractice.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTestRecorded {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTestRecorded() {

        onView(allOf(withId(R.id.toDetailsActivityButton), isDisplayed())).apply {
            perform(click())
        }

        onView(allOf(withId(R.id.incrementButton), isDisplayed())).apply {
            perform(click())
        }

        onView(allOf(withId(R.id.totalCountTextView), isDisplayed())).apply {
            check(matches(withText("Number of results: 1")))
        }

        onView(allOf(withId(R.id.incrementButton), isDisplayed())).apply {
            check(matches(isDisplayed()))
        }
    }
}
