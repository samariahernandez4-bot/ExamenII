package vista;

import controlador.ControladorRepuestos;
import modelo.Repuesto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaRepuestos extends JPanel {

    private final ControladorRepuestos controlador;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JTextField txtStockMinimo;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VistaRepuestos(ControladorRepuestos controlador) {

        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));

        crearFormulario();
        crearTabla();
        actualizarTabla();
    }

    private void crearFormulario() {

        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 10));

        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos del repuesto")
        );

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtCantidad = new JTextField();
        txtPrecio = new JTextField();
        txtStockMinimo = new JTextField();

        formulario.add(new JLabel("Código:"));
        formulario.add(txtCodigo);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Cantidad disponible:"));
        formulario.add(txtCantidad);

        formulario.add(new JLabel("Precio unitario:"));
        formulario.add(txtPrecio);

        formulario.add(new JLabel("Stock mínimo:"));
        formulario.add(txtStockMinimo);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnAgregar = new JButton("Agregar existencias");
        JButton btnDescontar = new JButton("Descontar existencias");
        JButton btnStockBajo = new JButton("Stock bajo");
        JButton btnLimpiar = new JButton("Limpiar");

        JPanel botones = new JPanel(new FlowLayout());

        botones.add(btnRegistrar);
        botones.add(btnBuscar);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        botones.add(btnAgregar);
        botones.add(btnDescontar);
        botones.add(btnStockBajo);
        botones.add(btnLimpiar);

        JPanel superior = new JPanel(new BorderLayout(10, 10));

        superior.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);

        btnRegistrar.addActionListener(e -> registrarRepuesto());
        btnBuscar.addActionListener(e -> buscarRepuesto());
        btnModificar.addActionListener(e -> modificarRepuesto());
        btnEliminar.addActionListener(e -> eliminarRepuesto());
        btnAgregar.addActionListener(e -> agregarExistencias());
        btnDescontar.addActionListener(e -> descontarExistencias());
        btnStockBajo.addActionListener(e -> mostrarStockBajo());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void crearTabla() {

        modeloTabla = new DefaultTableModel(
                new String[]{
                    "Código",
                    "Nombre",
                    "Cantidad",
                    "Precio unitario",
                    "Stock mínimo"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tabla.getSelectedRow() != -1) {

                int fila = tabla.getSelectedRow();

                txtCodigo.setText(
                        modeloTabla.getValueAt(fila, 0).toString()
                );

                txtNombre.setText(
                        modeloTabla.getValueAt(fila, 1).toString()
                );

                txtCantidad.setText(
                        modeloTabla.getValueAt(fila, 2).toString()
                );

                txtPrecio.setText(
                        modeloTabla.getValueAt(fila, 3).toString()
                );

                txtStockMinimo.setText(
                        modeloTabla.getValueAt(fila, 4).toString()
                );
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Inventario de repuestos")
        );

        add(scroll, BorderLayout.CENTER);
    }

    private Repuesto obtenerRepuestoFormulario() {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        int cantidad = Integer.parseInt(
                txtCantidad.getText().trim()
        );

        double precio = Double.parseDouble(
                txtPrecio.getText().trim()
        );

        int stockMinimo = Integer.parseInt(
                txtStockMinimo.getText().trim()
        );

        return new Repuesto(
                codigo,
                nombre,
                cantidad,
                precio,
                stockMinimo
        );
    }

    private void registrarRepuesto() {

        try {

            Repuesto repuesto = obtenerRepuestoFormulario();

            controlador.registrarRepuesto(repuesto);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Repuesto registrado correctamente."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void buscarRepuesto() {

        try {

            Repuesto repuesto = controlador.buscarRepuesto(
                    txtCodigo.getText().trim()
            );

            txtNombre.setText(repuesto.getNombre());
            txtCantidad.setText(String.valueOf(repuesto.getCantidad()));
            txtPrecio.setText(String.valueOf(repuesto.getPrecioUnitario()));
            txtStockMinimo.setText(String.valueOf(repuesto.getStockMinimo()));

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Búsqueda",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void modificarRepuesto() {

        try {

            String codigo = txtCodigo.getText().trim();

            Repuesto nuevoRepuesto = obtenerRepuestoFormulario();

            controlador.modificarRepuesto(codigo, nuevoRepuesto);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Repuesto modificado correctamente."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarRepuesto() {

        try {

            String codigo = txtCodigo.getText().trim();

            controlador.buscarRepuesto(codigo);

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar este repuesto?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {

                controlador.eliminarRepuesto(codigo);

                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(
                        this,
                        "Repuesto eliminado correctamente."
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void agregarExistencias() {

        try {

            String codigo = txtCodigo.getText().trim();

            int cantidad = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese la cantidad que desea agregar:"
                    )
            );

            controlador.agregarExistencias(codigo, cantidad);

            actualizarTabla();
            buscarRepuesto();

            JOptionPane.showMessageDialog(
                    this,
                    "Existencias agregadas correctamente."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void descontarExistencias() {

        try {

            String codigo = txtCodigo.getText().trim();

            int cantidad = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese la cantidad que desea descontar:"
                    )
            );

            controlador.descontarExistencias(codigo, cantidad);

            actualizarTabla();
            buscarRepuesto();

            JOptionPane.showMessageDialog(
                    this,
                    "Existencias descontadas correctamente."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarStockBajo() {

        modeloTabla.setRowCount(0);

        for (Repuesto repuesto : controlador.listarRepuestosConStockBajo()) {

            modeloTabla.addRow(new Object[]{
                repuesto.getCodigo(),
                repuesto.getNombre(),
                repuesto.getCantidad(),
                repuesto.getPrecioUnitario(),
                repuesto.getStockMinimo()
            });
        }

        JOptionPane.showMessageDialog(
                this,
                "Se muestran los repuestos con stock bajo."
        );
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Repuesto repuesto : controlador.listarRepuestos()) {

            modeloTabla.addRow(new Object[]{
                repuesto.getCodigo(),
                repuesto.getNombre(),
                repuesto.getCantidad(),
                repuesto.getPrecioUnitario(),
                repuesto.getStockMinimo()
            });
        }
    }

    private void limpiarCampos() {

        txtCodigo.setText("");
        txtNombre.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        txtStockMinimo.setText("");

        tabla.clearSelection();

        actualizarTabla();
    }
}