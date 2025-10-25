<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.mycompany.sistemacomida.model.Usuario" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Venta - Sistema de Comida</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <!-- Custom CSS para Dashboard (base de estilos) -->
    <link href="css/dashboard.css" rel="stylesheet">
    <!-- Custom CSS para Registro de Venta -->
    <link href="css/registroVenta.css" rel="stylesheet">
    <style>
        /* Estilos específicos para este JSP si son necesarios, se fusionarán con registroVenta.css */
    </style>
</head>
<body>
    <% 
        // Proteger la página: si no hay sesión, redirigir al login
        // Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado"); // Eliminado
        // if (usuario == null) { // Eliminado
        //     response.sendRedirect("login.jsp"); // Eliminado
        //     return; // Eliminado
        // } // Eliminado

        // Parámetro para saber si estamos en la fase de pago
        boolean isPaymentPhase = "confirmPayment".equals(request.getParameter("action"));
        request.setAttribute("isPaymentPhase", isPaymentPhase); // Hacerlo accesible para EL
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
                    <a class="nav-link" href="PerfilServlet">
                        <i class="fas fa-user-circle me-2"></i> Perfil
                    </a>
                </li>
            </ul>
        </div>
        <div class="content flex-grow-1">
            <div class="container-fluid">
                <h1 class="mb-4">Registro de Venta</h1>

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
                    <c:when test="${isPaymentPhase}">
                        <%-- Paso 2: Simulación de Pago --%>
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                <i class="fas fa-credit-card me-2"></i> Confirmar y Pagar Pedido: <c:out value="${sessionScope.codigoPedido}"/>
                            </div>
                            <div class="card-body">
                                <p><strong>Cliente:</strong> <c:out value="${sessionScope.nombreCliente}"/></p>
                                <p><strong>Subtotal:</strong> S/. <c:out value="${sessionScope.subtotalGeneral}"/></p>
                                <p><strong>IGV (18%):</strong> S/. <c:out value="${sessionScope.igvMonto}"/></p>
                                <h3><strong>Total a Pagar:</strong> S/. <span id="totalPagarEnPago"><c:out value="${sessionScope.total}"/></span></h3>

                                <hr>

                                <h4>Selecciona Método de Pago</h4>
                                <form action="VentaServlet" method="post" id="paymentForm">
                                    <input type="hidden" name="action" value="completePayment">
                                    
                                    <div class="mb-3">
                                        <label for="metodoPago" class="form-label">Método de Pago</label>
                                        <select class="form-select" id="metodoPago" name="metodoPago" required onchange="showPaymentDetails()">
                                            <option value="">Seleccione...</option>
                                            <option value="TARJETA">Tarjeta de Crédito/Débito</option>
                                            <option value="EFECTIVO">Efectivo</option>
                                            <option value="YAPE">Yape (Billetera Digital)</option>
                                            <option value="OTRO">Otro</option>
                                        </select>
                                    </div>

                                    <div id="paymentDetails" class="mt-3">
                                        <!-- Detalles específicos del método de pago se cargarán aquí con JS -->
                                    </div>

                                    <div class="d-grid mt-4">
                                        <button type="submit" class="btn btn-primary btn-lg" id="completePaymentBtn" disabled>
                                            <i class="fas fa-money-check-alt me-2"></i> Completar Pago
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <%-- Paso 1: Formulario de Creación de Pedido --%>
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                <i class="fas fa-file-invoice me-2"></i> Crear Nuevo Pedido
                            </div>
                            <div class="card-body">
                                <form action="VentaServlet" method="post" id="orderForm">
                                    <div class="mb-3">
                                        <label for="nombreCliente" class="form-label">Nombre del Cliente</label>
                                        <input type="text" class="form-control" id="nombreCliente" name="nombreCliente" value="<c:out value='${sessionScope.nombreCliente}'/>" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">Fecha</label>
                                        <input type="text" class="form-control" value="<%= new java.util.Date() %>" disabled>
                                    </div>
                                    <div class="mb-4">
                                        <label class="form-label">Código de Pedido</label>
                                        <input type="text" class="form-control" value="${sessionScope.codigoPedido != null ? sessionScope.codigoPedido : 'Automático'}" disabled>
                                    </div>

                                    <hr>

                                    <h4>Productos del Pedido</h4>
                                    <div class="mb-3">
                                        <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#selectProductModal">
                                            <i class="fas fa-plus-circle me-2"></i> Añadir Producto
                                        </button>
                                    </div>

                                    <div class="table-responsive mb-4">
                                        <table class="table table-hover" id="productosPedidoTable">
                                            <thead>
                                                <tr>
                                                    <th>Producto</th>
                                                    <th>Tipo</th>
                                                    <th>Variante</th>
                                                    <th>Precio Unitario</th>
                                                    <th>Cantidad</th>
                                                    <th>Subtotal</th>
                                                    <th>Acciones</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <!-- Los productos seleccionados se añadirán aquí con JavaScript -->
                                                <c:forEach var="detalle" items="${sessionScope.detallesVenta}">
                                                    <tr>
                                                        <td>
                                                            <input type="hidden" name="productoId" value="<c:out value='${detalle.id_producto}'/>">
                                                            <c:out value="${detalle.producto.nombre}"/>
                                                        </td>
                                                        <td><c:out value="${detalle.producto.tipo}"/></td>
                                                        <td><c:out value="${detalle.producto.variante}"/></td>
                                                        <td>S/. <span class="precio-unitario"><c:out value="${detalle.precio_unitario_venta}"/></span></td>
                                                        <td>
                                                            <input type="number" name="cantidad" class="form-control form-control-sm text-center" value="<c:out value='${detalle.cantidad}'/>" min="1" onchange="updateTotals()" style="width: 80px;">
                                                        </td>
                                                        <td>S/. <span class="subtotal-linea"><c:out value="${detalle.subtotal_linea}"/></span></td>
                                                        <td>
                                                            <button type="button" class="btn btn-sm btn-danger" onclick="removeProduct(this)"><i class="fas fa-trash"></i></button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                    <div class="row justify-content-end">
                                        <div class="col-md-5">
                                            <table class="table table-sm table-borderless">
                                                <tr>
                                                    <td><strong>Subtotal General:</strong></td>
                                                    <td class="text-end">S/. <span id="subtotalGeneral"><c:out value="${sessionScope.subtotalGeneral}"/></span></td>
                                                </tr>
                                                <tr>
                                                    <td><strong>IGV (18%):</strong></td>
                                                    <td class="text-end">S/. <span id="igvMonto"><c:out value="${sessionScope.igvMonto}"/></span></td>
                                                </tr>
                                                <tr>
                                                    <td><h4>Total:</h4></td>
                                                    <td class="text-end"><h4>S/. <span id="totalPagar"><c:out value="${sessionScope.total}"/></span></h4></td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>

                                    <div class="d-grid mt-4">
                                        <button type="submit" class="btn btn-primary btn-lg" id="processOrderBtn" disabled>
                                            <i class="fas fa-arrow-right me-2"></i> Procesar Pedido y Continuar al Pago
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Modal para Seleccionar Productos -->
    <div class="modal fade" id="selectProductModal" tabindex="-1" aria-labelledby="selectProductModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="selectProductModalLabel">Seleccionar Productos</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="text" class="form-control mb-3" id="searchProductInput" onkeyup="filterProductsModal()" placeholder="Buscar producto...">
                    <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                        <table class="table table-hover" id="modalProductosTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre</th>
                                    <th>Tipo</th>
                                    <th>Variante</th>
                                    <th>Precio</th>
                                    <th>Stock</th>
                                    <th>Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="prod" items="${productosDisponibles}">
                                    <tr>
                                        <td><c:out value="${prod.id_producto}"/></td>
                                        <td><c:out value="${prod.nombre}"/></td>
                                        <td><c:out value="${prod.tipo}"/></td>
                                        <td><c:out value="${prod.variante}"/></td>
                                        <td>S/. <span class="modal-precio-unitario"><c:out value="${prod.precio_unitario}"/></span></td>
                                        <td><span class="badge bg-secondary modal-stock"><c:out value="${prod.stock}"/></span></td>
                                        <td>
                                            <button type="button" class="btn btn-sm btn-success" onclick="addProductToOrder(this)" data-id="<c:out value='${prod.id_producto}'/>" data-nombre="<c:out value='${prod.nombre}'/>" data-tipo="<c:out value='${prod.tipo}'/>" data-variante="<c:out value='${prod.variante}'/>" data-precio="<c:out value='${prod.precio_unitario}'/>" data-stock="<c:out value='${prod.stock}'/>">
                                                Añadir
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS para Registro de Venta -->
    <script src="js/registroVenta.js"></script>
</body>
</html>
