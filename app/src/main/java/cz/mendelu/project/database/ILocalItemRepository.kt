package cz.mendelu.project.database

import cz.mendelu.project.model.Accomplishment
import cz.mendelu.project.model.Item
import cz.mendelu.project.model.Routine
import cz.mendelu.project.model.Statistics
import cz.mendelu.project.model.relations.ItemWithRoutines
import cz.mendelu.project.model.relations.ItemWithRoutinesAndAccomplishments
import cz.mendelu.project.model.relations.RoutineWithAccomplishments
import kotlinx.coroutines.flow.Flow

interface ILocalItemRepository {

    //--------------------- item ------------------------
    fun getAllItems(): Flow<List<Item>>

    fun getAllItemsList(): List<Item>

    suspend fun insert(item: Item): Long

    suspend fun update(item: Item)

    suspend fun delete(item: Item)

    suspend fun deleteItemById(itemId: Long)

    suspend fun getItem(id: Long) : Item

    //--------------------- routine ------------------------
    fun getAllRoutines(): Flow<List<Routine>>

    fun getAllRoutinesList(): List<Routine>

    suspend fun insert(routine: Routine): Long

    suspend fun update(routine: Routine)

    suspend fun delete(routine: Routine)

    suspend fun deleteRoutineById(routineId: Long)

    suspend fun getRoutine(id: Long) : Routine

    //--------------------- accomplishment ------------------------
    fun getAllAccomplishments(): Flow<List<Accomplishment>>

    fun getAllAccomplishmentsList(): List<Accomplishment>

    fun getAllAccomplishmentsBetweenList(startTime: Long, endTime: Long): List<Accomplishment>

    suspend fun insert(accomplishment: Accomplishment): Long

    suspend fun update(accomplishment: Accomplishment)

    fun setAllAccomplishmentsToCompletedOnTime()

    suspend fun delete(accomplishment: Accomplishment)

    suspend fun getAccomplishment(id: Long) : Accomplishment

    suspend fun deleteRoutineAccomplishmentsBetween(routineId: Long, startTime: Long, endTime: Long)

    //--------------------- relationships ------------------------
    fun getItemsWithRoutines(): Flow<List<ItemWithRoutines>>

    fun getItemWithRoutines(id: Long): Flow<ItemWithRoutines>
    fun getRoutinesWithAccomplishments(): List<RoutineWithAccomplishments>

    fun getItemsWithRoutinesAndAccomplishments(): Flow<List<ItemWithRoutinesAndAccomplishments>>

    suspend fun getRoutineWithAccomplishments(id: Long): RoutineWithAccomplishments

    //--------------------- statistics ------------------------
    suspend fun getConsecutiveDaysCompletedOnTime(): Int

    fun getTotalAccomplishments(): Int

    fun getAccomplishedCount(): Int

    fun getOnTimeAccomplishedCount(): Int

    fun getTotalRoutines(): Int

    fun getStatistics(): Statistics
}