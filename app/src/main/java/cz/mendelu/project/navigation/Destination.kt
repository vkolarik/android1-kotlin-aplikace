package cz.mendelu.project.navigation

sealed class Destination (val route: String){
    object SummaryScreen : Destination("summary")
    object AboutScreen : Destination("about")
    object StatisticsScreen : Destination("statistics")
    object ItemListScreen : Destination("item_list")
    object ItemDetailScreen : Destination("item_detail")
    object ItemAddEditScreen : Destination("add_edit_item")
    object RoutineAddEditScreen : Destination("add_edit_routine")
}