package cz.mendelu.project.ui.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.project.database.ILocalItemRepository
import cz.mendelu.project.datastore.IDataStoreRepository
import cz.mendelu.project.logic.AccomplishmentManager
import cz.mendelu.project.model.Accomplishment
import cz.mendelu.project.model.Item
import cz.mendelu.project.model.Routine
import cz.mendelu.project.utils.Constants
import cz.mendelu.project.utils.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val repository: ILocalItemRepository,
    private val dataStoreRepository: IDataStoreRepository,
    private val accomplishmentManager: AccomplishmentManager
) : ViewModel(), SummaryScreenActions {

    private val _summaryUIState: MutableStateFlow<SummaryUIState> =
        MutableStateFlow(SummaryUIState.Loading)
    val summaryUIState = _summaryUIState.asStateFlow()

    private var data = SummaryScreenData()

    fun loadIwraa() {
        viewModelScope.launch(Dispatchers.IO) {
            if (dataStoreRepository.getFirstRun()) {
                data.isFirstRun = true
                _summaryUIState.update {
                    SummaryUIState.Success(data)
                }
                dataStoreRepository.setFirstRun()
            } else {
                repository.getItemsWithRoutinesAndAccomplishments().collect {
                    data.iwraa = it
                    data.streak = repository.getConsecutiveDaysCompletedOnTime()
                    _summaryUIState.update {
                        SummaryUIState.Success(data)
                    }
                }
            }
        }
    }

    override fun finishTask(accomplishment: Accomplishment) {
        accomplishment.accomplishedTime = System.currentTimeMillis()
        viewModelScope.launch {
            repository.update(accomplishment)
        }
    }

    override fun unfinishTask(accomplishment: Accomplishment) {
        accomplishment.accomplishedTime = null
        viewModelScope.launch {
            repository.update(accomplishment)
        }
    }

    override fun finishAllTasks(startTime: Long, endTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllAccomplishmentsBetweenList(startTime, endTime).forEach {
                if (it.accomplishedTime == null){
                    finishTask(it)
                }
            }
        }
    }

    override fun insertSampleData() {
        viewModelScope.launch(Dispatchers.IO) {
            val millis = Time.getStartOfDay(System.currentTimeMillis() - Constants.ONE_DAY_MILLIS * 7)

            val bazalkaId = repository.insert(
                Item(
                    name = "Bazalka",
                    description = "Pokojová rostlina",
                    image = null
                )
            )
            val krecekId = repository.insert(
                Item(
                    name = "Křeček",
                    description = "Křeček Artur, koupen ve zverimexu u Mrtvého koně. Má šedou srst a modrý čumáček.",
                    image = null
                )
            )
            val pazitkaId = repository.insert(
                Item(
                    name = "Pažitka",
                    description = "Pažitka na balkóně, koupena 22.2.2023",
                    image = null
                )
            )

            repository.insert(
                Routine(
                    itemId = bazalkaId,
                    name = "Odpolední zalévání",
                    active = true,
                    startDate = millis,
                    frequencyDays = 2,
                    timeHours = 8,
                    timeMinutes = 0
                )
            )
            repository.insert(
                Routine(
                    itemId = krecekId,
                    name = "Nakrmit",
                    active = false,
                    startDate = millis,
                    frequencyDays = 1
                )
            )
            repository.insert(
                Routine(
                    itemId = krecekId,
                    name = "Vyměnit vodu",
                    active = true,
                    startDate = millis,
                    frequencyDays = 3
                )
            )
            repository.insert(
                Routine(
                    itemId = pazitkaId,
                    name = "Ostříhat",
                    active = false,
                    startDate = millis,
                    frequencyDays = 14
                )
            )
            repository.insert(
                Routine(
                    itemId = pazitkaId,
                    name = "Zalít",
                    active = false,
                    startDate = millis,
                    frequencyDays = 3
                )
            )
            accomplishmentManager.generateAccomplishmentsForAllRoutines()
            repository.setAllAccomplishmentsToCompletedOnTime()
            loadIwraa()
        }
    }

    override fun endFirstRun() {
        loadIwraa()
    }
}
