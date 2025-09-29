package cz.mendelu.project.ui.screens.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.project.R
import cz.mendelu.project.navigation.INavigationRouter
import cz.mendelu.project.ui.elements.BaseScreen
import cz.mendelu.project.ui.elements.PlaceholderScreenDefinition
import cz.mendelu.project.ui.theme.HalfMargin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<StatisticsViewModel>()

    val state = viewModel.itemListUIState

    var data by remember {
        mutableStateOf(StatisticsScreenData())
    }

    state.value.let {
        when (it) {
            is StatisticsUIState.Loading -> viewModel.loadItems()
            is StatisticsUIState.Success -> data = it.data
        }
    }


    BaseScreen(
        topBarText = stringResource(R.string.statistics),
        onBackClick = { navigationRouter.returnBack() },
        placeholderScreenDefinition = if (data.statistics == null) PlaceholderScreenDefinition(text = stringResource(
            R.string.loading
        )
        ) else null
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(HalfMargin())
                .fillMaxSize()
        ) {

                item {
                    StatisticsScreenContent(data = data)
                }

        }
    }
}

@Composable
fun StatisticsScreenContent(
    data: StatisticsScreenData
) {
    HorizontalCard(title = stringResource(R.string.overall_completion), description = "${data.statistics?.percentageAccomplished}%")
    HorizontalCard(title = stringResource(R.string.ontime_completion), description = "${data.statistics?.percentageOnTime}%")
    HorizontalCard(title = stringResource(R.string.total_number_of_completions), description = "${data.statistics?.totalFinishedAccomplishments}")
    HorizontalCard(title = stringResource(R.string.total_routines), description = "${data.statistics?.totalRoutines}")
    HorizontalCard(title = stringResource(R.string.days_without_missing), description = "${data.streak}")
}


@Composable
fun HorizontalCard(title: String, description: String) {
    Card(
        shape = RoundedCornerShape(HalfMargin()),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}