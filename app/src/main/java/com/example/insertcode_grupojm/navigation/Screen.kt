package com.example.insertcode_grupojm.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.insertcode_grupojm.model.Rol

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Inicio : Screen("inicio", "Dashboard", Icons.Default.Home)
    object Ventas : Screen("ventas", "Pedidos", Icons.Default.ShoppingCart)
    object Alertas : Screen("alertas", "Ruta GPS", Icons.Default.Notifications)
    object Perfil : Screen("perfil", "Perfil", Icons.Default.Person)

    // Alias para compatibilidad con código que invoque Screen.Registro
    val Registro: Screen get() = Register

    // Rutas adicionales de autenticación
    object Login : Screen("login", "Iniciar Sesión")
    object Register : Screen("register", "Registro")

    // Rutas de módulos técnicos (API REST y Persistencia Local)
    object Posts : Screen("posts", "API REST Posts")
    object RegistroIntegral : Screen("registro_integral", "Módulo Integral")
    object ListadoOffline : Screen("listado_offline", "Usuarios Offline")
}

val bottomNavItems = listOf(
    Screen.Inicio,
    Screen.Ventas,
    Screen.Alertas,
    Screen.Perfil
)

// Función de mapeo de rol hacia la pantalla inicial del Dashboard
fun rutaHomePorRol(rol: Rol?): String {
    return Screen.Inicio.route
}