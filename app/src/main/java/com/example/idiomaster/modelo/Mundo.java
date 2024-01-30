package com.example.idiomaster.modelo;

import java.util.List;

public class Mundo {
    private String nombre;
    private List<Nivel> niveles;

    public Mundo() {
    }

    public Mundo(String nombre, List<Nivel> niveles) {
        this.nombre = nombre;
        this.niveles = niveles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    @Override
    public String toString() {
        return "Mundo{" +
                "nombre='" + nombre + '\'' +
                ", niveles=" + niveles +
                '}';
    }
}
