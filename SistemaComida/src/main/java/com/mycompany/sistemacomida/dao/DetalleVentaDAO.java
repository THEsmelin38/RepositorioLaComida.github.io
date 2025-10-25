package com.mycompany.sistemacomida.dao;

import com.mycompany.sistemacomida.model.DetalleVenta;
import com.mycompany.sistemacomida.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {

    /**
     * Agrega un nuevo detalle de venta a la base de datos.
     * Este método es principalmente llamado por VentaDAO para transacciones.
     * @param detalle El objeto DetalleVenta a agregar.
     * @return true si se agrega correctamente, false en caso contrario.
     */
    public boolean addDetalleVenta(DetalleVenta detalle) {
        String sql = "INSERT INTO DetalleVentas (id_venta, id_producto, cantidad, precio_unitario_venta, subtotal_linea) VALUES (?, ?, ?, ?, ?)";
        boolean rowAffected = false;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, detalle.getId_venta());
            pstmt.setInt(2, detalle.getId_producto());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getPrecio_unitario_venta());
            pstmt.setDouble(5, detalle.getSubtotal_linea());

            rowAffected = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar detalle de venta: " + e.getMessage());
        }
        return rowAffected;
    }

    /**
     * Obtiene todos los detalles de venta asociados a un ID de venta.
     * @param id_venta El ID de la venta.
     * @return Una lista de objetos DetalleVenta.
     */
    public List<DetalleVenta> getDetallesByVentaId(int id_venta) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT id_detalle_venta, id_venta, id_producto, cantidad, precio_unitario_venta, subtotal_linea FROM DetalleVentas WHERE id_venta = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_venta);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setId_detalle_venta(rs.getInt("id_detalle_venta"));
                    detalle.setId_venta(rs.getInt("id_venta"));
                    detalle.setId_producto(rs.getInt("id_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecio_unitario_venta(rs.getDouble("precio_unitario_venta"));
                    detalle.setSubtotal_linea(rs.getDouble("subtotal_linea"));
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de venta por ID de venta: " + e.getMessage());
        }
        return detalles;
    }
}
