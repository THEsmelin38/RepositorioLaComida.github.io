package com.mycompany.sistemacomida.servlets;

import com.mycompany.sistemacomida.dao.UsuarioDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Servlet de utilidad para:
 * 1. Crear usuarios de prueba con contraseñas hasheadas
 * 2. Generar hashes BCrypt para contraseñas
 * 3. Diagnosticar problemas de login
 * 
 * IMPORTANTE: Este servlet debe ser deshabilitado en producción por seguridad.
 * Solo usar en desarrollo/testing.
 */
@WebServlet(name = "UtilServlet", urlPatterns = {"/UtilServlet"})
public class UtilServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Utilidad - Sistema de Comida</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            out.println("<div class='container mt-5'>");
            out.println("<div class='row'>");
            out.println("<div class='col-md-8 mx-auto'>");
            
            out.println("<div class='alert alert-warning' role='alert'>");
            out.println("<strong>⚠️ ADVERTENCIA:</strong> Este servlet es solo para desarrollo/testing. ");
            out.println("Deshabilítalo en producción.");
            out.println("</div>");
            
            // Formulario para crear usuario
            out.println("<div class='card mb-4'>");
            out.println("<div class='card-header bg-primary text-white'>");
            out.println("<h4 class='mb-0'>Crear Usuario de Prueba</h4>");
            out.println("</div>");
            out.println("<div class='card-body'>");
            out.println("<form method='POST' action='UtilServlet'>");
            out.println("<input type='hidden' name='action' value='crearUsuario'>");
            out.println("<div class='mb-3'>");
            out.println("<label class='form-label'>Nombre de Usuario:</label>");
            out.println("<input type='text' class='form-control' name='username' required>");
            out.println("</div>");
            out.println("<div class='mb-3'>");
            out.println("<label class='form-label'>Contraseña:</label>");
            out.println("<input type='text' class='form-control' name='password' required>");
            out.println("<small class='text-muted'>La contraseña será hasheada con BCrypt automáticamente</small>");
            out.println("</div>");
            out.println("<div class='mb-3'>");
            out.println("<label class='form-label'>Rol:</label>");
            out.println("<select class='form-select' name='rol' required>");
            out.println("<option value='admin'>Admin</option>");
            out.println("<option value='empleado'>Empleado</option>");
            out.println("</select>");
            out.println("</div>");
            out.println("<button type='submit' class='btn btn-primary'>Crear Usuario</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            
            // Formulario para generar hash
            out.println("<div class='card mb-4'>");
            out.println("<div class='card-header bg-success text-white'>");
            out.println("<h4 class='mb-0'>Generar Hash BCrypt</h4>");
            out.println("</div>");
            out.println("<div class='card-body'>");
            out.println("<form method='POST' action='UtilServlet'>");
            out.println("<input type='hidden' name='action' value='generarHash'>");
            out.println("<div class='mb-3'>");
            out.println("<label class='form-label'>Contraseña:</label>");
            out.println("<input type='text' class='form-control' name='password' required>");
            out.println("</div>");
            out.println("<button type='submit' class='btn btn-success'>Generar Hash</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            
            // Información útil
            out.println("<div class='card'>");
            out.println("<div class='card-header bg-info text-white'>");
            out.println("<h4 class='mb-0'>Información Útil</h4>");
            out.println("</div>");
            out.println("<div class='card-body'>");
            out.println("<h5>¿Por qué falla el login en otra máquina?</h5>");
            out.println("<ul>");
            out.println("<li><strong>Las contraseñas NO están hasheadas con BCrypt:</strong> Si insertaste usuarios manualmente con contraseñas en texto plano (ej: '123456'), el login SIEMPRE fallará.</li>");
            out.println("<li><strong>Base de datos diferente:</strong> La otra máquina puede tener usuarios diferentes o sin usuarios.</li>");
            out.println("<li><strong>Conexión a BD incorrecta:</strong> Verifica que MySQL esté corriendo y la base de datos 'bd_comida' exista.</li>");
            out.println("</ul>");
            out.println("<h5>Solución:</h5>");
            out.println("<ol>");
            out.println("<li>Usa este servlet para crear usuarios con contraseñas hasheadas correctamente.</li>");
            out.println("<li>O genera un hash BCrypt aquí y actualiza manualmente la base de datos con un UPDATE.</li>");
            out.println("<li>Revisa los logs del servidor (NetBeans/Tomcat) para ver mensajes de diagnóstico del LoginServlet.</li>");
            out.println("</ol>");
            out.println("</div>");
            out.println("</div>");
            
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Resultado - Utilidad</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            out.println("<div class='container mt-5'>");
            out.println("<div class='row'>");
            out.println("<div class='col-md-8 mx-auto'>");
            
            if ("crearUsuario".equals(action)) {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String rol = request.getParameter("rol");
                
                UsuarioDAO dao = new UsuarioDAO();
                boolean success = dao.crearUsuario(username, password, rol);
                
                if (success) {
                    out.println("<div class='alert alert-success' role='alert'>");
                    out.println("<h4 class='alert-heading'>✓ Usuario Creado Exitosamente</h4>");
                    out.println("<p><strong>Usuario:</strong> " + username + "</p>");
                    out.println("<p><strong>Rol:</strong> " + rol + "</p>");
                    out.println("<p><strong>Contraseña:</strong> " + password + " (ahora puedes iniciar sesión con esta contraseña)</p>");
                    out.println("<hr>");
                    out.println("<p class='mb-0'>El hash BCrypt se ha guardado correctamente en la base de datos.</p>");
                    out.println("</div>");
                } else {
                    out.println("<div class='alert alert-danger' role='alert'>");
                    out.println("<h4 class='alert-heading'>✗ Error al Crear Usuario</h4>");
                    out.println("<p>Posibles causas:</p>");
                    out.println("<ul>");
                    out.println("<li>El usuario ya existe</li>");
                    out.println("<li>Error de conexión a la base de datos</li>");
                    out.println("<li>La tabla Usuarios no existe</li>");
                    out.println("</ul>");
                    out.println("<p class='mb-0'>Revisa los logs del servidor para más detalles.</p>");
                    out.println("</div>");
                }
                
            } else if ("generarHash".equals(action)) {
                String password = request.getParameter("password");
                String hash = BCrypt.hashpw(password, BCrypt.gensalt());
                
                out.println("<div class='alert alert-success' role='alert'>");
                out.println("<h4 class='alert-heading'>✓ Hash BCrypt Generado</h4>");
                out.println("<p><strong>Contraseña original:</strong> <code>" + password + "</code></p>");
                out.println("<p><strong>Hash BCrypt:</strong></p>");
                out.println("<textarea class='form-control' rows='3' readonly>" + hash + "</textarea>");
                out.println("<hr>");
                out.println("<p><strong>Cómo usar este hash:</strong></p>");
                out.println("<p>Ejecuta este SQL en tu base de datos:</p>");
                out.println("<pre class='bg-dark text-white p-3'>UPDATE Usuarios SET password_hash = '" + hash + "' WHERE username = 'TU_USUARIO';</pre>");
                out.println("</div>");
            }
            
            out.println("<a href='UtilServlet' class='btn btn-secondary'>← Volver</a>");
            out.println("<a href='login.jsp' class='btn btn-primary'>Ir al Login</a>");
            
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet de utilidad para crear usuarios y generar hashes BCrypt";
    }
}
