package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource

import android.app.Application
import androidx.lifecycle.LiveData
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactMethod

/**
 * This class represents the repository needed to implement the insertions/queries/deletions from
 * the ContactMethod Dao interface
 *
 * @param application is used to relate the repository to the app
 */
class ContactMethodRepository (application: Application){
    private val contactMethodDao = ContactMethodRoomDatabase.getDatabase(application)!!.contactMethodDao()

    suspend fun insertContactMethodSettings(contactMethod: ContactMethod){
        contactMethodDao.insertContactMethodSettings(contactMethod)
    }

    suspend fun insertMultipleContactMethodSettings(contactMethods: List<ContactMethod>){
        contactMethodDao.insertMultipleContactMethodSettings(contactMethods)
    }

    suspend fun deleteContactMethodSettings(contactMethod: ContactMethod){
        contactMethodDao.deleteContactMethodSettings(contactMethod)
    }

    suspend fun updateContactMethodSettings(contactMethod: ContactMethod){
        contactMethodDao.updateContactMethodSettings(contactMethod)
    }

    suspend fun deleteAllContactMethodSettings() = contactMethodDao.deleteAllContactMethodSettings()

    fun getAllContactMethodSettings() = contactMethodDao.getAllContactMethodSettings()

    fun getAllContactMethodSettingsSync() = contactMethodDao.getAllContactMethodSettingsSync()

    fun getContactMethodSetting(message: String, contactType: String){
        contactMethodDao.getContactMethodSetting(message, contactType)
    }
}