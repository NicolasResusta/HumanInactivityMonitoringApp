package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource.ContactMethodRepository

/**
 * This class represents the main layer which communicates between the UI and the database by showing
 * the data from the database and updating it accordingly because of the use of LiveData objects. The
 * functions in it are extracted from the ContactMethodRepository class.
 *
 * @param application is the connection made to allow changes to the database from the app itself.
 */
class ContactMethodViewModel(application: Application): AndroidViewModel(application){
    private val repository: ContactMethodRepository = ContactMethodRepository(application)

    var contactMethodSettingsInDatabase: LiveData<List<ContactMethod>> = repository.getAllContactMethodSettings()
        private set

    fun insertContactMethodSettings(newContactMethod: ContactMethod){
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertContactMethodSettings(newContactMethod)
        }
    }

    fun insertMultipleContactMethodSettings(contactMethods: List<ContactMethod>){
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertMultipleContactMethodSettings(contactMethods)
        }
    }

    fun deleteContactMethodSettings(selectedContactMethod: ContactMethod){
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteContactMethodSettings(selectedContactMethod)
        }
    }

    fun updateContactMethodSettings(selectedContactMethod: ContactMethod){
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateContactMethodSettings(selectedContactMethod)
        }
    }

    fun deleteAllContactMethodSettings(){
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteAllContactMethodSettings()
        }
    }

    fun getContactMethodSetting(message: String, contactType: String){
        repository.getContactMethodSetting(message, contactType)
    }
}