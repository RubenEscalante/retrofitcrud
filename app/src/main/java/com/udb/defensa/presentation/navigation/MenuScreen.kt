package com.udb.defensa.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MenuScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Alumn : MenuScreen(
        route = Screen.Alumn.route,
        title = "CRUD Alumnos",
        icon = Icons.Default.Person
    )

    object Professor : MenuScreen(
        route = Screen.Professor.route,
        title = "CRUD Profesores",
        icon = Icons.Default.Person2
    )
}