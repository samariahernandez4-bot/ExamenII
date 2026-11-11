package controlador;

import modelo.Vehiculo;
import modelo.Cliente;
import repositorio.RepositorioGenerico;
import excepciones.ClienteNoEncontradoException;
import java.util.ArrayList;

public class ControladorVehiculos {

    private final RepositorioGenerico<Vehiculo> repositorio;

    public ControladorVehiculos() {
        repositorio = new RepositorioGenerico<>();
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException(
                "El vehiculo no puede estar vacio."
            );
        }

        if (repositorio.existe(vehiculo.getPlaca())) {
            throw new IllegalArgumentException(
                "Ya existe un vehiculo con esa placa."
            );
        }

        repositorio.registrar(vehiculo.getPlaca(), vehiculo);
    }

    public Vehiculo buscarVehiculo(String placa) {
        Vehiculo vehiculo = repositorio.buscar(placa);

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                "No se encontro el vehiculo con placa: " + placa
            );
        }

        return vehiculo;
    }

    public ArrayList<Vehiculo> listarVehiculos() {
        return repositorio.listar();
    }

    public void modificarVehiculo(String placa, Vehiculo nuevoVehiculo) {
        repositorio.modificar(placa, nuevoVehiculo);
    }

    public void eliminarVehiculo(String placa) {
        repositorio.eliminar(placa);
    }

    public int cantidadVehiculos() {
        return repositorio.cantidad();
    }

    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }

    public void verificarPropietario(
            String codigoCliente,
            ControladorClientes controladorClientes)
            throws ClienteNoEncontradoException {

        Cliente propietario = controladorClientes.buscarCliente(codigoCliente);

        if (propietario == null) {
            throw new ClienteNoEncontradoException(
                "El propietario no existe."
            );
        }
    }
}