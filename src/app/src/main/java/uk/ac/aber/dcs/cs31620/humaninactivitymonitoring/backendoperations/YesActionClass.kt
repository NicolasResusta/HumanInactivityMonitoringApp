package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.backendoperations

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.BuildNavigationGraph
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.MainActivity
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactMethodViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.theme.HumanInactivityMonitoringTheme

/**
 * This is the class which is triggered if the user press "Yes" to the notification shown when an
 * emergency is detected. It is a ComponentActivity class to be in accordance with the latest Kotlin
 * practices.
 */
class YesActionClass : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HumanInactivityMonitoringTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    onBackPressedDispatcher.onBackPressed()
                    DoActionBasedOnChoice()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        startActivity(Intent(this, MainActivity::class.java))
    }
}

/**
 * This function will perform different actions based on what the user has defined within the
 * "contact method settings screen" in the app. This choice will either be making a call to the
 * first emergency contact, sending an SMS to all the emergency contacts, or both. Once the action
 * is started the notification will be dismissed.
 */
@Composable
fun DoActionBasedOnChoice() {
    val context = LocalContext.current
    val contactMethodViewModel: ContactMethodViewModel = viewModel()
    val locationViewModel: LocationViewModel = viewModel()
    val contactMethodSettingsList by contactMethodViewModel.contactMethodSettingsInDatabase.observeAsState(
        listOf()
    )
    if (contactMethodSettingsList.isNotEmpty()) {
        val contactChoice =
            contactMethodSettingsList[contactMethodSettingsList.lastIndex].contactType
        val currentLocation by locationViewModel.getLocationLiveData().observeAsState()
        val contactMethodTypeChoice by rememberSaveable { mutableStateOf(contactChoice) }

        when (contactMethodTypeChoice) {
            "Calling" -> {
                CallingContactNumber()
            }
            "SMS message" -> {
                SmsSender(context = context, location = currentLocation)
            }
            "Calling & SMS" -> {
                SmsSender(context = context, location = currentLocation)
                CallingContactNumber()
            }
        }
    }
    with(NotificationManagerCompat.from(context)) {
        cancel(884) //This cancels out the notification
    }
}