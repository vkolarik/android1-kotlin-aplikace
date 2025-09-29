package cz.mendelu.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String? = null,
    var image: String? = null,
    var description: String? = null
)
