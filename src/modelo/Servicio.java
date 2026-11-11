package modelo;

public class Servicio {

    // Atributos privados
    private String codigo;
    private String descripcion;
    private double precioBase;
    private int tiempoEstimado;

    // Constructor
    public Servicio(String codigo, String descripcion,
                    double precioBase, int tiempoEstimado) {

        if (codigo == null || codigo.trim().isEmpty()
                || descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El codigo y la descripcion son obligatorios.");
        }

        if (precioBase <= 0 || Double.isNaN(precioBase)
                || Double.isInfinite(precioBase)) {
            throw new IllegalArgumentException(
                    "El precio debe ser positivo.");
        }

        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException(
                    "El tiempo estimado debe ser mayor que cero.");
        }

        this.codigo = codigo.trim();
        this.descripcion = descripcion.trim();
        this.precioBase = precioBase;
        this.tiempoEstimado = tiempoEstimado;
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    // Setters
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo obligatorio.");
        }
        this.codigo = codigo.trim();
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La descripcion es obligatoria.");
        }
        this.descripcion = descripcion.trim();
    }

    public void setPrecioBase(double precioBase) {
        if (precioBase <= 0 || Double.isNaN(precioBase)
                || Double.isInfinite(precioBase)) {
            throw new IllegalArgumentException("Precio invalido.");
        }
        this.precioBase = precioBase;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException("Tiempo invalido.");
        }
        this.tiempoEstimado = tiempoEstimado;
    }

    // Información del servicio
    @Override
    public String toString() {
        return "Codigo: " + codigo
                + " | Servicio: " + descripcion
                + " | Precio: L. " + precioBase
                + " | Tiempo: " + tiempoEstimado + " minutos";
    }
}