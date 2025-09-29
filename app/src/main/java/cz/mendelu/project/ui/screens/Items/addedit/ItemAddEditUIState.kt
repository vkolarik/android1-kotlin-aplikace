package cz.mendelu.project.ui.screens.Items.addedit

sealed class ItemAddEditUIState {
    object Loading : ItemAddEditUIState()
    object ItemSaved : ItemAddEditUIState()
    class ItemChanged(val data: ItemAddEditScreenData) : ItemAddEditUIState()
    object ItemDeleted : ItemAddEditUIState()

}