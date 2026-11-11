package repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class RepositorioGenerico<T> {

    // Lista de registros
    private final ArrayList<T> lista;

    // Mapa para búsquedas rápidas
    private final HashMap<String, T> mapa;

    // Pila para deshacer operaciones
    private final Stack<Operacion<T>> historial;

    // Constructor
    public RepositorioGenerico() {
        lista = new ArrayList<>();
        mapa = new HashMap<>();
        historial = new Stack<>();
    }

    // Clase interna para registrar operaciones
    private static class Operacion<T> {

        String tipo;
        String clave;
        T objeto;

        Operacion(String tipo, String clave, T objeto) {
            this.tipo = tipo;
            this.clave = clave;
            this.objeto = objeto;
        }
    }

    // Registrar un objeto
    public void registrar(String clave, T objeto) {

        if (clave == null || clave.trim().isEmpty()
                || objeto == null) {
            throw new IllegalArgumentException(
                    "Datos de registro invalidos.");
        }

        clave = clave.trim();

        if (mapa.containsKey(clave)) {
            throw new IllegalArgumentException(
                    "El codigo ya se encuentra registrado.");
        }

        lista.add(objeto);
        mapa.put(clave, objeto);

        historial.push(
                new Operacion<>("REGISTRAR", clave, objeto));
    }

    // Buscar por código
    public T buscar(String clave) {

        if (clave == null) {
            return null;
        }

        return mapa.get(clave.trim());
    }

    // Consultar todos los registros
    public ArrayList<T> listar() {
        return new ArrayList<>(lista);
    }

    // Modificar un registro
    public void modificar(String clave, T nuevoObjeto) {

        if (clave == null || nuevoObjeto == null) {
            throw new IllegalArgumentException(
                    "Datos de modificación invalidos.");
        }

        clave = clave.trim();

        T anterior = mapa.get(clave);

        if (anterior == null) {
            throw new IllegalArgumentException(
                    "No existe el registro.");
        }

        int posicion = lista.indexOf(anterior);

        lista.set(posicion, nuevoObjeto);
        mapa.put(clave, nuevoObjeto);

        historial.push(
                new Operacion<>("MODIFICAR", clave, anterior));
    }

    // Eliminar un registro
    public void eliminar(String clave) {

        if (clave == null) {
            return;
        }

        clave = clave.trim();

        T objeto = mapa.remove(clave);

        if (objeto != null) {

            lista.remove(objeto);

            historial.push(
                    new Operacion<>("ELIMINAR", clave, objeto));
        }
    }

    // Deshacer última operación
    public boolean deshacer() {

        if (historial.isEmpty()) {
            return false;
        }

        Operacion<T> operacion = historial.pop();

        switch (operacion.tipo) {

            case "REGISTRAR":
                lista.remove(operacion.objeto);
                mapa.remove(operacion.clave);
                break;

            case "ELIMINAR":
                lista.add(operacion.objeto);
                mapa.put(operacion.clave, operacion.objeto);
                break;

            case "MODIFICAR":
                T actual = mapa.get(operacion.clave);

                int posicion = lista.indexOf(actual);

                if (posicion >= 0) {
                    lista.set(posicion, operacion.objeto);
                }

                mapa.put(operacion.clave, operacion.objeto);
                break;

            default:
                return false;
        }

        return true;
    }

    // Cantidad de registros
    public int cantidad() {
        return lista.size();
    }

    // Comprobar si está vacío
    public boolean estaVacio() {
        return lista.isEmpty();
    }

    // Consultar si existe un código
    public boolean existe(String clave) {
        return clave != null && mapa.containsKey(clave.trim());
    }

    // Limpiar registros
    public void limpiar() {
        lista.clear();
        mapa.clear();
        historial.clear();
    }
}
