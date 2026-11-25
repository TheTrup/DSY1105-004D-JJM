package com.example.insertcode_grupojm.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.insertcode_grupojm.repository.VentasRepository
import com.example.insertcode_grupojm.ui.components.BottomNavBar
import com.example.insertcode_grupojm.ui.screen.ListadoOfflineScreen
import com.example.insertcode_grupojm.ui.screen.LoginScreen
import com.example.insertcode_grupojm.ui.screen.PostScreen
import com.example.insertcode_grupojm.ui.screen.RegisterScreen
import com.example.insertcode_grupojm.ui.screen.RegistroIntegralScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.AlertasScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.CatalogoScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.HomeScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.PerfilScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.VentasScreen
import com.example.insertcode_grupojm.ui.utils.rememberTamanoVentana
import com.example.insertcode_grupojm.viewmodel.AlertasViewModel
import com.example.insertcode_grupojm.viewmodel.DashboardViewModel
import com.example.insertcode_grupojm.viewmodel.LoginViewModel
import com.example.insertcode_grupojm.viewmodel.PerfilViewModel
import com.example.insertcode_grupojm.viewmodel.RegisterViewModel
import com.example.insertcode_grupojm.viewmodel.UsuarioIntegralViewModel
import com.example.insertcode_grupojm.viewmodel.VentasViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    navigationViewModel: NavigationViewModel,
    loginViewModel: LoginViewModel,
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Escucha eventos de navegación emitidos desde los ViewModels
    LaunchedEffect(Unit) {
        navigationViewModel.eventos.collectLatest { evento ->
            when (evento) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(evento.route) {
                        evento.popUpTo?.let { target ->
                            popUpTo(target) { inclusive = evento.inclusive }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                is NavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    // Navegación entre pestañas manteniendo el estado y selección de BottomNavBar
    val navegarAPestana: (String) -> Unit = { rutaDestino ->
        navController.navigate(rutaDestino) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    // La barra superior e inferior solo se muestran dentro del Dashboard
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    val ventasRepository = remember { VentasRepository() }
    val dashboardViewModel: DashboardViewModel = viewModel {
        DashboardViewModel(ventasRepository = ventasRepository)
    }
    val ventasViewModel: VentasViewModel = viewModel {
        VentasViewModel(repository = ventasRepository)
    }
    val alertasViewModel: AlertasViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()

    val loginState = loginViewModel.estado.collectAsState().value
    val perfilState = perfilViewModel.uiState.collectAsState().value
    val rolActivo = loginState.usuario?.rol ?: perfilState.usuario.rol

    Scaffold(
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "InsertCode",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Ventas & Distribución Móvil",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        FilledTonalIconButton(onClick = onToggleDarkMode) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = if (isDarkMode) "Cambiar a modo claro" else "Activar modo oscuro EVA-01",
                                tint = if (isDarkMode) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- Flujo de Autenticación ---
            composable(Screen.Login.route) {
                LoginScreen(
                    estado = loginState,
                    tamano = rememberTamanoVentana(),
                    onEmailChange = loginViewModel::onEmailChange,
                    onPasswordChange = loginViewModel::onPasswordChange,
                    onSimularSinConexionChange = loginViewModel::onSimularSinConexionChange,
                    onLoginClick = {
                        loginViewModel.onLoginClick { _ ->
                            navigationViewModel.navigateTo(
                                route = Screen.Inicio.route,
                                popUpTo = Screen.Login.route,
                                inclusive = true
                            )
                        }
                    },
                    onOAuthSuccess = { emailOAuth ->
                        loginViewModel.iniciarSesionOAuth(emailOAuth)
                        navigationViewModel.navigateTo(
                            route = Screen.Inicio.route,
                            popUpTo = Screen.Login.route,
                            inclusive = true
                        )
                    },
                    onRegistroClick = {
                        navigationViewModel.navigateTo(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                val registerViewModel: RegisterViewModel = viewModel()
                RegisterScreen(
                    onNavigateBack = { navigationViewModel.navigateBack() },
                    onRegisterSuccess = {
                        val correoRegistrado = registerViewModel.uiState.value.email
                        val claveRegistrada = registerViewModel.uiState.value.password
                        loginViewModel.registrarUsuarioLocal(correoRegistrado, claveRegistrada)
                        navigationViewModel.navigateTo(
                            route = Screen.Inicio.route,
                            popUpTo = Screen.Login.route,
                            inclusive = true
                        )
                    },
                    onOAuthSuccess = { emailOAuth ->
                        loginViewModel.iniciarSesionOAuth(emailOAuth)
                        navigationViewModel.navigateTo(
                            route = Screen.Inicio.route,
                            popUpTo = Screen.Login.route,
                            inclusive = true
                        )
                    },
                    viewModel = registerViewModel
                )
            }

            // --- Flujo del Dashboard Principal ---
            composable(Screen.Inicio.route) {
                HomeScreen(
                    viewModel = dashboardViewModel,
                    ventasViewModel = ventasViewModel,
                    rolActual = rolActivo,
                    onIrAPedidos = { navegarAPestana(Screen.Ventas.route) },
                    onIrARutaGps = { navegarAPestana(Screen.Alertas.route) }
                )
            }

            composable(Screen.Ventas.route) {
                VentasScreen(
                    viewModel = ventasViewModel,
                    rolActual = rolActivo
                )
            }

            composable(Screen.Catalogo.route) {
                CatalogoScreen(
                    onCotizarClick = { navegarAPestana(Screen.Ventas.route) }
                )
            }

            composable(Screen.Alertas.route) {
                AlertasScreen(viewModel = alertasViewModel)
            }

            composable(Screen.Perfil.route) {
                PerfilScreen(
                    loginViewModel = loginViewModel,
                    perfilViewModel = perfilViewModel,
                    onNavigateToPosts = { navigationViewModel.navigateTo(Screen.Posts.route) },
                    onNavigateToIntegral = { navigationViewModel.navigateTo(Screen.RegistroIntegral.route) },
                    onCerrarSesion = {
                        loginViewModel.cerrarSesion()
                        navigationViewModel.navigateTo(
                            route = Screen.Login.route,
                            popUpTo = Screen.Inicio.route,
                            inclusive = true
                        )
                    }
                )
            }

            // --- Rutas de Módulos Técnicos (API REST y Room) ---
            composable(Screen.Posts.route) {
                PostScreen()
            }

            composable(Screen.RegistroIntegral.route) {
                val integralViewModel: UsuarioIntegralViewModel = viewModel()
                RegistroIntegralScreen(
                    viewModel = integralViewModel,
                    onNavigateResumen = {
                        navigationViewModel.navigateTo(Screen.ListadoOffline.route)
                    },
                    onNavigateListado = {
                        navigationViewModel.navigateTo(Screen.ListadoOffline.route)
                    }
                )
            }

            composable(Screen.ListadoOffline.route) {
                val integralViewModel: UsuarioIntegralViewModel = viewModel()
                ListadoOfflineScreen(
                    viewModel = integralViewModel,
                    onVolver = { navigationViewModel.navigateBack() }
                )
            }
        }
    }
}