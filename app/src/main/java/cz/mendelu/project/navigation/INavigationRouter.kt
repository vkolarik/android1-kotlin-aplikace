package cz.mendelu.project.navigation

import androidx.navigation.NavController

interface INavigationRouter {

    fun returnBack()

    fun navigateToStatisticsScreen()

    fun navigateToAboutScreen()

    fun navigateToItemListScreen()
    fun navigateToItemDetailScreen(id: Long)
    fun navigateToItemAddEditScreen(id: Long?)
    fun navigateToRoutineAddEditScreen(itemId: Long, id: Long?)

    fun getNavController(): NavController?
}