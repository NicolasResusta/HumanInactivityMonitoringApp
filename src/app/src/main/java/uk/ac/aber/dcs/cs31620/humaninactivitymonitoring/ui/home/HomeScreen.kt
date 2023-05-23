package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.home

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.Contact
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactViewModel
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components.ContactCard
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components.MainScaffold
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
fun HomeScreenTopLevel(
    navController: NavHostController,
    contactViewModel: ContactViewModel = viewModel()
){
    val contactList by contactViewModel.contactsInDatabase.observeAsState(listOf())

    HomeScreen(
        navController = navController,
        contactsList = contactList,
        deleteContact = { contactToDelete ->
            contactViewModel.deleteContact(contactToDelete)
        }
    )

}

/**
 * This function represents what will be displayed on the Home Screen of the app. The main purpose
 * is to set out the emergency contact list and and the "Add a Contact" floating action button.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param deleteContact is the lambda function which when triggered will delete the selected contact
 * from the Emergency Contact database.
 * @param contactsList is the list object used to access the elements found in the contact database,
 * used to be displayed for the user to see what contacts they have on their emergency list.
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    deleteContact: (Contact) -> Unit = {},
    contactsList: List<Contact> = listOf()
    ) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    MainScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        pageContent = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 55.dp, end = 8.dp)
                        ){
                    Text(
                        text = stringResource(id = R.string.home_screen_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 1.dp, top = 60.dp)
                ) {

                    Text(
                        text = stringResource(id = R.string.contact_name),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 15.dp, bottom = 10.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = stringResource(id = R.string.contact_number),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 2.dp, bottom = 10.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier
                        .padding(start = 10.dp, bottom = 4.dp, top = 90.dp, end = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    contentPadding = PaddingValues(top = 15.dp, bottom = 80.dp)
                ) {
                    val sortedContactList = contactsList.sortedWith(compareBy { it.name })
                    items(sortedContactList) {
                        ContactCard(
                            contact = it,
                            modifier = Modifier.padding(end = 4.dp, top = 4.dp),
                            deleteAction = { contact ->
                               modifyContact(
                                   name = contact.name,
                                   number = contact.number,
                                   doAction = {
                                       deleteContact(contact)
                                       Toast.makeText(context, "The contact has been deleted", Toast.LENGTH_SHORT).show()
                                   }
                               )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton (
                onClick = {
                    navController.navigate(Screen.AddContactScreen.route)
                }
            ) {
                Text(text = "Add Contact  ")
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_contact_button)
                )
            }
        }
    )
}