package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactMethod
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.ContactMethodDao

/**
 * This class shows the way in which the ContactMethod database is created. The database callback
 * function overrides the onCreate method so that a database is created the first time the app is
 * launched. The getDatabase function is synchronized so that any changes within the UI/ViewModel
 * are implemented automatically.
 *
 */
@Database(entities = [ContactMethod::class], version = 1)
abstract class ContactMethodRoomDatabase : RoomDatabase() {

    abstract fun contactMethodDao(): ContactMethodDao

    companion object{
        private var instance: ContactMethodRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): ContactMethodRoomDatabase? {
            if(instance == null){
                instance =
                    Room.databaseBuilder<ContactMethodRoomDatabase>(
                        context.applicationContext,
                        ContactMethodRoomDatabase::class.java,
                        "contactMethodSettings_database"
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

        private suspend fun populateDatabase(context: Context, instance: ContactMethodRoomDatabase){
            val contactMethodSettings = ContactMethod(
                0,
                "Sample message",
                "Calling"
            )

            val dao = instance.contactMethodDao()
            dao.insertContactMethodSettings(contactMethodSettings)

        }
    }
}