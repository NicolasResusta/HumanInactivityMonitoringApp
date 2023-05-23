package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource.ContactRepository
/**
 * This class represents the main layer which communicates between the UI and the database by showing
 * the data from the database and updating it accordingly because of the use of LiveData objects. The
 * functions in it are extracted from the ContactRepository class.
 *
 * @param application is the connection made to allow changes to the database from the app itself.
 */
class ContactViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ContactRepository = ContactRepository(application)

    var contactsInDatabase: LiveData<List<Contact>> = repository.getAllContacts()
        private set


    fun insertContact(newContact: Contact){
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertContact(newContact)
        }
    }

    fun updateContact(newContact: Contact){
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateContact(newContact)
        }
    }

    fun deleteContact(selectedContact: Contact){
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteContact(selectedContact)
        }
    }

    fun deleteAllContacts() {
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteAllContacts()
        }
    }

    fun getSpecificContactName(name: String){
        repository.getContactName(name)
    }

    fun getSpecificContactNumber(number: String){
        repository.getContactNumber(number)
    }

    fun getContact(name: String, number: String){
        repository.getContact(name, number)
    }
}