package com.mycompany.sistemacomida.servlets;

import com.mycompany.sistemacomida.dao.ProductoDAO;
import com.mycompany.sistemacomida.dao.VentaDAO;
import com.mycompany.sistemacomida.model.Producto;
import com.mycompany.sistemacomida.model.DetalleVenta;
import com.mycompany.sistemacomida.model.Venta;
import com.mycompany.sistemacomida.model.Usuario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "VentaServlet", urlPatterns = {"/VentaServlet"})
public class VentaServlet extends HttpServlet {

    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;
    private static final double IGV_RATE = 0.18; // 18% de IGV

    @Override
    public void init() throws ServletException {
        super.init();
        productoDAO = new ProductoDAO();
        ventaDAO = new VentaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Verificar sesión de usuario
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.equals("create")) {
            showCreateOrderForm(request, response);
        } else if (action.equals("confirmPayment")) {
            showConfirmPayment(request, response);
        } else {
            showCreateOrderForm(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar sesión de usuario
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) { // Acción por defecto es procesar el pedido
            processOrder(request, response);
        } else if (action.equals("completePayment")) {
            completePayment(request, response);
        } else {
            processOrder(request, response);
        }
    }

    private void showCreateOrderForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Producto> productosDisponibles = productoDAO.getAllProductos();
        request.setAttribute("productosDisponibles", productosDisponibles);
        request.getRequestDispatcher("registroVenta.jsp").forward(request, response);
    }

    private void processOrder(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String nombreCliente = request.getParameter("nombreCliente");
        System.out.println("VentaServlet - Recibido nombreCliente: " + nombreCliente); // Depuración
        // Generar un código de pedido único
        String codigoPedido = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); 

        List<DetalleVenta> detallesVenta = new ArrayList<>();
        double subtotalGeneral = 0;

        String[] productoIds = request.getParameterValues("productoId");
        String[] cantidades = request.getParameterValues("cantidad");
        
        System.out.println("VentaServlet - Recibido productoIds: " + (productoIds != null ? String.join(", ", productoIds) : "null")); // Depuración
        System.out.println("VentaServlet - Recibido cantidades: " + (cantidades != null ? String.join(", ", cantidades) : "null")); // Depuración

        if (productoIds != null && cantidades != null) {
            for (int i = 0; i < productoIds.length; i++) {
                try {
                    int idProducto = Integer.parseInt(productoIds[i]);
                    int cantidad = Integer.parseInt(cantidades[i]);

                    if (cantidad > 0) {
                        Producto producto = productoDAO.getProductoById(idProducto);
                        System.out.println("VentaServlet - Producto encontrado para ID " + idProducto + ": " + (producto != null ? producto.getNombre() : "null")); // Depuración
                        System.out.println("VentaServlet - Stock del producto " + (producto != null ? producto.getNombre() : "") + ": " + (producto != null ? producto.getStock() : "N/A")); // Depuración
                        if (producto != null && producto.getStock() >= cantidad) {
                            double precioUnitarioVenta = producto.getPrecio_unitario();
                            double subtotalLinea = precioUnitarioVenta * cantidad;
                            subtotalGeneral += subtotalLinea;

                            DetalleVenta detalle = new DetalleVenta();
                            detalle.setId_producto(idProducto);
                            detalle.setCantidad(cantidad);
                            detalle.setPrecio_unitario_venta(precioUnitarioVenta);
                            detalle.setSubtotal_linea(subtotalLinea);
                            detalle.setProducto(producto); // Para mostrar en el JSP
                            detallesVenta.add(detalle);
                            System.out.println("VentaServlet - Detalle de venta añadido para producto " + producto.getNombre()); // Depuración
                        } else {
                            request.setAttribute("errorMessage", "Stock insuficiente para el producto: " + (producto != null ? producto.getNombre() : "Desconocido"));
                            showCreateOrderForm(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("errorMessage", "La cantidad para un producto debe ser mayor a 0.");
                        showCreateOrderForm(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Error en el formato de número de producto o cantidad.");
                    showCreateOrderForm(request, response);
                    return;
                }
            }
        }
        System.out.println("VentaServlet - Detalles de venta finalizados. Cantidad de detalles: " + detallesVenta.size()); // Depuración

        if (detallesVenta.isEmpty()) {
            request.setAttribute("errorMessage", "No se han seleccionado productos para el pedido.");
            showCreateOrderForm(request, response);
            return;
        }

        double igvMonto = subtotalGeneral * IGV_RATE;
        double total = subtotalGeneral + igvMonto;

        // Almacenar los datos del pedido en la sesión temporalmente para la confirmación de pago
        HttpSession session = request.getSession();
        session.setAttribute("nombreCliente", nombreCliente);
        session.setAttribute("codigoPedido", codigoPedido);
        session.setAttribute("detallesVenta", detallesVenta);
        session.setAttribute("subtotalGeneral", subtotalGeneral);
        session.setAttribute("igvMonto", igvMonto);
        session.setAttribute("total", total);

        System.out.println("VentaServlet - Realizando redirección a confirmPayment."); // Depuración final antes de redirigir
        response.sendRedirect("VentaServlet?action=confirmPayment");
    }

    private void showConfirmPayment(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("VentaServlet - Entrando a showConfirmPayment. Atributos de sesión: ");
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("  nombreCliente: " + session.getAttribute("nombreCliente"));
            System.out.println("  codigoPedido: " + session.getAttribute("codigoPedido"));
            System.out.println("  total: " + session.getAttribute("total"));
            // También podrías imprimir detallesVenta, pero es más complejo.
        }
        // Los datos del pedido ya están en la sesión, simplemente reenviar a la vista de confirmación
        request.getRequestDispatcher("registroVenta.jsp").forward(request, response);
    }

    private void completePayment(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        System.out.println("VentaServlet - Entrando a completePayment."); // Depuración
        
        String nombreCliente = (String) session.getAttribute("nombreCliente");
        String codigoPedido = (String) session.getAttribute("codigoPedido");
        List<DetalleVenta> detallesVenta = (List<DetalleVenta>) session.getAttribute("detallesVenta");
        double subtotalGeneral = (Double) session.getAttribute("subtotalGeneral");
        double igvMonto = (Double) session.getAttribute("igvMonto");
        double total = (Double) session.getAttribute("total");
        String metodoPago = request.getParameter("metodoPago");
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        System.out.println("VentaServlet - completePayment - Recuperando de sesión:"); // Depuración
        System.out.println("  nombreCliente: " + nombreCliente);
        System.out.println("  codigoPedido: " + codigoPedido);
        System.out.println("  total: " + total);
        System.out.println("  metodoPago: " + metodoPago);
        System.out.println("  usuarioLogueado ID: " + (usuarioLogueado != null ? usuarioLogueado.getId_usuario() : "null"));
        System.out.println("  detallesVenta size: " + (detallesVenta != null ? detallesVenta.size() : "null"));
        
        if (nombreCliente == null || codigoPedido == null || detallesVenta == null || usuarioLogueado == null) {
            request.setAttribute("errorMessage", "No se encontraron datos de pedido en la sesión. Por favor, intente de nuevo.");
            response.sendRedirect("VentaServlet?action=create");
            System.out.println("VentaServlet - completePayment - Error: Datos de sesión incompletos."); // Depuración
            return;
        }

        Venta nuevaVenta = new Venta(codigoPedido, nombreCliente, subtotalGeneral, igvMonto, total, "COMPLETADO", metodoPago, usuarioLogueado.getId_usuario());
        nuevaVenta.setDetalles(detallesVenta);

        System.out.println("VentaServlet - completePayment - Intentando añadir venta a DB."); // Depuración
        int idVentaGenerada = ventaDAO.addVenta(nuevaVenta);
        System.out.println("VentaServlet - completePayment - ID de venta generada: " + idVentaGenerada); // Depuración

        if (idVentaGenerada != -1) {
            System.out.println("VentaServlet - completePayment - Venta guardada, actualizando stock."); // Depuración
            // Actualizar stock de productos
            for (DetalleVenta detalle : detallesVenta) {
                productoDAO.updateStock(detalle.getId_producto(), -detalle.getCantidad()); // Restar cantidad del stock
                System.out.println("  - Stock actualizado para producto ID " + detalle.getId_producto() + ", cantidad: " + detalle.getCantidad()); // Depuración
            }
            // Limpiar atributos de sesión del pedido
            session.removeAttribute("nombreCliente");
            session.removeAttribute("codigoPedido");
            session.removeAttribute("detallesVenta");
            session.removeAttribute("subtotalGeneral");
            session.removeAttribute("igvMonto");
            session.removeAttribute("total");
            System.out.println("VentaServlet - completePayment - Atributos de sesión limpiados."); // Depuración

            request.setAttribute("message", "¡Pedido " + codigoPedido + " completado con éxito!");
            System.out.println("VentaServlet - completePayment - Redirigiendo a historialVentas.jsp."); // Depuración
            // Redirigir al historial de ventas o a una página de confirmación
            request.getRequestDispatcher("historialVentas.jsp").forward(request, response); // Asumiendo que tendremos un historialVentas.jsp

        } else {
            request.setAttribute("errorMessage", "Error al guardar la venta. Intente de nuevo.");
            System.out.println("VentaServlet - completePayment - Error: Fallo al guardar la venta en DB."); // Depuración
            showConfirmPayment(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestionar el registro y procesamiento de ventas";
    }
}
