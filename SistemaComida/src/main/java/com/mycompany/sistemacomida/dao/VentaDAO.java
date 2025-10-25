package com.mycompany.sistemacomida.dao;

import com.mycompany.sistemacomida.model.Venta;
import com.mycompany.sistemacomida.model.DetalleVenta;
import com.mycompany.sistemacomida.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import com.mycompany.sistemacomida.model.Producto;

public class VentaDAO {

    /**
     * Agrega una nueva venta a la base de datos y sus detalles.
     * @param venta El objeto Venta a agregar.
     * @return El ID de la venta generada, o -1 si falla.
     */
    public int addVenta(Venta venta) {
        String sqlVenta = "INSERT INTO Ventas (codigo_pedido, nombre_cliente, subtotal_general, igv, total, estado, metodo_pago, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO DetalleVentas (id_venta, id_producto, cantidad, precio_unitario_venta, subtotal_linea) VALUES (?, ?, ?, ?, ?)";
        int id_venta_generado = -1;

        Connection conn = null;
        PreparedStatement pstmtVenta = null;
        PreparedStatement pstmtDetalle = null;

        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // Insertar la venta
            pstmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            pstmtVenta.setString(1, venta.getCodigo_pedido());
            pstmtVenta.setString(2, venta.getNombre_cliente());
            pstmtVenta.setDouble(3, venta.getSubtotal_general());
            pstmtVenta.setDouble(4, venta.getIgv());
            pstmtVenta.setDouble(5, venta.getTotal());
            pstmtVenta.setString(6, venta.getEstado());
            pstmtVenta.setString(7, venta.getMetodo_pago());
            pstmtVenta.setInt(8, venta.getId_usuario());
            pstmtVenta.executeUpdate();

            ResultSet rs = pstmtVenta.getGeneratedKeys();
            if (rs.next()) {
                id_venta_generado = rs.getInt(1);
            }
            rs.close();

            // Insertar los detalles de la venta
            if (id_venta_generado != -1 && venta.getDetalles() != null) {
                pstmtDetalle = conn.prepareStatement(sqlDetalle);
                for (DetalleVenta detalle : venta.getDetalles()) {
                    pstmtDetalle.setInt(1, id_venta_generado);
                    pstmtDetalle.setInt(2, detalle.getId_producto());
                    pstmtDetalle.setInt(3, detalle.getCantidad());
                    pstmtDetalle.setDouble(4, detalle.getPrecio_unitario_venta());
                    pstmtDetalle.setDouble(5, detalle.getSubtotal_linea());
                    pstmtDetalle.addBatch(); // Agregar a un batch para inserción eficiente
                }
                pstmtDetalle.executeBatch();
            }

            conn.commit(); // Confirmar transacción
        } catch (SQLException e) {
            System.err.println("Error al agregar venta: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Deshacer transacción en caso de error
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
        } finally {
            ConexionDB.close(pstmtVenta);
            ConexionDB.close(pstmtDetalle);
            ConexionDB.close(conn);
        }
        return id_venta_generado;
    }

    /**
     * Obtiene una venta por su ID, incluyendo sus detalles.
     * @param id_venta El ID de la venta a buscar.
     * @return Un objeto Venta si se encuentra, de lo contrario null.
     */
    public Venta getVentaById(int id_venta) {
        Venta venta = null;
        String sqlVenta = "SELECT id_venta, codigo_pedido, nombre_cliente, fecha_venta, subtotal_general, igv, total, estado, metodo_pago, id_usuario FROM Ventas WHERE id_venta = ?";
        String sqlDetalles = "SELECT dv.id_detalle_venta, dv.id_venta, dv.id_producto, dv.cantidad, dv.precio_unitario_venta, dv.subtotal_linea, p.nombre, p.tipo, p.variante, p.precio_unitario, p.stock, p.imagen_url FROM DetalleVentas dv JOIN Productos p ON dv.id_producto = p.id_producto WHERE dv.id_venta = ?";

        Connection conn = null;
        PreparedStatement pstmtVenta = null;
        PreparedStatement pstmtDetalles = null;
        ResultSet rsVenta = null;
        ResultSet rsDetalles = null;

        try {
            conn = ConexionDB.getConnection();

            // Obtener datos de la venta
            pstmtVenta = conn.prepareStatement(sqlVenta);
            pstmtVenta.setInt(1, id_venta);
            rsVenta = pstmtVenta.executeQuery();

            if (rsVenta.next()) {
                venta = new Venta();
                venta.setId_venta(rsVenta.getInt("id_venta"));
                venta.setCodigo_pedido(rsVenta.getString("codigo_pedido"));
                venta.setNombre_cliente(rsVenta.getString("nombre_cliente"));
                venta.setFecha_venta(rsVenta.getTimestamp("fecha_venta"));
                venta.setSubtotal_general(rsVenta.getDouble("subtotal_general"));
                venta.setIgv(rsVenta.getDouble("igv"));
                venta.setTotal(rsVenta.getDouble("total"));
                venta.setEstado(rsVenta.getString("estado"));
                venta.setMetodo_pago(rsVenta.getString("metodo_pago"));
                venta.setId_usuario(rsVenta.getInt("id_usuario"));

                // Obtener detalles de la venta
                pstmtDetalles = conn.prepareStatement(sqlDetalles);
                pstmtDetalles.setInt(1, id_venta);
                rsDetalles = pstmtDetalles.executeQuery();

                List<DetalleVenta> detalles = new ArrayList<>();
                while (rsDetalles.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setId_detalle_venta(rsDetalles.getInt("id_detalle_venta"));
                    detalle.setId_venta(rsDetalles.getInt("id_venta"));
                    detalle.setId_producto(rsDetalles.getInt("id_producto"));
                    detalle.setCantidad(rsDetalles.getInt("cantidad"));
                    detalle.setPrecio_unitario_venta(rsDetalles.getDouble("precio_unitario_venta"));
                    detalle.setSubtotal_linea(rsDetalles.getDouble("subtotal_linea"));

                    // Cargar el objeto Producto asociado
                    Producto producto = new Producto();
                    producto.setId_producto(rsDetalles.getInt("id_producto"));
                    producto.setNombre(rsDetalles.getString("nombre"));
                    producto.setTipo(rsDetalles.getString("tipo"));
                    producto.setVariante(rsDetalles.getString("variante"));
                    producto.setPrecio_unitario(rsDetalles.getDouble("precio_unitario"));
                    producto.setStock(rsDetalles.getInt("stock"));
                    producto.setImagen_url(rsDetalles.getString("imagen_url"));
                    detalle.setProducto(producto);

                    detalles.add(detalle);
                }
                venta.setDetalles(detalles);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener venta por ID: " + e.getMessage());
        } finally {
            ConexionDB.close(rsDetalles);
            ConexionDB.close(pstmtDetalles);
            ConexionDB.close(rsVenta);
            ConexionDB.close(pstmtVenta);
            ConexionDB.close(conn);
        }
        return venta;
    }

    /**
     * Obtiene todas las ventas de la base de datos.
     * @return Una lista de objetos Venta.
     */
    public List<Venta> getAllVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT id_venta, codigo_pedido, nombre_cliente, fecha_venta, subtotal_general, igv, total, estado, metodo_pago, id_usuario FROM Ventas ORDER BY fecha_venta DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId_venta(rs.getInt("id_venta"));
                venta.setCodigo_pedido(rs.getString("codigo_pedido"));
                venta.setNombre_cliente(rs.getString("nombre_cliente"));
                venta.setFecha_venta(rs.getTimestamp("fecha_venta"));
                venta.setSubtotal_general(rs.getDouble("subtotal_general"));
                venta.setIgv(rs.getDouble("igv"));
                venta.setTotal(rs.getDouble("total"));
                venta.setEstado(rs.getString("estado"));
                venta.setMetodo_pago(rs.getString("metodo_pago"));
                venta.setId_usuario(rs.getInt("id_usuario"));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las ventas: " + e.getMessage());
        }
        return ventas;
    }

    /**
     * Elimina una venta de la base de datos por su ID.
     * @param id_venta El ID de la venta a eliminar.
     * @return true si se elimina correctamente, false en caso contrario.
     */
    public boolean deleteVenta(int id_venta) {
        String sql = "DELETE FROM Ventas WHERE id_venta = ?";
        boolean rowAffected = false;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_venta);

            rowAffected = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar venta: " + e.getMessage());
        }
        return rowAffected;
    }

    /**
     * Obtiene las ventas de una fecha específica.
     * @param fecha La fecha para filtrar las ventas.
     * @return Una lista de ventas de la fecha especificada.
     */
    public List<Venta> getVentasByDate(LocalDate fecha) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT id_venta, codigo_pedido, nombre_cliente, fecha_venta, subtotal_general, igv, total, estado, metodo_pago, id_usuario FROM Ventas WHERE DATE(fecha_venta) = ? ORDER BY fecha_venta DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Convertir LocalDate a java.sql.Date
            pstmt.setDate(1, java.sql.Date.valueOf(fecha));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Venta venta = new Venta();
                    venta.setId_venta(rs.getInt("id_venta"));
                    venta.setCodigo_pedido(rs.getString("codigo_pedido"));
                    venta.setNombre_cliente(rs.getString("nombre_cliente"));
                    venta.setFecha_venta(rs.getTimestamp("fecha_venta"));
                    venta.setSubtotal_general(rs.getDouble("subtotal_general"));
                    venta.setIgv(rs.getDouble("igv"));
                    venta.setTotal(rs.getDouble("total"));
                    venta.setEstado(rs.getString("estado"));
                    venta.setMetodo_pago(rs.getString("metodo_pago"));
                    venta.setId_usuario(rs.getInt("id_usuario"));
                    ventas.add(venta);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas por fecha: " + e.getMessage());
        }
        return ventas;
    }

    /**
     * Obtiene las ventas por estado.
     * @param estado El estado para filtrar las ventas.
     * @return Una lista de ventas con el estado especificado.
     */
    public List<Venta> getVentasByEstado(String estado) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT id_venta, codigo_pedido, nombre_cliente, fecha_venta, subtotal_general, igv, total, estado, metodo_pago, id_usuario FROM Ventas WHERE estado = ? ORDER BY fecha_venta DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estado);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Venta venta = new Venta();
                    venta.setId_venta(rs.getInt("id_venta"));
                    venta.setCodigo_pedido(rs.getString("codigo_pedido"));
                    venta.setNombre_cliente(rs.getString("nombre_cliente"));
                    venta.setFecha_venta(rs.getTimestamp("fecha_venta"));
                    venta.setSubtotal_general(rs.getDouble("subtotal_general"));
                    venta.setIgv(rs.getDouble("igv"));
                    venta.setTotal(rs.getDouble("total"));
                    venta.setEstado(rs.getString("estado"));
                    venta.setMetodo_pago(rs.getString("metodo_pago"));
                    venta.setId_usuario(rs.getInt("id_usuario"));
                    ventas.add(venta);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas por estado: " + e.getMessage());
        }
        return ventas;
    }
}
