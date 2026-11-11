package modelo;

public class Vehiculo {

    // Atributos privados
    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private String color;
    private Cliente propietario;

    // Constructor
    public Vehiculo(String placa, String marca, String modelo,
                    int anio, String color, Cliente propietario) {

        if (placa == null || placa.trim().isEmpty()
                || marca == null || marca.trim().isEmpty()
                || modelo == null || modelo.trim().isEmpty()
                || color == null || color.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Todos los campos del vehiculo son obligatorios.");
        }

        if (anio <= 0) {
            throw new IllegalArgumentException(
                    "El anio del vehiculo debe ser valido.");
        }

        if (propietario == null) {
            throw new IllegalArgumentException(
                    "El vehiculo debe tener un propietario.");
        }

        this.placa = placa.trim().toUpperCase();
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.anio = anio;
        this.color = color.trim();
        this.propietario = propietario;
    }

    // Getters
    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public String getColor() {
        return color;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    // Setters
    public void setPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria.");
        }
        this.placa = placa.trim().toUpperCase();
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAnio(int anio) {
        if (anio <= 0) {
            throw new IllegalArgumentException("Anio invalido.");
        }
        this.anio = anio;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPropietario(Cliente propietario) {
        if (propietario == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un propietario.");
        }
        this.propietario = propietario;
    }

    // Información del vehiculo
    @Override
    public String toString() {
        return "Placa: " + placa
                + " | Marca: " + marca
                + " | Modelo: " + modelo
                + " | Anio: " + anio
                + " | Color: " + color
                + " | Propietario: " + propietario.getNombreCompleto();
    }
}
