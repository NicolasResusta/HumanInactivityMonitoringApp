package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.backendoperations

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.MainActivity
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.contactMethod.ContactMethodSettingsScreenTopLevel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.currentSettings.CurrentSettingsScreenTopLevel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.home.AddContactScreenTopLevel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.home.HomeScreenTopLevel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.theme.HumanInactivityMonitoringTheme
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.timeAndBatterySettings.TimeAndBatterySettingsScreenTopLevel

class NoActionClass : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HumanInactivityMonitoringTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {
                        composable(Screen.HomeScreen.route) {
                            HomeScreenTopLevel(
                                navController = navController
                            )
                        }
                        composable(Screen.TimeAndBatterySettingsScreen.route) {
                            TimeAndBatterySettingsScreenTopLevel(
                                navController = navController
                            )
                        }
                        composable(Screen.ContactMethodSettingScreen.route) {
                            ContactMethodSettingsScreenTopLevel(
                                navController = navController
                            )
                        }
                        composable(Screen.CurrentSettingsScreen.route) {
                            CurrentSettingsScreenTopLevel(
                                navController = navController
                            )
                        }
                        composable(Screen.AddContactScreen.route) {
                            AddContactScreenTopLevel(
                                navController = navController
                            )
                        }
                        composable(Screen.TestingScreen.route) {
                            TestingScreenTopLevel(
                                navController = navController
                            )
                        }
                    }
                    with(NotificationManagerCompat.from(this)) {
                        cancel(884)
                    }//This removes the notification once the "No" is pressed
                }
            }
        }
    }
}