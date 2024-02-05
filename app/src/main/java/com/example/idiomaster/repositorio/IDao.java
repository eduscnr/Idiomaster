package com.example.idiomaster.repositorio;

import com.example.idiomaster.modelo.Usuario;

public interface IDao {
    public boolean registrarUsuario(String emailUsuario);
    public Usuario iniciarSesionUsuario(String emailUsuario);
    public Usuario cambiarIdioma(String emailUsuario, String idiomaCambiar);
}
