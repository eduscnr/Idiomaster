package com.example.idiomaster.modelo;

public class Cuento {
    private String titulo,autor,narracion;

    public Cuento(String titulo, String autor, String narracion) {
        this.titulo = titulo;
        this.autor = autor;
        this.narracion = narracion;
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

    public String getNarracion() {
        return narracion;
    }

    public void setNarracion(String narracion) {
        this.narracion = narracion;
    }
}
