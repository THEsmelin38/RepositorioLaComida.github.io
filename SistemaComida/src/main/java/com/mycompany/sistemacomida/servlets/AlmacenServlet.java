package com.mycompany.sistemacomida.servlets;

import com.mycompany.sistemacomida.dao.ProductoDAO;
import com.mycompany.sistemacomida.model.Producto;
import com.mycompany.sistemacomida.model.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AlmacenServlet", urlPatterns = {"/AlmacenServlet"})
public class AlmacenServlet extends HttpServlet {

    private ProductoDAO productoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        productoDAO = new ProductoDAO();
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
        if (action == null) {
            action = "list"; // Acción por defecto es listar productos
        }

        switch (action) {
            case "list":
                listProductos(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteProducto(request, response);
                break;
            default:
                listProductos(request, response);
                break;
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
        if (action == null) {
            action = "add"; // Acción por defecto para POST es añadir
        }

        switch (action) {
            case "add":
                addProducto(request, response);
                break;
            case "update":
                updateProducto(request, response);
                break;
            case "updateStock":
                updateStock(request, response);
                break;
            default:
                listProductos(request, response);
                break;
        }
    }

    private void listProductos(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Producto> listaProductos = productoDAO.getAllProductos();
        request.setAttribute("listaProductos", listaProductos);
        request.getRequestDispatcher("almacen.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id_producto = Integer.parseInt(request.getParameter("id"));
        Producto productoExistente = productoDAO.getProductoById(id_producto);
        request.setAttribute("producto", productoExistente);
        request.getRequestDispatcher("almacen.jsp").forward(request, response);
    }

    private void addProducto(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String tipo = request.getParameter("tipo");
        String variante = request.getParameter("variante");
        
        // Validar y convertir precio_unitario
        String precioUnitarioStr = request.getParameter("precio_unitario");
        System.out.println("AlmacenServlet - Recibido precio_unitario: " + precioUnitarioStr); // <--- Línea de depuración
        double precio_unitario = 0.0;
        if (precioUnitarioStr != null && !precioUnitarioStr.trim().isEmpty()) {
            try {
                precio_unitario = Double.parseDouble(precioUnitarioStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "El precio unitario no es un número válido.");
                listProductos(request, response);
                return;
            }
        } else {
            request.setAttribute("errorMessage", "El precio unitario no puede estar vacío.");
            listProductos(request, response);
            return;
        }
        
        // Validar y convertir stock
        String stockStr = request.getParameter("stock");
        System.out.println("AlmacenServlet - Recibido stock: " + stockStr); // <--- Línea de depuración
        int stock = 0;
        if (stockStr != null && !stockStr.trim().isEmpty()) {
            try {
                stock = Integer.parseInt(stockStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "El stock no es un número entero válido.");
                listProductos(request, response);
                return;
            }
        } else {
            request.setAttribute("errorMessage", "El stock no puede estar vacío.");
            listProductos(request, response);
            return;
        }
        
        String imagen_url = request.getParameter("imagen_url");

        Producto nuevoProducto = new Producto(nombre, tipo, variante, precio_unitario, stock, imagen_url);
        if (productoDAO.addProducto(nuevoProducto)) {
            request.setAttribute("message", "Producto añadido correctamente.");
        } else {
            request.setAttribute("errorMessage", "Error al añadir el producto.");
        }
        listProductos(request, response);
    }

    private void updateProducto(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id_producto = Integer.parseInt(request.getParameter("id_producto"));
        String nombre = request.getParameter("nombre");
        String tipo = request.getParameter("tipo");
        String variante = request.getParameter("variante");
        
        // Validar y convertir precio_unitario
        String precioUnitarioStr = request.getParameter("precio_unitario");
        System.out.println("AlmacenServlet - Recibido precio_unitario (UPDATE): " + precioUnitarioStr); // <--- Línea de depuración
        double precio_unitario = 0.0;
        if (precioUnitarioStr != null && !precioUnitarioStr.trim().isEmpty()) {
            try {
                precio_unitario = Double.parseDouble(precioUnitarioStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "El precio unitario no es un número válido.");
                showEditForm(request, response); // Mostrar el formulario de edición de nuevo con el error
                return;
            }
        } else {
            request.setAttribute("errorMessage", "El precio unitario no puede estar vacío.");
            showEditForm(request, response); // Mostrar el formulario de edición de nuevo con el error
            return;
        }
        
        // Validar y convertir stock
        String stockStr = request.getParameter("stock");
        System.out.println("AlmacenServlet - Recibido stock (UPDATE): " + stockStr); // <--- Línea de depuración
        int stock = 0;
        if (stockStr != null && !stockStr.trim().isEmpty()) {
            try {
                stock = Integer.parseInt(stockStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "El stock no es un número entero válido.");
                showEditForm(request, response); // Mostrar el formulario de edición de nuevo con el error
                return;
            }
        } else {
            request.setAttribute("errorMessage", "El stock no puede estar vacío.");
            showEditForm(request, response); // Mostrar el formulario de edición de nuevo con el error
            return;
        }
        
        String imagen_url = request.getParameter("imagen_url");

        Producto producto = new Producto(id_producto, nombre, tipo, variante, precio_unitario, stock, imagen_url, null, null);
        if (productoDAO.updateProducto(producto)) {
            request.setAttribute("message", "Producto actualizado correctamente.");
        } else {
            request.setAttribute("errorMessage", "Error al actualizar el producto.");
        }
        response.sendRedirect("AlmacenServlet?action=list"); // Redirigir para evitar doble envío
    }

    private void deleteProducto(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id_producto = Integer.parseInt(request.getParameter("id"));
        if (productoDAO.deleteProducto(id_producto)) {
            request.setAttribute("message", "Producto eliminado correctamente.");
        } else {
            request.setAttribute("errorMessage", "Error al eliminar el producto.");
        }
        listProductos(request, response);
    }

    private void updateStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id_producto = Integer.parseInt(request.getParameter("id_producto"));
        
        // Validar y convertir cantidad
        String cantidadStr = request.getParameter("cantidad");
        System.out.println("AlmacenServlet - Recibido cantidad (UPDATE STOCK): " + cantidadStr); // <--- Línea de depuración
        int cantidad = 0;
        if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
            try {
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "La cantidad a añadir no es un número entero válido.");
                listProductos(request, response); // Mantener en la lista de productos con el error
                return;
            }
        } else {
            request.setAttribute("errorMessage", "La cantidad a añadir no puede estar vacía.");
            listProductos(request, response); // Mantener en la lista de productos con el error
            return;
        }
        
        if (productoDAO.updateStock(id_producto, cantidad)) {
            request.setAttribute("message", "Stock actualizado correctamente.");
        } else {
            request.setAttribute("errorMessage", "Error al actualizar el stock.");
        }
        response.sendRedirect("AlmacenServlet?action=list"); // Redirigir para evitar doble envío
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestionar operaciones de Almacén";
    }
}
