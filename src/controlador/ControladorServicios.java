package controlador;

import modelo.Servicio;
import repositorio.RepositorioGenerico;
import java.util.ArrayList;

public class ControladorServicios {

    private final RepositorioGenerico<Servicio> repositorio;

    public ControladorServicios() {
        repositorio = new RepositorioGenerico<>();
    }

    public void registrarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException(
                "El servicio no puede estar vacio."
            );
        }

        repositorio.registrar(servicio.getCodigo(), servicio);
    }

    public Servicio buscarServicio(String codigo) {
        Servicio servicio = repositorio.buscar(codigo);

        if (servicio == null) {
            throw new IllegalArgumentException(
                "No se encontro el servicio con codigo: " + codigo
            );
        }

        return servicio;
    }

    public ArrayList<Servicio> listarServicios() {
        return repositorio.listar();
    }

    public void modificarServicio(String codigo, Servicio nuevoServicio) {
        repositorio.modificar(codigo, nuevoServicio);
    }

    public void eliminarServicio(String codigo) {
        repositorio.eliminar(codigo);
    }

    public int cantidadServicios() {
        return repositorio.cantidad();
    }

    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }
}
