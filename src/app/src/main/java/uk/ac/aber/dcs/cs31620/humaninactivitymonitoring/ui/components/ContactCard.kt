package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.Contact
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R

/**
 * This function shows the way a contact is shown within the contact list screen. Each contact card
 * represents each of the contact's information.
 *
 * @param contact is the contact object which holds all the information of the user's emergency contacts.
 * @param deleteAction represents the "delete" icon, and when pressed will remove the contact from
 * the list.
 * @param modifier is the modifier for the card's features.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactCard(
    contact: Contact,
    deleteAction: (Contact) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var openDialog by rememberSaveable { mutableStateOf(false)}
        var stateOfDialogFunction by rememberSaveable { mutableStateOf(false) }
        ConstraintLayout {
            val (nameRef, numberRef, deleteRef) = createRefs()

            Text(
                text = contact.name,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .constrainAs(nameRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)

                    },
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = contact.number,
                modifier = Modifier
                    .padding(start = 172.dp)
                    .constrainAs(numberRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = { openDialog = true },
                modifier = Modifier
                    .padding(start = 340.dp, end = 5.dp)
                    .constrainAs(deleteRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete_button)
                )
                if (openDialog){
                    AlertDialog(
                        onDismissRequest = {  },
                        title = {
                            Text(text = "Are you sure you want to delete this contact?")
                        },
                        text = {
                            Text(text = "Contact to delete is: \n\n" +
                                    "name: ${contact.name} \n" +
                                    "number: ${contact.number}")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = { 
                                    stateOfDialogFunction = true
                                }
                            ) {
                                Text(text = "Confirm")
                                if(stateOfDialogFunction) {
                                    openDialog = false
                                    deleteAction(contact)
                                    stateOfDialogFunction = false
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
        }
    }
}