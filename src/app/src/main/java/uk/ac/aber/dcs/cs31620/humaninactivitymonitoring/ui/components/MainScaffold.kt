package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * This function represents the scaffolding arrangement for the home and vocab list screen.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param coroutineScope this is used to launch and end coroutines within the scaffold.
 * @param pageContent this is a composable lambda function which holds the content of the page.
 * @param floatingActionButton is the representation of the Floating Action Button in the scaffold.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    floatingActionButton: @Composable () -> Unit = {},
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {},
    coroutineScope: CoroutineScope
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()


    NavigationDrawer(
        navController = navController,
        drawerState = drawerState,
        closeDrawer = {
            coroutineScope.launch {
                drawerState.close()
            }
        }
    ) {
        Scaffold(
            topBar = {
                MainTopAppBar(
                    onClick = {
                        coroutineScope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else {
                                drawerState.open()
                            }
                        }
                    }
                )
            },
            floatingActionButton = floatingActionButton,
            bottomBar = { MainNavigationBar(navController = navController) },
            content = { innerPadding -> pageContent(innerPadding) }
        )
    }
}