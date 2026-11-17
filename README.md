# InsertCode Móvil — Gestión de Proyectos y Consultoría TI

Aplicación móvil nativa desarrollada en **Kotlin** y **Jetpack Compose** bajo arquitectura **MVVM** para la gestión operativa de proyectos y servicios tecnológicos de **InsertCode** (*Desarrollo a la Medida*, *Dashboards Power BI*, *App Móvil* y *Outsourcing TI*).

---

## Arquitectura y Stack Tecnológico

* **Lenguaje y UI:** Kotlin + Jetpack Compose (Material Design 3) con diseño responsivo (`TamanoVentana`).
* **Arquitectura:** MVVM (Model - View - ViewModel) con flujos reactivos unidireccionales mediante `StateFlow` y `combine`.
* **Navegación:** Navigation Compose con control centralizado (`NavigationViewModel`) y barra inferior dinámica.
* **Autenticación:**
    * Validación local con roles (`ADMIN`, `SUPERVISOR`, `OPERADOR`).
    * Integración con **Google Credential Manager** (OAuth 2.0) + opción explícita de respaldo (*Modo Demo*) para entornos sin certificado SHA-1 registrado.
* **Persistencia Local:**
    * **Room Database** (operaciones CRUD offline con SQLite).
    * **Jetpack DataStore / SharedPreferences** para preferencias de sesión.
* **Consumo de API REST:** **Retrofit 2** + Corrutinas.
* **Recursos Nativos:** Captura de fotografía de perfil mediante Cámara (`FileProvider` y solicitud de permisos en tiempo de ejecución) y selección desde Galería.

---

## Credenciales de Prueba (Control de Acceso por Roles)

| Rol | Correo Electrónico | Contraseña | Permisos en el MVP |
| :--- | :--- | :--- | :--- |
| **Administrador** | `admin@guardian.test` | `123456` | Gestión de usuarios de prueba, roles y control total. |
| **Supervisor** | `supervisor@guardian.test` | `123456` | Consulta de indicadores KPI, estados y seguimiento operativo. |
| **Vendedor** | `vendedor@guardian.test` | `123456` | Registro de nuevos pedidos y gestión comercial inicial. |
| **Distribuidor / Repartidor** | `repartidor@guardian.test` | `123456` | Consulta de rutas asignadas y actualización de entregas. |

*(También es posible ingresar mediante el botón **Continuar con Google** o utilizando la acción **Usar Demo** en caso de ejecutar en un emulador sin cuenta vinculada).*

---

## Estructura del Proyecto

```text
com.example.insertcode_grupojm
├── data/           # Base de datos Room (DAO, Entities) y DataStore
├── model/          # Entidades de dominio (Pedido, Alerta, Usuario, Rol) y DTOs
├── navigation/     # Grafo de navegación (AppNavigation, Screen, NavigationViewModel)
├── network/        # Cliente Retrofit y definición de endpoints REST
├── repository/     # Fuentes de datos reactivas (VentasRepository, AlertasRepository)
├── ui/
│   ├── components/ # Componentes reutilizables (BottomNavBar, ImagenInteligente)
│   ├── screen/     # Pantallas de Auth (Login, Register) y módulos técnicos (Room/Retrofit)
│   ├── screens/    # Pantallas principales del Dashboard (Home, Ventas, Alertas, Perfil)
│   └── theme/      # Paleta de colores, tipografía y tema Material 3
└── viewmodel/      # Lógica de presentación y gestión de estado (StateFlow)