package cz.mendelu.project.ui.screens.Items.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.project.database.ILocalItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(private val repository: ILocalItemRepository) :
    ViewModel() {

    private val _itemDetailUIState: MutableStateFlow<ItemDetailUIState> =
        MutableStateFlow(ItemDetailUIState.Loading())

    val itemDetailUIState = _itemDetailUIState.asStateFlow()

    fun loadItem(id: Long) {
        viewModelScope.launch {
            repository.getItemWithRoutines(id).collect() {
                _itemDetailUIState.value = ItemDetailUIState.Success(it)
            }
        }
    }
}
