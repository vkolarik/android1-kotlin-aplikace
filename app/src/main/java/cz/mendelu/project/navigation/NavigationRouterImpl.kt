package cz.mendelu.project.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun navigateToStatisticsScreen() {
        navController.navigate(Destination.StatisticsScreen.route)
    }

    override fun navigateToAboutScreen() {
        navController.navigate(Destination.AboutScreen.route)
    }

    override fun navigateToItemListScreen() {
        navController.navigate(Destination.ItemListScreen.route)
    }

    override fun navigateToItemDetailScreen(id: Long) {
        navController.navigate(Destination.ItemDetailScreen.route + "/" + id)
    }

    override fun navigateToItemAddEditScreen(id: Long?) {
        if (id != null) {
            navController.navigate(Destination.ItemAddEditScreen.route + "/" + id)
        } else {
            navController.navigate(Destination.ItemAddEditScreen.route)
        }

    }

    override fun navigateToRoutineAddEditScreen(itemId: Long, id: Long?) {
        if (id != null) {
            navController.navigate(Destination.RoutineAddEditScreen.route + "/$itemId/$id")
        } else {
            navController.navigate(Destination.RoutineAddEditScreen.route + "/$itemId")
        }
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun getNavController(): NavController {
        return navController
    }

}