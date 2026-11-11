package vista;

import controlador.ControladorClientes;
import excepciones.ClienteNoEncontradoException;
import modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaClientes extends JPanel {

    private final ControladorClientes controlador;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtIdentidad;
    private JTextField txtTelefono;
    private JTextField txtDireccion;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VistaClientes(ControladorClientes controlador) {

        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));

        crearFormulario();
        crearTabla();
        actualizarTabla();
    }

    private void crearFormulario() {

        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 10));

        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos del cliente")
        );

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtIdentidad = new JTextField();
        txtTelefono = new JTextField();
        txtDireccion = new JTextField();

        formulario.add(new JLabel("Codigo:"));
        formulario.add(txtCodigo);

        formulario.add(new JLabel("Nombre completo:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Identidad:"));
        formulario.add(txtIdentidad);

        formulario.add(new JLabel("Telefono:"));
        formulario.add(txtTelefono);

        formulario.add(new JLabel("Direccion:"));
        formulario.add(txtDireccion);

        JPanel botones = new JPanel(new FlowLayout());

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

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

        btnRegistrar.addActionListener(e -> registrarCliente());

        btnBuscar.addActionListener(e -> buscarCliente());

        btnModificar.addActionListener(e -> modificarCliente());

        btnEliminar.addActionListener(e -> eliminarCliente());

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void crearTabla() {

        modeloTabla = new DefaultTableModelImpl(
                new String[]{
                    "Codigo",
                    "Nombre",
                    "Identidad",
                    "Telefono",
                    "Direccion"
                }, 0);

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

                txtIdentidad.setText(
                        modeloTabla.getValueAt(fila, 2).toString()
                );

                txtTelefono.setText(
                        modeloTabla.getValueAt(fila, 3).toString()
                );

                txtDireccion.setText(
                        modeloTabla.getValueAt(fila, 4).toString()
                );
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Clientes registrados")
        );

        add(scroll, BorderLayout.CENTER);
    }

    private void registrarCliente() {

        try {

            Cliente cliente = obtenerClienteFormulario();

            controlador.registrarCliente(cliente);

            actualizarTabla();

            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente registrado correctamente."
            );

        } catch (HeadlessException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void buscarCliente() {

        try {

            Cliente cliente = controlador.buscarCliente(
                    txtCodigo.getText().trim()
            );

            txtNombre.setText(cliente.getNombreCompleto());
            txtIdentidad.setText(cliente.getIdentidad());
            txtTelefono.setText(cliente.getTelefono());
            txtDireccion.setText(cliente.getDireccion());

        } catch (ClienteNoEncontradoException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Busqueda",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void modificarCliente() {

        try {

            String codigo = txtCodigo.getText().trim();

            Cliente nuevoCliente = obtenerClienteFormulario();

            controlador.modificarCliente(codigo, nuevoCliente);

            actualizarTabla();

            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente modificado correctamente."
            );

        } catch (HeadlessException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarCliente() {

        try {

            String codigo = txtCodigo.getText().trim();

            if (codigo.isEmpty()) {
                throw new IllegalArgumentException(
                        "Ingrese el codigo del cliente."
                );
            }

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar este cliente?",
                    "Confirmar eliminacion",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {

                controlador.buscarCliente(codigo);

                controlador.eliminarCliente(codigo);

                actualizarTabla();

                limpiarCampos();

                JOptionPane.showMessageDialog(
                        this,
                        "Cliente eliminado correctamente."
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

    private Cliente obtenerClienteFormulario() {

        return new Cliente(
                txtCodigo.getText().trim(),
                txtNombre.getText().trim(),
                txtIdentidad.getText().trim(),
                txtTelefono.getText().trim(),
                txtDireccion.getText().trim()
        );
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Cliente cliente : controlador.listarClientes()) {

            modeloTabla.addRow(new Object[]{
                cliente.getCodigo(),
                cliente.getNombreCompleto(),
                cliente.getIdentidad(),
                cliente.getTelefono(),
                cliente.getDireccion()
            });
        }
    }

    private void limpiarCampos() {

        txtCodigo.setText("");
        txtNombre.setText("");
        txtIdentidad.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");

        tabla.clearSelection();
    }

    private static class DefaultTableModelImpl extends DefaultTableModel {

        public DefaultTableModelImpl(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    }
}