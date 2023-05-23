package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.backendoperations

/**
 * This data class defines the way in which the user's current location is extracted
 *
 * @property longitude is the user's longitude
 * @property latitude is the user's latitude
 */
data class LocationDetails (
    val longitude: String,
    val latitude: String
    )
