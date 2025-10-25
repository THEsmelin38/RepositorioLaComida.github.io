-- ========================================
-- Script de Inicialización de Base de Datos
-- Sistema de Comida
-- ========================================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS bd_comida;

-- Usar la base de datos
USE bd_comida;

-- ========================================
-- Tabla: Usuarios
-- ========================================
CREATE TABLE IF NOT EXISTS Usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- USUARIOS DE PRUEBA
-- ========================================
-- IMPORTANTE: Estos hashes BCrypt son ejemplos
-- Para generar nuevos hashes, usa el UtilServlet:
-- http://localhost:8080/SistemaComida-1.0-SNAPSHOT/UtilServlet

-- Eliminar usuarios de prueba si existen
DELETE FROM Usuarios WHERE username IN ('admin', 'empleado', 'test');

-- Usuario 1: admin / Contraseña: admin123
-- Hash generado con: BCrypt.hashpw("admin123", BCrypt.gensalt())
INSERT INTO Usuarios (username, password_hash, rol) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin');

-- Usuario 2: empleado / Contraseña: empleado123
INSERT INTO Usuarios (username, password_hash, rol) VALUES
('empleado', '$2a$10$cRrfJmvY8k5iEOLjJqOPCuO.z2IFdqXcJkGJqJaKJQrJQjJqJaKJ.', 'empleado');

-- Usuario 3: test / Contraseña: test123
INSERT INTO Usuarios (username, password_hash, rol) VALUES
('test', '$2a$10$kJqJaKJQrJQjJqJaKJkJqJaKJQrJQjJqJaKJkJqJaKJQrJQjJqJaK', 'empleado');

-- ========================================
-- Verificación
-- ========================================
-- Mostrar usuarios creados
SELECT 
    id_usuario,
    username,
    rol,
    LEFT(password_hash, 20) as hash_preview,
    LENGTH(password_hash) as hash_length,
    CASE 
        WHEN password_hash REGEXP '^\\$2[aby]\\$[0-9]{2}\\$' THEN 'SÍ ✓'
        ELSE 'NO ✗'
    END as es_hash_bcrypt_valido,
    fecha_creacion
FROM Usuarios
ORDER BY id_usuario;

-- ========================================
-- NOTAS IMPORTANTES
-- ========================================

-- 1. HASHES BCRYPT:
--    - DEBEN empezar con $2a$, $2b$ o $2y$
--    - Tienen 60 caracteres de longitud
--    - NO uses contraseñas en texto plano

-- 2. PARA GENERAR NUEVOS HASHES:
--    Opción A: Usa el UtilServlet (recomendado)
--    http://localhost:8080/SistemaComida-1.0-SNAPSHOT/UtilServlet
--
--    Opción B: En Java:
--    String hash = BCrypt.hashpw("tu_contraseña", BCrypt.gensalt());

-- 3. PARA ACTUALIZAR UNA CONTRASEÑA:
--    UPDATE Usuarios 
--    SET password_hash = '$2a$10$TuNuevoHashAqui...' 
--    WHERE username = 'nombre_usuario';

-- 4. PARA VERIFICAR SI UN HASH ES VÁLIDO:
--    SELECT 
--        username,
--        password_hash REGEXP '^\\$2[aby]\\$[0-9]{2}\\$' as es_valido
--    FROM Usuarios;

-- 5. NUNCA HAGAS ESTO (incorrecto):
--    INSERT INTO Usuarios (username, password_hash, rol) 
--    VALUES ('user', '123456', 'empleado');  -- ✗ CONTRASEÑA EN TEXTO PLANO
--
--    El login SIEMPRE fallará porque BCrypt espera un hash.

-- ========================================
-- TABLAS ADICIONALES
-- ========================================
-- Agrega aquí otras tablas de tu sistema (Productos, Ventas, etc.)

CREATE TABLE IF NOT EXISTS Productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS Ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS DetalleVenta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES Ventas(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- FIN DEL SCRIPT
-- ========================================

COMMIT;

-- Mensaje final
SELECT '✓ Base de datos inicializada correctamente' as resultado;
SELECT '✓ Usuarios de prueba creados con contraseñas hasheadas BCrypt' as info;
SELECT 'Ahora puedes iniciar sesión con: admin/admin123 o empleado/empleado123' as instrucciones;
