package com.example.idiomaster.modelo;

public class ProgresoFireBase {
    private String idioma;
    private int progresoMundo;
    private int progresoNivel;

    public ProgresoFireBase(String idioma, int progresoMundo, int progresoNivel) {
        this.idioma = idioma;
        this.progresoMundo = progresoMundo;
        this.progresoNivel = progresoNivel;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
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

    @Override
    public String toString() {
        return "ProgresoFireBase{" +
                "idioma='" + idioma + '\'' +
                ", progresoMundo=" + progresoMundo +
                ", progresoNivel=" + progresoNivel +
                '}';
    }
}

