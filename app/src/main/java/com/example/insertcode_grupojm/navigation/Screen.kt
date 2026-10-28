package com.example.insertcode_grupojm.navigation

import com.example.insertcode_grupojm.model.Rol

// Rutas de la aplicación. Centralizarlas evita errores de escritura en los strings.
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object HomeAdmin : Screen("home_admin")
    data object HomeSupervisor : Screen("home_supervisor")
    data object HomeOperador : Screen("home_operador")

    // Pantallas de la Guía 11 (formulario de registro)
    data object Registro : Screen("registro")
    data object Resumen : Screen("resumen")

    // Guia 12 Camara y Galeria
    data object Perfil : Screen("perfil")

    // Guía 13: Módulo Integral Room
    data object RegistroIntegral : Screen("registro_integral")
    data object ResumenIntegral : Screen("resumen_integral")
    data object ListadoOffline : Screen("listado_offline")

    // Guia 14: API REST
    data object ApiPosts : Screen("api_posts")

    // Implementacion de Registro
    data object RegistroUsuario : Screen("registro_usuario")
}

// Devuelve la ruta del Home que corresponde a cada rol (login diferenciado por rol).
fun rutaHomePorRol(rol: Rol): String = when (rol) {
    Rol.ADMIN -> Screen.HomeAdmin.route
    Rol.SUPERVISOR -> Screen.HomeSupervisor.route
    Rol.OPERADOR -> Screen.HomeOperador.route
}
