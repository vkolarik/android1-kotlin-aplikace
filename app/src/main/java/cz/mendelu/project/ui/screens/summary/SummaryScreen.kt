package cz.mendelu.project.ui.screens.summary

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.project.R
import cz.mendelu.project.navigation.INavigationRouter
import cz.mendelu.project.ui.elements.BaseScreen
import cz.mendelu.project.ui.elements.PlaceholderScreenDefinition
import cz.mendelu.project.ui.elements.TaskCard
import cz.mendelu.project.ui.elements.TaskItem
import cz.mendelu.project.ui.theme.BasicMargin
import cz.mendelu.project.utils.DayInfo
import cz.mendelu.project.utils.Time

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<SummaryViewModel>()

    val state = viewModel.summaryUIState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(SummaryScreenData())
    }

    state.value.let {
        when (it) {
            is SummaryUIState.Loading -> {
                viewModel.loadIwraa()
            }

            is SummaryUIState.Success -> {
                data = it.data
            }
        }
    }

    BaseScreen(
        topBarText = stringResource(id = R.string.app_name),
        actions = {
            if (state.value != SummaryUIState.Loading && !data.isFirstRun) {
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.menu)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.align(Alignment.Top)
                ) {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        navigationRouter.navigateToItemListScreen()
                    }, text = {
                        Text(text = stringResource(id = R.string.objects))
                    })
                    DropdownMenuItem(onClick = {
                        expanded = false
                        navigationRouter.navigateToStatisticsScreen()
                    }, text = {
                        Text(text = stringResource(id = R.string.statistics))
                    })
                    DropdownMenuItem(onClick = {
                        expanded = false
                        navigationRouter.navigateToAboutScreen()
                    }, text = {
                        Text(text = stringResource(id = R.string.about_app))
                    })
                }
            }
        },
        placeholderScreenDefinition = if (state.value == SummaryUIState.Loading) {
            PlaceholderScreenDefinition(
                text = stringResource(id = R.string.loading)
            )
        } else null
    ) {
        if (data.isFirstRun) {
            Column(modifier = Modifier.padding(it)) {
                FirstRunScreenContent(actions = viewModel, data = data)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(it)) {
                item {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(BasicMargin())
                    ) {

                        SummaryScreenContent(
                            navigationRouter = navigationRouter,
                            data = data,
                            actions = viewModel
                        )

                    }
                }
            }
        }
    }
}


@Composable
fun SummaryScreenContent(
    navigationRouter: INavigationRouter,
    data: SummaryScreenData,
    actions: SummaryScreenActions
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.current_streak) + ": ${data.streak}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.streak_description),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
        }
    }

    val currentLocale = LocalContext.current.resources.configuration.locales[0]
    val javaLocale = java.util.Locale(currentLocale.language, currentLocale.country)
    val daysInfo = Time.getLast7DaysDates(locale = javaLocale)
    val tasksMap = mutableMapOf<DayInfo, MutableList<@Composable () -> Unit>>()

    // Collect tasks
    daysInfo.forEach { dayInfo ->
        val tasksForDay = mutableListOf<@Composable () -> Unit>()
        data.iwraa.forEach { itemWithRoutinesAndAccomplishments ->
            itemWithRoutinesAndAccomplishments.routines.forEach { routinesWithAccomplishments ->
                routinesWithAccomplishments.accomplishments.forEach { accomplishment ->
                    if (accomplishment.shouldAccomplishTime in dayInfo.startOfDayMillis..dayInfo.endOfDayMillis) {
                        tasksForDay.add {
                            TaskItem(
                                //imageUri = itemWithRoutinesAndAccomplishments.item.image,
                                name = itemWithRoutinesAndAccomplishments.item.name ?: "",
                                description = routinesWithAccomplishments.routine.name ?: "",
                                specifiedTime = if (routinesWithAccomplishments.routine.timeHours != null) "${
                                    String.format(
                                        "%02d",
                                        routinesWithAccomplishments.routine.timeHours
                                    )
                                }:${
                                    String.format(
                                        "%02d",
                                        routinesWithAccomplishments.routine.timeMinutes
                                    )
                                }" else null,
                                completed = accomplishment.accomplishedTime != null,
                                overdue = (accomplishment.shouldAccomplishTime
                                    ?: 0) < System.currentTimeMillis() && accomplishment.accomplishedTime == null,
                                onClick = {
                                    navigationRouter.navigateToRoutineAddEditScreen(
                                        itemId = itemWithRoutinesAndAccomplishments.item.id!!,
                                        id = routinesWithAccomplishments.routine.id
                                    )
                                },
                                onCheckboxClick = {
                                    if (accomplishment.accomplishedTime == null) {
                                        actions.finishTask(accomplishment)
                                    } else {
                                        actions.unfinishTask(accomplishment)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        tasksMap[dayInfo] = tasksForDay
    }

// Render the collected tasks
    Column {
        tasksMap.forEach { (dayInfo, taskItems) ->
            Spacer(modifier = Modifier.height(8.dp))
            TaskCard(
                title = dayInfo.dayName,
                subtitle = dayInfo.formattedDate,
                onSelectAllClick = if (taskItems.isEmpty()) null else {
                    { actions.finishAllTasks(dayInfo.startOfDayMillis, dayInfo.endOfDayMillis) }
                }
            ) {
                if (taskItems.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_tasks),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    taskItems.forEach { taskItem ->
                        taskItem()
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FirstRunScreenContent(
    actions: SummaryScreenActions,
    data: SummaryScreenData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = stringResource(R.string.app_icon),
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.welcome_in_planty_app),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.start_with_these_actions),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                actions.endFirstRun()
                data.isFirstRun = false
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(R.string.continue_to_app),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Button(
            onClick = {
                actions.insertSampleData()
                data.isFirstRun = false
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(R.string.insert_sample_data),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            text = stringResource(R.string.explore_and_enjoy_app),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

