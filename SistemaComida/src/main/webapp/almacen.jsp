<%-- 
    Document   : almacen
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
    <title>Almacén - Sistema de Comida</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <!-- Custom CSS para Almacén -->
    <link href="css/almacen.css" rel="stylesheet">
</head>
<body>
    <% 
        // Proteger la página: si no hay sesión, redirigir al login
        // Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        // if (usuario == null) {
        //     response.sendRedirect("index.html");
        //     return; // Detener la ejecución del JSP
        // }
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
                    <a class="nav-link active" href="AlmacenServlet?action=list">
                        <i class="fas fa-boxes me-2"></i> Almacén
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="HistorialVentasServlet?action=list">
                        <i class="fas fa-history me-2"></i> Historial de Ventas
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="PerfilServlet">
                        <i class="fas fa-user-circle me-2"></i> Perfil
                    </a>
                </li>
            </ul>
        </div>
        <div class="content flex-grow-1">
            <div class="container-fluid">
                <h1 class="mb-4">Gestión de Almacén</h1>

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

                <!-- Formulario para Añadir/Editar Producto -->
                <div class="card mb-4">
                    <div class="card-header">
                        <i class="fas fa-cube me-2"></i>
                        <c:choose>
                            <c:when test="${producto != null}">
                                Editar Producto: <c:out value="${producto.nombre}"/>
                            </c:when>
                            <c:otherwise>
                                Añadir Nuevo Producto
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-body">
                        <form action="AlmacenServlet" method="post" id="almacenForm">
                            <c:if test="${producto != null}">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="id_producto" value="${producto.id_producto}">
                            </c:if>
                            <c:if test="${producto == null}">
                                <input type="hidden" name="action" value="add">
                            </c:if>

                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="nombre" class="form-label">Nombre</label>
                                    <input type="text" class="form-control" id="nombre" name="nombre" value="<c:out value='${producto.nombre}'/>" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="tipo" class="form-label">Tipo</label>
                                    <input type="text" class="form-control" id="tipo" name="tipo" value="<c:out value='${producto.tipo}'/>" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="variante" class="form-label">Variante</label>
                                    <input type="text" class="form-control" id="variante" name="variante" value="<c:out value='${producto.variante}'/>">
                                </div>
                                <div class="col-md-6">
                                    <label for="precio_unitario" class="form-label">Precio Unitario</label>
                                    <input type="number" step="0.01" class="form-control" id="precio_unitario" name="precio_unitario" value="<c:out value='${producto.precio_unitario}'/>" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="stock" class="form-label">Stock</label>
                                    <input type="number" class="form-control" id="stock" name="stock" value="<c:out value='${producto.stock}'/>" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="imagen_url" class="form-label">URL de Imagen (Opcional)</label>
                                    <input type="text" class="form-control" id="imagen_url" name="imagen_url" value="<c:out value='${producto.imagen_url}'/>">
                                </div>
                                <div class="col-12 mt-4">
                                    <button type="submit" class="btn btn-primary">
                                        <c:if test="${producto != null}">
                                            <i class="fas fa-save me-2"></i> Actualizar Producto
                                        </c:if>
                                        <c:if test="${producto == null}">
                                            <i class="fas fa-plus-circle me-2"></i> Añadir Producto
                                        </c:if>
                                    </button>
                                    <c:if test="${producto != null}">
                                        <a href="AlmacenServlet?action=list" class="btn btn-secondary ms-2">Cancelar</a>
                                    </c:if>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Panel de registro de almacén (tabla de productos) -->
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <div><i class="fas fa-warehouse me-2"></i> Productos en Almacén</div>
                        <button type="button" class="btn btn-sm btn-outline-light" data-bs-toggle="modal" data-bs-target="#addStockModal">
                            <i class="fas fa-plus me-2"></i> Agregar Stock
                        </button>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <input type="text" class="form-control" id="searchInput" onkeyup="filterTable()" placeholder="Buscar por nombre, tipo o variante...">
                        </div>
                        <div class="table-responsive">
                            <table class="table table-hover" id="productosTable">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nombre</th>
                                        <th>Tipo</th>
                                        <th>Variante</th>
                                        <th>Precio Unitario</th>
                                        <th>Stock</th>
                                        <th>Imagen</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="prod" items="${listaProductos}">
                                        <tr>
                                            <td><c:out value="${prod.id_producto}"/></td>
                                            <td><c:out value="${prod.nombre}"/></td>
                                            <td><c:out value="${prod.tipo}"/></td>
                                            <td><c:out value="${prod.variante}"/></td>
                                            <td><c:out value="${prod.precio_unitario}"/></td>
                                            <td><span class="badge bg-secondary"><c:out value="${prod.stock}"/></span></td>
                                            <td>
                                                <c:if test="${not empty prod.imagen_url}">
                                                    <img src="<c:out value='${prod.imagen_url}'/>" alt="Imagen" style="width: 50px; height: 50px; object-fit: cover; border-radius: 5px;">
                                                </c:if>
                                                <c:if test="${empty prod.imagen_url}">
                                                    N/A
                                                </c:if>
                                            </td>
                                            <td>
                                                <a href="AlmacenServlet?action=edit&id=${prod.id_producto}" class="btn btn-sm btn-info me-2" title="Editar"><i class="fas fa-edit"></i></a>
                                                <a href="AlmacenServlet?action=delete&id=${prod.id_producto}" class="btn btn-sm btn-danger" onclick="return confirm('¿Estás seguro de que quieres eliminar este producto?')" title="Eliminar"><i class="fas fa-trash"></i></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para Agregar Stock -->
    <div class="modal fade" id="addStockModal" tabindex="-1" aria-labelledby="addStockModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="addStockModalLabel">Agregar Stock a Producto</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="AlmacenServlet" method="post">
                    <input type="hidden" name="action" value="updateStock">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="stockProductId" class="form-label">Producto</label>
                            <select class="form-select" id="stockProductId" name="id_producto" required>
                                <option value="">Selecciona un producto</option>
                                <c:forEach var="prod" items="${listaProductos}">
                                    <option value="<c:out value='${prod.id_producto}'/>"><c:out value="${prod.nombre}"/> - Stock actual: <c:out value="${prod.stock}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="stockQuantity" class="form-label">Cantidad a Añadir</label>
                            <input type="number" class="form-control" id="stockQuantity" name="cantidad" value="1" min="1" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary">Actualizar Stock</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS para Almacén -->
    <script src="js/almacen.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.querySelector('#almacenForm'); 
            if (form) {
                form.addEventListener('submit', function(event) { // Añadir 'event' como parámetro
                    // Recorrer todos los campos de entrada de tipo number
                    const numberInputs = form.querySelectorAll('input[type="number"]');
                    numberInputs.forEach(input => {
                        console.log(`Original value for ${input.name}: ${input.value}`);
                        // Reemplazar comas por puntos antes de enviar
                        if (input.value.includes(',')) {
                            input.value = input.value.replace(',', '.');
                            console.log(`Modified value for ${input.name}: ${input.value}`);
                        }
                    });
                    // Esto es solo para depuración; NO lo uses en producción
                    // event.preventDefault(); 
                });
            }
        });
    </script>
</body>
</html>
