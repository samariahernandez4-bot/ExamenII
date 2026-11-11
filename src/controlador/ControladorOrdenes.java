package controlador;

import modelo.OrdenTrabajo;
import modelo.Cliente;
import modelo.Vehiculo;
import modelo.Servicio;
import modelo.Repuesto;
import repositorio.RepositorioGenerico;
import excepciones.ClienteNoEncontradoException;
import java.util.ArrayList;

public class ControladorOrdenes {

    private final RepositorioGenerico<OrdenTrabajo> repositorio;

    private final ControladorClientes controladorClientes;
    private final ControladorVehiculos controladorVehiculos;
    private final ControladorServicios controladorServicios;
    private final ControladorRepuestos controladorRepuestos;

    public ControladorOrdenes(
            ControladorClientes clientes,
            ControladorVehiculos vehiculos,
            ControladorServicios servicios,
            ControladorRepuestos repuestos) {

        repositorio = new RepositorioGenerico<>();

        this.controladorClientes = clientes;
        this.controladorVehiculos = vehiculos;
        this.controladorServicios = servicios;
        this.controladorRepuestos = repuestos;
    }

    public void registrarOrden(OrdenTrabajo orden) {
        if (orden == null) {
            throw new IllegalArgumentException(
                "La orden no puede estar vacia."
            );
        }

        repositorio.registrar(orden.getNumero(), orden);
    }

    public OrdenTrabajo buscarOrden(String numero) {
        OrdenTrabajo orden = repositorio.buscar(numero);

        if (orden == null) {
            throw new IllegalArgumentException(
                "No se encontro la orden: " + numero
            );
        }

        return orden;
    }

    public ArrayList<OrdenTrabajo> listarOrdenes() {
        return repositorio.listar();
    }

    public void crearOrden(
            String numero,
            String codigoCliente,
            String placaVehiculo,
            String diagnostico,
            String mecanico)
            throws ClienteNoEncontradoException {

        Cliente cliente = controladorClientes.buscarCliente(codigoCliente);

        Vehiculo vehiculo = controladorVehiculos.buscarVehiculo(placaVehiculo);

        if (!vehiculo.getPropietario().getCodigo().equals(
                cliente.getCodigo())) {

            throw new IllegalArgumentException(
                "El vehiculo no pertenece al cliente seleccionado."
            );
        }

        OrdenTrabajo orden = new OrdenTrabajo(
                numero,
                cliente,
                vehiculo,
                diagnostico,
                mecanico
        );

        registrarOrden(orden);
    }

    public void agregarServicio(String numeroOrden, String codigoServicio) {
        OrdenTrabajo orden = buscarOrden(numeroOrden);

        Servicio servicio = controladorServicios.buscarServicio(codigoServicio);

        orden.agregarServicio(servicio);
    }

    public void agregarRepuesto(
            String numeroOrden,
            String codigoRepuesto,
            int cantidad) {

        OrdenTrabajo orden = buscarOrden(numeroOrden);

        Repuesto repuesto = controladorRepuestos.buscarRepuesto(codigoRepuesto);

        orden.agregarRepuesto(repuesto, cantidad);
    }

    public void iniciarOrden(String numero) {
        buscarOrden(numero).iniciarProceso();
    }

    public void finalizarOrden(String numero) {
        buscarOrden(numero).finalizar();
    }

    public void cancelarOrden(String numero) {
        buscarOrden(numero).cancelar();
    }

    public double calcularTotal(String numero) {
        return buscarOrden(numero).calcularCostoTotal();
    }

    public int cantidadOrdenes() {
        return repositorio.cantidad();
    }

    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }
}