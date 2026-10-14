# InsertCode_GrupoJM — Modo Guardián

Aplicación móvil Android (Kotlin + Jetpack Compose + MVVM) para la evaluación de
**Desarrollo de Aplicaciones Móviles (DSY1105)**, EA2: Login Funcional APP.

## Usuarios de prueba (ficticios)

| Correo                      | Contraseña | Rol         |
|-----------------------------|------------|-------------|
| admin@guardian.test         | 123456     | Admin       |
| supervisor@guardian.test    | 123456     | Supervisor  |
| operador@guardian.test      | 123456     | Operador    |

## Estructura

```
app/src/main/java/com/example/insertcode_grupojm/
├── MainActivity.kt
├── model/        Rol, Usuario, LoginUiState, LoginErrores, ResultadoLogin, UsuarioState
├── repository/   AuthRepository (datos de prueba), PreferenciasRepository (último correo)
├── viewmodel/    LoginViewModel, LoginViewModelFactory, UsuarioViewModel
├── navigation/   Screen, NavigationEvent, NavigationViewModel, AppNavigation
└── ui/
    ├── screen/       LoginScreen, HomeAdmin/Supervisor/Operador, Registro, Resumen
    ├── components/   HomeBase
    ├── utils/        WindowSizeUtils
    └── theme/
```

## Probar el manejo de errores

- Credenciales incorrectas: muestra "Correo o contraseña incorrectos".
- Error de conectividad: activar el interruptor "Simular sin conexión (prueba)" antes de ingresar.
