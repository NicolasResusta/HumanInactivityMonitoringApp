package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.Contact
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components.modifyContact
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.Screen


/**
 * This function represents the top level used to connect the UI with the view model in order to be
 * able to allow communication with the database. It is also the function to be used to represent
 * the screen in the MainActivity navigation graph.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param contactViewModel is used to connect the ContactViewModel class to the UI from this screen.
 */
@Composable
fun AddContactScreenTopLevel(
    navController: NavHostController,
    contactViewModel: ContactViewModel = viewModel()
){
    //val contactList by contactViewModel.contactsInDatabase.observeAsState(listOf())
    val context = LocalContext.current

    AddContactScreen(
        navController = navController,
        addContact = {newContact ->
            contactViewModel.insertContact(newContact)
        },
        context = context
        )

}

/**
 * This function represents the screen in charge of adding contacts to the user's emergency contact
 * list. It does this by accessing the contacts within the user's own Android device and adding it
 * to the contact database.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param addContact is the lambda function used to add contacts to the contact database and later
 * show them on the Home Screen.
 * @param context is the context of the application. Placed here rather than a composable variable
 * differentiate it from an Activity type.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(
    navController: NavHostController,
    addContact: (Contact) -> Unit = {},
    context: Context
) {

    var contactNumber by rememberSaveable { mutableStateOf("") }
    var contactName by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val activity = LocalContext.current as Activity
    var isError by rememberSaveable { mutableStateOf(false) }
    var openDialog by rememberSaveable { mutableStateOf(false) }


    val contactFinderLauncher = //receiver used to access the device's own contact database.
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK){
                val contactData = it.data?.data
                contactData.let {
                    //val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET)
                    val cursor = activity.contentResolver.query(contactData!!, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        val number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        contactName = name
                        contactNumber = number
                    }
                    cursor?.close()
                }
            }
        }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.add_contact_screen),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 30.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(route = Screen.HomeScreen.route) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.arrow_icon)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
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
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 60.dp)
            ) {
                Text(
                    text = "Press the icon to access the contacts",
                    modifier = Modifier.weight(9f),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(20.dp))

                IconButton(
                    onClick = {
                        if (hasContactPermission(context)) {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                            contactFinderLauncher.launch(intent)
                        } else {
                            requestContactPermission(context, activity)
                        }
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Contacts,
                            contentDescription = stringResource(id = R.string.add_contact_button)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                TextField(
                    value = contactName,
                    onValueChange = { finalName ->
                        contactName = finalName
                    },
                    placeholder = {
                        Text(text = "This is where the contact name will be shown")
                    },
                    readOnly = false,
                    label = {
                        Text(text = "Contact Name")
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                TextField(
                    value = contactNumber,
                    onValueChange = {newNum ->
                        contactNumber = newNum
                    },
                    readOnly = true,
                    placeholder = {
                        Text(text = "This is where the contact number will be shown")
                    },
                    label = {
                        Text(text = "Contact Number")
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Button(
                    onClick = {
                        if(contactName.isEmpty() || contactNumber.isEmpty()){
                            isError = true
                            openDialog = true
                        } else {
                        modifyContact(
                            name = contactName,
                            number = contactNumber,
                            doAction = { contact ->
                                addContact(contact)
                                Toast.makeText(
                                    context,
                                    "The contact with name: ${contact.name} and number: ${contact.number} has been added to the list",
                                    Toast.LENGTH_SHORT
                                ).show()
                                contactName = ""
                                contactNumber = ""
                                focusManager.clearFocus()
                            }
                        )
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.save_button))
                    if(isError && openDialog){
                        AlertDialog(
                            onDismissRequest = { },
                            title = {
                                Text(text = "Error")
                            },
                            text = {
                                Text(text = "The name and contact number can't be empty")
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

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {
                        contactName = ""
                        contactNumber = ""
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.clear_button))
                }
            }
        }
    }
}

/**
 * This function checks whether the user has allowed the app access to the device's contacts.
 *
 * @param context is making the callback to Android to check on the permission
 * @return true if the access has been allowed
 */
fun hasContactPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED;
}

/**
 * This function requests Android and the user permission to access the devices' own contact database
 * if it has been determined that the permission is denied from the "hasContactPermission" function.
 *
 * @param context the context of the application
 * @param activity the context of the current activity, i.e. contact permission acess
 */
fun requestContactPermission(context: Context, activity: Activity) {
    if (!hasContactPermission(context)) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), 1)
    }
}