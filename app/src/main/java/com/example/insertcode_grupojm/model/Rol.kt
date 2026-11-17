package com.example.insertcode_grupojm.model

// "etiqueta" es el texto que se muestra al usuario en pantalla.
enum class Rol(val etiqueta: String, val descripcionPermisos: String) {
    ADMIN(
        etiqueta = "Administrador",
        descripcionPermisos = "Gestión de usuarios de prueba, roles y permisos del sistema."
    ),
    SUPERVISOR(
        etiqueta = "Supervisor",
        descripcionPermisos = "Supervisión de indicadores KPI, estados de pedidos y seguimiento logístico."
    ),
    VENDEDOR(
        etiqueta = "Vendedor",
        descripcionPermisos = "Consulta comercial y registro de nuevos pedidos autorizados."
    ),
    REPARTIDOR(
        etiqueta = "Distribuidor / Repartidor",
        descripcionPermisos = "Consulta de rutas asignadas y actualización de estados de entrega."
    )
}
