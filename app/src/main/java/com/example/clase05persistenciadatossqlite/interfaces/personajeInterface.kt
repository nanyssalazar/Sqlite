package com.example.clase05persistenciadatossqlite.interfaces

import com.example.clase05persistenciadatossqlite.modelos.Personaje

public interface personajeInterface {
    fun personajeEliminado()
    fun editarPersonaje(personaje: Personaje)

}