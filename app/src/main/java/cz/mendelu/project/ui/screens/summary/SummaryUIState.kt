package cz.mendelu.project.ui.screens.summary


sealed class SummaryUIState {
    object Loading : SummaryUIState()
    class Success(val data : SummaryScreenData) : SummaryUIState()

}