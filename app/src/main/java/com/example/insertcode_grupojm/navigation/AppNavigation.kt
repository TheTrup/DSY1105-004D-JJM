package com.example.insertcode_grupojm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.insertcode_grupojm.ui.screen.HomeAdminScreen
import com.example.insertcode_grupojm.ui.screen.HomeOperadorScreen
import com.example.insertcode_grupojm.ui.screen.HomeSupervisorScreen
import com.example.insertcode_grupojm.ui.screen.ListadoOfflineScreen
import com.example.insertcode_grupojm.ui.screen.LoginRoute
import com.example.insertcode_grupojm.ui.screen.PerfilScreen
import com.example.insertcode_grupojm.ui.screen.RegistroIntegralScreen
import com.example.insertcode_grupojm.ui.screen.RegistroScreen
import com.example.insertcode_grupojm.ui.screen.ResumenIntegralScreen
import com.example.insertcode_grupojm.ui.screen.ResumenScreen
import com.example.insertcode_grupojm.ui.screen.PostScreen
import com.example.insertcode_grupojm.viewmodel.LoginViewModel
import com.example.insertcode_grupojm.viewmodel.UsuarioIntegralViewModel
import com.example.insertcode_grupojm.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    navigationViewModel: NavigationViewModel = viewModel()
) {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    // ViewModel compartido para Guía 14
    val usuarioIntegralViewModel: UsuarioIntegralViewModel = viewModel()

    LaunchedEffect(Unit) {
        navigationViewModel.eventos.collect { evento ->
            when (evento) {
                is NavigationEvent.NavigateTo -> navController.navigate(evento.route) {
                    evento.popUpTo?.let { destino ->
                        popUpTo(destino) { inclusive = evento.inclusive }
                    }
                    launchSingleTop = true
                }
                NavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    val cerrarSesion: (String) -> Unit = { rutaHomeActual ->
        loginViewModel.cerrarSesion()
        navigationViewModel.navigateTo(
            route = Screen.Login.route,
            popUpTo = rutaHomeActual,
            inclusive = true
        )
    }

    NavHost(
        navController = navController,
        startDestination = Screen.ApiPosts.route // Inicia directo para evaluar
    ) {
        composable(Screen.Login.route) {
            LoginRoute(loginViewModel = loginViewModel, navigationViewModel = navigationViewModel)
        }
        composable(Screen.HomeAdmin.route) {
            HomeAdminScreen(loginViewModel = loginViewModel, onCerrarSesion = { cerrarSesion(Screen.HomeAdmin.route) })
        }
        composable(Screen.HomeSupervisor.route) {
            HomeSupervisorScreen(loginViewModel = loginViewModel, onCerrarSesion = { cerrarSesion(Screen.HomeSupervisor.route) })
        }
        composable(Screen.HomeOperador.route) {
            HomeOperadorScreen(loginViewModel = loginViewModel, onCerrarSesion = { cerrarSesion(Screen.HomeOperador.route) })
        }
        composable(Screen.Registro.route) {
            RegistroScreen(viewModel = usuarioViewModel, onRegistroExitoso = { navigationViewModel.navigateTo(Screen.Resumen.route) }, onVolver = { navigationViewModel.navigateBack() })
        }
        composable(Screen.Resumen.route) {
            ResumenScreen(viewModel = usuarioViewModel, onVolverAlLogin = { navigationViewModel.navigateTo(Screen.Login.route, Screen.Login.route) })
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(onVolver = { navigationViewModel.navigateBack() })
        }
        composable(Screen.ApiPosts.route) {
            PostScreen()
        }

        // Módulo Integral Room (Guía 14)
        composable(Screen.RegistroIntegral.route) {
            RegistroIntegralScreen(
                viewModel = usuarioIntegralViewModel,
                onNavigateResumen = { navController.navigate(Screen.ResumenIntegral.route) },
                onNavigateListado = { navController.navigate(Screen.ListadoOffline.route) }
            )
        }
        composable(Screen.ResumenIntegral.route) {
            ResumenIntegralScreen(
                viewModel = usuarioIntegralViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Screen.ListadoOffline.route) {
            ListadoOfflineScreen(
                viewModel = usuarioIntegralViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}