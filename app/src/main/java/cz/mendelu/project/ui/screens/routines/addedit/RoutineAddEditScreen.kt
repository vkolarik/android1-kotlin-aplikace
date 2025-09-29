package cz.mendelu.project.ui.screens.routines.addedit

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.project.R
import cz.mendelu.project.navigation.INavigationRouter
import cz.mendelu.project.ui.elements.BaseScreen
import cz.mendelu.project.ui.theme.BasicMargin
import cz.mendelu.project.ui.theme.HalfMargin
import cz.mendelu.project.utils.Time
import java.util.Calendar

@Composable
fun RoutineAddEditScreen(navigationRouter: INavigationRouter, itemId: Long, id: Long?) {

    val viewModel = hiltViewModel<RoutineAddEditViewModel>()

    val state = viewModel.addEditRoutineUIState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(RoutineAddEditScreenData())
    }

    state.value.let {
        when (it) {
            RoutineAddEditUIState.Loading -> {
                viewModel.loadRoutine(itemId, id)
            }

            is RoutineAddEditUIState.RoutineChanged -> {
                data = it.data
            }

            RoutineAddEditUIState.RoutineSaved -> {
                LaunchedEffect(it) {
                    navigationRouter.returnBack()
                }
            }

            RoutineAddEditUIState.RoutineDeleted -> {
                //launched effect zamezi rekompozici nad touto promennou a tak se nedostaneme na bilou obrazovku
                LaunchedEffect(it) {
                    navigationRouter.returnBack()
                }
            }
        }
    }

    BaseScreen(
        topBarText = if (id == null) stringResource(R.string.add_routine) else stringResource(id = R.string.edit_routine),
        onBackClick = { navigationRouter.returnBack() },
        actions = {
            if (id != null) {
                IconButton(onClick = { viewModel.deleteRoutine() }) {
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
                RoutineAddEditScreenContent(data = data, actions = viewModel)
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineAddEditScreenContent(
    data: RoutineAddEditScreenData,
    actions: RoutineAddEditScreenActions,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HalfMargin()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(HalfMargin())
    ) {


        // ------------------- ITEM NAME -----------------------------
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.routine_of_object) + " ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(data.item.name)
                    }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(BasicMargin())
            )
        }

        // ------------------- ROUTINE NAME -----------------------------

        TextField(
            label = { stringResource(id = R.string.title) },
            modifier = Modifier
                .fillMaxWidth(),
            value = data.routine.name ?: "",
            onValueChange = {
                data.routine.name = it
                actions.onRoutineDataChanged(data)
            },
            singleLine = true,
            isError = data.errors.nameError != null,
            supportingText = {
                if (data.errors.nameError != null) {
                    Text(text = stringResource(id = data.errors.nameError!!))
                }
            }
        )

        // ------------------- FREQUENCY -----------------------------

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.frequency))
        }

        val options = listOf(FrequencyPicker.MANUAL, FrequencyPicker.DAILY, FrequencyPicker.WEEKLY)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, value ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = {
                        data.selectedFrequencyOption = value
                        actions.onRoutineDataChanged(data)
                    },
                    selected = value == data.selectedFrequencyOption
                ) {
                    Text(stringResource(id = value.titleResource))
                }
            }
        }

        Spacer(modifier = Modifier.height(HalfMargin()))

        // ------------------- FREQUENCY MANUAL INPUT -----------------------------

        if (data.selectedFrequencyOption == FrequencyPicker.MANUAL) {
            TextField(
                label = { Text(stringResource(R.string.how_many_days_to_repeat_routine)) },
                modifier = Modifier
                    .fillMaxWidth(),
                value = data.frequencyManualInput,
                onValueChange = {
                    data.frequencyManualInput = it
                    actions.onRoutineDataChanged(data)
                },
                maxLines = 1,
                isError = data.errors.frequencyInputError != null,
                supportingText = {
                    if (data.errors.frequencyInputError != null) {
                        Text(text = stringResource(id = data.errors.frequencyInputError!!))
                    }
                }
            )
        }

        // ------------------- START DATE -----------------------------
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = data.routine.startDate ?: System.currentTimeMillis()
        val datePickerDialog = DatePickerDialog(
            LocalContext.current,
            { _, year, month, dayOfMonth ->
                // Format the selected date and update the text field value
                val formattedDate = "$dayOfMonth.${month + 1}.$year"
                data.dateInput = formattedDate
                actions.onRoutineDataChanged(data)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // UI
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = data.dateInput,
                onValueChange = {
                    data.dateInput = it
                    actions.onRoutineDataChanged(data)
                },
                label = { Text(stringResource(R.string.start_date)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = stringResource(R.string.select_date)
                        )
                    }
                },
                isError = data.errors.dateInputError != null,
                supportingText = {
                    if (data.errors.dateInputError != null) {
                        Text(text = stringResource(id = data.errors.dateInputError!!))
                    }
                }
            )
        }
        //DatePickerTextField()

        // ------------------- TIME INPUT SWITCH -----------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.set_a_specific_time))
            Switch(
                checked = data.showTimeInput,
                onCheckedChange = {
                    data.showTimeInput = !data.showTimeInput
                    actions.onRoutineDataChanged(data)
                }
            )
        }

        // ------------------- TIME INPUT -----------------------------
        if (data.showTimeInput) {
            TextField(
                label = { Text(stringResource(R.string.specific_time_with_example)) },
                modifier = Modifier
                    .fillMaxWidth(),
                value = data.timeInput,
                onValueChange = {
                    data.timeInput = it
                    actions.onRoutineDataChanged(data)
                },
                maxLines = 6,
                isError = data.errors.timeInputError != null,
                supportingText = {
                    if (data.errors.timeInputError != null) {
                        Text(text = stringResource(id = data.errors.timeInputError!!))
                    }
                }
            )
        }

        // ------------------- ACTIVE SWITCH -----------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(id = R.string.active).capitalize(Locale.current))
            Switch(
                checked = data.isActive,
                onCheckedChange = {
                    if (!data.isActive) data.dateInput =
                        Time.convertMillisToDateString(System.currentTimeMillis())
                    data.isActive = !data.isActive
                    actions.onRoutineDataChanged(data)
                }
            )
        }


        // ------------------- SAVE BUTTON -----------------------------
        Column(
            modifier = Modifier
                .padding(horizontal = HalfMargin())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = {
                    actions.saveRoutine()
                }) {
                Text(stringResource(id = R.string.save))
            }
        }

    }
}

enum class FrequencyPicker(val titleResource: Int) {
    MANUAL(R.string.custom),
    DAILY(R.string.daily),
    WEEKLY(R.string.weekly)
}