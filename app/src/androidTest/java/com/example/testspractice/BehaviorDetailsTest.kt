package com.example.testspractice

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 27)
class BehaviorDetailsTest {

    private val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val packageName = context.packageName

    @Before
    fun setup() {

        uiDevice.pressHome()

        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        Assert.assertNotNull(intent)

        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        context.startActivity(intent)

        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)

        val toDetails: UiObject = uiDevice.findObject(UiSelector().textContains("to details"))
        toDetails.clickAndWaitForNewWindow()
    }

    @Test
    fun test_DetailsActivityIsStarted() {
        val decrementButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        Assert.assertNotNull(decrementButton)
    }

    @Test
    fun test_DecrementButtonIsWorking() {
        val decrementButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        decrementButton.click()
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )
        assertEquals(changedText.text, "Number of results: -1")
    }

    @Test
    fun test_IncrementButtonIsWorking() {
        val incrementButton = uiDevice.findObject(By.res(packageName, "incrementButton"))
        incrementButton.click()
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )
        assertEquals(changedText.text, "Number of results: 1")
    }

    companion object {
        private const val TIMEOUT = 5000L
    }
}