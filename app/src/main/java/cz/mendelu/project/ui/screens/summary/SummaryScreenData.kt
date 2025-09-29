package cz.mendelu.project.ui.screens.summary

import cz.mendelu.project.model.relations.ItemWithRoutinesAndAccomplishments

class SummaryScreenData {
    var iwraa: List<ItemWithRoutinesAndAccomplishments> = listOf()
    var isFirstRun : Boolean = false
    var streak: Int = -1
}