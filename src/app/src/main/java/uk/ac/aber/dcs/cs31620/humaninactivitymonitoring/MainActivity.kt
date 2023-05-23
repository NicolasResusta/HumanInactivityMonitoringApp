package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.backendoperations.*
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource.ContactMethodRepository
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource.ContactRepository
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource.TimeAndBatteryRepository
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.Contact
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactMethodViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBatteryViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.contactMethod.ContactMethodSettingsScreenTopLevel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.currentSettings.CurrentSettingsScreenTopLevel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.home.AddContactScreenTopLevel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.home.HomeScreenTopLevel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.theme.HumanInactivityMonitoringTheme
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.timeAndBatterySettings.TimeAndBatterySettingsScreenTopLevel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HumanInactivityMonitoringTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(!hasLocationPermission(this) || !hasCallingPermission(this) || !hasSMSPermission(this)){
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE), 1)
            //This will request all the permissions needed to run the app
        }
    }

    override fun onPause() {
        super.onPause()
        startService(Intent(this, ServiceClass::class.java))
    }

    override fun onResume() {
        super.onResume()
        stopService(Intent(this, ServiceClass::class.java))
        setContent {
            HumanInactivityMonitoringTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                    SendNotificationIfBatteryBelowPercentage()
                }
            }
        }
    }
}

/**
 * This function builds the navigation graph which the navController uses to determine which screen
 * should be shown depending on the action carried out or the state the app is in.
 *
 * @param timeAndBatteryViewModel is used to connect the TimeAndBatteryViewModel class to the UI
 * from this screen.
 * @param contactMethodViewModel is used to connect the ContactMethodViewModel class to the UI from
 * this screen.
 * @param locationViewModel is used to connect the LocationViewModel class to the UI from this
 * screen.
 * @param contactViewModel is used to connect the ContactViewModel class to the UI from this screen.
 */
@Composable
fun BuildNavigationGraph(
    contactViewModel: ContactViewModel = viewModel(),
    timeAndBatteryViewModel: TimeAndBatteryViewModel = viewModel(),
    contactMethodViewModel: ContactMethodViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel()
) {
    val contactList by contactViewModel.contactsInDatabase.observeAsState(listOf())
    val timeAndBatteryList by timeAndBatteryViewModel.timeAndBatterySettingsInDatabase.observeAsState(
        listOf()
    )
    val contactMethodList by contactMethodViewModel.contactMethodSettingsInDatabase.observeAsState(
        listOf()
    )
    val navController = rememberNavController()
    val startDestination = remember { Screen.HomeScreen.route }
    val context = LocalContext.current.applicationContext

    LaunchedEffect(key1 = 1) {
        val contactRepository = ContactRepository(context as Application)
        val timeAndBatteryRepository = TimeAndBatteryRepository(context as Application)
        val contactMethodRepository = ContactMethodRepository(context as Application)
        contactRepository.getContactsSync()
        timeAndBatteryRepository.getAllTimeAndBatterySettingsSync()
        contactMethodRepository.getAllContactMethodSettingsSync()
    }


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreenTopLevel(
                navController = navController,
                contactViewModel
            )
        }
        composable(Screen.TimeAndBatterySettingsScreen.route) {
            TimeAndBatterySettingsScreenTopLevel(
                navController = navController,
                timeAndBatteryViewModel = timeAndBatteryViewModel
            )
        }
        composable(Screen.ContactMethodSettingScreen.route) {
            ContactMethodSettingsScreenTopLevel(
                navController = navController,
                contactMethodViewModel = contactMethodViewModel
            )
        }
        composable(Screen.CurrentSettingsScreen.route) {
            CurrentSettingsScreenTopLevel(
                navController = navController,
                timeAndBatteryViewModel = timeAndBatteryViewModel,
                contactMethodViewModel = contactMethodViewModel
            )
        }
        composable(Screen.AddContactScreen.route) {
            AddContactScreenTopLevel(
                navController = navController,
                contactViewModel = contactViewModel
            )
        }
        /*composable(Screen.TestingScreen.route) {
            TestingScreenTopLevel(
                navController = navController,
                locationViewModel = locationViewModel,
                contactViewModel = contactViewModel,
                contactMethodViewModel = contactMethodViewModel,
                timeAndBatteryViewModel = timeAndBatteryViewModel
            )
        }*/
    }
    with(NotificationManagerCompat.from(context)) {
        cancel(884) //This cancels out the notification
        //cancel(520)
    }
}
