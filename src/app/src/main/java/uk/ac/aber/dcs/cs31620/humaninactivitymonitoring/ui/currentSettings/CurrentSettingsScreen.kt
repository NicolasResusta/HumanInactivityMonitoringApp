package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.currentSettings

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactMethod
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactMethodViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBattery
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBatteryViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components.MainScaffold
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.Screen
import kotlin.math.absoluteValue

/**
 * This function represents the top level used to connect the UI with the view model in order to be
 * able to allow communication with the database. It is also the function to be used to represent
 * the screen in the MainActivity navigation graph.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param contactMethodViewModel is used to connect the ContactMethodViewModel class to the UI from
 * this screen
 * @param timeAndBatteryViewModel is used to connect the TimeAndBatteryViewModel class to the UI from
 * this screen
 */
@Composable
fun CurrentSettingsScreenTopLevel(
    navController: NavHostController,
    timeAndBatteryViewModel: TimeAndBatteryViewModel = viewModel(),
    contactMethodViewModel: ContactMethodViewModel = viewModel()
){
    val timeAndBatterySettingsList by timeAndBatteryViewModel.timeAndBatterySettingsInDatabase.observeAsState(
        listOf())
    val contactMethodSettingsList by contactMethodViewModel.contactMethodSettingsInDatabase.observeAsState(
        listOf())

    CurrentSettingsScreen(
        navController = navController,
        timeAndBatterySettingsList = timeAndBatterySettingsList,
        contactMethodSettingsList = contactMethodSettingsList
    )
}

/**
 * This function represents the screen where all the current time and battery plus contact method
 * settings will be displayed. This means that it will take the information from the database and
 * show it in this screen.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param contactMethodSettingsList is the list object used to access the elements found in the
 * contact method database.
 * @param timeAndBatterySettingsList is the list object used to access all the elements found in the
 * time and battery database.
 */
@Composable
fun CurrentSettingsScreen(
    navController: NavHostController,
    timeAndBatterySettingsList: List<TimeAndBattery>,
    contactMethodSettingsList: List<ContactMethod>
){
    if(timeAndBatterySettingsList.isNotEmpty()) {
        val coroutineScope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current
        val lockedScreenTime = timeAndBatterySettingsList[timeAndBatterySettingsList.lastIndex].time
        val typeTime = timeAndBatterySettingsList[timeAndBatterySettingsList.lastIndex].timeType
        val battery = timeAndBatterySettingsList[timeAndBatterySettingsList.lastIndex].battery
        val contactCustomMessage = contactMethodSettingsList[contactMethodSettingsList.lastIndex].message
        val contactType = contactMethodSettingsList[contactMethodSettingsList.lastIndex].contactType

        MainScaffold(
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
                    Spacer(modifier = Modifier.height(50.dp))

                    ConstraintLayout {
                        val (title1, box1, title2, box2) = createRefs()

                        Text(
                            text = "Current Locked Screen Timer",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                                .focusable()
                                .constrainAs(title1) {
                                    /*start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)*/
                                },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "${lockedScreenTime.absoluteValue} $typeTime",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                                .focusable()
                                .constrainAs(box1) {
                                    top.linkTo(title1.bottom)
                                },
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Current Lowest Battery % Allowed",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                                .focusable()
                                .constrainAs(title2) {
                                    top.linkTo(box1.bottom)
                                },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "${battery.absoluteValue} %",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                                .focusable()
                                .constrainAs(box2) {
                                    top.linkTo(title2.bottom)
                                },
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 65.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                navController.navigate(Screen.TimeAndBatterySettingsScreen.route)
                                focusManager.clearFocus()
                            }
                        ) {
                            Text(text = stringResource(id = R.string.change_button_timeAndBat))
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    ConstraintLayout {
                        val (title1, box1, title2, box2) = createRefs()

                        Text(
                            text = "Current SMS custom message",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                                .focusable()
                                .constrainAs(title1) {
                                    /*start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)*/
                                },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = contactCustomMessage,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                                .focusable()
                                .constrainAs(box1) {
                                    top.linkTo(title1.bottom)
                                },
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Current Method Of Contact",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                                .focusable()
                                .constrainAs(title2) {
                                    top.linkTo(box1.bottom)
                                },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = contactType,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                                .focusable()
                                .constrainAs(box2) {
                                    top.linkTo(title2.bottom)
                                },
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    //Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 65.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                navController.navigate(Screen.ContactMethodSettingScreen.route)
                                focusManager.clearFocus()
                            }
                        ) {
                            Text(text = stringResource(id = R.string.change_button_contMethod))
                        }
                    }
                }
            }
        )
    }
}