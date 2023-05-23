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
 * data in the TimeAndBattery's database.
 */
@Dao
interface TimeAndBatteryDao {
    @Insert
    suspend fun insertTimeAndBatterySetting(timeAndBattery: TimeAndBattery)

    @Insert
    suspend fun insertMultipleTimeAndBatterySettings(timeAndBatteryList: List<TimeAndBattery>)

    @Delete
    suspend fun deleteTimeAndBatterySetting(timeAndBattery: TimeAndBattery)

    @Update(entity = TimeAndBattery::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTimeAndBatterySetting(timeAndBattery: TimeAndBattery)

    @Query("DELETE FROM timeAndBatterySettings_table")
    suspend fun deleteAllTimeAndBatterySettings()

    @Query("SELECT * FROM timeAndBatterySettings_table")
    fun getAllTimeAndBatterySettings(): LiveData<List<TimeAndBattery>>

    @Query("SELECT * FROM timeAndBatterySettings_table")
    fun getAllTimeAndBatterySettingsSync(): List<TimeAndBattery>

    @Query("SELECT * FROM timeAndBatterySettings_table WHERE time = :time AND battery = :battery AND timeType = :timeType")
    fun getTimeAndBatterySetting(time: Int, battery: Int, timeType: String): LiveData<List<TimeAndBattery>>


}