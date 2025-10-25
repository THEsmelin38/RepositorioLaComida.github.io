package com.mycompany.sistemacomida.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionDB {
    // Configuración de conexión
    // NOTA: Si usas XAMPP, verifica el puerto en XAMPP Control Panel > Config > MySQL
    // Puerto por defecto de XAMPP: 3306 (puede ser 3307 si hay conflictos)
    private static final String URL = "jdbc:mysql://localhost:3306/bd_comida?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = ""; // XAMPP por defecto no tiene contraseña

    // Obtener conexión
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("Intentando conectar a la base de datos...");
            System.out.println("URL: " + URL);
            System.out.println("Usuario: " + USUARIO);
            Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("✓ Conexión exitosa a la base de datos");
            return conn;
        } catch (SQLException e) {
            System.err.println("✗ ERROR DE CONEXIÓN A LA BASE DE DATOS:");
            System.err.println("URL: " + URL);
            System.err.println("Error: " + e.getMessage());
            System.err.println("Código de error SQL: " + e.getErrorCode());
            System.err.println("\nPosibles causas:");
            System.err.println("1. MySQL no está corriendo en esta máquina");
            System.err.println("2. La base de datos 'bd_comida' no existe");
            System.err.println("3. Las credenciales son incorrectas");
            System.err.println("4. El puerto 3306 está bloqueado o en uso");
            throw e;
        }
    }

    // Cerrar conexión
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    // Cerrar PreparedStatement
    public static void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
    }

    // Cerrar ResultSet
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }
    }
}
