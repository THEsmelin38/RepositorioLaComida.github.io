package com.mycompany.sistemacomida.model;

import java.sql.Timestamp;

public class Producto {
    private int id_producto;
    private String nombre;
    private String tipo;
    private String variante;
    private double precio_unitario;
    private int stock;
    private String imagen_url;
    private Timestamp fecha_creacion;
    private Timestamp fecha_actualizacion;

    public Producto() {
    }

    public Producto(int id_producto, String nombre, String tipo, String variante, double precio_unitario, int stock, String imagen_url, Timestamp fecha_creacion, Timestamp fecha_actualizacion) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.variante = variante;
        this.precio_unitario = precio_unitario;
        this.stock = stock;
        this.imagen_url = imagen_url;
        this.fecha_creacion = fecha_creacion;
        this.fecha_actualizacion = fecha_actualizacion;
    }
    
    // Constructor sin id_producto para la creación de nuevos productos
    public Producto(String nombre, String tipo, String variante, double precio_unitario, int stock, String imagen_url) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.variante = variante;
        this.precio_unitario = precio_unitario;
        this.stock = stock;
        this.imagen_url = imagen_url;
    }

    // Getters y Setters
    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getVariante() {
        return variante;
    }

    public void setVariante(String variante) {
        this.variante = variante;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Timestamp getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(Timestamp fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }
}
