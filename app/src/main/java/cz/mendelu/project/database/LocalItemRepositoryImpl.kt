package cz.mendelu.project.database

import android.util.Log
import cz.mendelu.project.model.Accomplishment
import cz.mendelu.project.model.Item
import cz.mendelu.project.model.Routine
import cz.mendelu.project.model.Statistics
import cz.mendelu.project.model.relations.ItemWithRoutines
import cz.mendelu.project.model.relations.ItemWithRoutinesAndAccomplishments
import cz.mendelu.project.model.relations.RoutineWithAccomplishments
import cz.mendelu.project.utils.Time
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalItemRepositoryImpl @Inject constructor(private val dao: ItemDao) :
    ILocalItemRepository {
    override fun getAllItems(): Flow<List<Item>> {
        return dao.getAllItems()
    }

    override fun getAllItemsList(): List<Item> {
        return dao.getAllItemsList()
    }

    override suspend fun insert(item: Item): Long {
        return dao.insert(item)
    }

    override suspend fun insert(routine: Routine): Long {
        return dao.insert(routine)
    }

    override suspend fun insert(accomplishment: Accomplishment): Long {
        return dao.insert(accomplishment)
    }

    override suspend fun update(item: Item) {
        return dao.update(item)
    }

    override suspend fun update(routine: Routine) {
        return dao.update(routine)
    }

    override suspend fun update(accomplishment: Accomplishment) {
        return dao.update(accomplishment)
    }

    override suspend fun delete(item: Item) {
        return dao.delete(item)
    }

    override suspend fun delete(routine: Routine) {
        return dao.delete(routine)
    }

    override suspend fun delete(accomplishment: Accomplishment) {
        return dao.delete(accomplishment)
    }

    override suspend fun deleteItemById(itemId: Long) {
        dao.deleteItemById(itemId)
    }

    override suspend fun getItem(id: Long): Item {
        return dao.getItem(id)
    }

    override fun getAllRoutines(): Flow<List<Routine>> {
        return dao.getAllRoutines()
    }

    override fun getAllRoutinesList(): List<Routine> {
        return dao.getAllRoutinesList()
    }

    override suspend fun deleteRoutineById(routineId: Long) {
        dao.deleteRoutineById(routineId)
    }

    override suspend fun getRoutine(id: Long): Routine {
        return dao.getRoutine(id)
    }

    override fun getAllAccomplishments(): Flow<List<Accomplishment>> {
        return dao.getAllAccomplishments()
    }

    override fun getAllAccomplishmentsList(): List<Accomplishment> {
        return dao.getAllAccomplishmentsList()
    }

    override fun getAllAccomplishmentsBetweenList(
        startTime: Long,
        endTime: Long
    ): List<Accomplishment> {
        return dao.getAllAccomplishmentsBetweenList(startTime, endTime)
    }

    override fun setAllAccomplishmentsToCompletedOnTime() {
        dao.setAllAccomplishmentsToCompletedOnTime()
    }

    override suspend fun getAccomplishment(id: Long): Accomplishment {
        return dao.getAccomplishment(id)
    }

    override suspend fun deleteRoutineAccomplishmentsBetween(
        routineId: Long,
        startTime: Long,
        endTime: Long
    ) {
        dao.deleteRoutineAccomplishmentsBetween(routineId, startTime, endTime)
    }


    override fun getItemsWithRoutines(): Flow<List<ItemWithRoutines>> {
        return dao.getItemsWithRoutines()
    }

    override fun getItemWithRoutines(id: Long): Flow<ItemWithRoutines> {
        return dao.getItemWithRoutines(id)
    }

    override fun getRoutinesWithAccomplishments(): List<RoutineWithAccomplishments> {
        return dao.getRoutinesWithAccomplishments()
    }

    override fun getItemsWithRoutinesAndAccomplishments(): Flow<List<ItemWithRoutinesAndAccomplishments>> {
        return dao.getItemsWithRoutinesAndAccomplishments()
    }

    override suspend fun getRoutineWithAccomplishments(id: Long): RoutineWithAccomplishments {
        return dao.getRoutineWithAccomplishments(id)
    }


    override suspend fun getConsecutiveDaysCompletedOnTime(): Int {
        val sortedAccomplishments = dao.getAllAccomplishmentsList()

        if (sortedAccomplishments.isEmpty()) return 0

        // Calculate the maximum possible consecutive days from the last item in the list
        val maxPossibleConsecutiveDays = Time.numberOfDaysBetweenTwoTimes(Time.currentTime(), sortedAccomplishments.last().shouldAccomplishTime!!) + 1

        Log.i("planty", "maxpos: $maxPossibleConsecutiveDays")

        var maxConsecutiveDays = maxPossibleConsecutiveDays
        var todaySkipped = false

        // Iterate through sorted accomplishments
        for (accomplishment in sortedAccomplishments) {
            val isCompletedOnTime = accomplishment.accomplishedTime != null &&
                    accomplishment.accomplishedTime!! <= accomplishment.shouldAccomplishTime!!

            if (!isCompletedOnTime) {
                // Check if shouldAccomplishTime is today
                if (Time.getStartOfDay(accomplishment.shouldAccomplishTime!!) == Time.getStartOfDay(Time.currentTime())) {
                    // Skip today's accomplishment
                    todaySkipped = true
                    continue
                } else {
                    // Calculate days difference between shouldAccomplishTime and today
                    val daysDifference = Time.numberOfDaysBetweenTwoTimes(Time.currentTime(), accomplishment.shouldAccomplishTime!!)
                    maxConsecutiveDays = daysDifference
                    break
                }
            }
        }

        // Adjust maxConsecutiveDays if today was skipped
        if (todaySkipped) {
            maxConsecutiveDays--
        }

        // Ensure maxConsecutiveDays is not negative
        return maxOf(maxConsecutiveDays, 0)
    }

    override fun getTotalAccomplishments(): Int {
        return dao.getTotalAccomplishments()
    }

    override fun getAccomplishedCount(): Int {
        return dao.getAccomplishedCount()
    }

    override fun getOnTimeAccomplishedCount(): Int {
        return dao.getOnTimeAccomplishedCount()
    }

    override fun getTotalRoutines(): Int {
        return dao.getTotalRoutines()
    }

    override fun getStatistics(): Statistics {
        return dao.getStatistics()
    }

}