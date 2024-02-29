package com.example.idiomaster.modelo;

public class Cuento {
    private String titulo,autor = "", cuento;

    public Cuento(String titulo, String autor, String cuento) {
        this.titulo = titulo;
        this.autor = autor;
        this.cuento = cuento;
    }
    public Cuento(){

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCuento() {
        return cuento;
    }

    public void setCuento(String cuento) {
        this.cuento = cuento;
    }
}
