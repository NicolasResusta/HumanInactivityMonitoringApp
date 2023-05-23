package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * This class defines the values and properties of the Contact data in the database.
 *
 * @param id is the ID of the contacts
 * @param name represents where the name the contact is stored.
 * @param number represents where the phone number of the contact is stored.
 */
@Entity(tableName = "contacts_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Int = 0,
    val name: String,
    val number: String
)