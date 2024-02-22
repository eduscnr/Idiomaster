package com.example.idiomaster.modelo;

import java.util.List;

public class UsuarioFireBase {
    private String email;
    private List<ProgresoFireBase> progreso;

    public UsuarioFireBase(String email, List<ProgresoFireBase> progreso) {
        this.email = email;
        this.progreso = progreso;
    }

    public UsuarioFireBase() {
    }

    public UsuarioFireBase(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProgresoFireBase> getProgreso() {
        return progreso;
    }

    public void setProgreso(List<ProgresoFireBase> progreso) {
        this.progreso = progreso;
    }

    @Override
    public String toString() {
        return "UsuarioFireBase{" +
                "email='" + email + '\'' +
                ", progreso=" + progreso +
                '}';
    }
}

