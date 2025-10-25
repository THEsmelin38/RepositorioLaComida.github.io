<%-- 
    Document   : perfil
    Created on : Oct 22, 2025, 10:00:00 AM
    Author     : HP
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.mycompany.sistemacomida.model.Usuario" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de Usuario - Sistema de Comida</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <!-- Custom CSS para Dashboard (base de estilos) -->
    <link href="css/dashboard.css" rel="stylesheet">
    <!-- Custom CSS para Perfil -->
    <link href="css/perfil.css" rel="stylesheet">
</head>
<body>
    <% 
        // Proteger la página: si no hay sesión, redirigir al login
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return; // Detener la ejecución del JSP
        }
    %>

    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="dashboard.jsp">Sistema de Comida</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Bienvenido, <c:out value="${usuarioLogueado.username}"/>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                            <li><a class="dropdown-item" href="#">Perfil</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="LogoutServlet">Cerrar Sesión</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="d-flex">
        <div class="sidebar p-3">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link" href="dashboard.jsp">
                        <i class="fas fa-home me-2"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="VentaServlet?action=create">
                        <i class="fas fa-shopping-cart me-2"></i> Ventas
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="AlmacenServlet?action=list">
                        <i class="fas fa-boxes me-2"></i> Almacén
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="HistorialVentasServlet?action=list">
                        <i class="fas fa-history me-2"></i> Historial de Ventas
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="PerfilServlet">
                        <i class="fas fa-user-circle me-2"></i> Perfil
                    </a>
                </li>
            </ul>
        </div>
        <div class="content flex-grow-1">
            <div class="container-fluid">
                <h1 class="mb-4">Perfil de Usuario</h1>

                <!-- Mensajes de éxito/error -->
                <c:if test="${not empty requestScope.message}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${requestScope.message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <c:if test="${not empty requestScope.errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${requestScope.errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <div class="row">
                    <div class="col-md-6">
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-user me-2"></i> Información del Perfil
                            </div>
                            <div class="card-body">
                                <div class="text-center mb-4">
                                    <img src="img/UPT.png" alt=""/>
                              
                                    <h4><c:out value="${usuarioLogueado.username}"/></h4>
                                    <p class="text-muted">Rol: <c:out value="${usuarioLogueado.rol}"/></p>
                                </div>

                                <h5 class="mb-3">Editar Nombre de Usuario</h5>
                                <form action="PerfilServlet" method="post">
                                    <input type="hidden" name="action" value="updateProfile">
                                    <div class="mb-3">
                                        <label for="username" class="form-label">Nuevo Nombre de Usuario</label>
                                        <input type="text" class="form-control" id="username" name="username" value="<c:out value='${usuarioLogueado.username}'/>" required>
                                    </div>
                                    <button type="submit" class="btn btn-primary"><i class="fas fa-save me-2"></i> Guardar Cambios</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-lock me-2"></i> Cambiar Contraseña
                            </div>
                            <div class="card-body">
                                <form action="PerfilServlet" method="post">
                                    <input type="hidden" name="action" value="changePassword">
                                    <div class="mb-3">
                                        <label for="currentPassword" class="form-label">Contraseña Actual</label>
                                        <input type="password" class="form-control" id="currentPassword" name="currentPassword" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="newPassword" class="form-label">Nueva Contraseña</label>
                                        <input type="password" class="form-control" id="newPassword" name="newPassword" required>
                                    </div>
                                    <div class="mb-4">
                                        <label for="confirmNewPassword" class="form-label">Confirmar Nueva Contraseña</label>
                                        <input type="password" class="form-control" id="confirmNewPassword" name="confirmNewPassword" required>
                                    </div>
                                    <button type="submit" class="btn btn-primary"><i class="fas fa-key me-2"></i> Cambiar Contraseña</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
