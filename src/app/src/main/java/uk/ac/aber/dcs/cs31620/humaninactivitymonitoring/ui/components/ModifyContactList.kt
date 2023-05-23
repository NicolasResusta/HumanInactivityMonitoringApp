package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components

import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.Contact

/**
 * This function is used to implement the viewModel functions within the UI
 *
 * @param name is the name of the contact.
 * @param number is the number of the contact.
 * @param doAction the action which uses the name and number of the contact to get carried out.
 */
fun modifyContact(
    name: String,
    number: String,
    doAction: (Contact) -> Unit = {}
){
    if(name.isNotEmpty() && number.isNotEmpty()){
        val contact = Contact(
            id = 0,
            name = name,
            number = number
        )
        doAction(contact)
    }

}