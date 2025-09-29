package cz.mendelu.project.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import cz.mendelu.project.model.Item
import cz.mendelu.project.model.Routine

data class ItemWithRoutinesAndAccomplishments(
    @Embedded
    val item: Item,
    @Relation(
        entity = Routine::class,
        parentColumn = "id",
        entityColumn = "itemId",
    )
    val routines: List<RoutineWithAccomplishments>
)