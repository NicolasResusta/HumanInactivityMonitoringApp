package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.backendoperations

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * This viewModel represents the data gathered from the LocationLiveData class and continuously
 * updates it onto the application's UI.
 */
class LocationViewModel(application: Application): AndroidViewModel(application) {

    private val locationLiveData = LocationLiveData(application)

    /**
     * This function gets the LiveData list of location data that the LocationLiveData generates
     */
    fun getLocationLiveData() = locationLiveData

    /**
     * This function keeps the location data found when the app first has access to the user's
     * location.
     */
    fun startLocationUpdates(){
        locationLiveData.startLocationUpdates()
    }
}