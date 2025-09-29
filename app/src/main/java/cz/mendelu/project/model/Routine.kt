package cz.mendelu.project.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "routines",
    foreignKeys = [
        ForeignKey(
            entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Routine(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var itemId: Long? = null,
    var name: String? = null,
    var active: Boolean? = null,
    var startDate: Long? = null,
    var lastGeneratedAccomplishments: Long? = null,
    var frequencyDays: Int? = null,
    var timeHours: Int? = null,
    var timeMinutes: Int? = null,
)