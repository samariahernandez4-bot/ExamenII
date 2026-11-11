package controlador;

import modelo.Factura;
import modelo.OrdenTrabajo;
import repositorio.RepositorioGenerico;
import excepciones.PagoInsuficienteException;
import java.util.ArrayList;

public class ControladorFacturas {

    private final RepositorioGenerico<Factura> repositorio;
    private final ControladorOrdenes controladorOrdenes;

    public ControladorFacturas(ControladorOrdenes controladorOrdenes) {
        this.repositorio = new RepositorioGenerico<>();
        this.controladorOrdenes = controladorOrdenes;
    }

    public Factura generarFactura(
            String numeroFactura,
            String numeroOrden,
            double porcentajeImpuesto) {

        OrdenTrabajo orden = controladorOrdenes.buscarOrden(numeroOrden);

        for (Factura factura : repositorio.listar()) {
          if (factura.getOrden().getNumero()
        .equals(numeroOrden)) {

                throw new IllegalArgumentException(
                    "Esta orden ya tiene una factura registrada."
                );
            }
        }

        Factura factura = new Factura(
                numeroFactura,
                orden,
                porcentajeImpuesto
        );

        repositorio.registrar(numeroFactura, factura);

        return factura;
    }

    public Factura buscarFactura(String numeroFactura) {
        Factura factura = repositorio.buscar(numeroFactura);

        if (factura == null) {
            throw new IllegalArgumentException(
                "No se encontro la factura: " + numeroFactura
            );
        }

        return factura;
    }

    public ArrayList<Factura> listarFacturas() {
        return repositorio.listar();
    }

    public void registrarPago(
            String numeroFactura,
            double pago)
            throws PagoInsuficienteException {

        Factura factura = buscarFactura(numeroFactura);
        factura.realizarPago(pago);
    }

    public int cantidadFacturas() {
        return repositorio.cantidad();
    }

    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }

    public double calcularIngresosTotales() {
        double ingresos = 0;

        for (Factura factura : repositorio.listar()) {
            if (factura.isPagada()) {
                ingresos += factura.getTotal();
            }
        }

        return ingresos;
    }
}