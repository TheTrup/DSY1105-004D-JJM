package com.example.insertcode_grupojm.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.insertcode_grupojm.R
import com.example.insertcode_grupojm.model.LoginErrores
import com.example.insertcode_grupojm.model.LoginUiState
import com.example.insertcode_grupojm.navigation.NavigationViewModel
import com.example.insertcode_grupojm.navigation.Screen
import com.example.insertcode_grupojm.navigation.rutaHomePorRol
import com.example.insertcode_grupojm.ui.theme.InsertCode_GrupoJMTheme
import com.example.insertcode_grupojm.ui.utils.TamanoVentana
import com.example.insertcode_grupojm.ui.utils.rememberTamanoVentana
import com.example.insertcode_grupojm.viewmodel.LoginViewModel

// ---------------------------------------------------------------------------
// Conexión con los ViewModels (stateful). La pantalla visual de abajo es
// "stateless": recibe estado y lambdas, por eso se puede previsualizar.
// ---------------------------------------------------------------------------
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
            // Si el login es correcto, se navega al Home del rol y se borra el login de la pila
            loginViewModel.onLoginClick { rol ->
                navigationViewModel.navigateTo(
                    route = rutaHomePorRol(rol),
                    popUpTo = Screen.Login.route,
                    inclusive = true
                )
            }
        },
        onRegistroClick = { navigationViewModel.navigateTo(Screen.Registro.route) }
    )
}

// ---------------------------------------------------------------------------
// Pantalla de login con dos layouts según el tamaño de ventana
// ---------------------------------------------------------------------------
@Composable
fun LoginScreen(
    estado: LoginUiState,
    tamano: TamanoVentana,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSimularSinConexionChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onRegistroClick: () -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (tamano == TamanoVentana.Compact) {
                // Compact (celular): todo en una sola Column vertical
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    EncabezadoLogin()
                    FormularioLogin(
                        estado, onEmailChange, onPasswordChange,
                        onSimularSinConexionChange, onLoginClick, onRegistroClick
                    )
                }
            } else {
                // Medium / Expanded: Card centrada con un Row (encabezado | formulario)
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
                                estado, onEmailChange, onPasswordChange,
                                onSimularSinConexionChange, onLoginClick, onRegistroClick
                            )
                        }
                    }
                }
            }
        }
    }
}

// Logo y títulos (se reutiliza en ambos layouts)
@Composable
private fun EncabezadoLogin() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_guardian),
            contentDescription = "Logo Modo Guardián",
            modifier = Modifier.size(96.dp)
        )
        Text(
            text = "Modo Guardián",
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

// Campos, mensajes de error y botones (se reutiliza en ambos layouts)
@Composable
private fun FormularioLogin(
    estado: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSimularSinConexionChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onRegistroClick: () -> Unit
) {
    // Solo es estado visual de la UI (mostrar/ocultar clave), por eso vive aquí y no en el ViewModel
    var mostrarClave by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Campo de correo con error y mensaje de apoyo
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

        // Campo de contraseña oculta, con botón para mostrarla u ocultarla
        OutlinedTextField(
            value = estado.password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            singleLine = true,
            enabled = !estado.isLoading,
            isError = estado.errores.password != null,
            supportingText = { estado.errores.password?.let { Text(it) } },
            visualTransformation = if (mostrarClave) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                TextButton(onClick = { mostrarClave = !mostrarClave }) {
                    Text(if (mostrarClave) "Ocultar" else "Mostrar")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Error general: credenciales incorrectas o falla de conectividad
        estado.errores.general?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Interruptor de prueba para demostrar el manejo de error de conectividad
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

        // Botón habilitado solo si isLoginEnabled; muestra progreso mientras carga
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

        TextButton(
            onClick = onRegistroClick,
            enabled = !estado.isLoading,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}

// ---------------------------------------------------------------------------
// Previews: uno por cada clase de tamaño de ventana
// ---------------------------------------------------------------------------
@Preview(name = "Compact (celular)", widthDp = 360, heightDp = 780, showBackground = true)
@Composable
private fun LoginCompactPreview() {
    InsertCode_GrupoJMTheme(dynamicColor = false) {
        LoginScreen(LoginUiState(), TamanoVentana.Compact, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Medium (tablet pequeña)", widthDp = 700, heightDp = 500, showBackground = true)
@Composable
private fun LoginMediumPreview() {
    InsertCode_GrupoJMTheme(dynamicColor = false) {
        LoginScreen(LoginUiState(), TamanoVentana.Medium, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Expanded (tablet grande)", widthDp = 1000, heightDp = 640, showBackground = true)
@Composable
private fun LoginExpandedPreview() {
    InsertCode_GrupoJMTheme(dynamicColor = false) {
        LoginScreen(LoginUiState(), TamanoVentana.Expanded, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Compact con error", widthDp = 360, heightDp = 780, showBackground = true)
@Composable
private fun LoginErrorPreview() {
    InsertCode_GrupoJMTheme(dynamicColor = false) {
        LoginScreen(
            estado = LoginUiState(
                email = "correo-invalido",
                password = "123",
                errores = LoginErrores(
                    email = "Correo inválido",
                    password = "Mínimo 6 caracteres",
                    general = "Sin conexión. Revisa tu red e inténtalo de nuevo"
                )
            ),
            tamano = TamanoVentana.Compact,
            onEmailChange = {}, onPasswordChange = {}, onSimularSinConexionChange = {},
            onLoginClick = {}, onRegistroClick = {}
        )
    }
}
