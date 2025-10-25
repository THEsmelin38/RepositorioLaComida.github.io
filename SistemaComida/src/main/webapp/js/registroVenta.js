document.addEventListener('DOMContentLoaded', function() {
    // updateTotals(); // Calcular totales iniciales al cargar la página (ya no es necesario aquí, se llama en showPaymentDetails)
    // showPaymentDetails(); // Mostrar detalles de pago si es fase de pago (ya se llama al cargar el DOM)
});

const IGV_RATE = 0.18; // Tasa de IGV (18%)

// Función que se llama al cargar la página para inicializar los totales
// y los detalles de pago si estamos en la fase de pago.
document.addEventListener('DOMContentLoaded', function() {
    // Verificar si estamos en la fase de pago
    const isPaymentPhaseElement = document.getElementById('totalPagarEnPago');
    if (isPaymentPhaseElement) {
        // Estamos en la fase de pago, inicializar los detalles de pago
        showPaymentDetails();
        updateTotals(); // Actualizar totales para habilitar/deshabilitar botón de pago
    } else {
        // Estamos en la fase de creación de pedido
        updateTotals(); // Calcular totales iniciales al cargar la página
    }
});

function addProductToOrder(button) {
    const id = button.dataset.id;
    const nombre = button.dataset.nombre;
    const tipo = button.dataset.tipo;
    const variante = button.dataset.variante;
    const precio = parseFloat(button.dataset.precio);
    const stock = parseInt(button.dataset.stock);

    const tableBody = document.getElementById('productosPedidoTable').getElementsByTagName('tbody')[0];

    // Verificar si el producto ya está en la tabla
    let existingRow = null;
    const rows = tableBody.getElementsByTagName('tr');
    for (let i = 0; i < rows.length; i++) {
        const productIdInput = rows[i].querySelector('input[name="productoId"]');
        if (productIdInput && parseInt(productIdInput.value) === parseInt(id)) {
            existingRow = rows[i];
            break;
        }
    }

    if (existingRow) {
        // Si el producto ya existe, incrementar la cantidad
        const quantityInput = existingRow.querySelector('input[name="cantidad"]');
        let currentQuantity = parseInt(quantityInput.value);
        if (currentQuantity < stock) {
            quantityInput.value = currentQuantity + 1;
            updateTotals();
        } else {
            alert('No hay suficiente stock disponible para este producto.');
        }
    } else {
        // Si el producto no existe, añadir una nueva fila
        const newRow = tableBody.insertRow();
        newRow.innerHTML = `
            <td>
                <input type="hidden" name="productoId" value="${id}">
                ${nombre}
            </td>
            <td>${tipo}</td>
            <td>${variante}</td>
            <td>S/. <span class="precio-unitario">${precio.toFixed(2)}</span></td>
            <td>
                <input type="number" name="cantidad" class="form-control form-control-sm text-center" value="1" min="1" max="${stock}" onchange="updateTotals()" style="width: 80px;">
            </td>
            <td>S/. <span class="subtotal-linea">${precio.toFixed(2)}</span></td>
            <td>
                <button type="button" class="btn btn-sm btn-danger" onclick="removeProduct(this)"><i class="fas fa-trash"></i></button>
            </td>
        `;
        updateTotals();
    }
    
    // Cerrar el modal después de añadir un producto (opcional)
    // var modal = bootstrap.Modal.getInstance(document.getElementById('selectProductModal'));
    // modal.hide();
}

function removeProduct(button) {
    const row = button.closest('tr');
    row.remove();
    updateTotals();
}

function updateTotals() {
    console.log("updateTotals: Iniciando cálculo de totales.");
    let subtotalGeneral = 0;
    let isCreationPhase = true; // Asumimos fase de creación por defecto

    // Verificar si estamos en la fase de pago
    const totalPagarEnPagoElement = document.getElementById('totalPagarEnPago');
    if (totalPagarEnPagoElement) {
        isCreationPhase = false;
        // En fase de pago, el subtotal general ya viene de la sesión
        subtotalGeneral = parseFloat(totalPagarEnPagoElement.textContent) / (1 + IGV_RATE);
        console.log(`updateTotals (Pago): Subtotal General (calculado de total): ${subtotalGeneral}`);
    }

    if (isCreationPhase) {
        const tableBody = document.getElementById('productosPedidoTable');
        if (tableBody) {
            const rows = tableBody.getElementsByTagName('tr');
            for (let i = 0; i < rows.length; i++) {
                const quantityInput = rows[i].querySelector('input[name="cantidad"]');
                const precioUnitarioSpan = rows[i].querySelector('.precio-unitario');
                const subtotalLineaSpan = rows[i].querySelector('.subtotal-linea');

                if (quantityInput && precioUnitarioSpan && subtotalLineaSpan) {
                    let cantidad = parseInt(quantityInput.value);
                    let precioUnitario = parseFloat(precioUnitarioSpan.textContent);

                    // Asegurarse de que la cantidad no exceda el stock disponible
                    const maxStock = parseInt(quantityInput.max);
                    if (cantidad > maxStock) {
                        cantidad = maxStock;
                        quantityInput.value = maxStock;
                        alert('La cantidad no puede exceder el stock disponible (' + maxStock + ').');
                    }
                    if (cantidad < 1) {
                        cantidad = 1;
                        quantityInput.value = 1;
                    }

                    const subtotalLinea = precioUnitario * cantidad;
                    subtotalLineaSpan.textContent = subtotalLinea.toFixed(2);
                    subtotalGeneral += subtotalLinea;
                }
            }
        } else {
             console.warn("updateTotals (Creación): Tabla productosPedidoTable no encontrada.");
        }
    }

    const igvMonto = subtotalGeneral * IGV_RATE;
    const totalPagar = subtotalGeneral + igvMonto;

    // Solo actualizar los spans si están presentes (fase de creación)
    const subtotalGeneralSpan = document.getElementById('subtotalGeneral');
    const igvMontoSpan = document.getElementById('igvMonto');
    const totalPagarSpan = document.getElementById('totalPagar');

    if (subtotalGeneralSpan) subtotalGeneralSpan.textContent = subtotalGeneral.toFixed(2);
    if (igvMontoSpan) igvMontoSpan.textContent = igvMonto.toFixed(2);
    if (totalPagarSpan) totalPagarSpan.textContent = totalPagar.toFixed(2);

    // Habilitar/deshabilitar botón de procesar pedido (solo en fase de creación)
    const processOrderBtn = document.getElementById('processOrderBtn');
    if (processOrderBtn) {
        processOrderBtn.disabled = subtotalGeneral <= 0;
        console.log(`updateTotals (Creación): Botón Procesar Pedido deshabilitado: ${processOrderBtn.disabled}`);
    }

    // Habilitar/deshabilitar botón de completar pago (solo en fase de pago)
    const completePaymentBtn = document.getElementById('completePaymentBtn');
    if (completePaymentBtn) {
        const metodoPago = document.getElementById('metodoPago').value;
        console.log(`updateTotals (Pago): Subtotal General: ${subtotalGeneral}, Método de Pago: ${metodoPago}`); // Depuración
        completePaymentBtn.disabled = subtotalGeneral <= 0 || metodoPago === '';
        console.log(`updateTotals (Pago): Botón Completar Pago deshabilitado: ${completePaymentBtn.disabled}`); // Depuración
    }
}

function filterProductsModal() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchProductInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("modalProductosTable");
    tr = table.getElementsByTagName("tr");

    for (i = 1; i < tr.length; i++) { // Empieza en 1 para omitir el encabezado
        tr[i].style.display = "none";
        td = tr[i].getElementsByTagName("td");
        let found = false;
        for (var j = 1; j < td.length - 1; j++) { // Buscar en Nombre, Tipo, Variante (columnas 1, 2, 3)
            if (td[j]) {
                txtValue = td[j].textContent || td[j].innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            tr[i].style.display = "";
        }
    }
}

function showPaymentDetails() {
    const metodoPago = document.getElementById('metodoPago').value;
    const paymentDetailsDiv = document.getElementById('paymentDetails');
    let htmlContent = '';

    console.log(`showPaymentDetails: Método de Pago seleccionado: ${metodoPago}`); // Depuración

    switch (metodoPago) {
        case 'TARJETA':
            htmlContent = `
                <div class="mb-3">
                    <label for="numeroTarjeta" class="form-label">Número de Tarjeta</label>
                    <input type="text" class="form-control" id="numeroTarjeta" name="numeroTarjeta" placeholder="XXXX-XXXX-XXXX-XXXX" required>
                </div>
                <div class="mb-3">
                    <label for="nombreTarjeta" class="form-label">Nombre en la Tarjeta</label>
                    <input type="text" class="form-control" id="nombreTarjeta" name="nombreTarjeta" required>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="fechaExpiracion" class="form-label">Fecha de Expiración</label>
                        <input type="text" class="form-control" id="fechaExpiracion" name="fechaExpiracion" placeholder="MM/AA" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="cvv" class="form-label">CVV</label>
                        <input type="text" class="form-control" id="cvv" name="cvv" placeholder="XXX" required>
                    </div>
                </div>
            `;
            break;
        case 'EFECTIVO':
            htmlContent = `
                <div class="mb-3">
                    <label for="montoEntregado" class="form-label">Monto Entregado</label>
                    <input type="number" step="0.01" class="form-control" id="montoEntregado" name="montoEntregado" placeholder="Ej: 100.00" required>
                </div>
                <div class="mb-3">
                    <label for="cambio" class="form-label">Cambio</label>
                    <input type="text" class="form-control" id="cambio" name="cambio" disabled>
                </div>
            `;
            break;
        case 'YAPE':
            htmlContent = `
                <div class="mb-3">
                    <p>Escanea el código QR o transfiere al número <strong>+51 987 654 321</strong>.</p>
                    <img src="https://via.placeholder.com/150?text=QR+Yape" alt="Código QR Yape" class="img-fluid border p-2 rounded">
                </div>
                <div class="mb-3">
                    <label for="numeroOperacionYape" class="form-label">Número de Operación / Confirmación</label>
                    <input type="text" class="form-control" id="numeroOperacionYape" name="numeroOperacionYape" required>
                </div>
            `;
            break;
        case 'OTRO':
            htmlContent = `
                <div class="mb-3">
                    <label for="otroMetodo" class="form-label">Especifica otro método de pago</label>
                    <input type="text" class="form-control" id="otroMetodo" name="otroMetodo" required>
                </div>
            `;
            break;
        default:
            htmlContent = '';
            break;
    }
    paymentDetailsDiv.innerHTML = htmlContent;

    // Actualizar estado del botón de completar pago
    updateTotals(); 
}

// Event listener para calcular el cambio en efectivo
document.addEventListener('input', function(event) {
    if (event.target && event.target.id === 'montoEntregado') {
        const montoEntregado = parseFloat(event.target.value);
        // Obtener el total a pagar del span en la fase de pago
        const totalPagarElement = document.getElementById('totalPagarEnPago');
        const totalPagar = totalPagarElement ? parseFloat(totalPagarElement.textContent.replace('S/.', '').trim()) : 0;
        
        console.log(`Cálculo de cambio: Monto Entregado: ${montoEntregado}, Total a Pagar: ${totalPagar}`); // Depuración
        const cambio = montoEntregado - totalPagar;
        document.getElementById('cambio').value = isNaN(cambio) ? '' : cambio.toFixed(2);
        console.log(`Cálculo de cambio: Cambio: ${cambio}`); // Depuración
    }
});
