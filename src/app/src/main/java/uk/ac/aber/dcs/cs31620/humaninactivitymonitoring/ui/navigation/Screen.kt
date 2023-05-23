package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation

/**
 * This class holds the different screens which would be shown throughout the app based on the route
 * given to them.
 *
 * @param route this helps the navController from the navigation graph to follow the state of the
 * different screens through the flow of actions in the app's lifecycle.
 */
sealed class Screen(
    val route: String
){
    object HomeScreen : Screen("homeScreen")
    object TimeAndBatterySettingsScreen : Screen("timeAndBatterySettingsScreen")
    object ContactMethodSettingScreen : Screen("contactMethodSettingScreen")
    object CurrentSettingsScreen : Screen("currentSettingsScreen")
    object AddContactScreen : Screen("addContactScreen")
    object TestingScreen : Screen("testingScreen")
    object NoActionScreen : Screen("noActionScreen")
}

val screens = listOf(
    Screen.HomeScreen,
    Screen.CurrentSettingsScreen
)
