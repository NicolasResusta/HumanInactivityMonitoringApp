package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * This class defines the values and properties of the ContactMethod data in the database.
 *
 * @param id is the ID of the contactMethod elements
 * @param message represents the personalised emergency message the user has defined.
 * @param contactType represents the type of contact the user wants to use in cases of emergency
 * towards the contacts in their emergency list.
 */
@Entity(tableName = "contactMethod_table")
data class ContactMethod(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Int = 0,
    val message: String,
    val contactType: String
)