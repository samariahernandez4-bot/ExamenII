package modelo;

import java.time.LocalDateTime;

public class Factura {

    // Atributos
    private final String numeroFactura;
    private final LocalDateTime fecha;
    private final OrdenTrabajo orden;

    private final double subtotalServicios;
    private final double subtotalRepuestos;
    private final double porcentajeImpuesto;
    private final double impuesto;
    private final double total;

    private double pagoRecibido;
    private double cambio;
    private boolean pagada;

    // Constructor
    public Factura(String numeroFactura, OrdenTrabajo orden,
                   double porcentajeImpuesto) {

        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El numero de factura es obligatorio.");
        }

        if (orden == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una orden.");
        }

        if (orden.getEstado() != OrdenTrabajo.Estado.FINALIZADA) {
            throw new IllegalArgumentException(
                    "Solo se pueden facturar ordenes finalizadas.");
        }

        if (porcentajeImpuesto < 0
                || Double.isNaN(porcentajeImpuesto)
                || Double.isInfinite(porcentajeImpuesto)) {
            throw new IllegalArgumentException(
                    "El porcentaje de impuesto no es valido.");
        }

        this.numeroFactura = numeroFactura.trim();
        this.fecha = LocalDateTime.now();
        this.orden = orden;

        this.subtotalServicios = orden.calcularSubtotalServicios();
        this.subtotalRepuestos = orden.calcularSubtotalRepuestos();

        this.porcentajeImpuesto = porcentajeImpuesto;

        double subtotal = subtotalServicios + subtotalRepuestos;

        this.impuesto = subtotal * (porcentajeImpuesto / 100);

        this.total = subtotal + impuesto;

        this.pagoRecibido = 0;
        this.cambio = 0;
        this.pagada = false;
    }

    // Getters
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public OrdenTrabajo getOrden() {
        return orden;
    }

    public double getSubtotalServicios() {
        return subtotalServicios;
    }

    public double getSubtotalRepuestos() {
        return subtotalRepuestos;
    }

    public double getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public double getTotal() {
        return total;
    }

    public double getPagoRecibido() {
        return pagoRecibido;
    }

    public double getCambio() {
        return cambio;
    }

    public boolean isPagada() {
        return pagada;
    }

    // Procesar pago en efectivo
    public void realizarPago(double pagoRecibido)
            throws excepciones.PagoInsuficienteException {

        if (pagada) {
            throw new IllegalStateException(
                    "Esta factura ya fue pagada.");
        }

        if (Double.isNaN(pagoRecibido)
                || Double.isInfinite(pagoRecibido)
                || pagoRecibido < 0) {
            throw new IllegalArgumentException(
                    "El pago debe ser un valor valido.");
        }

        if (pagoRecibido < total) {
            throw new excepciones.PagoInsuficienteException(
                    "Pago insuficiente. Total: L. " + total
                    + " | Recibido: L. " + pagoRecibido);
        }

        this.pagoRecibido = pagoRecibido;
        this.cambio = pagoRecibido - total;
        this.pagada = true;
    }

    // Mostrar factura
    @Override
    public String toString() {

        return """
               ========== AUTOTECH SOLUTIONS ==========
               FACTURA: """ + numeroFactura + "\n"
                + "Fecha: " + fecha + "\n"
                + "Orden: " + orden.getNumero() + "\n"
                + "Cliente: " + orden.getCliente().getNombreCompleto()
                + "\n"
                + "Vehiculo: " + orden.getVehiculo().getPlaca() + "\n"
                + "----------------------------------------\n"
                + "Subtotal servicios: L. " + subtotalServicios + "\n"
                + "Subtotal repuestos: L. " + subtotalRepuestos + "\n"
                + "Impuesto (" + porcentajeImpuesto + "%): L. "
                + impuesto + "\n"
                + "TOTAL: L. " + total + "\n"
                + "Pago recibido: L. " + pagoRecibido + "\n"
                + "Cambio: L. " + cambio + "\n"
                + "Estado de pago: "
                + (pagada ? "PAGADA" : "PENDIENTE") + "\n"
                + "========================================";
    }
}
