package cz.mendelu.project.ui.screens.routines.addedit

data class RoutineAddEditErrors(
    var errorOccurred: Boolean = false,
    var nameError: Int? = null,
    var dateInputError: Int? = null,
    var timeInputError: Int? = null,
    var frequencyInputError: Int? = null,
)