package cz.mendelu.project.ui.screens.Items.list

import cz.mendelu.project.model.relations.ItemWithRoutines

sealed class ItemListUIState {
    class Loading : ItemListUIState()
    class Success(val iwrs: List<ItemWithRoutines>) : ItemListUIState()

}
