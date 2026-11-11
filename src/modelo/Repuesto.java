package modelo;

public class Repuesto {

    // Atributos privados
    private String codigo;
    private String nombre;
    private int cantidad;
    private double precioUnitario;
    private int stockMinimo;

    // Constructor
    public Repuesto(String codigo, String nombre, int cantidad,
                    double precioUnitario, int stockMinimo) {

        if (codigo == null || codigo.trim().isEmpty()
                || nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El codigo y nombre son obligatorios.");
        }

        if (cantidad < 0) {
            throw new IllegalArgumentException(
                    "La cantidad no puede ser negativa.");
        }

        if (precioUnitario <= 0 || Double.isNaN(precioUnitario)
                || Double.isInfinite(precioUnitario)) {
            throw new IllegalArgumentException(
                    "El precio unitario debe ser positivo.");
        }

        if (stockMinimo < 0) {
            throw new IllegalArgumentException(
                    "El stock minimo no puede ser negativo.");
        }

        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.stockMinimo = stockMinimo;
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    // Setters
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public void setPrecioUnitario(double precioUnitario) {
        if (precioUnitario <= 0 || Double.isNaN(precioUnitario)
                || Double.isInfinite(precioUnitario)) {
            throw new IllegalArgumentException("Precio invalido.");
        }
        this.precioUnitario = precioUnitario;
    }

    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("Stock minimo invalido.");
        }
        this.stockMinimo = stockMinimo;
    }

    // Registrar entrada de repuestos
    public void agregarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de entrada debe ser positiva.");
        }

        this.cantidad = Math.addExact(this.cantidad, cantidad);
    }

    // Descontar repuestos del inventario
    public void descontarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad a descontar debe ser positiva.");
        }

        if (cantidad > this.cantidad) {
            throw new IllegalArgumentException(
                    "No hay suficientes repuestos disponibles.");
        }

        this.cantidad -= cantidad;
    }

    // Consultar si el inventario está bajo
    public boolean tieneStockBajo() {
        return cantidad <= stockMinimo;
    }

    // Información del repuesto
    @Override
    public String toString() {
        return "Codigo: " + codigo
                + " | Nombre: " + nombre
                + " | Cantidad: " + cantidad
                + " | Precio: L. " + precioUnitario
                + " | Stock minimo: " + stockMinimo;
    }
}