package cz.mendelu.project.ui.screens.routines.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.project.R
import cz.mendelu.project.database.ILocalItemRepository
import cz.mendelu.project.logic.AccomplishmentManager
import cz.mendelu.project.utils.Constants
import cz.mendelu.project.utils.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RoutineAddEditViewModel @Inject constructor(
    private val repository: ILocalItemRepository,
    private val accomplishmentManager: AccomplishmentManager
) : ViewModel(), RoutineAddEditScreenActions {

    private val _addEditRoutineUIState: MutableStateFlow<RoutineAddEditUIState> = MutableStateFlow(
        RoutineAddEditUIState.Loading
    )

    val addEditRoutineUIState = _addEditRoutineUIState.asStateFlow()

    private var data = RoutineAddEditScreenData()

    override fun saveRoutine() {
        val currentErrors = RoutineAddEditErrors()

        // NAME
        if (data.routine.name.isNullOrBlank()) {
            currentErrors.errorOccurred = true
            currentErrors.nameError = R.string.field_must_not_be_empty
        }

        //DATE
        val dateFormat = SimpleDateFormat("d.M.yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        try {
            val date = dateFormat.parse(data.dateInput)
            if (date == null) {
                currentErrors.errorOccurred = true
                currentErrors.dateInputError = R.string.invalid_date
            }
        } catch (e: Exception) {
            currentErrors.errorOccurred = true
            currentErrors.dateInputError = R.string.enter_date_in_format_example
        }

        //TIME
        if (data.showTimeInput) {
            val timePattern = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")

            if (!timePattern.matches(data.timeInput)) {
                currentErrors.errorOccurred = true
                currentErrors.timeInputError = R.string.enter_time_in_format_example
            }
        }

        //FREQUENCY
        if (data.selectedFrequencyOption == FrequencyPicker.MANUAL) {
            if (isStringConvertibleToInteger(data.frequencyManualInput)) {
                if (data.frequencyManualInput.toInt() !in 1..1825) {
                    currentErrors.errorOccurred = true
                    currentErrors.frequencyInputError = R.string.valid_range_is_1_to_1825
                }
            } else {
                currentErrors.errorOccurred = true
                currentErrors.frequencyInputError = R.string.frequency_is_required
            }
        }

        if (!currentErrors.errorOccurred) {
            data.routine.itemId = data.item.id
            if (data.showTimeInput) {
                data.routine.timeHours = data.timeInput.split(":")[0].toInt()
                data.routine.timeMinutes = data.timeInput.split(":")[1].toInt()
            } else {
                data.routine.timeHours = null
                data.routine.timeMinutes = null
            }
            data.routine.frequencyDays = when (data.selectedFrequencyOption) {
                FrequencyPicker.WEEKLY -> {
                    7
                }

                FrequencyPicker.DAILY -> {
                    1
                }

                else -> {
                    data.frequencyManualInput.toInt()
                }
            }
            data.routine.active = data.isActive
            data.routine.startDate = convertDateToMillis(data.dateInput)


            //if active status was changed
            if (data.startingActiveState != data.isActive) {
                //set last generated time to yesterday before midnight to regenerate this day
                data.routine.lastGeneratedAccomplishments =
                    Time.getEndOfDay(Time.currentTime() - Constants.ONE_DAY_MILLIS)
                //remove all accomplishments generated for today
                viewModelScope.launch {
                    if (data.routine.id != null) {
                        repository.deleteRoutineAccomplishmentsBetween(
                            data.routine.id!!,
                            Time.getStartOfDay(Time.currentTime()),
                            Time.getEndOfDay(Time.currentTime())
                        )
                    }
                }
            }

            viewModelScope.launch {
                if (data.routine.id != null) {
                    repository.update(data.routine)
                } else {
                    repository.insert(data.routine)
                }
                _addEditRoutineUIState.update { RoutineAddEditUIState.RoutineSaved }
                accomplishmentManager.generateAccomplishmentsForAllRoutines()
            }
        } else {
            data.errors = currentErrors
            _addEditRoutineUIState.update { RoutineAddEditUIState.RoutineChanged(data) }
        }
    }

    override fun onRoutineDataChanged(data: RoutineAddEditScreenData) {
        data.timeInput = data.timeInput.replace(Regex("[^0-9:]"), "")
        data.dateInput = data.dateInput.replace(Regex("[^0-9.]"), "")
        data.frequencyManualInput = data.frequencyManualInput.replace(Regex("[^0-9]"), "")
        this.data = data
        _addEditRoutineUIState.update { RoutineAddEditUIState.RoutineChanged(data) }
    }

    fun loadRoutine(itemId: Long, id: Long?) {

        viewModelScope.launch {
            if (id != null) {
                data.routine = repository.getRoutine(id)
                data.showTimeInput = data.routine.timeHours != null
                data.selectedFrequencyOption = when (data.routine.frequencyDays) {
                    1 -> {
                        FrequencyPicker.DAILY
                    }

                    7 -> {
                        FrequencyPicker.WEEKLY
                    }

                    else -> {
                        FrequencyPicker.MANUAL
                    }
                }
                data.frequencyManualInput = data.routine.frequencyDays.toString()
                data.isActive = data.routine.active == true
                data.timeInput =
                    if (data.routine.timeHours == null && data.routine.timeMinutes == null) "" else "${
                        String.format(
                            "%02d",
                            data.routine.timeHours
                        )
                    }:${String.format("%02d", data.routine.timeMinutes)}"
                data.dateInput = Time.convertMillisToDateString(
                    data.routine.startDate ?: System.currentTimeMillis()
                )
                data.startingActiveState = data.routine.active == true

            }
            data.item = repository.getItem(itemId)
            _addEditRoutineUIState.update {
                RoutineAddEditUIState.RoutineChanged(data)
            }
        }
    }

    override fun deleteRoutine() {
        viewModelScope.launch {
            repository.deleteRoutineById(data.routine.id!!)
            _addEditRoutineUIState.update {
                RoutineAddEditUIState.RoutineDeleted
            }
        }
    }

    private fun isStringConvertibleToInteger(input: String): Boolean {
        return try {
            input.toInt()
            true // Conversion succeeded
        } catch (e: NumberFormatException) {
            false // Conversion failed
        }
    }

    fun convertDateToMillis(dateString: String): Long {
        val dateFormat = SimpleDateFormat("d.M.yyyy", Locale.getDefault())
        val date: Date? = dateFormat.parse(dateString)
        return date?.time ?: 0L
    }
}