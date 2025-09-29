package cz.mendelu.project.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "accomplishments",
    foreignKeys = [
        ForeignKey(
            entity = Routine::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Accomplishment(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var routineId: Long? = null,
    var shouldAccomplishTime: Long? = null,
    var accomplishedTime: Long? = null
)
