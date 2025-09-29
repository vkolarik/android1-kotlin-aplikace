package cz.mendelu.project.ui.screens.Items.addedit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.project.R
import cz.mendelu.project.navigation.INavigationRouter
import cz.mendelu.project.ui.elements.BaseScreen
import cz.mendelu.project.ui.elements.BigImageFromUri
import cz.mendelu.project.ui.theme.BasicMargin
import cz.mendelu.project.ui.theme.HalfMargin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAddEditScreen(navigationRouter: INavigationRouter, id: Long?) {

    val context = LocalContext.current

    val viewModel = hiltViewModel<ItemAddEditViewModel>()

    val state = viewModel.addEditItemUIState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(ItemAddEditScreenData())
    }

    state.value.let {
        when (it) {
            ItemAddEditUIState.Loading -> {
                viewModel.loadItem(id)
            }

            is ItemAddEditUIState.ItemChanged -> {
                data = it.data
            }

            ItemAddEditUIState.ItemSaved -> {
                LaunchedEffect(it) {
                    navigationRouter.returnBack()
                }
            }

            ItemAddEditUIState.ItemDeleted -> {
                //launched effect zamezi rekompozici nad touto promennou a tak se nedostaneme na bilou obrazovku
                LaunchedEffect(it) {
                    navigationRouter.returnBack()
                }
            }
        }
    }

    BaseScreen(
        topBarText = if (id == null) stringResource(R.string.add_object) else stringResource(R.string.edit_object),
        onBackClick = {
            viewModel.checkForSavedImageOnBackClick(context)
            navigationRouter.returnBack()
        },
        actions = {
            if (id != null) {
                IconButton(onClick = { viewModel.deleteItem(context = context) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            item {
                ItemAddEditScreenContent(data = data, actions = viewModel)
            }
        }


    }
}

@Composable
fun ItemAddEditScreenContent(
    data: ItemAddEditScreenData,
    actions: ItemAddEditScreenActions
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            actions.newImagePreview(context, it)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(HalfMargin())
    ) {
        Column(modifier = Modifier.padding(HalfMargin()), horizontalAlignment = Alignment.End) {
            BigImageFromUri(uri = data.item.image)
            Row {
                if (data.item.image != null) {
                    Button(
                        onClick = {
                            actions.deleteImage(context = context)
                        }
                    ) {
                        Text(stringResource(R.string.remove_image), color = Color.White)
                    }
                }
                Button(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    modifier = Modifier.padding(start = HalfMargin())
                ) {
                    Text(
                        if (data.item.image != null) stringResource(R.string.change_image) else stringResource(
                            R.string.add_image
                        ),
                        color = Color.White
                    )
                }
            }
        }

        TextField(
            label = { Text(stringResource(R.string.title)) },
            modifier = Modifier
                .padding(horizontal = HalfMargin())
                .fillMaxWidth(),
            value = data.item.name ?: "",
            onValueChange = {
                data.item.name = it
                actions.onItemDataChanged(data)
            },
            singleLine = true,
            isError = data.errors.nameError != null,
            supportingText = {
                if (data.errors.nameError != null) {
                    Text(text = data.errors.nameError!!)
                }
            }
        )


        TextField(
            label = { Text(stringResource(R.string.description_optional)) },
            modifier = Modifier
                .padding(horizontal = HalfMargin())
                .fillMaxWidth(),
            value = data.item.description ?: "",
            onValueChange = {
                data.item.description = it
                actions.onItemDataChanged(data)
            },
            maxLines = 6,
            isError = data.errors.descriptionError != null,
            supportingText = {
                if (data.errors.descriptionError != null) {
                    Text(text = data.errors.descriptionError!!)
                }
            }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = BasicMargin())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = {
                    actions.onItemDataChanged(data)
                    actions.saveItem()
                }) {
                Text(text = stringResource(R.string.save))
            }
        }

    }
}


