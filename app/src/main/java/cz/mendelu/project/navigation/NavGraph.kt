package cz.mendelu.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.project.ui.screens.Items.addedit.ItemAddEditScreen
import cz.mendelu.project.ui.screens.Items.detail.ItemDetailScreen
import cz.mendelu.project.ui.screens.Items.list.ItemListScreen
import cz.mendelu.project.ui.screens.about.AboutScreen
import cz.mendelu.project.ui.screens.routines.addedit.RoutineAddEditScreen
import cz.mendelu.project.ui.screens.statistics.StatisticsScreen
import cz.mendelu.project.ui.screens.summary.SummaryScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigationRouter: INavigationRouter = remember {
        NavigationRouterImpl(navController)
    },
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        // ------------------- SUMMARY -----------------------
        composable(Destination.SummaryScreen.route) {
            SummaryScreen(navigationRouter = navigationRouter)
        }

        // ------------------- ABOUT -----------------------
        composable(Destination.AboutScreen.route) {
            AboutScreen(navigationRouter = navigationRouter/*, id = null*/)
        }

        // ------------------- STATISTICS -----------------------
        composable(Destination.StatisticsScreen.route) {
            StatisticsScreen(navigationRouter = navigationRouter)
        }

        // ------------------- ITEM -----------------------
        composable(Destination.ItemListScreen.route) {
            ItemListScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.ItemDetailScreen.route + "/{id}", arguments = listOf(
            navArgument(name = "id") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )) {
            val id = it.arguments?.getLong("id")
            ItemDetailScreen(navigationRouter = navigationRouter, id = id!!)
        }

        composable(Destination.ItemAddEditScreen.route) {
            ItemAddEditScreen(navigationRouter = navigationRouter, id = null)
        }

        composable(Destination.ItemAddEditScreen.route + "/{id}", arguments = listOf(
            navArgument(name = "id") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )) {
            val id = it.arguments?.getLong("id")
            ItemAddEditScreen(navigationRouter = navigationRouter, id = id!!)
        }

        // ------------------- ROUTINE -----------------------

        composable(Destination.RoutineAddEditScreen.route + "/{item_id}",
            arguments = listOf(
                navArgument(name = "item_id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )) {
            val itemId = it.arguments?.getLong("item_id")
            RoutineAddEditScreen(navigationRouter = navigationRouter, itemId = itemId!!, id = null)
        }

        composable(Destination.RoutineAddEditScreen.route + "/{item_id}/{routine_id}",
            arguments = listOf(
                navArgument(name = "item_id") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument(name = "routine_id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )) {
            val itemId = it.arguments?.getLong("item_id")
            val routineId = it.arguments?.getLong("routine_id")
            RoutineAddEditScreen(navigationRouter = navigationRouter, itemId = itemId!!, id = routineId!!)
        }
    }
}