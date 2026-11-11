package controlador;

import modelo.Cliente;
import repositorio.RepositorioGenerico;
import excepciones.ClienteNoEncontradoException;
import java.util.ArrayList;

public class ControladorClientes {

    private final RepositorioGenerico<Cliente> repositorio;

    public ControladorClientes() {
        repositorio = new RepositorioGenerico<>();
    }

    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede estar vacio.");
        }

        for (Cliente existente : repositorio.listar()) {
            if (existente.getIdentidad().equals(cliente.getIdentidad())) {
                throw new IllegalArgumentException(
                    "Ya existe un cliente con esa identidad."
                );
            }
        }

        repositorio.registrar(cliente.getCodigo(), cliente);
    }

    public Cliente buscarCliente(String codigo)
            throws ClienteNoEncontradoException {

        Cliente cliente = repositorio.buscar(codigo);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(
                "No se encontro el cliente con codigo: " + codigo
            );
        }

        return cliente;
    }

    public ArrayList<Cliente> listarClientes() {
        return repositorio.listar();
    }

    public void modificarCliente(String codigo, Cliente nuevoCliente) {
        repositorio.modificar(codigo, nuevoCliente);
    }

    public void eliminarCliente(String codigo) {
        repositorio.eliminar(codigo);
    }

    public int cantidadClientes() {
        return repositorio.cantidad();
    }

    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }
}
