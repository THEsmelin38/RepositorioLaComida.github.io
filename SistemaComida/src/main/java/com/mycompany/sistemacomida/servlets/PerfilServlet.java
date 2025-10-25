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

@WebServlet(name = "PerfilServlet", urlPatterns = {"/PerfilServlet"})
public class PerfilServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Verificar sesión de usuario
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        request.setAttribute("usuario", usuarioLogueado);
        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Verificar sesión de usuario
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        String action = request.getParameter("action");

        if (action == null) {
            action = "updateProfile"; // Acción por defecto
        }

        switch (action) {
            case "updateProfile":
                updateProfile(request, response, usuarioLogueado, session);
                break;
            case "changePassword":
                changePassword(request, response, usuarioLogueado, session);
                break;
            default:
                response.sendRedirect("PerfilServlet");
                break;
        }
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogueado, HttpSession session) 
            throws ServletException, IOException {
        String newUsername = request.getParameter("username");

        if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(usuarioLogueado.getUsername())) {
            // Verificar si el nuevo username ya existe
            Usuario existingUser = usuarioDAO.getUsuarioByNombreUsuario(newUsername);
            if (existingUser != null && existingUser.getId_usuario() != usuarioLogueado.getId_usuario()) {
                request.setAttribute("errorMessage", "El nombre de usuario ya está en uso.");
            } else {
                usuarioLogueado.setUsername(newUsername);
                if (usuarioDAO.updateUsuario(usuarioLogueado)) {
                    session.setAttribute("usuarioLogueado", usuarioLogueado); // Actualizar sesión
                    request.setAttribute("message", "Perfil actualizado correctamente.");
                } else {
                    request.setAttribute("errorMessage", "Error al actualizar el perfil.");
                }
            }
        } else if (newUsername.equals(usuarioLogueado.getUsername())) {
            request.setAttribute("message", "No se realizaron cambios en el perfil.");
        }
        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogueado, HttpSession session) 
            throws ServletException, IOException {
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");

        if (!BCrypt.checkpw(currentPassword, usuarioLogueado.getPassword_hash())) {
            request.setAttribute("errorMessage", "La contraseña actual es incorrecta.");
        } else if (!newPassword.equals(confirmNewPassword)) {
            request.setAttribute("errorMessage", "Las nuevas contraseñas no coinciden.");
        } else if (newPassword.length() < 6) { // Ejemplo de validación simple
            request.setAttribute("errorMessage", "La nueva contraseña debe tener al menos 6 caracteres.");
        } else {
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            if (usuarioDAO.updatePassword(usuarioLogueado.getId_usuario(), hashedNewPassword)) {
                // Actualizar el objeto usuarioLogueado en la sesión con el nuevo hash
                usuarioLogueado.setPassword_hash(hashedNewPassword);
                session.setAttribute("usuarioLogueado", usuarioLogueado);
                request.setAttribute("message", "Contraseña cambiada correctamente.");
            } else {
                request.setAttribute("errorMessage", "Error al cambiar la contraseña.");
            }
        }
        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestionar el perfil del usuario";
    }
}
