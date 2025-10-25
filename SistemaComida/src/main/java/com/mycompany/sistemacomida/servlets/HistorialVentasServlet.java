package com.mycompany.sistemacomida.servlets;

import com.mycompany.sistemacomida.dao.VentaDAO;
import com.mycompany.sistemacomida.model.Venta;
import com.mycompany.sistemacomida.model.Usuario;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "HistorialVentasServlet", urlPatterns = {"/HistorialVentasServlet"})
public class HistorialVentasServlet extends HttpServlet {

    private VentaDAO ventaDAO;

    @Override
    public void init() throws ServletException {
        super.init();
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
        if (action == null) {
            action = "list"; // Acción por defecto es listar ventas
        }

        switch (action) {
            case "list":
                listVentas(request, response);
                break;
            case "view":
                viewVenta(request, response);
                break;
            case "generatePdf":
                generatePdf(request, response);
                break;
            case "delete":
                deleteVenta(request, response);
                break;
            default:
                listVentas(request, response);
                break;
        }
    }

    private void listVentas(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Venta> listaVentas = ventaDAO.getAllVentas();
        request.setAttribute("listaVentas", listaVentas);
        request.getRequestDispatcher("historialVentas.jsp").forward(request, response);
    }

    private void viewVenta(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id_venta = Integer.parseInt(request.getParameter("id"));
        Venta venta = ventaDAO.getVentaById(id_venta);
        request.setAttribute("venta", venta);
        request.getRequestDispatcher("historialVentas.jsp").forward(request, response);
    }

    private void deleteVenta(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id_venta = Integer.parseInt(request.getParameter("id"));
        if (ventaDAO.deleteVenta(id_venta)) {
            request.setAttribute("message", "Venta eliminada correctamente.");
        } else {
            request.setAttribute("errorMessage", "Error al eliminar la venta.");
        }
        listVentas(request, response);
    }

    private void generatePdf(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id_venta = Integer.parseInt(request.getParameter("id"));
        Venta venta = ventaDAO.getVentaById(id_venta);

        if (venta == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Venta no encontrada.");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"pedido_" + venta.getCodigo_pedido() + ".pdf\"");

        try (PdfWriter writer = new PdfWriter(response.getOutputStream());
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Detalle del Pedido: " + venta.getCodigo_pedido()).setFontSize(20).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Cliente: " + venta.getNombre_cliente()));
            document.add(new Paragraph("Fecha: " + venta.getFecha_venta()));
            document.add(new Paragraph("Estado: " + venta.getEstado()));
            document.add(new Paragraph("Método de Pago: " + venta.getMetodo_pago()));
            document.add(new Paragraph("\n"));

            // Tabla de productos
            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1, 1}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell(new Paragraph("Producto").setBold());
            table.addHeaderCell(new Paragraph("Cantidad").setBold());
            table.addHeaderCell(new Paragraph("P. Unitario").setBold());
            table.addHeaderCell(new Paragraph("Subtotal").setBold());

            for (com.mycompany.sistemacomida.model.DetalleVenta detalle : venta.getDetalles()) {
                table.addCell(detalle.getProducto().getNombre() + " (" + detalle.getProducto().getVariante() + ")");
                table.addCell(String.valueOf(detalle.getCantidad()));
                table.addCell(String.format("S/. %.2f", detalle.getPrecio_unitario_venta()));
                table.addCell(String.format("S/. %.2f", detalle.getSubtotal_linea()));
            }
            document.add(table);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph(String.format("Subtotal General: S/. %.2f", venta.getSubtotal_general())).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph(String.format("IGV (18%%): S/. %.2f", venta.getIgv())).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph(String.format("Total a Pagar: S/. %.2f", venta.getTotal())).setFontSize(14).setBold().setTextAlignment(TextAlignment.RIGHT));

        } catch (Exception e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el PDF.");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestionar el historial de ventas";
    }
}
