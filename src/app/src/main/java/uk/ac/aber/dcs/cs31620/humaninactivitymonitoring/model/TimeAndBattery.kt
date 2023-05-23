package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * This class defines the values and properties of the TimeAndBattery data in the database.
 *
 * @param id is the ID of the time and battery elements
 * @param time represents the time the user has defined to be appropriate for the phone to be locked/off
 * in one go.
 * @param battery represents the battery % the user has considered can be the lowest before assuming
 * an emergency is happening.
 * @param timeType represents which kind of time the "time" parameter serves. Minutes, hours, days...
 */
@Entity(tableName = "timeAndBatterySettings_table")
data class TimeAndBattery(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Int = 0,
    val time: Int,
    val battery: Int,
    val timeType: String
)