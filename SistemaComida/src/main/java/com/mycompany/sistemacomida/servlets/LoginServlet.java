package com.mycompany.sistemacomida.servlets;

import com.mycompany.sistemacomida.dao.UsuarioDAO;
import com.mycompany.sistemacomida.model.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    

    private static final boolean MODO_DESARROLLO = true;
    private static final String DEV_USERNAME = "admin";
    private static final String DEV_PASSWORD = "admin";
    private static final String DEV_ROL = "ADMIN";


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // ========================================
        // MODO DESARROLLO ACTIVO
        // ========================================
        if (MODO_DESARROLLO) {
            System.out.println("⚠️ ⚠️ ⚠️ MODO DESARROLLO ACTIVO ⚠️ ⚠️ ⚠️");
            System.out.println("Login ficticio - NO se usa base de datos");
            System.out.println("Usuario ingresado: " + username);
            
            // Login ficticio - acepta cualquier usuario/contraseña
            // O puedes validar contra DEV_USERNAME y DEV_PASSWORD
            if (username != null && !username.trim().isEmpty() &&
                password != null && !password.trim().isEmpty()) {
                
                // Crear usuario ficticio
                Usuario usuarioFicticio = new Usuario();
                usuarioFicticio.setId_usuario(1);
                usuarioFicticio.setUsername(username); // Usa el username ingresado
                usuarioFicticio.setRol(DEV_ROL);
                usuarioFicticio.setPassword_hash(""); // No se usa
                
                // Guardar en sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogueado", usuarioFicticio);
                
                System.out.println("✓ LOGIN FICTICIO EXITOSO");
                System.out.println("  Usuario: " + username);
                System.out.println("  Rol: " + DEV_ROL);
                System.out.println("⚠️ RECUERDA: Desactiva MODO_DESARROLLO antes de producción");
                
                // Redirigir al dashboard
                response.sendRedirect("dashboard.jsp");
                return;
            } else {
                // Si están vacíos, mostrar error
                request.setAttribute("errorMessage", "Por favor ingresa usuario y contraseña");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
        }
        // ========================================
        // FIN MODO DESARROLLO
        // ========================================

        // DEBUG: Log del intento de login
        System.out.println("=== INTENTO DE LOGIN ===");
        System.out.println("Usuario: " + username);
        System.out.println("Contraseña recibida (longitud): " + (password != null ? password.length() : "null"));

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.getUsuarioByNombreUsuario(username);

        // DEBUG: Verificar si se encontró el usuario
        if (usuario == null) {
            System.out.println("ERROR: Usuario '" + username + "' NO encontrado en la base de datos");
            request.setAttribute("errorMessage", "Nombre de usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        System.out.println("Usuario encontrado: ID=" + usuario.getId_usuario() + ", Rol=" + usuario.getRol());
        String passwordHash = usuario.getPassword_hash();
        System.out.println("Hash almacenado (primeros 20 caracteres): " + 
                          (passwordHash != null && passwordHash.length() > 20 ? passwordHash.substring(0, 20) + "..." : passwordHash));
        System.out.println("Hash almacenado (longitud total): " + (passwordHash != null ? passwordHash.length() : "null"));
        
        // Verificar si el hash parece ser un hash BCrypt válido (empieza con $2a$, $2b$ o $2y$)
        boolean isBCryptHash = passwordHash != null && passwordHash.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
        System.out.println("¿Es hash BCrypt válido? " + isBCryptHash);

        // Verificar si el usuario existe y la contraseña es correcta
        try {
            if (BCrypt.checkpw(password, passwordHash)) {
                // Las credenciales son correctas
                System.out.println("✓ LOGIN EXITOSO para usuario: " + username);
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogueado", usuario); // Guardar el objeto usuario en la sesión
                
                // Redirigir al dashboard
                response.sendRedirect("dashboard.jsp");
            } else {
                // Credenciales incorrectas
                System.out.println("✗ CONTRASEÑA INCORRECTA para usuario: " + username);
                request.setAttribute("errorMessage", "Nombre de usuario o contraseña incorrectos.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            // Error al verificar el hash (probablemente no es un hash BCrypt válido)
            System.err.println("✗ ERROR: El password_hash NO es un hash BCrypt válido!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("SOLUCIÓN: Las contraseñas deben estar hasheadas con BCrypt en la base de datos");
            request.setAttribute("errorMessage", "Error de configuración del sistema. Contacte al administrador.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        
        System.out.println("======================\n");
    }

    @Override
    public String getServletInfo() {
        return "Servlet para manejar la autenticación de usuarios";
    }
}