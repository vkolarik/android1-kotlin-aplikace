package cz.mendelu.project.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import cz.mendelu.project.model.Accomplishment
import cz.mendelu.project.model.Routine


data class RoutineWithAccomplishments(
    @Embedded val routine: Routine,
    @Relation(
        entity = Accomplishment::class,
        parentColumn = "id",
        entityColumn = "routineId"
    ) val accomplishments: List<Accomplishment>
)