package cz.mendelu.project.ui.screens.Items.addedit

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.project.database.ILocalItemRepository
import cz.mendelu.project.ui.elements.copyImageToAppStorage
import cz.mendelu.project.ui.elements.deleteImageFromAppStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemAddEditViewModel @Inject constructor(
    private val repository: ILocalItemRepository
) : ViewModel(), ItemAddEditScreenActions {

    private val _addEditItemUIState: MutableStateFlow<ItemAddEditUIState> = MutableStateFlow(
        ItemAddEditUIState.Loading
    )

    val addEditItemUIState = _addEditItemUIState.asStateFlow()

    private var data = ItemAddEditScreenData()

    override fun saveItem() {
        Log.i("planty", "SAVING id:" + data.item.id.toString())
        Log.i("planty", "SAVING name:" + data.item.name.toString())

        val currentErrors = ItemAddEditErrors()

        if (data.item.name.isNullOrBlank()) {
            currentErrors.errorOccurred = true
            currentErrors.nameError = "Pole nesmí být prázdné"
        }


        if (!currentErrors.errorOccurred) {
            viewModelScope.launch {
                if (data.item.id != null) {
                    repository.update(data.item)
                } else {
                    repository.insert(data.item)
                }
                _addEditItemUIState.update { ItemAddEditUIState.ItemSaved }
            }
        } else {
            data.errors = currentErrors
            _addEditItemUIState.update { ItemAddEditUIState.ItemChanged(data) }
        }
    }

    override fun onItemDataChanged(data: ItemAddEditScreenData) {
        this.data = data
        _addEditItemUIState.update { ItemAddEditUIState.ItemChanged(data) }
    }

    fun loadItem(id: Long?) {
        if (id != null) {
            //edit
            viewModelScope.launch {
                data.item = repository.getItem(id)
                _addEditItemUIState.update {
                    ItemAddEditUIState.ItemChanged(data)
                }
            }
        } else {
            ItemAddEditUIState.ItemChanged(data)
        }
    }

    override fun deleteItem(context: Context) {
        viewModelScope.launch {
            if (data.item.image != null) deleteImageFromAppStorage(Uri.parse(data.item.image))
            repository.deleteItemById(data.item.id!!)
            _addEditItemUIState.update {
                ItemAddEditUIState.ItemDeleted
            }
        }
    }

    override fun newImagePreview(context: Context, uri: Uri) {
        data.item.image = copyImageToAppStorage(context, uri).toString()
        writeImageToDB()
        onItemDataChanged(data)
    }

    override fun deleteImage(context: Context) {
        deleteImageFromAppStorage(Uri.parse(data.item.image))
        clearImageFromDB()
        data.item.image = null
        onItemDataChanged(data)
    }

    fun checkForSavedImageOnBackClick(context: Context) {
        if (data.item.id == null && data.item.image != null) {
            deleteImage(context = context)
        }
    }

    private fun clearImageFromDB() {
        if (data.item.id != null) {
            viewModelScope.launch {
                val item = repository.getItem(data.item.id!!)
                val updatedItem = item.copy(image = null)
                repository.update(updatedItem)
            }
        }
    }

    private fun writeImageToDB() {
        if (data.item.id != null) {
            viewModelScope.launch {
                val item = repository.getItem(data.item.id!!)
                val updatedItem = item.copy(image = data.item.image)
                repository.update(updatedItem)
            }
        }
    }
}