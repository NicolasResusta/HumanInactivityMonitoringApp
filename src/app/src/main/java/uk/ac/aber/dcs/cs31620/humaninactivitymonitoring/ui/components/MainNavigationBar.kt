package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.navigation.screens

/**
 * This function shows how the navigation bar is displayed and how it works within the app.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 */
@Composable
fun MainNavigationBar(
    navController: NavController,
){
    val icons = mapOf (
        Screen.HomeScreen to IconGroup(
            filledIcon = Icons.Filled.Contacts,
            outlineIcon = Icons.Outlined.Contacts,
            label = stringResource(id = R.string.contacts_list)
        ),
        Screen.CurrentSettingsScreen to IconGroup(
            filledIcon = Icons.Filled.Tune,
            outlineIcon = Icons.Outlined.Tune,
            label = stringResource(id = R.string.current_settings)
        )
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any{ it.route == screen.route } == true
            val labelText = icons[screen]!!.label
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = (
                                if (isSelected)
                                    icons[screen]!!.filledIcon
                                else
                                    icons[screen]!!.outlineIcon),
                        contentDescription = labelText
                    )
                },
                label = { Text(text = labelText) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
