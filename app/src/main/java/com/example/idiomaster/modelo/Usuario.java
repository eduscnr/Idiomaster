package com.example.idiomaster.modelo;

public class Usuario {
    private String email;
    private int progresoMundo;
    private int progresoNivel;
    private String idioma;

    public Usuario(String email, int progresoMundo, int progresoNivel, String idioma) {
        this.email = email;
        this.progresoMundo = progresoMundo;
        this.progresoNivel = progresoNivel;
        this.idioma = idioma;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProgresoMundo() {
        return progresoMundo;
    }

    public void setProgresoMundo(int progresoMundo) {
        this.progresoMundo = progresoMundo;
    }

    public int getProgresoNivel() {
        return progresoNivel;
    }

    public void setProgresoNivel(int progresoNivel) {
        this.progresoNivel = progresoNivel;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", progresoMundo=" + progresoMundo +
                ", progresoNivel=" + progresoNivel +
                ", idioma='" + idioma + '\'' +
                '}';
    }
}
