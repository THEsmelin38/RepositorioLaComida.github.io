<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.mycompany.sistemacomida.model.Usuario" %>
<%@ page import="com.mycompany.sistemacomida.model.Venta" %>
<%@ page import="com.mycompany.sistemacomida.model.Producto" %>
<%@ page import="com.mycompany.sistemacomida.dao.VentaDAO" %>
<%@ page import="com.mycompany.sistemacomida.dao.ProductoDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Sistema de Comida</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- Custom CSS para Dashboard -->
    <link href="css/dashboard.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        .dashboard-container {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
            margin: 20px;
            overflow: hidden;
        }
        
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
            border: none;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        }
        
        .navbar-brand {
            font-weight: 700;
            font-size: 1.5rem;
            color: white !important;
        }
        
        .sidebar {
            background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);
            min-height: calc(100vh - 80px);
            box-shadow: 4px 0 20px rgba(0, 0, 0, 0.1);
        }
        
        .sidebar .nav-link {
            color: #ecf0f1;
            padding: 15px 20px;
            margin: 5px 0;
            border-radius: 10px;
            transition: all 0.3s ease;
            font-weight: 500;
        }
        
        .sidebar .nav-link:hover {
            background: rgba(255, 255, 255, 0.1);
            color: #fff;
            transform: translateX(5px);
        }
        
        .sidebar .nav-link.active {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }
        
        .content {
            background: #f8f9fa;
            min-height: calc(100vh - 80px);
            padding: 30px;
        }
        
        .welcome-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 20px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
        }
        
        .stats-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
            transition: all 0.3s ease;
            border: none;
            position: relative;
            overflow: hidden;
        }
        
        .stats-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #667eea, #764ba2);
        }
        
        .stats-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
        }
        
        .stats-icon {
            width: 60px;
            height: 60px;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            color: white;
            margin-bottom: 15px;
        }
        
        .stats-number {
            font-size: 2.5rem;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 5px;
        }
        
        .stats-label {
            color: #7f8c8d;
            font-weight: 500;
            font-size: 0.9rem;
        }
        
        .quick-actions {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
        }
        
        .action-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 12px;
            padding: 15px 25px;
            color: white;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            transition: all 0.3s ease;
            margin: 5px;
        }
        
        .action-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
            color: white;
        }
        
        .action-btn i {
            margin-right: 8px;
            font-size: 18px;
        }
        
        .chart-container {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
        }
        
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .fade-in {
            animation: fadeInUp 0.6s ease-out;
        }
    </style>
</head>
<body>
    <% 
        // Proteger la página: si no hay sesión, redirigir al login
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return; // Detener la ejecución del JSP
        }
        
        // Obtener estadísticas básicas del sistema
        VentaDAO ventaDAO = new VentaDAO();
        ProductoDAO productoDAO = new ProductoDAO();
        
        // Estadísticas básicas (usando métodos existentes)
        List<Venta> todasLasVentas = ventaDAO.getAllVentas();
        double totalVentasHoy = 0.0;
        int totalPedidosHoy = 0;
        
        // Calcular ventas de hoy manualmente
        LocalDate hoy = LocalDate.now();
        for (Venta venta : todasLasVentas) {
            if (venta.getFecha_venta() != null) {
                LocalDate fechaVenta = venta.getFecha_venta().toLocalDateTime().toLocalDate();
                if (fechaVenta.equals(hoy)) {
                    totalVentasHoy += venta.getTotal();
                    totalPedidosHoy++;
                }
            }
        }
        
        // Estadísticas de productos
        List<Producto> productos = productoDAO.getAllProductos();
        int totalProductos = productos.size();
        int totalStock = productos.stream().mapToInt(Producto::getStock).sum();
        
        // Calcular pedidos pendientes manualmente
        int pedidosPendientes = 0;
        for (Venta venta : todasLasVentas) {
            if ("PENDIENTE".equals(venta.getEstado())) {
                pedidosPendientes++;
            }
        }
        
        // Fecha actual formateada
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"));
    %>

    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="dashboard.jsp">
                <i class="fas fa-utensils me-2"></i>.
            </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="fas fa-user-circle me-2"></i>Bienvenido, <c:out value="${usuarioLogueado.username}"/>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                                <li><a class="dropdown-item" href="PerfilServlet"><i class="fas fa-user me-2"></i>Perfil</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="LogoutServlet"><i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión</a></li>
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
                    <a class="nav-link active" href="dashboard.jsp">
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
                <!-- Tarjeta de Bienvenida -->
                <div class="welcome-card fade-in">
                    <div class="row align-items-center">
                        <div class="col-md-8">
                            <h1 class="mb-2"><i class="fas fa-sun me-2"></i>¡Buenos días!</h1>
                            <h3 class="mb-3">Bienvenido de vuelta, <c:out value="${usuarioLogueado.username}"/></h3>
                            <p class="mb-0"><i class="fas fa-calendar me-2"></i><%= fechaActual %></p>
                        </div>
                        <div class="col-md-4 text-end">
                            <i class="fas fa-chart-line" style="font-size: 4rem; opacity: 0.3;"></i>
                        </div>
                    </div>
                </div>

                <!-- Estadísticas Principales -->
                <div class="row fade-in">
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="stats-card">
                            <div class="stats-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                                <i class="fas fa-dollar-sign"></i>
                            </div>
                            <div class="stats-number">S/. <%= String.format("%.2f", totalVentasHoy) %></div>
                            <div class="stats-label">Ventas de Hoy</div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="stats-card">
                            <div class="stats-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                                <i class="fas fa-shopping-bag"></i>
                            </div>
                            <div class="stats-number"><%= totalPedidosHoy %></div>
                            <div class="stats-label">Pedidos Completados</div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="stats-card">
                            <div class="stats-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                                <i class="fas fa-boxes"></i>
                            </div>
                            <div class="stats-number"><%= totalStock %></div>
                            <div class="stats-label">Unidades en Stock</div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="stats-card">
                            <div class="stats-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                                <i class="fas fa-clock"></i>
                            </div>
                            <div class="stats-number"><%= pedidosPendientes %></div>
                            <div class="stats-label">Pedidos Pendientes</div>
                        </div>
                    </div>
                </div>

                <!-- Acciones Rápidas -->
                <div class="row fade-in">
                    <div class="col-12">
                        <div class="quick-actions">
                            <h4 class="mb-4"><i class="fas fa-bolt me-2"></i>Acciones Rápidas</h4>
                            <div class="d-flex flex-wrap">
                                <a href="VentaServlet?action=create" class="action-btn">
                                    <i class="fas fa-plus"></i> Nueva Venta
                                </a>
                                <a href="AlmacenServlet?action=list" class="action-btn">
                                    <i class="fas fa-warehouse"></i> Gestionar Almacén
                                </a>
                                <a href="HistorialVentasServlet?action=list" class="action-btn">
                                    <i class="fas fa-chart-bar"></i> Ver Historial
                                </a>
                                <a href="AlmacenServlet?action=add" class="action-btn">
                                    <i class="fas fa-plus-circle"></i> Agregar Producto
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Información Adicional -->
                <div class="row fade-in">
                    <div class="col-md-6">
                        <div class="chart-container">
                            <h5 class="mb-3"><i class="fas fa-chart-pie me-2"></i>Resumen del Sistema</h5>
                            <div class="row text-center">
                                <div class="col-6">
                                    <h3 class="text-primary"><%= totalProductos %></h3>
                                    <p class="text-muted">Productos Registrados</p>
                                </div>
                                <div class="col-6">
                                    <h3 class="text-success"><%= totalPedidosHoy %></h3>
                                    <p class="text-muted">Pedidos Hoy</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="chart-container">
                            <h5 class="mb-3"><i class="fas fa-info-circle me-2"></i>Estado del Sistema</h5>
                            <div class="d-flex align-items-center mb-2">
                                <i class="fas fa-check-circle text-success me-2"></i>
                                <span>Sistema operativo</span>
                            </div>
                            <div class="d-flex align-items-center mb-2">
                                <i class="fas fa-database text-info me-2"></i>
                                <span>Base de datos conectada</span>
                            </div>
                            <div class="d-flex align-items-center">
                                <i class="fas fa-shield-alt text-warning me-2"></i>
                                <span>Sesión activa</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Font Awesome para iconos -->
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</body>
</html>
