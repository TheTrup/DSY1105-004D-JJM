package com.example.insertcode_grupojm.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    data object Inicio : Screen("inicio", "Inicio", Icons.Default.Home)
    data object Ventas : Screen("ventas", "Ventas", Icons.Default.ShoppingCart)
    data object Alertas : Screen("alertas", "Alertas", Icons.Default.Notifications)
    data object Perfil : Screen("perfil", "Perfil", Icons.Default.Person)
    data object PostsApi : Screen("posts_api", "API Posts")
}

val bottomNavItems = listOf(
    Screen.Inicio,
    Screen.Ventas,
    Screen.Alertas,
    Screen.Perfil
)