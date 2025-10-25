package com.mycompany.sistemacomida.dao;

import com.mycompany.sistemacomida.model.Producto;
import com.mycompany.sistemacomida.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    /**
     * Obtiene todos los productos de la base de datos.
     * @return Una lista de objetos Producto.
     */
    public List<Producto> getAllProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, tipo, variante, precio_unitario, stock, imagen_url, fecha_creacion, fecha_actualizacion FROM Productos ORDER BY nombre";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId_producto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setTipo(rs.getString("tipo"));
                producto.setVariante(rs.getString("variante"));
                producto.setPrecio_unitario(rs.getDouble("precio_unitario"));
                producto.setStock(rs.getInt("stock"));
                producto.setImagen_url(rs.getString("imagen_url"));
                producto.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                producto.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Obtiene un producto por su ID.
     * @param id_producto El ID del producto.
     * @return Un objeto Producto si se encuentra, de lo contrario null.
     */
    public Producto getProductoById(int id_producto) {
        Producto producto = null;
        String sql = "SELECT id_producto, nombre, tipo, variante, precio_unitario, stock, imagen_url, fecha_creacion, fecha_actualizacion FROM Productos WHERE id_producto = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_producto);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto();
                    producto.setId_producto(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setTipo(rs.getString("tipo"));
                    producto.setVariante(rs.getString("variante"));
                    producto.setPrecio_unitario(rs.getDouble("precio_unitario"));
                    producto.setStock(rs.getInt("stock"));
                    producto.setImagen_url(rs.getString("imagen_url"));
                    producto.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                    producto.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
        }
        return producto;
    }

    /**
     * Agrega un nuevo producto a la base de datos.
     * @param producto El objeto Producto a agregar.
     * @return true si se agrega correctamente, false en caso contrario.
     */
    public boolean addProducto(Producto producto) {
        String sql = "INSERT INTO Productos (nombre, tipo, variante, precio_unitario, stock, imagen_url) VALUES (?, ?, ?, ?, ?, ?)";
        boolean rowAffected = false;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getTipo());
            pstmt.setString(3, producto.getVariante());
            pstmt.setDouble(4, producto.getPrecio_unitario());
            pstmt.setInt(5, producto.getStock());
            pstmt.setString(6, producto.getImagen_url());

            rowAffected = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar producto: " + e.getMessage());
        }
        return rowAffected;
    }

    /**
     * Actualiza un producto existente en la base de datos.
     * @param producto El objeto Producto con los datos actualizados.
     * @return true si se actualiza correctamente, false en caso contrario.
     */
    public boolean updateProducto(Producto producto) {
        String sql = "UPDATE Productos SET nombre = ?, tipo = ?, variante = ?, precio_unitario = ?, stock = ?, imagen_url = ? WHERE id_producto = ?";
        boolean rowAffected = false;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getTipo());
            pstmt.setString(3, producto.getVariante());
            pstmt.setDouble(4, producto.getPrecio_unitario());
            pstmt.setInt(5, producto.getStock());
            pstmt.setString(6, producto.getImagen_url());
            pstmt.setInt(7, producto.getId_producto());

            rowAffected = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
        return rowAffected;
    }

    /**
     * Elimina un producto de la base de datos por su ID.
     * @param id_producto El ID del producto a eliminar.
     * @return true si se elimina correctamente, false en caso contrario.
     */
    public boolean deleteProducto(int id_producto) {
        String sql = "DELETE FROM Productos WHERE id_producto = ?";
        boolean rowAffected = false;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_producto);

            rowAffected = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
        return rowAffected;
    }

    /**
     * Actualiza el stock de un producto.
     * @param id_producto El ID del producto.
     * @param cantidad La cantidad a sumar o restar del stock actual.
     * @return true si se actualiza correctamente, false en caso contrario.
     */
    public boolean updateStock(int id_producto, int cantidad) {
        String sql = "UPDATE Productos SET stock = stock + ? WHERE id_producto = ?";
        boolean rowAffected = false;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, id_producto);

            rowAffected = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock del producto: " + e.getMessage());
        }
        return rowAffected;
    }
}
