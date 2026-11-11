package vista;

import controlador.ControladorServicios;
import modelo.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaServicios extends JPanel {

    private final ControladorServicios controlador;

    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtTiempo;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VistaServicios(ControladorServicios controlador) {

        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));

        crearFormulario();
        crearTabla();
        actualizarTabla();
    }

    private void crearFormulario() {

        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));

        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos del servicio")
        );

        txtCodigo = new JTextField();
        txtDescripcion = new JTextField();
        txtPrecio = new JTextField();
        txtTiempo = new JTextField();

        formulario.add(new JLabel("Código:"));
        formulario.add(txtCodigo);

        formulario.add(new JLabel("Descripción:"));
        formulario.add(txtDescripcion);

        formulario.add(new JLabel("Precio base:"));
        formulario.add(txtPrecio);

        formulario.add(new JLabel("Tiempo estimado (minutos):"));
        formulario.add(txtTiempo);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        JPanel botones = new JPanel(new FlowLayout());

        botones.add(btnRegistrar);
        botones.add(btnBuscar);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        JPanel superior = new JPanel(new BorderLayout(10, 10));

        superior.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);

        btnRegistrar.addActionListener(e -> registrarServicio());
        btnBuscar.addActionListener(e -> buscarServicio());
        btnModificar.addActionListener(e -> modificarServicio());
        btnEliminar.addActionListener(e -> eliminarServicio());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void crearTabla() {

        modeloTabla = new DefaultTableModel(
                new String[]{
                    "Código",
                    "Descripción",
                    "Precio base",
                    "Tiempo estimado"
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

                txtDescripcion.setText(
                        modeloTabla.getValueAt(fila, 1).toString()
                );

                txtPrecio.setText(
                        modeloTabla.getValueAt(fila, 2).toString()
                );

                txtTiempo.setText(
                        modeloTabla.getValueAt(fila, 3).toString()
                );
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Servicios registrados")
        );

        add(scroll, BorderLayout.CENTER);
    }

    private Servicio obtenerServicioFormulario() {

        String codigo = txtCodigo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        double precio = Double.parseDouble(
                txtPrecio.getText().trim()
        );

        int tiempo = Integer.parseInt(
                txtTiempo.getText().trim()
        );

        return new Servicio(
                codigo,
                descripcion,
                precio,
                tiempo
        );
    }

    private void registrarServicio() {

        try {

            Servicio servicio = obtenerServicioFormulario();

            controlador.registrarServicio(servicio);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Servicio registrado correctamente."
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

    private void buscarServicio() {

        try {

            Servicio servicio = controlador.buscarServicio(
                    txtCodigo.getText().trim()
            );

            txtDescripcion.setText(servicio.getDescripcion());
            txtPrecio.setText(String.valueOf(servicio.getPrecioBase()));
            txtTiempo.setText(String.valueOf(servicio.getTiempoEstimado()));

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Búsqueda",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void modificarServicio() {

        try {

            String codigo = txtCodigo.getText().trim();

            Servicio nuevoServicio = obtenerServicioFormulario();

            controlador.modificarServicio(codigo, nuevoServicio);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Servicio modificado correctamente."
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

    private void eliminarServicio() {

        try {

            String codigo = txtCodigo.getText().trim();

            controlador.buscarServicio(codigo);

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar este servicio?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {

                controlador.eliminarServicio(codigo);

                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(
                        this,
                        "Servicio eliminado correctamente."
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

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Servicio servicio : controlador.listarServicios()) {

            modeloTabla.addRow(new Object[]{
                servicio.getCodigo(),
                servicio.getDescripcion(),
                servicio.getPrecioBase(),
                servicio.getTiempoEstimado()
            });
        }
    }

    private void limpiarCampos() {

        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtTiempo.setText("");

        tabla.clearSelection();
    }
}