package com.example.gamezone
class Puesto(val numero: Int) {
    var estado: EstadoPuesto = EstadoPuesto.Libre
        private set

    fun asignarEstado(nuevoEstado: EstadoPuesto) {
        this.estado = nuevoEstado
    }

    fun estaDisponible(): Boolean {
        return estado is EstadoPuesto.Libre
    }
}