package com.mycompany.sistemacomida.dao;

import com.mycompany.sistemacomida.model.Usuario;
import com.mycompany.sistemacomida.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    /**
     * Busca un usuario en la base de datos por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return Un objeto Usuario si se encuentra, de lo contrario null.
     */
    public Usuario getUsuarioByNombreUsuario(String username) {
        Usuario usuario = null;
        String sql = "SELECT id_usuario, username, password_hash, rol FROM Usuarios WHERE username = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPassword_hash(rs.getString("password_hash"));
                    usuario.setRol(rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
            // En una aplicación real, aquí se debería usar un sistema de logging
        }
        return usuario;
    }

    /**
     * Actualiza el nombre de usuario de un usuario existente.
     * @param usuario El objeto Usuario con el ID y el nuevo username.
     * @return true si se actualiza correctamente, false en caso contrario.
     */
    public boolean updateUsuario(Usuario usuario) {
        String sql = "UPDATE Usuarios SET username = ? WHERE id_usuario = ?";
        boolean rowAffected = false;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getUsername());
            pstmt.setInt(2, usuario.getId_usuario());

            rowAffected = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar nombre de usuario: " + e.getMessage());
        }
        return rowAffected;
    }

    /**
     * Actualiza la contraseña hasheada de un usuario.
     * @param id_usuario El ID del usuario.
     * @param newHashedPassword La nueva contraseña hasheada.
     * @return true si se actualiza correctamente, false en caso contrario.
     */
    public boolean updatePassword(int id_usuario, String newHashedPassword) {
        String sql = "UPDATE Usuarios SET password_hash = ? WHERE id_usuario = ?";
        boolean rowAffected = false;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newHashedPassword);
            pstmt.setInt(2, id_usuario);

            rowAffected = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
        }
        return rowAffected;
    }
    
    /**
     * Crea un nuevo usuario en la base de datos con contraseña hasheada.
     * @param username El nombre de usuario.
     * @param password La contraseña en texto plano (será hasheada con BCrypt).
     * @param rol El rol del usuario (ej: "admin", "empleado").
     * @return true si se crea correctamente, false en caso contrario.
     */
    public boolean crearUsuario(String username, String password, String rol) {
        String sql = "INSERT INTO Usuarios (username, password_hash, rol) VALUES (?, ?, ?)";
        boolean success = false;
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Hashear la contraseña con BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, rol);
            
            success = pstmt.executeUpdate() > 0;
            
            if (success) {
                System.out.println("✓ Usuario '" + username + "' creado exitosamente con rol: " + rol);
            }
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
        return success;
    }
}