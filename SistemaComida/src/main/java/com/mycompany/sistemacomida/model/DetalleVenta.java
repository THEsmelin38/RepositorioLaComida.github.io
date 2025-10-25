package com.mycompany.sistemacomida.model;

public class DetalleVenta {
    private int id_detalle_venta;
    private int id_venta;
    private int id_producto;
    private int cantidad;
    private double precio_unitario_venta;
    private double subtotal_linea;
    
    // Opcional: Para mostrar en JSP, podemos incluir el objeto Producto completo
    private Producto producto;

    public DetalleVenta() {
    }

    public DetalleVenta(int id_detalle_venta, int id_venta, int id_producto, int cantidad, double precio_unitario_venta, double subtotal_linea) {
        this.id_detalle_venta = id_detalle_venta;
        this.id_venta = id_venta;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_unitario_venta = precio_unitario_venta;
        this.subtotal_linea = subtotal_linea;
    }
    
    // Constructor para la creación de nuevos detalles de venta
    public DetalleVenta(int id_venta, int id_producto, int cantidad, double precio_unitario_venta, double subtotal_linea) {
        this.id_venta = id_venta;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_unitario_venta = precio_unitario_venta;
        this.subtotal_linea = subtotal_linea;
    }

    // Getters y Setters
    public int getId_detalle_venta() {
        return id_detalle_venta;
    }

    public void setId_detalle_venta(int id_detalle_venta) {
        this.id_detalle_venta = id_detalle_venta;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_unitario_venta() {
        return precio_unitario_venta;
    }

    public void setPrecio_unitario_venta(double precio_unitario_venta) {
        this.precio_unitario_venta = precio_unitario_venta;
    }

    public double getSubtotal_linea() {
        return subtotal_linea;
    }

    public void setSubtotal_linea(double subtotal_linea) {
        this.subtotal_linea = subtotal_linea;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
