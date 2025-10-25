package com.mycompany.sistemacomida.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase de utilidad para generar hashes BCrypt.
 * Ejecuta este programa para generar hashes que puedes usar en tu base de datos.
 * 
 * IMPORTANTE: Esta clase es solo para desarrollo/testing.
 */
public class GenerarHashesBCrypt {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("GENERADOR DE HASHES BCRYPT");
        System.out.println("=================================================\n");
        
        // Array de contraseñas comunes para generar hashes
        String[][] usuarios = {
            {"admin", "admin123", "admin"},
            {"empleado", "empleado123", "empleado"},
            {"test", "test123", "empleado"},
            {"demo", "demo123", "empleado"}
        };
        
        System.out.println("Generando hashes BCrypt para usuarios de prueba...\n");
        
        for (String[] usuario : usuarios) {
            String username = usuario[0];
            String password = usuario[1];
            String rol = usuario[2];
            
            // Generar hash BCrypt
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            
            System.out.println("Usuario: " + username);
            System.out.println("Contraseña: " + password);
            System.out.println("Rol: " + rol);
            System.out.println("Hash BCrypt: " + hash);
            System.out.println("\nSQL INSERT:");
            System.out.println("INSERT INTO Usuarios (username, password_hash, rol) VALUES");
            System.out.println("('" + username + "', '" + hash + "', '" + rol + "');");
            System.out.println("\nSQL UPDATE:");
            System.out.println("UPDATE Usuarios SET password_hash = '" + hash + "' WHERE username = '" + username + "';");
            System.out.println("\n" + repetirCaracter('=', 80) + "\n");
        }
        
        System.out.println("✓ Hashes generados exitosamente!");
        System.out.println("\nNOTAS:");
        System.out.println("1. Cada vez que ejecutes este programa, se generarán hashes DIFERENTES");
        System.out.println("   (aunque la contraseña sea la misma). Esto es normal con BCrypt.");
        System.out.println("2. Todos estos hashes son válidos para las contraseñas indicadas.");
        System.out.println("3. Copia el SQL INSERT o UPDATE y ejecútalo en tu base de datos.");
        System.out.println("4. Verifica que el hash tenga 60 caracteres y empiece con $2a$");
        System.out.println("\n¿Cómo verificar si un hash es válido?");
        System.out.println("   - Longitud: debe tener exactamente 60 caracteres");
        System.out.println("   - Formato: debe empezar con $2a$, $2b$ o $2y$");
        System.out.println("   - Ejemplo: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJ...");
    }
    
    /**
     * Método auxiliar para generar un hash individual
     */
    public static String generarHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    /**
     * Método auxiliar para verificar si una contraseña coincide con un hash
     */
    public static boolean verificarHash(String password, String hash) {
        try {
            return BCrypt.checkpw(password, hash);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: El hash no es válido - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para validar si un string es un hash BCrypt válido
     */
    public static boolean esHashBCryptValido(String hash) {
        if (hash == null) return false;
        // BCrypt hashes empiezan con $2a$, $2b$ o $2y$ y tienen 60 caracteres
        return hash.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    }
    
    /**
     * Método auxiliar para repetir un carácter (compatible con Java 8)
     * Reemplaza String.repeat() que solo existe desde Java 11+
     */
    private static String repetirCaracter(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
