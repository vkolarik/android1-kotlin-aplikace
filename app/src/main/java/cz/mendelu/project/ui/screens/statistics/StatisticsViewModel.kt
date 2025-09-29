package cz.mendelu.project.ui.screens.statistics

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.project.database.ILocalItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val repository: ILocalItemRepository) : ViewModel(){

    val itemListUIState : MutableState<StatisticsUIState> = mutableStateOf(StatisticsUIState.Loading())

    private var data = StatisticsScreenData()

    fun loadItems(){
        viewModelScope.launch(Dispatchers.IO) {
            data.statistics = repository.getStatistics()
            data.streak = repository.getConsecutiveDaysCompletedOnTime()
            itemListUIState.value = StatisticsUIState.Success(data)
        }
    }
}
