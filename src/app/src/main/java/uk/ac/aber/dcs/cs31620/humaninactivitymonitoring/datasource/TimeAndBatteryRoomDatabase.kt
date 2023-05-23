package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBattery
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBatteryDao

/**
 * This class shows the way in which the TimeAndBattery database is created. The database callback
 * function overrides the onCreate method so that a database is created the first time the app is
 * launched. The getDatabase function is synchronized so that any changes within the UI/ViewModel
 * are implemented automatically.
 */
@Database(entities = [TimeAndBattery::class], version = 1)
abstract class TimeAndBatteryRoomDatabase : RoomDatabase() {

    abstract fun timeAndBatteryDao(): TimeAndBatteryDao

    companion object{
        private var instance: TimeAndBatteryRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): TimeAndBatteryRoomDatabase? {
            if(instance == null){
                instance =
                    Room.databaseBuilder<TimeAndBatteryRoomDatabase>(
                        context.applicationContext,
                        TimeAndBatteryRoomDatabase::class.java,
                        "timeAndBatterySettings_database"
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

        private suspend fun populateDatabase(context: Context, instance: TimeAndBatteryRoomDatabase){
            val timeAndBatterySettings = TimeAndBattery(
                0,
                15,
                15,
                "minutes"
            )

            val dao = instance.timeAndBatteryDao()
            dao.insertTimeAndBatterySetting(timeAndBatterySettings)

        }
    }
}