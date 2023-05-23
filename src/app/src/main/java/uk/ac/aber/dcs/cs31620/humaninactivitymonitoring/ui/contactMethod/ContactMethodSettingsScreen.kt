package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.contactMethod

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactMethod
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactMethodViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBattery
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBatteryViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components.ScaffoldContactMethodSettings
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.timeAndBatterySettings.TimeAndBatterySettingsScreen

/**
 * This function represents the top level used to connect the UI with the view model in order to be
 * able to allow communication with the database. It is also the function to be used to represent
 * the screen in the MainActivity navigation graph.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param contactMethodViewModel is used to connect the ContactMethodViewModel class to the UI from
 * this screen
 */
@Composable
fun ContactMethodSettingsScreenTopLevel(
    navController: NavHostController,
    contactMethodViewModel: ContactMethodViewModel = viewModel()
){
    val contactMethodSettingsList by contactMethodViewModel.contactMethodSettingsInDatabase.observeAsState(
        listOf())

    ContactMethodSettingsScreen(
        navController = navController,
        contactMethodSettingsList = contactMethodSettingsList,
        insertContactMethodSetting = {newContactMethodSetting ->
            contactMethodViewModel.insertContactMethodSettings(newContactMethodSetting)
        },
        deleteContactMethodSetting = {selectedContactMethodSetting ->
            contactMethodViewModel.deleteContactMethodSettings(selectedContactMethodSetting)
        }
    )

}

/**
 * This function represents the contact method settings screen, where the user can edit their
 * emergency message and determine the type of contact method the app will use in case of an
 * emergency situation.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param insertContactMethodSetting is the lambda function which performs the insertion of a new
 * emergency message and a new form of contact.
 * @param deleteContactMethodSetting is the lambda function which performs the deletion of the
 * current contact method settings present in the database.
 * @param contactMethodSettingsList is the list object used to access the elements found in the
 * contact method database.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactMethodSettingsScreen(
    navController: NavHostController,
    insertContactMethodSetting: (ContactMethod) -> Unit = {},
    deleteContactMethodSetting: (ContactMethod) -> Unit = {},
    contactMethodSettingsList: List<ContactMethod>
) {
    if (contactMethodSettingsList.isNotEmpty()) {
        val coroutineScope = rememberCoroutineScope()
        val lastMessage = contactMethodSettingsList[contactMethodSettingsList.lastIndex].message
        var lastMessageDB by rememberSaveable { mutableStateOf(lastMessage) }
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current.applicationContext
        var isError by rememberSaveable { mutableStateOf(false) }
        var openDialog by rememberSaveable { mutableStateOf(false) }
        val menuItemList = listOf("Select an option", "Calling", "SMS message", "Calling & SMS")
        var selectedItem by rememberSaveable { mutableStateOf(menuItemList[0]) }
        var expanded by rememberSaveable { mutableStateOf(false) }

        ScaffoldContactMethodSettings(
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

                    Spacer(modifier = Modifier.padding(10.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.sms_message),
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            //fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.padding(10.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = lastMessageDB,
                            onValueChange = { message ->
                                lastMessageDB = message
                            },
                            label = {
                                Text(text = "Emergency Message")
                            }
                        )
                    }

                    Spacer(modifier = Modifier.padding(40.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.type_of_method),
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            //fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.padding(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        },
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        TextField(
                            value = selectedItem,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            placeholder = {
                                Text(text = "Select the most appropriate option")
                            }
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

                    Spacer(modifier = Modifier.padding(40.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (lastMessageDB.isEmpty() || selectedItem == menuItemList[0]) {
                                    isError = true
                                    openDialog = true
                                } else {
                                    changeContactMethodSettings(
                                        message = lastMessageDB,
                                        contactType = selectedItem,
                                        doAction = { newContactMethodSetting ->
                                            deleteContactMethodSetting(contactMethodSettingsList[contactMethodSettingsList.lastIndex])
                                            insertContactMethodSetting(newContactMethodSetting)
                                            Toast.makeText(
                                                context,
                                                "The settings have been saved",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            lastMessageDB = ""
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
                            if (isError && openDialog) {
                                AlertDialog(
                                    onDismissRequest = { },
                                    title = {
                                        Text(text = "Error")
                                    },
                                    text = {
                                        Text(
                                            text = "The message can't be empty\n" +
                                                    "The method type has to be either Call, SMS or both"
                                        )
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
                                lastMessageDB = ""
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
}


/**
 * This function is used to implement the viewModel functions within the UI
 *
 * @param message is the personalised emergency message the user will display when sending an SMS.
 * @param contactType is the contact method the user will use in case of confirmed emergency.
 * @param doAction the action which uses the name and number of the contact to get carried out.
 */
private fun changeContactMethodSettings(
    message: String,
    contactType: String,
    doAction: (ContactMethod) -> Unit = {}
){
    if(message.isNotEmpty() && contactType.isNotEmpty()){
        val contactMethod = ContactMethod(
            id = 0,
            message, contactType
        )
        doAction(contactMethod)
    }

}