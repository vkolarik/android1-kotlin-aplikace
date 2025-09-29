package cz.mendelu.project.logic

import android.util.Log
import cz.mendelu.project.database.ILocalItemRepository
import cz.mendelu.project.model.Accomplishment
import cz.mendelu.project.model.Routine
import cz.mendelu.project.utils.Constants
import cz.mendelu.project.utils.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccomplishmentManager @Inject constructor(
    private val repository: ILocalItemRepository
) {
    suspend fun generateAccomplishmentsForAllRoutines() {
        withContext(Dispatchers.IO) {
            val routines = repository.getAllRoutinesList()
            val today = System.currentTimeMillis()
            val todayStartOfDay = Time.getStartOfDay(today)

            Log.i("planty", "body")

            for (routine in routines) {
                if (routine.active == true){
                    Log.i("planty", "loop+" + routine.lastGeneratedAccomplishments)
                    Log.i("planty", "let")
                    val lastGenerated =
                        routine.lastGeneratedAccomplishments ?: (today - Constants.ONE_DAY_MILLIS)
                    val lastGeneratedStartOfDay = Time.getStartOfDay(lastGenerated)

                    if (lastGeneratedStartOfDay < todayStartOfDay) {
                        Log.i("planty", "lastGenerated < today")

                        generateAccomplishments(routine)

                        routine.lastGeneratedAccomplishments = today
                        repository.update(routine)
                    }
                }
            }
        }
    }

    private suspend fun generateAccomplishments(routine: Routine) {
        var loopDate = 0
        val start = Time.getStartOfDay(routine.startDate!!)
        val today = Time.getStartOfDay(Time.currentTime())

        Log.i("planty", "generateAccomplishments")

        while (start + loopDate <= today) {
            Log.i("planty", "while")

            val differenceInDays = ((today - (start + loopDate)) / Constants.ONE_DAY_MILLIS).toInt()

            Log.i(
                "planty",
                "dif $differenceInDays freq ${routine.frequencyDays} start $start today $today loopD $loopDate"
            )

            if (differenceInDays % (routine.frequencyDays!!) == 0) {
                val shouldAccomplishTime = calculateShouldAccomplishTime(start + loopDate, routine)
                Log.i(
                    "planty",
                    "create acc ct $loopDate dif $differenceInDays freq ${routine.frequencyDays} sat $shouldAccomplishTime"
                )
                val accomplishment = Accomplishment(
                    routineId = routine.id,
                    shouldAccomplishTime = shouldAccomplishTime,
                    accomplishedTime = null // Not accomplished yet
                )
                repository.insert(accomplishment)
            }
            loopDate += Constants.ONE_DAY_MILLIS
        }
    }

    private fun calculateShouldAccomplishTime(dateInMillis: Long, routine: Routine): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = dateInMillis
            if (routine.timeHours != null && routine.timeMinutes != null) {
                set(Calendar.HOUR_OF_DAY, routine.timeHours!!)
                set(Calendar.MINUTE, routine.timeMinutes!!)
            } else {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
            }
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
}
