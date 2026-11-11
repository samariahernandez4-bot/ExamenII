package controlador;

import modelo.Repuesto;
import repositorio.RepositorioGenerico;
import java.util.ArrayList;

public class ControladorRepuestos {

    private final RepositorioGenerico<Repuesto> repositorio;

    public ControladorRepuestos() {
        repositorio = new RepositorioGenerico<>();
    }

    public void registrarRepuesto(Repuesto repuesto) {
        if (repuesto == null) {
            throw new IllegalArgumentException(
                "El repuesto no puede estar vacio."
            );
        }

        repositorio.registrar(repuesto.getCodigo(), repuesto);
    }

    public Repuesto buscarRepuesto(String codigo) {
        Repuesto repuesto = repositorio.buscar(codigo);

        if (repuesto == null) {
            throw new IllegalArgumentException(
                "No se encontro el repuesto con codigo: " + codigo
            );
        }

        return repuesto;
    }

    public ArrayList<Repuesto> listarRepuestos() {
        return repositorio.listar();
    }

    public void modificarRepuesto(String codigo, Repuesto nuevoRepuesto) {
        repositorio.modificar(codigo, nuevoRepuesto);
    }

    public void eliminarRepuesto(String codigo) {
        repositorio.eliminar(codigo);
    }

    public void agregarExistencias(String codigo, int cantidad) {
        Repuesto repuesto = buscarRepuesto(codigo);
        repuesto.agregarCantidad(cantidad);
    }

    public void descontarExistencias(String codigo, int cantidad) {
        Repuesto repuesto = buscarRepuesto(codigo);
        repuesto.descontarCantidad(cantidad);
    }

    public ArrayList<Repuesto> listarRepuestosConStockBajo() {
        ArrayList<Repuesto> resultado = new ArrayList<>();

        for (Repuesto repuesto : repositorio.listar()) {
            if (repuesto.tieneStockBajo()) {
                resultado.add(repuesto);
            }
        }

        return resultado;
    }

    public int cantidadRepuestos() {
        return repositorio.cantidad();
    }

    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }
}
