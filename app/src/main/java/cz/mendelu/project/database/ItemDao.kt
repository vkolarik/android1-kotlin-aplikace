package cz.mendelu.project.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import cz.mendelu.project.model.Accomplishment
import cz.mendelu.project.model.Item
import cz.mendelu.project.model.Routine
import cz.mendelu.project.model.Statistics
import cz.mendelu.project.model.relations.ItemWithRoutines
import cz.mendelu.project.model.relations.ItemWithRoutinesAndAccomplishments
import cz.mendelu.project.model.relations.RoutineWithAccomplishments
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    //--------------------- item ------------------------
    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items")
    fun getAllItemsList(): List<Item>

    @Insert
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("DELETE FROM items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Long)

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItem(id: Long): Item

    //--------------------- routine ------------------------
    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<Routine>>

    @Query("SELECT * FROM routines")
    fun getAllRoutinesList(): List<Routine>

    @Insert
    suspend fun insert(routine: Routine): Long

    @Update
    suspend fun update(routine: Routine)

    @Delete
    suspend fun delete(routine: Routine)

    @Query("DELETE FROM routines WHERE id = :routineId")
    suspend fun deleteRoutineById(routineId: Long)

    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutine(id: Long): Routine

    //--------------------- accomplishment ------------------------
    @Query("SELECT * FROM accomplishments")
    fun getAllAccomplishments(): Flow<List<Accomplishment>>

    @Query("SELECT * FROM accomplishments ORDER BY shouldAccomplishTime DESC")
    fun getAllAccomplishmentsList(): List<Accomplishment>

    @Query("SELECT * FROM accomplishments WHERE shouldAccomplishTime BETWEEN :startTime AND :endTime")
    fun getAllAccomplishmentsBetweenList(startTime: Long, endTime: Long): List<Accomplishment>

    @Insert
    suspend fun insert(accomplishment: Accomplishment): Long

    @Update
    suspend fun update(accomplishment: Accomplishment)

    @Query("UPDATE accomplishments SET accomplishedTime = 1")
    fun setAllAccomplishmentsToCompletedOnTime()

    @Delete
    suspend fun delete(accomplishment: Accomplishment)

    @Query("SELECT * FROM accomplishments WHERE id = :id")
    suspend fun getAccomplishment(id: Long): Accomplishment

    @Query("DELETE FROM accomplishments WHERE routineId = :routineId AND shouldAccomplishTime BETWEEN :startTime AND :endTime")
    suspend fun deleteRoutineAccomplishmentsBetween(routineId: Long, startTime: Long, endTime: Long)


    //--------------------- relationships ------------------------
    @Transaction
    @Query("SELECT * FROM items")
    fun getItemsWithRoutines(): Flow<List<ItemWithRoutines>>

    @Transaction
    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemWithRoutines(id: Long): Flow<ItemWithRoutines>

    @Transaction
    @Query("SELECT * FROM routines")
    fun getRoutinesWithAccomplishments(): List<RoutineWithAccomplishments>

    @Transaction
    @Query("SELECT * FROM items")
    fun getItemsWithRoutinesAndAccomplishments(): Flow<List<ItemWithRoutinesAndAccomplishments>>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutineWithAccomplishments(id: Long): RoutineWithAccomplishments

    //--------------------- statistics ------------------------
    @Query("SELECT COUNT(*) FROM accomplishments")
    fun getTotalAccomplishments(): Int

    @Query("SELECT COUNT(*) FROM accomplishments WHERE accomplishedTime IS NOT NULL")
    fun getAccomplishedCount(): Int

    @Query("SELECT COUNT(*) FROM accomplishments WHERE accomplishedTime IS NOT NULL AND accomplishedTime <= shouldAccomplishTime")
    fun getOnTimeAccomplishedCount(): Int

    @Query("SELECT COUNT(*) FROM routines")
    fun getTotalRoutines(): Int

    fun getStatistics(): Statistics {
        val totalAccomplishments = getTotalAccomplishments()
        val accomplishedCount = getAccomplishedCount()
        val onTimeAccomplishedCount = getOnTimeAccomplishedCount()
        val totalRoutines = getTotalRoutines()

        val percentageAccomplished = if (totalAccomplishments > 0) {
            (accomplishedCount.toDouble() / totalAccomplishments.toDouble()) * 100
        } else {
            0.0
        }

        val percentageOnTime = if (totalAccomplishments > 0) {
            (onTimeAccomplishedCount.toDouble() / totalAccomplishments.toDouble()) * 100
        } else {
            0.0
        }

        return Statistics(
            percentageAccomplished = percentageAccomplished.toInt(),
            percentageOnTime = percentageOnTime.toInt(),
            totalRoutines = totalRoutines,
            totalFinishedAccomplishments = accomplishedCount
        )
    }

}