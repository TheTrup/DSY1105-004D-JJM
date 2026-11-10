package com.example.insertcode_grupojm.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.insertcode_grupojm.R
import com.example.insertcode_grupojm.model.LoginErrores
import com.example.insertcode_grupojm.model.LoginUiState
import com.example.insertcode_grupojm.navigation.NavigationViewModel
import com.example.insertcode_grupojm.navigation.Screen
import com.example.insertcode_grupojm.navigation.rutaHomePorRol
import com.example.insertcode_grupojm.ui.utils.TamanoVentana
import com.example.insertcode_grupojm.ui.utils.rememberTamanoVentana
import com.example.insertcode_grupojm.ui.utils.GoogleAuthHelper
import com.example.insertcode_grupojm.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    loginViewModel: LoginViewModel,
    navigationViewModel: NavigationViewModel
) {
    val estado by loginViewModel.estado.collectAsState()

    LoginScreen(
        estado = estado,
        tamano = rememberTamanoVentana(),
        onEmailChange = loginViewModel::onEmailChange,
        onPasswordChange = loginViewModel::onPasswordChange,
        onSimularSinConexionChange = loginViewModel::onSimularSinConexionChange,
        onLoginClick = {
            loginViewModel.onLoginClick { rol ->
                navigationViewModel.navigateTo(
                    route = rutaHomePorRol(rol),
                    popUpTo = Screen.Login.route,
                    inclusive = true
                )
            }
        },
        onOAuthSuccess = {
            navigationViewModel.navigateTo(
                route = Screen.Inicio.route,
                popUpTo = Screen.Login.route,
                inclusive = true
            )
        },
        onRegistroClick = { navigationViewModel.navigateTo(Screen.Register.route) }
    )
}

@Composable
fun LoginScreen(
    estado: LoginUiState,
    tamano: TamanoVentana,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSimularSinConexionChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onOAuthSuccess: () -> Unit,
    onRegistroClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (tamano == TamanoVentana.Compact) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    EncabezadoLogin()
                    FormularioLogin(
                        estado = estado,
                        onEmailChange = onEmailChange,
                        onPasswordChange = onPasswordChange,
                        onSimularSinConexionChange = onSimularSinConexionChange,
                        onLoginClick = onLoginClick,
                        onOAuthSuccess = onOAuthSuccess,
                        onRegistroClick = onRegistroClick,
                        snackbarHostState = snackbarHostState
                    )
                }
            } else {
                Card(modifier = Modifier.widthIn(max = 880.dp)) {
                    Row(
                        modifier = Modifier.padding(32.dp),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            EncabezadoLogin()
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            FormularioLogin(
                                estado = estado,
                                onEmailChange = onEmailChange,
                                onPasswordChange = onPasswordChange,
                                onSimularSinConexionChange = onSimularSinConexionChange,
                                onLoginClick = onLoginClick,
                                onOAuthSuccess = onOAuthSuccess,
                                onRegistroClick = onRegistroClick,
                                snackbarHostState = snackbarHostState
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EncabezadoLogin() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo InsertCode",
            modifier = Modifier.size(96.dp)
        )
        Text(
            text = "InsertCode Móvil",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FormularioLogin(
    estado: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSimularSinConexionChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onOAuthSuccess: () -> Unit,
    onRegistroClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var mostrarClave by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthHelper = remember { GoogleAuthHelper(context) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = estado.email,
            onValueChange = onEmailChange,
            label = { Text("Correo electrónico") },
            singleLine = true,
            enabled = !estado.isLoading,
            isError = estado.errores.email != null,
            supportingText = { estado.errores.email?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            singleLine = true,
            enabled = !estado.isLoading,
            isError = estado.errores.password != null,
            supportingText = { estado.errores.password?.let { Text(it) } },
            visualTransformation = if (mostrarClave) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                TextButton(onClick = { mostrarClave = !mostrarClave }) {
                    Text(if (mostrarClave) "Ocultar" else "Mostrar")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        estado.errores.general?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Simular sin conexión (prueba)",
                style = MaterialTheme.typography.bodySmall
            )
            Switch(
                checked = estado.simularSinConexion,
                onCheckedChange = onSimularSinConexionChange,
                enabled = !estado.isLoading
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onLoginClick,
            enabled = estado.isLoginEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (estado.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("Ingresando...")
            } else {
                Text("Iniciar sesión")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Divisor OAuth
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "O inicia sesión con",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Google
        OutlinedButton(
            onClick = {
                coroutineScope.launch {
                    googleAuthHelper.signInWithGoogle(
                        serverClientId = "301636417833-nn5kif3k8903e0upd8anp5d682j3k898.apps.googleusercontent.com",
                        onSuccess = { _, _ -> onOAuthSuccess() },
                        onError = { err ->
                            coroutineScope.launch { snackbarHostState.showSnackbar(err) }
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Text(
                text = "G  Continuar con Google",
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón Apple
        Button(
            onClick = { onOAuthSuccess() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "  Continuar con Apple",
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onRegistroClick,
            enabled = !estado.isLoading,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}