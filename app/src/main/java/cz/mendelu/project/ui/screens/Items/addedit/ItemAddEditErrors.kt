package cz.mendelu.project.ui.screens.Items.addedit

data class ItemAddEditErrors(
    var errorOccurred: Boolean = false,
    var nameError: String? = null,
    var imageError: String? = null,
    var descriptionError: String? = null,
)