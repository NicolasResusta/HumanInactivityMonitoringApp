package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.timeAndBatterySettings

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.Contact
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBattery
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBatteryViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components.ScaffoldTimeAndBatterySettingsScreen
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components.modifyContact
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.Screen

/**
 * This function represents the top level used to connect the UI with the view model in order to be
 * able to allow communication with the database. It is also the function to be used to represent
 * the screen in the MainActivity navigation graph.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param timeAndBatteryViewModel is used to connect the TimeAndBatteryViewModel class to the UI
 * from this screen
 */
@Composable
fun TimeAndBatterySettingsScreenTopLevel(
    navController: NavHostController,
    timeAndBatteryViewModel: TimeAndBatteryViewModel = viewModel()
){
    val timeAndBatterySettingsList by timeAndBatteryViewModel.timeAndBatterySettingsInDatabase.observeAsState(
        listOf())

    TimeAndBatterySettingsScreen(
        navController = navController,
        insertTimeAndBatterySetting = { newTimeAndBatterySetting ->
            timeAndBatteryViewModel.insertTimeAndBatterySetting(newTimeAndBatterySetting)
        },
        deleteTimeAndBatterySetting = {selectedTimeAndBatterySetting ->
            timeAndBatteryViewModel.deleteTimeAndBatterySetting(selectedTimeAndBatterySetting)
        },
        timeAndBatterySettingsList = timeAndBatterySettingsList
        )

}

/**
 * This function represents the time and battery settings screen, where the user can edit their
 * maximum allowed locked screen time, and the lowest the devices battery can get before making
 * it an emergency.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param insertTimeAndBatterySetting is the lambda function which performs the insertion of new
 * time and battery settings into the database.
 * @param deleteTimeAndBatterySetting is the lambda function which performs the deletion of the
 * current time and battery settings present in the database.
 * @param timeAndBatterySettingsList is the list object used to access the elements found in the
 * time and battery settings database.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeAndBatterySettingsScreen(
    navController: NavHostController,
    insertTimeAndBatterySetting: (TimeAndBattery) -> Unit = {},
    deleteTimeAndBatterySetting: (TimeAndBattery) -> Unit = {},
    timeAndBatterySettingsList: List<TimeAndBattery>
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var userTime by rememberSaveable { mutableStateOf("") }
    var userBattery by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var start by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current.applicationContext
    val menuItemList = listOf("Time type", "seconds", "minutes", "hours", "days")
    var selectedItem by rememberSaveable { mutableStateOf(menuItemList[0]) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    ScaffoldTimeAndBatterySettingsScreen(
        navController = navController,
        coroutineScope = coroutineScope,
        pageContent = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.padding(15.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 25.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.time_message),
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        //fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.padding(15.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 25.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .weight(1f),
                        value = userTime,
                        onValueChange = { time ->
                            userTime = time
                        },
                        label = {
                            Text(text = "Input the time")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        },
                        modifier = Modifier
                            //.padding(start = 25.dp, end = 20.dp)
                            .weight(1f)
                    ) {
                        TextField(
                            value = selectedItem,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            menuItemList.forEach { selectedOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedItem = selectedOption
                                        //Toast.makeText(context, selectedOption, Toast.LENGTH_SHORT)
                                        //    .show()
                                        expanded = false
                                    },
                                    text = {
                                        Text(text = selectedOption)
                                    }
                                )
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.padding(20.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 25.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.battery_message),
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        //fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 25.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(start = 25.dp, end = 20.dp)
                            .weight(9f),
                        value = userBattery,
                        onValueChange = { battery ->
                            userBattery = battery
                        },
                        label = {
                            Text(text = "Input the appropriate %")
                                },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Icon(
                        modifier = Modifier
                            //.padding(start = 10.dp)
                            .weight(1f),
                        imageVector = Icons.Default.BatteryFull,
                        contentDescription = stringResource(id = R.string.battery_icon)
                    )
                }

                Spacer(modifier = Modifier.padding(40.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 25.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if(userBattery.isEmpty() || userTime.isEmpty() || userBattery.toInt() < 5 || userBattery.toInt() > 100 || userTime.toInt() < 0 || selectedItem == menuItemList[0]){
                                isError = true
                                openDialog = true
                            } else {
                                changeTimeAndBatterySettings(
                                    time = userTime.toInt(),
                                    battery = userBattery.toInt(),
                                    timeType = selectedItem,
                                    doAction = { newTimeAndBatterySetting ->
                                        deleteTimeAndBatterySetting(timeAndBatterySettingsList[timeAndBatterySettingsList.lastIndex])
                                        insertTimeAndBatterySetting(newTimeAndBatterySetting)
                                        Toast.makeText(
                                            context,
                                            "The settings have been saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        userBattery = ""
                                        userTime = ""
                                        selectedItem = menuItemList[0]
                                        focusManager.clearFocus()
                                    }
                                )
                            }
                                  },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f)
                    ) {
                        Text(text = stringResource(id = R.string.save_button))
                        if(isError && openDialog){
                            AlertDialog(
                                onDismissRequest = { },
                                title = {
                                    Text(text = "Error")
                                },
                                text = {
                                    Text(text = "All the fields have to be filled\n" +
                                            "The battery has to be between 5 - 100\n" +
                                            "The time can't be negative\n" +
                                            "The type has to be either mins, hrs or days")
                                },
                                confirmButton = {
                                    TextButton(onClick = { openDialog = false }) {
                                        Text(text = "Close")
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = stringResource(id = R.string.error_icon)
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(30.dp))

                    Button(
                        onClick = {
                            userBattery = ""
                            userTime = ""
                            selectedItem = menuItemList[0]
                            focusManager.clearFocus()
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f)
                    ) {
                        Text(text = stringResource(id = R.string.clear_button))
                    }
                }
            }
        }
    )
}

/**
 * This function is used to implement the viewModel functions within the UI
 *
 * @param time is the time number for the user to have their screen locked.
 * @param battery is the lowest the battery can get before becoming an emergency.
 * @param timeType is the time type (hours, days, etc) for the user to have their screen locked.
 * @param doAction the action which uses the name and number of the contact to get carried out.
 */
private fun changeTimeAndBatterySettings(
    time: Int,
    battery: Int,
    timeType: String,
    doAction: (TimeAndBattery) -> Unit = {}
){
    if(time >= 0 && battery >= 0 && timeType.isNotEmpty()){
        val timeAndBattery = TimeAndBattery(
            id = 0,
            time = time,
            battery = battery,
            timeType = timeType
        )
        doAction(timeAndBattery)
    }

}
