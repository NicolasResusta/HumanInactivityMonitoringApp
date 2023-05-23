package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.backendoperations

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.telephony.SmsManager
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.*

/*fun isTheDeviceLocked(context: Context): Boolean {
    return (context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isDeviceSecure
}*/

/**
 * This function takes the current battery the phone is at from a BatteryManager
 * and converts it to an integer for the other functions to use.
 *
 * @param context is the context of the app to extract the phone's battery
 */
fun getTheBatteryPercentage(context: Context): Int {
    val batteryManager = context.getSystemService(BATTERY_SERVICE) as BatteryManager
    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
}

/**
 * This function takes an Intent filter based on a receiver to return a boolean which
 * defines whether the user's phone is charging or not.
 *
 * @param context is the application's context to determine whether the phone is charging or not.
 * @return true if phone is being charged, false if not charging
 */
fun checkIfPhoneIsBeingCharged(context: Context): Boolean {
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { intentFilter ->
        context.registerReceiver(null, intentFilter)
    }
    val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    return (status == BatteryManager.BATTERY_STATUS_CHARGING
            || status == BatteryManager.BATTERY_STATUS_FULL)
}

/**
 * This function makes a phone call to the first emergency contact the user has placed on their
 * emergency list. It is always dependent on the user to have already saved a contact within the list
 * to allow this to work.
 */
@Composable
fun CallingContactNumber() {
    val context = LocalContext.current
    val contactViewModel: ContactViewModel = viewModel()
    val contactList by contactViewModel.contactsInDatabase.observeAsState(listOf())
    if (contactList.isNotEmpty()) {
        val phoneNumber = contactList[contactList.lastIndex].number
        val contactNumber by rememberSaveable { mutableStateOf(phoneNumber) }
        val activity = LocalContext.current as Activity
        val dialingAction = Uri.parse("tel: $contactNumber")

        val intentOfCalling = Intent(Intent.ACTION_CALL, dialingAction)
        if (hasCallingPermission(context)) {
            try {
                context.startActivity(intentOfCalling)
            } catch (s: SecurityException) {
                Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}

/**
 * This function will send an SMS message with the user's predefined emergency message and their
 * current location (as a link if SDK > 28) to all the contacts the user has added to their
 * emergency list.
 *
 * @param context is the context of the application needed to carry out actions such as checking
 * whether the user has allowed the app access to their location or to send SMS.
 * @param location is the current details of the user's location (latitude and longitude numbers)
 */
@Composable
fun SmsSender(context: Context, location: LocationDetails?) {
    val contactViewModel: ContactViewModel = viewModel()
    val contactMethodViewModel: ContactMethodViewModel = viewModel()
    val contactList by contactViewModel.contactsInDatabase.observeAsState(listOf())
    val contactMethodList by contactMethodViewModel.contactMethodSettingsInDatabase.observeAsState(
        listOf()
    )

    val locationUri = "https://www.google.com/maps/?q=${location?.latitude},${location?.longitude}"
    //this makes sure the link always comes out right without spelling or syntax errors

    if (contactList.isNotEmpty() && contactMethodList.isNotEmpty()) {
        val activity = LocalContext.current as Activity
        val messageContent = contactMethodList[contactMethodList.lastIndex].message
        val message by rememberSaveable { mutableStateOf(messageContent) }
        GPS(location)
        if (hasSMSPermission(context) && hasLocationPermission(context)) {
            contactList.forEach { contactNumber ->
                try {
                    val smsManager: SmsManager = SmsManager.getDefault()
                    if (location != null) {
                        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O){
                        smsManager.sendTextMessage(
                            contactNumber.number,
                            null,
                            "$message. \n\nUser's location ---> \n\n" +
                                    "Latitude: ${location.latitude} \n" +
                                    "Longitude: ${location.longitude}",
                            null,
                            null
                        )
                        } else {
                            smsManager.sendTextMessage(
                                contactNumber.number,
                                null,
                                "$message. \n\nUser's location ---> \n\n" +
                                        locationUri,
                                null,
                                null
                            )
                        }
                    }
                    Toast.makeText(
                        context,
                        "Message to ${contactNumber.name} Sent",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error : " + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

/*@Composable
fun SendNotificationIfTimerRunsOut() {
    val context = LocalContext.current
    val wakeLock : PowerManager.WakeLock
    val pm : PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "appname::tag")
    val timeAndBatteryViewModel: TimeAndBatteryViewModel = viewModel()
    val timeAndBatterySettingsList by timeAndBatteryViewModel.timeAndBatterySettingsInDatabase.observeAsState(
        listOf()
    )
    wakeLock.acquire(30*60*1000L *//*30 minutes*//*)
    if (timeAndBatterySettingsList.isNotEmpty()) {
        var timerTime =
            timeAndBatterySettingsList[timeAndBatterySettingsList.lastIndex].time.toLong()
        val typeTime = timeAndBatterySettingsList[timeAndBatterySettingsList.lastIndex].timeType
        val timerTypeTime by rememberSaveable { mutableStateOf(typeTime) }
        var isTimerRunning by rememberSaveable { mutableStateOf(true) }

        when (timerTypeTime) {
            "seconds" -> {
                timerTime *= 1000
            }
            "minutes" -> {
                timerTime *= 60000
            }
            "hours" -> {
                timerTime *= 3600000
            }
            "days" -> {
                timerTime *= 86400000
            }
        }


        val timer = object : CountDownTimer(timerTime, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                println("seconds remaining: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                isTimerRunning = false
            }
        }

        if(isTheDeviceLocked(context)){
            timer.cancel()
            timer.start()
        } else if(!isTheDeviceLocked(context)){
            timer.cancel()
            wakeLock.release()
        }
        if (!isTimerRunning){
            timer.cancel()
            displayNotification(context)
        }
    }
}*/

/**
 * This function will send a notification to the user's phone if it gets below a certain
 * battery percentage. This process will take place whenever the onStart Android process takes place
 * (i.e. when the user gets onto the app).
 *
 */
@Composable
fun SendNotificationIfBatteryBelowPercentage() {
    val context = LocalContext.current
    val timeAndBatteryViewModel: TimeAndBatteryViewModel = viewModel()
    val timeAndBatterySettingsList by timeAndBatteryViewModel.timeAndBatterySettingsInDatabase.observeAsState(
        listOf()
    )
    if (timeAndBatterySettingsList.isNotEmpty()) {
        val batteryPercentage =
            timeAndBatterySettingsList[timeAndBatterySettingsList.lastIndex].battery
        val minimumBattery by rememberSaveable { mutableStateOf(batteryPercentage) }

        if (!checkIfPhoneIsBeingCharged(context) && getTheBatteryPercentage(context) < minimumBattery) {
            displayNotification(context)
        }
    }
}

/**
 * This function checks whether the user has allowed the app access to making phone calls.
 *
 * @param context is making the callback to Android to check on the permission
 * @return true if the access has been allowed
 */
fun hasCallingPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_GRANTED;
}

/*fun requestCallingPermission(context: Context, activity: Activity) {
    if (!hasCallingPermission(context)) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
    }
}*/

/**
 * This function checks whether the user has allowed the app access to the user's current location.
 *
 * @param context is making the callback to Android to check on the permission
 * @return true if the access has been allowed
 */
fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED;
}

/*
fun requestLocationPermission(context: Context, activity: Activity) {
    if (!hasLocationPermission(context)) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
    }
}
*/

/**
 * This function checks whether the user has allowed the app access to sending SMS messages.
 *
 * @param context is making the callback to Android to check on the permission
 * @return true if the access has been allowed
 */
fun hasSMSPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) ==
            PackageManager.PERMISSION_GRANTED;
}

/*fun requestSMSPermission(context: Context, activity: Activity) {
    if (!hasSMSPermission(context)) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.SEND_SMS), 1)
    }
}*/

/**
 * This function converts the information on location the LocationDetails class generates into
 * readable and organised latitude and longitude coordinates.
 *
 * @param location is the user's location raw details
 */
@Composable
fun GPS(location: LocationDetails?) {
    location?.let {
        Text(text = "${location.latitude} \n ${location.longitude}")
    }
}

@Composable
fun TestingScreenTopLevel(
    navController: NavHostController,
    locationViewModel: LocationViewModel = viewModel(),
    contactViewModel: ContactViewModel = viewModel(),
    contactMethodViewModel: ContactMethodViewModel = viewModel(),
    timeAndBatteryViewModel: TimeAndBatteryViewModel = viewModel()
) {
    val currentLocation by locationViewModel.getLocationLiveData().observeAsState()
    val contactList by contactViewModel.contactsInDatabase.observeAsState(listOf())
    val timeAndBatteryList by timeAndBatteryViewModel.timeAndBatterySettingsInDatabase.observeAsState(
        listOf()
    )
    val contactMethodSettingsList by contactMethodViewModel.contactMethodSettingsInDatabase.observeAsState(
        listOf()
    )

    TestingScreen(
        navController = navController,
        location = currentLocation,
        contactsList = contactList,
        contactMethodSettingsList,
        timeAndBatterySettingsList = timeAndBatteryList
    )
}

@Composable
fun TestingScreen(
    navController: NavHostController,
    location: LocationDetails?,
    contactsList: List<Contact> = listOf(),
    contactMethodSettingsList: List<ContactMethod> = listOf(),
    timeAndBatterySettingsList: List<TimeAndBattery> = listOf()
) {
    Column(
        modifier = Modifier
            .padding(
                start = 50.dp,
                top = 40.dp,
                end = 50.dp
            )
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        if (contactsList.isNotEmpty() && contactMethodSettingsList.isNotEmpty() && timeAndBatterySettingsList.isNotEmpty()) {
            val context = LocalContext.current
            var makeFunction by rememberSaveable { mutableStateOf(false) }
            var openDialog by rememberSaveable { mutableStateOf(false) }


            Button(onClick = { openDialog = true }) {
                Text(text = "Trigger a notification")
                if (openDialog) {
                    AlertDialog(
                        onDismissRequest = { },
                        title = {
                            Text(text = "Notification Trigger")
                        },
                        text = {
                            Text(text = "Confirm for the notification to be shown")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    makeFunction = true
                                }
                            ) {
                                Text(text = "Confirm")
                                if (makeFunction) {
                                    openDialog = false
                                    displayNotification(context)
                                    makeFunction = false
                                }
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openDialog = false
                                }
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    )
                }
            }
            //SendNotificationIfBatteryNotRight()
            //SendNotificationIfTimerRunsOut()
        }
    }
}

