package com.mycompany.sistemacomida.servlets;

import com.mycompany.sistemacomida.util.ConexionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet de prueba para diagnosticar la conexión a la base de datos MySQL.
 * Accede a: http://localhost:8080/SistemaComida-1.0-SNAPSHOT/TestConexion
 * 
 * ⚠️ IMPORTANTE: Deshabilita este servlet en producción por seguridad.
 */
@WebServlet(name = "TestConexionServlet", urlPatterns = {"/TestConexion"})
public class TestConexionServlet extends HttpServlet {

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
            out.println("<title>Test de Conexión - Base de Datos</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<style>");
            out.println("body { background-color: #f8f9fa; padding: 20px; }");
            out.println(".test-card { margin-bottom: 20px; }");
            out.println(".success { color: #28a745; font-weight: bold; }");
            out.println(".error { color: #dc3545; font-weight: bold; }");
            out.println(".info-table { font-family: monospace; font-size: 14px; }");
            out.println(".warning-box { background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<div class='container'>");
            out.println("<h1 class='mb-4'>🔧 Test de Conexión a Base de Datos</h1>");
            
            // Warning box
            out.println("<div class='warning-box'>");
            out.println("</div>");
            
            // Test de conexión
            Connection conn = null;
            boolean connectionSuccess = false;
            String errorMessage = null;
            
            try {
                out.println("<div class='card test-card'>");
                out.println("<div class='card-header bg-primary text-white'>");
                out.println("<h4 class='mb-0'>1. Test de Conexión a MySQL</h4>");
                out.println("</div>");
                out.println("<div class='card-body'>");
                
                long startTime = System.currentTimeMillis();
                conn = ConexionDB.getConnection();
                long endTime = System.currentTimeMillis();
                
                if (conn != null && !conn.isClosed()) {
                    connectionSuccess = true;
                    out.println("<p class='success'>✓ CONEXIÓN EXITOSA</p>");
                    out.println("<p><strong>Tiempo de conexión:</strong> " + (endTime - startTime) + " ms</p>");
                }
                
                out.println("</div>");
                out.println("</div>");
                
            } catch (SQLException e) {
                connectionSuccess = false;
                errorMessage = e.getMessage();
                
                out.println("<div class='card test-card border-danger'>");
                out.println("<div class='card-header bg-danger text-white'>");
                out.println("<h4 class='mb-0'>1. Test de Conexión a MySQL</h4>");
                out.println("</div>");
                out.println("<div class='card-body'>");
                out.println("<p class='error'>✗ ERROR DE CONEXIÓN</p>");
                out.println("<p><strong>Mensaje:</strong> " + e.getMessage() + "</p>");
                out.println("<p><strong>Código de error SQL:</strong> " + e.getErrorCode() + "</p>");
                out.println("<p><strong>Estado SQL:</strong> " + e.getSQLState() + "</p>");
                out.println("</div>");
                out.println("</div>");
            }
            
            // Información de la base de datos
            if (connectionSuccess && conn != null) {
                try {
                    out.println("<div class='card test-card'>");
                    out.println("<div class='card-header bg-info text-white'>");
                    out.println("<h4 class='mb-0'>2. Información de la Base de Datos</h4>");
                    out.println("</div>");
                    out.println("<div class='card-body'>");
                    
                    DatabaseMetaData metaData = conn.getMetaData();
                    
                    out.println("<table class='table table-sm info-table'>");
                    out.println("<tr><th>Nombre del DBMS:</th><td>" + metaData.getDatabaseProductName() + "</td></tr>");
                    out.println("<tr><th>Versión del DBMS:</th><td>" + metaData.getDatabaseProductVersion() + "</td></tr>");
                    out.println("<tr><th>Driver JDBC:</th><td>" + metaData.getDriverName() + "</td></tr>");
                    out.println("<tr><th>Versión del Driver:</th><td>" + metaData.getDriverVersion() + "</td></tr>");
                    out.println("<tr><th>URL de conexión:</th><td>" + metaData.getURL() + "</td></tr>");
                    out.println("<tr><th>Usuario:</th><td>" + metaData.getUserName() + "</td></tr>");
                    out.println("<tr><th>Esquema actual:</th><td>" + conn.getCatalog() + "</td></tr>");
                    out.println("<tr><th>Solo lectura:</th><td>" + (conn.isReadOnly() ? "Sí" : "No") + "</td></tr>");
                    out.println("<tr><th>Auto-commit:</th><td>" + (conn.getAutoCommit() ? "Activado" : "Desactivado") + "</td></tr>");
                    out.println("</table>");
                    
                    out.println("</div>");
                    out.println("</div>");
                    
                } catch (SQLException e) {
                    out.println("<div class='alert alert-warning'>Error al obtener metadata: " + e.getMessage() + "</div>");
                }
                
                // Listar tablas
                try {
                    out.println("<div class='card test-card'>");
                    out.println("<div class='card-header bg-success text-white'>");
                    out.println("<h4 class='mb-0'>3. Tablas en la Base de Datos 'bd_comida'</h4>");
                    out.println("</div>");
                    out.println("<div class='card-body'>");
                    
                    DatabaseMetaData metaData = conn.getMetaData();
                    ResultSet tables = metaData.getTables("bd_comida", null, "%", new String[]{"TABLE"});
                    
                    out.println("<table class='table table-striped'>");
                    out.println("<thead><tr><th>Nombre de la Tabla</th><th>Tipo</th></tr></thead>");
                    out.println("<tbody>");
                    
                    boolean hasTables = false;
                    while (tables.next()) {
                        hasTables = true;
                        String tableName = tables.getString("TABLE_NAME");
                        String tableType = tables.getString("TABLE_TYPE");
                        out.println("<tr><td><strong>" + tableName + "</strong></td><td>" + tableType + "</td></tr>");
                    }
                    
                    if (!hasTables) {
                        out.println("<tr><td colspan='2' class='text-center text-muted'>No se encontraron tablas</td></tr>");
                    }
                    
                    out.println("</tbody>");
                    out.println("</table>");
                    
                    out.println("</div>");
                    out.println("</div>");
                    
                    tables.close();
                    
                } catch (SQLException e) {
                    out.println("<div class='alert alert-warning'>Error al listar tablas: " + e.getMessage() + "</div>");
                }
                
                // Test de tabla Usuarios
                try {
                    out.println("<div class='card test-card'>");
                    out.println("<div class='card-header bg-warning'>");
                    out.println("<h4 class='mb-0'>4. Test de Tabla 'Usuarios'</h4>");
                    out.println("</div>");
                    out.println("<div class='card-body'>");
                    
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Usuarios");
                    
                    if (rs.next()) {
                        int totalUsuarios = rs.getInt("total");
                        out.println("<p class='success'>✓ Tabla 'Usuarios' existe</p>");
                        out.println("<p><strong>Total de usuarios:</strong> " + totalUsuarios + "</p>");
                        
                        if (totalUsuarios == 0) {
                            out.println("<div class='alert alert-info'>");
                            out.println("<strong>ℹ️ Información:</strong> La tabla existe pero está vacía. ");
                            out.println("Ejecuta el script <code>init_database.sql</code> para crear usuarios de prueba.");
                            out.println("</div>");
                        }
                    }
                    
                    rs.close();
                    stmt.close();
                    
                    // Mostrar algunos usuarios
                    if (true) {
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery("SELECT id_usuario, username, rol, LEFT(password_hash, 20) as hash_preview FROM Usuarios LIMIT 5");
                        
                        out.println("<h5 class='mt-3'>Usuarios en la base de datos:</h5>");
                        out.println("<table class='table table-sm'>");
                        out.println("<thead><tr><th>ID</th><th>Username</th><th>Rol</th><th>Hash (preview)</th></tr></thead>");
                        out.println("<tbody>");
                        
                        while (rs.next()) {
                            out.println("<tr>");
                            out.println("<td>" + rs.getInt("id_usuario") + "</td>");
                            out.println("<td><strong>" + rs.getString("username") + "</strong></td>");
                            out.println("<td>" + rs.getString("rol") + "</td>");
                            out.println("<td><code>" + rs.getString("hash_preview") + "...</code></td>");
                            out.println("</tr>");
                        }
                        
                        out.println("</tbody>");
                        out.println("</table>");
                        
                        rs.close();
                        stmt.close();
                    }
                    
                    out.println("</div>");
                    out.println("</div>");
                    
                } catch (SQLException e) {
                    out.println("<div class='card test-card border-danger'>");
                    out.println("<div class='card-header bg-danger text-white'>");
                    out.println("<h4 class='mb-0'>4. Test de Tabla 'Usuarios'</h4>");
                    out.println("</div>");
                    out.println("<div class='card-body'>");
                    out.println("<p class='error'>✗ Error al consultar tabla 'Usuarios'</p>");
                    out.println("<p><strong>Mensaje:</strong> " + e.getMessage() + "</p>");
                    out.println("<div class='alert alert-warning mt-3'>");
                    out.println("<strong>Posibles causas:</strong>");
                    out.println("<ul>");
                    out.println("<li>La tabla 'Usuarios' no existe</li>");
                    out.println("<li>No tienes permisos para leer la tabla</li>");
                    out.println("<li>La base de datos 'bd_comida' no existe</li>");
                    out.println("</ul>");
                    out.println("<strong>Solución:</strong> Ejecuta el script <code>init_database.sql</code>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("</div>");
                }
            }
            
            // Diagnóstico y recomendaciones
            out.println("<div class='card test-card'>");
            out.println("<div class='card-header bg-secondary text-white'>");
            out.println("<h4 class='mb-0'>5. Diagnóstico y Recomendaciones</h4>");
            out.println("</div>");
            out.println("<div class='card-body'>");
            
            if (connectionSuccess) {
                out.println("<div class='alert alert-success'>");
                out.println("<h5>✓ Estado: CORRECTO</h5>");
                out.println("<p>La conexión a la base de datos funciona correctamente. El sistema está listo para usar.</p>");
                out.println("</div>");
                
                out.println("<h5>Siguiente paso:</h5>");
                out.println("<ul>");
                out.println("<li>Si usas <strong>MODO_DESARROLLO</strong> en LoginServlet, cámbialo a <code>false</code> para usar autenticación real</li>");
                out.println("<li>Accede al login: <a href='login.jsp'>login.jsp</a></li>");
                out.println("<li>O crea usuarios: <a href='UtilServlet'>UtilServlet</a></li>");
                out.println("</ul>");
            } else {
                out.println("<div class='alert alert-danger'>");
                out.println("<h5>✗ Estado: ERROR</h5>");
                out.println("<p><strong>Error:</strong> " + (errorMessage != null ? errorMessage : "No se pudo conectar") + "</p>");
                out.println("</div>");
                
                out.println("<h5>Posibles causas:</h5>");
                out.println("<ol>");
                out.println("<li><strong>MySQL no está corriendo:</strong> Verifica en Servicios de Windows (services.msc)</li>");
                out.println("<li><strong>La base de datos 'bd_comida' no existe:</strong> Ejecuta el script <code>init_database.sql</code></li>");
                out.println("<li><strong>Credenciales incorrectas:</strong> Verifica usuario/contraseña en <code>ConexionDB.java</code></li>");
                out.println("<li><strong>Puerto 3306 bloqueado:</strong> Verifica firewall o si otro servicio usa el puerto</li>");
                out.println("<li><strong>Driver JDBC no cargado:</strong> Ejecuta <code>fix-driver-problem.bat</code></li>");
                out.println("</ol>");
                
                out.println("<h5>Soluciones:</h5>");
                out.println("<ul>");
                out.println("<li>Inicia MySQL: <code>net start MySQL80</code> (en CMD como administrador)</li>");
                out.println("<li>Crea la base de datos: <code>mysql -u root -p &lt; init_database.sql</code></li>");
                out.println("<li>Usa el <strong>MODO_DESARROLLO</strong> mientras solucionas el problema</li>");
                out.println("</ul>");
            }
            
            out.println("</div>");
            out.println("</div>");
            
            // Footer
            out.println("<div class='text-center mt-4'>");
            out.println("<a href='TestConexion' class='btn btn-primary'>🔄 Probar de Nuevo</a> ");
            out.println("<a href='login.jsp' class='btn btn-secondary'>← Volver al Login</a> ");
            out.println("<a href='UtilServlet' class='btn btn-info'>🔧 Utilidades</a>");
            out.println("</div>");
            
            out.println("</div>"); // container
            out.println("</body>");
            out.println("</html>");
            
            // Cerrar conexión
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet de prueba para diagnosticar conexión a base de datos MySQL";
    }
}
