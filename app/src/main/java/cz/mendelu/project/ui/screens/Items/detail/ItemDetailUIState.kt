package cz.mendelu.project.ui.screens.Items.detail

import cz.mendelu.project.model.relations.ItemWithRoutines

sealed class ItemDetailUIState {
    class Loading : ItemDetailUIState()
    class Success(val iwr: ItemWithRoutines?) : ItemDetailUIState()

}
