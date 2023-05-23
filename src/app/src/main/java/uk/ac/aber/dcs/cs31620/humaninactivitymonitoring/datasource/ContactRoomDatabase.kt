package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.Contact
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactDao

/**
 * This class shows the way in which the Contact database is created. The database callback
 * function overrides the onCreate method so that a database is created the first time the app is
 * launched. The getDatabase function is synchronized so that any changes within the UI/ViewModel
 * are implemented automatically.
 *
 */
@Database(entities = [Contact::class], version = 1)
abstract class ContactRoomDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object{
        private var instance: ContactRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): ContactRoomDatabase? {
            if(instance == null){
                instance =
                    Room.databaseBuilder<ContactRoomDatabase>(
                        context.applicationContext,
                        ContactRoomDatabase::class.java,
                        "contact_database"
                    )
                        .allowMainThreadQueries()
                        .addCallback(roomDatabaseCallback(context))
                        .build()
            }
            return instance
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    coroutineScope.launch {
                        populateDatabase(context, getDatabase(context)!!)
                    }
                }
            }
        }

        private suspend fun populateDatabase(context: Context, instance: ContactRoomDatabase){
            val contact1 = Contact(0, "Tim", "123456789")
            val contact2 = Contact(0, "Stan", "014785636")

            val contactList = mutableListOf(
                contact1,
                contact2
            )

            val dao = instance.contactDao()
            dao.insertMultipleContacts(contactList)
        }
    }
}