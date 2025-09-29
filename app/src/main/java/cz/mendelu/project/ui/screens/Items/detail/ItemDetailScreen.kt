package cz.mendelu.project.ui.screens.Items.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.project.R
import cz.mendelu.project.model.Routine
import cz.mendelu.project.model.relations.ItemWithRoutines
import cz.mendelu.project.navigation.INavigationRouter
import cz.mendelu.project.ui.elements.BaseScreen
import cz.mendelu.project.ui.elements.BigImageFromUri
import cz.mendelu.project.ui.elements.PlaceholderScreenDefinition
import cz.mendelu.project.ui.theme.BasicMargin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(navigationRouter: INavigationRouter, id: Long) {

    val viewModel = hiltViewModel<ItemDetailViewModel>()

    val state = viewModel.itemDetailUIState.collectAsStateWithLifecycle()

    var iwr: ItemWithRoutines? = null

    state.value.let {
        when (it) {
            is ItemDetailUIState.Loading -> {
                viewModel.loadItem(id)
            }

            is ItemDetailUIState.Success -> {
                iwr = it.iwr
                if(it.iwr == null){
                    LaunchedEffect(it) {
                        navigationRouter.returnBack()
                    }
                }
            }
        }
    }

    BaseScreen(
        topBarText = if (iwr != null) "${iwr!!.item.name}" else "",
        onBackClick = { navigationRouter.returnBack() },
        actions = {
            if (iwr != null) {
                IconButton(onClick = { navigationRouter.navigateToItemAddEditScreen(iwr!!.item.id) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
            }
        },
        placeholderScreenDefinition = if (iwr == null || iwr?.item?.id == null) PlaceholderScreenDefinition(text = stringResource(
            id = R.string.loading
        )) else null
    ) {
        if (iwr != null) {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                item {
                    ItemListScreenContent(
                        iwr = iwr!!, navigationRouter = navigationRouter
                    )
                }
            }

        }

    }
}

@Composable
fun ItemListScreenContent(
    iwr: ItemWithRoutines,
    navigationRouter: INavigationRouter
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Image Section
        BigImageFromUri(uri = iwr.item.image)

        // Notes Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.caption),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = iwr.item.description ?: stringResource(R.string.undefined))
            }
        }

        // Routines Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Row(
                    modifier = Modifier.padding(horizontal = BasicMargin()),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.routines),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box {
                        IconButton(onClick = {
                            navigationRouter.navigateToRoutineAddEditScreen(
                                iwr.item.id!!,
                                null
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.dropdown)
                            )
                        }
                    }
                }

                // List of routines
                iwr.routines.forEach { routine ->
                    RoutineItem(
                        routine,
                        onClick = {
                            navigationRouter.navigateToRoutineAddEditScreen(
                                iwr.item.id!!,
                                routine.id
                            )
                        })
                }
            }
        }
    }
}

@Composable
fun RoutineItem(routine: Routine, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = BasicMargin()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = routine.name + " (" + (if (routine.active == true) stringResource(R.string.active) else stringResource(
                    R.string.inactive
                )) + ")",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(text = stringResource(R.string.frequency_in_days) + ": ${routine.frequencyDays}")
        }
        IconButton(onClick = { onClick() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.edit_routine)
            )
        }
    }
}