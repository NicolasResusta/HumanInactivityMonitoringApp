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
 * data in the ContactMethod's database.
 */
@Dao
interface ContactMethodDao {
    @Insert
    suspend fun insertContactMethodSettings(contactMethod: ContactMethod)

    @Insert
    suspend fun insertMultipleContactMethodSettings(contactMethods: List<ContactMethod>)

    @Delete
    suspend fun deleteContactMethodSettings(contactMethod: ContactMethod)

    @Update(onConflict = OnConflictStrategy.REPLACE, entity = ContactMethod::class)
    suspend fun updateContactMethodSettings(contactMethod: ContactMethod)

    @Query("DELETE FROM contactMethod_table")
    suspend fun deleteAllContactMethodSettings()

    @Query("SELECT * FROM contactMethod_table")
    fun getAllContactMethodSettings(): LiveData<List<ContactMethod>>

    @Query("SELECT * FROM contactMethod_table")
    fun getAllContactMethodSettingsSync(): List<ContactMethod>

    @Query("SELECT * FROM contactMethod_table WHERE message = :message AND contactType = :contactType")
    fun getContactMethodSetting(message: String, contactType: String): LiveData<List<ContactMethod>>
}