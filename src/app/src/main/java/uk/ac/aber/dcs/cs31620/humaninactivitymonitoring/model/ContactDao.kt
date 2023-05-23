package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * This interface is the first form of exchange which defines the methods for interacting with the
 * data in the Contact's database.
 */
@Dao
interface ContactDao {
    @Insert
    suspend fun insertContact(contact: Contact)

    @Insert
    suspend fun insertMultipleContacts(contactList: List<Contact>)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Update(entity = Contact::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateContact(contact: Contact)

    @Query("DELETE FROM contacts_table")
    suspend fun deleteAllContacts()

    @Query("SELECT * FROM contacts_table")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts_table")
    fun getAllContactsSync(): List<Contact>

    @Query("SELECT * FROM contacts_table WHERE name = :name AND number = :number")
    fun getContact(name: String, number: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts_table WHERE name = :name")
    fun getContactName(name: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts_table WHERE number = :number")
    fun getContactNumber(number: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts_table WHERE id = :id")
    fun getContactId(id: Int): LiveData<List<Contact>>
}