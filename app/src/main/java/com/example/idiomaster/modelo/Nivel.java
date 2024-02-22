package com.example.idiomaster.modelo;

import java.util.List;

public class Nivel {
    private int id;
    private List<String> palabras;
    private String instrucciones;
    private String nombre;

    public Nivel() {
    }

    public Nivel(int id, List<String> palabras, String instrucciones, String nombre) {
        this.id = id;
        this.palabras = palabras;
        this.instrucciones = instrucciones;
        this.nombre = nombre;
    }

    public List<String> getPalabras() {
        return palabras;
    }

    public void setPalabras(List<String> palabras) {
        this.palabras = palabras;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Nivel{" +
                "id=" + id +
                ", palabras=" + palabras +
                ", instrucciones='" + instrucciones + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
