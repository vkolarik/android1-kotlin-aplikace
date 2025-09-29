package cz.mendelu.project.ui.screens.Items.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.project.database.ILocalItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(private val repository: ILocalItemRepository) : ViewModel(){

    val itemListUIState : MutableState<ItemListUIState> = mutableStateOf(ItemListUIState.Loading())

    fun loadItems(){
        viewModelScope.launch {
            repository.getItemsWithRoutines().collect{
                itemListUIState.value = ItemListUIState.Success(it)
            }
        }
    }
}
