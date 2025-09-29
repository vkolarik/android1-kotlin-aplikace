package cz.mendelu.project.ui.screens.Items.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.project.R
import cz.mendelu.project.model.relations.ItemWithRoutines
import cz.mendelu.project.navigation.INavigationRouter
import cz.mendelu.project.ui.elements.BaseScreen
import cz.mendelu.project.ui.elements.PlaceholderScreenDefinition
import cz.mendelu.project.ui.elements.ProfileImage
import cz.mendelu.project.ui.theme.BasicMargin
import cz.mendelu.project.ui.theme.HalfMargin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<ItemListViewModel>()

    val iwrs: MutableList<ItemWithRoutines> = mutableListOf()

    viewModel.itemListUIState.value.let {
        when (it) {
            is ItemListUIState.Loading -> {
                viewModel.loadItems()
            }

            is ItemListUIState.Success -> {
                iwrs.addAll(it.iwrs)
            }
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.objects),
        onBackClick = { navigationRouter.returnBack() },
        floatingActionButton = {
            FloatingActionButton(content = {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }, onClick = {
                navigationRouter.navigateToItemAddEditScreen(null)
            })
        }, placeholderScreenDefinition = if (iwrs.size == 0) {
            PlaceholderScreenDefinition(
                image = R.drawable.undraw_add_files_re_v09g,
                text = stringResource(R.string.no_objects_created_use_button)
            )
        } else null
    ) {
        ItemListScreenContent(
            paddingValues = it, iwrs = iwrs, navigationRouter = navigationRouter
        )
    }
}

@Composable
fun ItemListScreenContent(
    paddingValues: PaddingValues, iwrs: List<ItemWithRoutines>, navigationRouter: INavigationRouter
) {

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(top = HalfMargin())
            .fillMaxSize()
    ) {
        iwrs.forEachIndexed { index, it ->
            item {
                ItemRow(navigationRouter = navigationRouter, iwr = it, onClick = {
                    navigationRouter.navigateToItemDetailScreen(it.item.id!!)
                })
            }
        }
    }
}

@Composable
fun ItemRow(
    iwr: ItemWithRoutines,
    onClick: () -> Unit,
    navigationRouter: INavigationRouter
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasicMargin(), vertical = HalfMargin()),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(BasicMargin())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = HalfMargin())
                ) {
                    // Image preview
                    ProfileImage(uri = iwr.item.image)

                    // Item name (heading)
                    Text(
                        text = iwr.item.name!!,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(start = 16.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { navigationRouter.navigateToItemAddEditScreen(iwr.item.id) },
                        modifier = Modifier.padding(end = HalfMargin()),
                    ) {
                        Icon(
                            modifier = Modifier.size(HalfMargin() * 4),
                            imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.edit),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Text(
                    text = if (iwr.routines.size > 0) stringResource(id = R.string.routines)+": " + iwr.routines.size.toString() else stringResource(
                        R.string.no_routines
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = HalfMargin())
                )


                iwr.routines.forEach { routine ->
                    Text(
                        text = routine.name ?: stringResource(id = R.string.undefined),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }


                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { onClick() },
                        modifier = Modifier.padding(end = HalfMargin()),
                    ) {
                        Text(text = stringResource(R.string.details))
                    }
                }
            }
        }

    }

}