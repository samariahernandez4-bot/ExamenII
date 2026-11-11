package modelo;

public class Cliente {

    // Atributos privados
    private String codigo;
    private String nombreCompleto;
    private String identidad;
    private String telefono;
    private String direccion;

    // Constructor
    public Cliente(String codigo, String nombreCompleto,
                   String identidad, String telefono, String direccion) {

        if (codigo == null || codigo.trim().isEmpty()
                || nombreCompleto == null || nombreCompleto.trim().isEmpty()
                || identidad == null || identidad.trim().isEmpty()
                || telefono == null || telefono.trim().isEmpty()
                || direccion == null || direccion.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Todos los campos del cliente son obligatorios.");
        }

        this.codigo = codigo.trim();
        this.nombreCompleto = nombreCompleto.trim();
        this.identidad = identidad.trim();
        this.telefono = telefono.trim();
        this.direccion = direccion.trim();
    }

    // Métodos getters y setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo es obligatorio.");
        }
        this.codigo = codigo.trim();
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombreCompleto = nombreCompleto.trim();
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Método para mostrar información del cliente
    @Override
    public String toString() {
        return "Codigo: " + codigo
                + " | Nombre: " + nombreCompleto
                + " | Identidad: " + identidad
                + " | Telefono: " + telefono
                + " | Direccion: " + direccion;
    }
}