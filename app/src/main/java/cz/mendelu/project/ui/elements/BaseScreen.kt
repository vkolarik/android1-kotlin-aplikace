package cz.mendelu.project.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.project.ui.theme.BasicMargin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String? = null,
    onBackClick: (() -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    loading: Boolean = false,
    placeholderScreenDefinition: PlaceholderScreenDefinition? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit, //drzet jako posledni parametr
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                if (topBarText != null) {
                    Text(
                        text = topBarText
                    )
                }

            }, navigationIcon = {
                if (onBackClick != null) {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            ), actions = actions
            )
        }, floatingActionButton = floatingActionButton
    ) {
        if (loading) {
            LoadingScreen()
        } else if (placeholderScreenDefinition != null) {
            PlaceholderScreen(definition = placeholderScreenDefinition)
        } else {
            content(it)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

data class PlaceholderScreenDefinition(
    val image: Int? = null, val text: String? = null
)

@Composable
fun PlaceholderScreen(definition: PlaceholderScreenDefinition) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(BasicMargin())
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (definition.image != null) {
                Image(
                    modifier = Modifier.size(300.dp),
                    painter = painterResource(id = definition.image),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(BasicMargin()))
            }
            if (definition.text != null) {
                Text(text = definition.text, textAlign = TextAlign.Center)
            }
        }
    }
}