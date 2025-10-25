package com.mycompany.sistemacomida.model;

public class Usuario {
    private int id_usuario;
    private String username;
    private String password_hash;
    private String rol;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con todos los campos
    public Usuario(int id_usuario, String username, String password_hash, String rol) {
        this.id_usuario = id_usuario;
        this.username = username;
        this.password_hash = password_hash;
        this.rol = rol;
    }

    // Getters y Setters
    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}