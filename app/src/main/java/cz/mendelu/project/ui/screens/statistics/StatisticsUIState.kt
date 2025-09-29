package cz.mendelu.project.ui.screens.statistics

sealed class StatisticsUIState {
    class Loading : StatisticsUIState()
    class Success(val data: StatisticsScreenData) : StatisticsUIState()

}
