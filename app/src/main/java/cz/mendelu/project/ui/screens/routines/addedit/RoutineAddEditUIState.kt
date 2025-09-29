package cz.mendelu.project.ui.screens.routines.addedit

sealed class RoutineAddEditUIState {
    object Loading : RoutineAddEditUIState()
    object RoutineSaved : RoutineAddEditUIState()
    class RoutineChanged(val data: RoutineAddEditScreenData) : RoutineAddEditUIState()
    object RoutineDeleted : RoutineAddEditUIState()

}