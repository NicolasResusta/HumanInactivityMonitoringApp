package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R
import kotlinx.coroutines.CoroutineScope
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.Screen

/**
 * This function represents the scaffolding arrangement for the contact method settings screen.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param coroutineScope this is used to launch and end coroutines within the scaffold.
 * @param pageContent this is a composable lambda function which holds the content of the page.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTimeAndBatterySettingsScreen(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
    ){
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.time_and_battery_screen),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 30.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(route = Screen.HomeScreen.route) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.arrow_icon)
                        )
                    }
                }
            )
        },
        content = { innerPadding -> pageContent(innerPadding)}
    )
}