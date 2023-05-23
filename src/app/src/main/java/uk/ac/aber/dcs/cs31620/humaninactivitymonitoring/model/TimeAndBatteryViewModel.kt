package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource.TimeAndBatteryRepository

/**
 * This class represents the main layer which communicates between the UI and the database by showing
 * the data from the database and updating it accordingly because of the use of LiveData objects. The
 * functions in it are extracted from the TimeAndBatteryRepository class.
 *
 * @param application is the connection made to allow changes to the database from the app itself.
 */
class TimeAndBatteryViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TimeAndBatteryRepository = TimeAndBatteryRepository(application)

    var timeAndBatterySettingsInDatabase: LiveData<List<TimeAndBattery>> =
        repository.getAllTimeAndBatterySettings()
        private set

    fun insertTimeAndBatterySetting(newTimeAndBattery: TimeAndBattery){
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertTimeAndBatterySetting(newTimeAndBattery)
        }
    }

    fun updateTimeAndBatterySetting(newTimeAndBattery: TimeAndBattery){
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateTimeAndBatterySetting(newTimeAndBattery)
        }
    }

    fun deleteTimeAndBatterySetting(selectedTimeAndBattery: TimeAndBattery){
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteTimeAndBatterySettings(selectedTimeAndBattery)
        }
    }

    fun deleteAllTimeAndBatterySettings(){
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteAllTimeAndBatterySettings()
        }
    }

    fun getSpecificTimeAndBatterySetting(time: Int, battery: Int, timeType: String){
        repository.getTimeAndBatterySetting(time, battery, timeType)
    }
}
