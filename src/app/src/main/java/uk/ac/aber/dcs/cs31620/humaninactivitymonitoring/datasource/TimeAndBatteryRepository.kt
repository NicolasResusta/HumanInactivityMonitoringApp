package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource

import android.app.Application
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBattery

/**
 * This class represents the repository needed to implement the insertions/queries/deletions from
 * the TimeAndBattery Dao
 *
 * @param application is used to relate the repository to the app
 */
class TimeAndBatteryRepository(application: Application) {
    private val timeAndBatteryDao =
        TimeAndBatteryRoomDatabase.getDatabase(application)!!.timeAndBatteryDao()

    suspend fun insertTimeAndBatterySetting(timeAndBattery: TimeAndBattery){
        timeAndBatteryDao.insertTimeAndBatterySetting(timeAndBattery)
    }

    suspend fun insertMultipleTimeAndBatterySettings(timeAndBatterySettings: List<TimeAndBattery>){
        timeAndBatteryDao.insertMultipleTimeAndBatterySettings(timeAndBatterySettings)
    }

    suspend fun deleteTimeAndBatterySettings(timeAndBattery: TimeAndBattery){
        timeAndBatteryDao.deleteTimeAndBatterySetting(timeAndBattery)
    }

    suspend fun deleteAllTimeAndBatterySettings() = timeAndBatteryDao.deleteAllTimeAndBatterySettings()

    suspend fun updateTimeAndBatterySetting(timeAndBattery: TimeAndBattery){
        timeAndBatteryDao.updateTimeAndBatterySetting(timeAndBattery)
    }

    fun getTimeAndBatterySetting(time: Int, battery: Int, timeType: String){
        timeAndBatteryDao.getTimeAndBatterySetting(time, battery, timeType)
    }

    fun getAllTimeAndBatterySettings() = timeAndBatteryDao.getAllTimeAndBatterySettings()

    fun getAllTimeAndBatterySettingsSync() = timeAndBatteryDao.getAllTimeAndBatterySettingsSync()
}