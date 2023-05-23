package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * This class represents the features of the icons shown within the navigation bar.
 *
 * @property filledIcon represents the state of the icon when it is filled
 * @property outlineIcon represents the state of the icon when it isn't filled
 * @property label represents the label of the icon
 */
data class IconGroup(
    val filledIcon: ImageVector,
    val outlineIcon: ImageVector,
    val label: String
)
