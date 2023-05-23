package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource

import android.app.Application
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.Contact

/**
 * This class represents the repository needed to implement the insertions/queries/deletions from
 * the Contact Dao
 *
 * @param application is used to relate the repository to the app
 */
class ContactRepository(application: Application) {
    private val contactDao = ContactRoomDatabase.getDatabase(application)!!.contactDao()

    suspend fun insertContact(contact: Contact){
        contactDao.insertContact(contact)
    }

    suspend fun insertMultipleContacts(contacts: List<Contact>){
        contactDao.insertMultipleContacts(contacts)
    }

    suspend fun deleteContact(contact: Contact){
        contactDao.deleteContact(contact)
    }

    suspend fun deleteAllContacts() = contactDao.deleteAllContacts()

    suspend fun updateContact(contact: Contact){
        contactDao.updateContact(contact)
    }

    fun getAllContacts() = contactDao.getAllContacts()

    fun getContact(name: String, number: String){
        contactDao.getContact(name, number)
    }

    fun getContactNumber(number: String){
        contactDao.getContactNumber(number)
    }

    fun getContactName(number: String){
        contactDao.getContactName(number)
    }

    fun getContactsSync() = contactDao.getAllContactsSync()

    fun getContactId(id: Int){
        contactDao.getContactId(id)
    }
}