package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.composetests

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.MainActivity
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ComposeUnitTesting {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun getStartingScreen() {
        val title = composeTestRule.activity.getString(R.string.home_screen_title)
        val nameTitle = composeTestRule.activity.getString(R.string.contact_name)
        val numberTitle = composeTestRule.activity.getString(R.string.contact_number)

        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText(nameTitle).assertExists()
        composeTestRule.onNodeWithText(numberTitle).assertExists()
        composeTestRule.onNodeWithText("Add Contact  ").assertExists()
    }

    @Test
    fun getAddContactButtonScreenFunctionality() {

        val title = composeTestRule.activity.getString(R.string.add_contact_screen)
        val btnDesc = composeTestRule.activity.getString(R.string.add_contact_button)
        val btnSave = composeTestRule.activity.getString(R.string.save_button)
        val btnClear = composeTestRule.activity.getString(R.string.clear_button)

        composeTestRule.onNodeWithText("Add Contact  ").performClick()
        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText("Press the icon to access the contacts").assertExists()
        composeTestRule.onNodeWithText("Contact Name").assertExists()
        composeTestRule.onNodeWithText("Contact Number").assertExists()
        composeTestRule.onNodeWithContentDescription(btnDesc).assertExists().assertHasClickAction()
        composeTestRule.onNodeWithText(btnSave).assertExists()
        composeTestRule.onNodeWithText(btnClear).assertExists()
    }

    @Test
    fun getContactsShown() {
        val title = composeTestRule.activity.getString(R.string.add_contact_screen)
        val btnDesc = composeTestRule.activity.getString(R.string.add_contact_button)
        val btnSave = composeTestRule.activity.getString(R.string.save_button)
        val btnClear = composeTestRule.activity.getString(R.string.clear_button)

        composeTestRule.onNodeWithText("Add Contact  ").performClick()
        composeTestRule.onNodeWithContentDescription(btnDesc).assertHasClickAction()
    }

    @Test
    fun changingTimeAndBatterySettingsUsingNavDrawer() {
        val navDrawer = composeTestRule.activity.getString(R.string.nav_drawer_menu)
        val timeAndBatteryScreen =
            composeTestRule.activity.getString(R.string.time_and_battery_screen)
        val btnSave = composeTestRule.activity.getString(R.string.save_button)
        val arrowIcon = composeTestRule.activity.getString(R.string.arrow_icon)
        val currentSettingScreenIcon = composeTestRule.activity.getString(R.string.current_settings)

        composeTestRule.onNodeWithContentDescription(navDrawer).performClick()
        composeTestRule.onNodeWithContentDescription(timeAndBatteryScreen).performClick()
        composeTestRule.onNodeWithText("Input the time").performClick().performTextInput("20")
        composeTestRule.onNodeWithText("Time type").performClick()
        composeTestRule.onNodeWithText("minutes").performClick()
        composeTestRule.onNodeWithText("Input the appropriate %").performClick()
            .performTextInput("20")
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithContentDescription(arrowIcon).performClick()
        composeTestRule.onNodeWithContentDescription(currentSettingScreenIcon).performClick()
        composeTestRule.onNodeWithText("20 minutes").assertExists()
        composeTestRule.onNodeWithText("20 %").assertExists()

    }

    @Test
    fun changingTimeAndBatterySettingsUsingChangeSettingsButton() {
        val currentSettingScreenIcon = composeTestRule.activity.getString(R.string.current_settings)
        val changeSettingsButton =
            composeTestRule.activity.getString(R.string.change_button_timeAndBat)
        val btnSave = composeTestRule.activity.getString(R.string.save_button)
        val arrowIcon = composeTestRule.activity.getString(R.string.arrow_icon)

        composeTestRule.onNodeWithContentDescription(currentSettingScreenIcon).performClick()
        composeTestRule.onNodeWithText(changeSettingsButton).performClick()
        composeTestRule.onNodeWithText("Input the time").performClick().performTextInput("20")
        composeTestRule.onNodeWithText("Time type").performClick()
        composeTestRule.onNodeWithText("minutes").performClick()
        composeTestRule.onNodeWithText("Input the appropriate %").performClick()
            .performTextInput("20")
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithContentDescription(arrowIcon).performClick()
        composeTestRule.onNodeWithContentDescription(currentSettingScreenIcon).performClick()
        composeTestRule.onNodeWithText("20 minutes").assertExists()
        composeTestRule.onNodeWithText("20 %").assertExists()
    }

    @Test
    fun tryInputtingWrongDetailsOnTimeAndBatteryScreen() {
        val navDrawer = composeTestRule.activity.getString(R.string.nav_drawer_menu)
        val timeAndBatteryScreen =
            composeTestRule.activity.getString(R.string.time_and_battery_screen)
        val btnSave = composeTestRule.activity.getString(R.string.save_button)
        val arrowIcon = composeTestRule.activity.getString(R.string.arrow_icon)
        val errorIcon = composeTestRule.activity.getString(R.string.error_icon)
        val btnClear = composeTestRule.activity.getString(R.string.clear_button)

        composeTestRule.onNodeWithContentDescription(navDrawer).performClick()
        composeTestRule.onNodeWithContentDescription(timeAndBatteryScreen).performClick()
        composeTestRule.onNodeWithText("Input the time").performClick().performTextInput("-50")
        composeTestRule.onNodeWithText("Time type").performClick()
        composeTestRule.onNodeWithText("minutes").performClick()
        composeTestRule.onNodeWithText("Input the appropriate %").performClick()
            .performTextInput("20")
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithContentDescription(errorIcon).assertExists()
        composeTestRule.onNodeWithText("Close").assertExists().performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithText("Input the time").performClick().performTextInput("50")
        composeTestRule.onNodeWithText("Input the appropriate %").performClick()
            .performTextInput("20")
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithContentDescription(errorIcon).assertExists()
        composeTestRule.onNodeWithText("Close").assertExists().performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithText("Input the time").performClick().performTextInput("50")
        composeTestRule.onNodeWithText("Time type").performClick()
        composeTestRule.onNodeWithText("minutes").performClick()
        composeTestRule.onNodeWithText("Input the appropriate %").performClick()
            .performTextInput("150")
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithContentDescription(errorIcon).assertExists()
        composeTestRule.onNodeWithText("Close").assertExists().performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithContentDescription(errorIcon).assertExists()
        composeTestRule.onNodeWithText("Close").assertExists().performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithContentDescription(arrowIcon).performClick()
    }

    @Test
    fun changeContactMethodSettingsUsingNavDrawer() {
        val navDrawer = composeTestRule.activity.getString(R.string.nav_drawer_menu)
        val contactMethodScreen = composeTestRule.activity.getString(R.string.contact_method_screen)
        val btnClear = composeTestRule.activity.getString(R.string.clear_button)
        val btnSave = composeTestRule.activity.getString(R.string.save_button)
        val arrowIcon = composeTestRule.activity.getString(R.string.arrow_icon)
        val currentSettingScreenIcon = composeTestRule.activity.getString(R.string.current_settings)

        composeTestRule.onNodeWithContentDescription(navDrawer).performClick()
        composeTestRule.onNodeWithContentDescription(contactMethodScreen).performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithText("Emergency Message").performClick()
            .performTextInput("EMERGENCY MESSAGE!!")
        composeTestRule.onNodeWithText("Select an option").performClick()
        composeTestRule.onNodeWithText("Calling & SMS").performClick()
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithContentDescription(arrowIcon).performClick()
        composeTestRule.onNodeWithContentDescription(currentSettingScreenIcon).performClick()
        composeTestRule.onNodeWithText("EMERGENCY MESSAGE!!").assertExists()
        composeTestRule.onNodeWithText("Calling & SMS").assertExists()

    }

    @Test
    fun changingContactMethodSettingsUsingChangeSettingsButton() {
        val currentSettingScreenIcon = composeTestRule.activity.getString(R.string.current_settings)
        val changeSettingsButton =
            composeTestRule.activity.getString(R.string.change_button_contMethod)
        val btnSave = composeTestRule.activity.getString(R.string.save_button)
        val arrowIcon = composeTestRule.activity.getString(R.string.arrow_icon)
        val btnClear = composeTestRule.activity.getString(R.string.clear_button)

        composeTestRule.onNodeWithContentDescription(currentSettingScreenIcon).performClick()
        composeTestRule.onNodeWithText(changeSettingsButton).performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithText("Emergency Message").performClick()
            .performTextInput("EMERGENCY MESSAGE!!")
        composeTestRule.onNodeWithText("Select an option").performClick()
        composeTestRule.onNodeWithText("Calling & SMS").performClick()
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithContentDescription(arrowIcon).performClick()
        composeTestRule.onNodeWithContentDescription(currentSettingScreenIcon).performClick()
        composeTestRule.onNodeWithText("EMERGENCY MESSAGE!!").assertExists()
        composeTestRule.onNodeWithText("Calling & SMS").assertExists()

    }

    @Test
    fun tryInputtingWrongDetailsOnContactMethodScreen() {
        val navDrawer = composeTestRule.activity.getString(R.string.nav_drawer_menu)
        val contactMethodScreen = composeTestRule.activity.getString(R.string.contact_method_screen)
        val btnSave = composeTestRule.activity.getString(R.string.save_button)
        val arrowIcon = composeTestRule.activity.getString(R.string.arrow_icon)
        val errorIcon = composeTestRule.activity.getString(R.string.error_icon)
        val btnClear = composeTestRule.activity.getString(R.string.clear_button)

        composeTestRule.onNodeWithContentDescription(navDrawer).performClick()
        composeTestRule.onNodeWithContentDescription(contactMethodScreen).performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithText("Emergency Message").performClick()
            .performTextInput("EMERGENCY MESSAGE!!")
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithContentDescription(errorIcon).assertExists()
        composeTestRule.onNodeWithText("Close").assertExists().performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithText("Select an option").performClick()
        composeTestRule.onNodeWithText("Calling & SMS").performClick()
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithContentDescription(errorIcon).assertExists()
        composeTestRule.onNodeWithText("Close").assertExists().performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithText(btnSave).performClick()
        composeTestRule.onNodeWithText("Error").assertExists()
        composeTestRule.onNodeWithContentDescription(errorIcon).assertExists()
        composeTestRule.onNodeWithText("Close").assertExists().performClick()
        composeTestRule.onNodeWithText(btnClear).performClick()
        composeTestRule.onNodeWithContentDescription(arrowIcon).performClick()

    }

    @Test
    fun deletingAContact() {
        val btnDelete = composeTestRule.activity.getString(R.string.delete_button)

        composeTestRule.onNodeWithContentDescription(btnDelete).assertExists().performClick()
        composeTestRule.onNodeWithText("Are you sure you want to delete this contact?").assertExists()
        composeTestRule.onNodeWithText("Confirm").assertExists()
        composeTestRule.onNodeWithText("Cancel").assertExists().performClick()
        composeTestRule.onNodeWithContentDescription(btnDelete).assertExists().performClick()
        composeTestRule.onNodeWithText("Are you sure you want to delete this contact?").assertExists()
        composeTestRule.onNodeWithText("Cancel").assertExists()
        composeTestRule.onNodeWithText("Confirm").assertExists().performClick()

    }

}