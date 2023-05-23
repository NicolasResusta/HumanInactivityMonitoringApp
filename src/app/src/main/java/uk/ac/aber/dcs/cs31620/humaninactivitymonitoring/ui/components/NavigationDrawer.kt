package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.Screen

/**
 * This function covers the display and functionality of the navigation drawer used in the app.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param drawerState this state variable determines whether the drawer should be opened or closed.
 * @param closeDrawer This shows the action of closing the drawer after the item is selected.
 * @param content this is the content displayed on the navigation drawer shown as a composable
 * function.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    closeDrawer: () -> Unit = {},
    content: @Composable () -> Unit = {}
){
    val itemsInDrawer = listOf(
        Pair(
            Icons.Default.Settings,
            stringResource(id = R.string.time_and_battery_screen)
        ),
        Pair(
            Icons.Default.Settings,
            stringResource(id = R.string.contact_method_screen)
        ),
        /*Pair(
            Icons.Default.Notes,
            stringResource(id = R.string.testing_button)
        )*/
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val selectedItem = rememberSaveable { mutableStateOf(5) }
            /* This variable always has to be bigger than the list in order to avoid the UI selecting
             a predefined one which might make it less clear for the user.*/
            Text(
                //This is to have some top padding, as the one given in the item modifier wasn't great
                //cause I needed a general one, not for every item on the list, and spacers were too much
                "",
                fontSize = 2.sp,
                modifier = Modifier
                    .padding(top = 7.dp, bottom = 5.dp))

            itemsInDrawer.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = item.first,
                            contentDescription = item.second
                        )
                    },
                    label = { Text(item.second) },

                    selected = index == selectedItem.value,
                    onClick = {

                        selectedItem.value = index

                        when (index) {
                            0 -> {
                                closeDrawer()
                                navController.navigate(route = Screen.TimeAndBatterySettingsScreen.route)
                            }
                            1 -> {
                                closeDrawer()
                                navController.navigate(route = Screen.ContactMethodSettingScreen.route)
                            }
                            /*2 -> {
                                closeDrawer()
                                navController.navigate(route = Screen.TestingScreen.route)
                            }*/
                        }
                    },
                    modifier = Modifier
                        .padding(top = 2.dp, bottom = 2.dp, end = 10.dp),
                )
            }
        },
        content = content
    )
}