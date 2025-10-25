package com.mycompany.sistemacomida.model;

import java.sql.Timestamp;
import java.util.List;

public class Venta {
    private int id_venta;
    private String codigo_pedido;
    private String nombre_cliente;
    private Timestamp fecha_venta;
    private double subtotal_general;
    private double igv;
    private double total;
    private String estado;
    private String metodo_pago;
    private int id_usuario;
    private List<DetalleVenta> detalles;

    public Venta() {
    }

    public Venta(int id_venta, String codigo_pedido, String nombre_cliente, Timestamp fecha_venta, double subtotal_general, double igv, double total, String estado, String metodo_pago, int id_usuario) {
        this.id_venta = id_venta;
        this.codigo_pedido = codigo_pedido;
        this.nombre_cliente = nombre_cliente;
        this.fecha_venta = fecha_venta;
        this.subtotal_general = subtotal_general;
        this.igv = igv;
        this.total = total;
        this.estado = estado;
        this.metodo_pago = metodo_pago;
        this.id_usuario = id_usuario;
    }
    
    // Constructor para la creación de nuevas ventas (sin id_venta ni fecha_venta)
    public Venta(String codigo_pedido, String nombre_cliente, double subtotal_general, double igv, double total, String estado, String metodo_pago, int id_usuario) {
        this.codigo_pedido = codigo_pedido;
        this.nombre_cliente = nombre_cliente;
        this.subtotal_general = subtotal_general;
        this.igv = igv;
        this.total = total;
        this.estado = estado;
        this.metodo_pago = metodo_pago;
        this.id_usuario = id_usuario;
    }

    // Getters y Setters
    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public String getCodigo_pedido() {
        return codigo_pedido;
    }

    public void setCodigo_pedido(String codigo_pedido) {
        this.codigo_pedido = codigo_pedido;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public Timestamp getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(Timestamp fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public double getSubtotal_general() {
        return subtotal_general;
    }

    public void setSubtotal_general(double subtotal_general) {
        this.subtotal_general = subtotal_general;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}
