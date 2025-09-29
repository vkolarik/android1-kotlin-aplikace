package cz.mendelu.project.ui.screens.summary

import cz.mendelu.project.model.Accomplishment

interface SummaryScreenActions {
    fun finishTask(accomplishment: Accomplishment)

    fun unfinishTask(accomplishment: Accomplishment)

    fun finishAllTasks(startTime: Long, endTime: Long)

    fun insertSampleData()

    fun endFirstRun()
}