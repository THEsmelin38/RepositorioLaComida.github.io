<%-- 
    Document   : historialVentas
    Created on : Oct 22, 2025, 10:00:00 AM
    Author     : HP
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.mycompany.sistemacomida.model.Usuario" %>
<%@ page import="com.mycompany.sistemacomida.model.Venta" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Ventas - Sistema de Comida</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <!-- Custom CSS para Dashboard (base de estilos) -->
    <link href="css/dashboard.css" rel="stylesheet">
    <!-- Custom CSS para Historial de Ventas -->
    <link href="css/historialVentas.css" rel="stylesheet">
</head>
<body>
    <% 
        // Proteger la página: si no hay sesión, redirigir al login
        // Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        // if (usuario == null) {
        //     response.sendRedirect("login.jsp");
        //     return; // Detener la ejecución del JSP
        // }

        // Si estamos viendo una venta específica, se pasará como atributo de request
        Venta ventaDetalle = (Venta) request.getAttribute("venta");
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
                    <a class="nav-link active"" href="HistorialVentasServlet?action=list">
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
                <h1 class="mb-4">Historial de Ventas</h1>

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

                <c:choose>
                    <c:when test="${venta != null}">
                        <%-- Vista de Detalle de Venta --%>
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                <i class="fas fa-file-invoice-dollar me-2"></i> Detalles del Pedido: <c:out value="${venta.codigo_pedido}"/>
                            </div>
                            <div class="card-body">
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <p><strong>Cliente:</strong> <c:out value="${venta.nombre_cliente}"/></p>
                                        <p><strong>Fecha:</strong> <fmt:formatDate value="${venta.fecha_venta}" pattern="dd/MM/yyyy HH:mm"/></p>
                                        <p><strong>Estado:</strong> <span class="badge bg-info"><c:out value="${venta.estado}"/></span></p>
                                    </div>
                                    <div class="col-md-6 text-end">
                                        <p><strong>Subtotal:</strong> S/. <fmt:formatNumber value="${venta.subtotal_general}" pattern="#.00"/></p>
                                        <p><strong>IGV (18%):</strong> S/. <fmt:formatNumber value="${venta.igv}" pattern="#.00"/></p>
                                        <h3><strong>Total:</strong> S/. <fmt:formatNumber value="${venta.total}" pattern="#.00"/></h3>
                                        <p><strong>Método de Pago:</strong> <c:out value="${venta.metodo_pago}"/></p>
                                    </div>
                                </div>
                                
                                <h4>Productos Vendidos</h4>
                                <div class="table-responsive">
                                    <table class="table table-bordered table-sm">
                                        <thead>
                                            <tr>
                                                <th>Producto</th>
                                                <th>Tipo</th>
                                                <th>Variante</th>
                                                <th>Cantidad</th>
                                                <th>P. Unitario</th>
                                                <th>Subtotal</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="detalle" items="${venta.detalles}">
                                                <tr>
                                                    <td><c:out value="${detalle.producto.nombre}"/></td>
                                                    <td><c:out value="${detalle.producto.tipo}"/></td>
                                                    <td><c:out value="${detalle.producto.variante}"/></td>
                                                    <td><c:out value="${detalle.cantidad}"/></td>
                                                    <td>S/. <fmt:formatNumber value="${detalle.precio_unitario_venta}" pattern="#.00"/></td>
                                                    <td>S/. <fmt:formatNumber value="${detalle.subtotal_linea}" pattern="#.00"/></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="mt-4 text-end">
                                    <a href="HistorialVentasServlet?action=list" class="btn btn-secondary me-2"><i class="fas fa-arrow-left me-2"></i> Volver al Historial</a>
                                    <a href="HistorialVentasServlet?action=generatePdf&id=${venta.id_venta}" class="btn btn-success me-2" target="_blank"><i class="fas fa-file-pdf me-2"></i> Generar PDF</a>
                                    <a href="HistorialVentasServlet?action=delete&id=${venta.id_venta}" class="btn btn-danger" onclick="return confirm('¿Estás seguro de que quieres eliminar esta venta?')"><i class="fas fa-trash me-2"></i> Eliminar Venta</a>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <%-- Tabla de Historial de Ventas --%>
                        <div class="card">
                            <div class="card-header bg-primary text-white">
                                <i class="fas fa-list-alt me-2"></i> Lista de Pedidos
                            </div>
                            <div class="card-body">
                                <div class="mb-3">
                                    <input type="text" class="form-control" id="searchInput" onkeyup="filterTable()" placeholder="Buscar por código, cliente o estado...">
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-hover" id="ventasTable">
                                        <thead>
                                            <tr>
                                                <th>Código Pedido</th>
                                                <th>Cliente</th>
                                                <th>Fecha</th>
                                                <th>Total</th>
                                                <th>Estado</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="ventaItem" items="${listaVentas}">
                                                <tr>
                                                    <td><c:out value="${ventaItem.codigo_pedido}"/></td>
                                                    <td><c:out value="${ventaItem.nombre_cliente}"/></td>
                                                    <td><fmt:formatDate value="${ventaItem.fecha_venta}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                    <td>S/. <fmt:formatNumber value="${ventaItem.total}" pattern="#.00"/></td>
                                                    <td><span class="badge 
                                                            <c:choose>
                                                                <c:when test="${ventaItem.estado eq 'COMPLETADO'}">bg-success</c:when>
                                                                <c:when test="${ventaItem.estado eq 'PENDIENTE'}">bg-warning text-dark</c:when>
                                                                <c:when test="${ventaItem.estado eq 'CANCELADO'}">bg-danger</c:when>
                                                                <c:otherwise>bg-secondary</c:otherwise>
                                                            </c:choose>
                                                            "><c:out value="${ventaItem.estado}"/></span></td>
                                                    <td>
                                                        <a href="HistorialVentasServlet?action=view&id=${ventaItem.id_venta}" class="btn btn-sm btn-info btn-action" title="Ver Detalle"><i class="fas fa-eye"></i></a>
                                                        <a href="HistorialVentasServlet?action=generatePdf&id=${ventaItem.id_venta}" class="btn btn-sm btn-success btn-action" title="Generar PDF" target="_blank"><i class="fas fa-file-pdf"></i></a>
                                                        <a href="HistorialVentasServlet?action=delete&id=${ventaItem.id_venta}" class="btn btn-sm btn-danger btn-action" onclick="return confirm('¿Estás seguro de que quieres eliminar esta venta?')" title="Eliminar"><i class="fas fa-trash"></i></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function filterTable() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("searchInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("ventasTable");
            tr = table.getElementsByTagName("tr");

            for (i = 1; i < tr.length; i++) { // Empieza en 1 para omitir el encabezado
                tr[i].style.display = "none";
                td = tr[i].getElementsByTagName("td");
                // Buscar en Código Pedido, Cliente, Estado (columnas 0, 1, 4)
                if (td[0] && td[0].textContent.toUpperCase().indexOf(filter) > -1 ||
                    td[1] && td[1].textContent.toUpperCase().indexOf(filter) > -1 ||
                    td[4] && td[4].textContent.toUpperCase().indexOf(filter) > -1) {
                    tr[i].style.display = "";
                }
            }
        }
    </script>
</body>
</html>
