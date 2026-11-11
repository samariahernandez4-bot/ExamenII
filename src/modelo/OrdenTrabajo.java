package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrdenTrabajo {

    // Estados de una orden
    public enum Estado {
        PENDIENTE,
        EN_PROCESO,
        FINALIZADA,
        CANCELADA
    }

    // Atributos
    private final String numero;
    private final LocalDateTime fecha;
    private final Cliente cliente;
    private final Vehiculo vehiculo;

    private String diagnostico;
    private String mecanico;

    private final ArrayList<Servicio> servicios;
    private final LinkedHashMap<Repuesto, Integer> repuestos;

    private Estado estado;
    private boolean inventarioDescontado;

    // Constructor
    public OrdenTrabajo(String numero, Cliente cliente,
                        Vehiculo vehiculo, String diagnostico,
                        String mecanico) {

        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El numero de orden es obligatorio.");
        }

        if (cliente == null || vehiculo == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un cliente y un vehiculo.");
        }

        if (!vehiculo.getPropietario().getCodigo()
                .equals(cliente.getCodigo())) {
            throw new IllegalArgumentException(
                    "El vehiculo no pertenece al cliente seleccionado.");
        }

        if (diagnostico == null || diagnostico.trim().isEmpty()
                || mecanico == null || mecanico.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El diagnostico y el mecanico son obligatorios.");
        }

        this.numero = numero.trim();
        this.fecha = LocalDateTime.now();
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.diagnostico = diagnostico.trim();
        this.mecanico = mecanico.trim();

        this.servicios = new ArrayList<>();
        this.repuestos = new LinkedHashMap<>();

        this.estado = Estado.PENDIENTE;
        this.inventarioDescontado = false;
    }

    // Getters
    public String getNumero() {
        return numero;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getMecanico() {
        return mecanico;
    }

    public Estado getEstado() {
        return estado;
    }

    public boolean isInventarioDescontado() {
        return inventarioDescontado;
    }

    public List<Servicio> getServicios() {
        return Collections.unmodifiableList(servicios);
    }

    public Map<Repuesto, Integer> getRepuestos() {
        return Collections.unmodifiableMap(repuestos);
    }

    // Modificar diagnóstico
    public void setDiagnostico(String diagnostico) {
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El diagnostico es obligatorio.");
        }

        this.diagnostico = diagnostico.trim();
    }

    // Modificar mecánico
    public void setMecanico(String mecanico) {
        if (mecanico == null || mecanico.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del mecánico es obligatorio.");
        }

        this.mecanico = mecanico.trim();
    }

    // Agregar un servicio
    public void agregarServicio(Servicio servicio) {
        if (estado != Estado.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden modificar ordenes pendientes.");
        }

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio no puede ser nulo.");
        }

        servicios.add(servicio);
    }

    // Agregar repuestos a la orden
    public void agregarRepuesto(Repuesto repuesto, int cantidad) {
        if (estado != Estado.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden modificar ordenes pendientes.");
        }

        if (repuesto == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "Repuesto o cantidad invalidos.");
        }

        int cantidadAnterior = repuestos.getOrDefault(repuesto, 0);

        repuestos.put(repuesto,
                Math.addExact(cantidadAnterior, cantidad));
    }

    // Calcular subtotal de servicios
    public double calcularSubtotalServicios() {
        double subtotal = 0;

        for (Servicio servicio : servicios) {
            subtotal += servicio.getPrecioBase();
        }

        return subtotal;
    }

    // Calcular subtotal de repuestos
    public double calcularSubtotalRepuestos() {
        double subtotal = 0;

        for (Map.Entry<Repuesto, Integer> entrada
                : repuestos.entrySet()) {

            subtotal += entrada.getKey().getPrecioUnitario()
                    * entrada.getValue();
        }

        return subtotal;
    }

    // Calcular costo total
    public double calcularCostoTotal() {
        return calcularSubtotalServicios()
                + calcularSubtotalRepuestos();
    }

    // Cambiar orden a EN_PROCESO
    public void iniciarProceso() {
        if (estado != Estado.PENDIENTE) {
            throw new IllegalStateException(
                    "La orden no esta pendiente.");
        }

        if (servicios.isEmpty()) {
            throw new IllegalStateException(
                    "La orden debe tener al menos un servicio.");
        }

        estado = Estado.EN_PROCESO;
    }

    // Finalizar y descontar inventario una sola vez
    public void finalizar() {

        if (estado != Estado.EN_PROCESO) {
            throw new IllegalStateException(
                    "La orden debe estar en proceso para finalizar.");
        }

        if (inventarioDescontado) {
            throw new IllegalStateException(
                    "El inventario ya fue descontado.");
        }

        // Primero comprobar todas las existencias
        for (Map.Entry<Repuesto, Integer> entrada
                : repuestos.entrySet()) {

            if (entrada.getValue() > entrada.getKey().getCantidad()) {
                throw new IllegalStateException(
                        "Stock insuficiente para: "
                        + entrada.getKey().getNombre());
            }
        }

        // Descontar después de comprobar todas las existencias
        for (Map.Entry<Repuesto, Integer> entrada
                : repuestos.entrySet()) {

            entrada.getKey().descontarCantidad(entrada.getValue());
        }

        inventarioDescontado = true;
        estado = Estado.FINALIZADA;
    }

    // Cancelar orden
    public void cancelar() {
        if (estado == Estado.FINALIZADA
                || estado == Estado.CANCELADA) {
            throw new IllegalStateException(
                    "No se puede cancelar esta orden.");
        }

        estado = Estado.CANCELADA;
    }

    // Información de la orden
    @Override
    public String toString() {
        return "Orden: " + numero
                + " | Cliente: " + cliente.getNombreCompleto()
                + " | Vehiculo: " + vehiculo.getPlaca()
                + " | Estado: " + estado
                + " | Total: L. " + calcularCostoTotal();
    }
}