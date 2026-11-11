package vista;

import controlador.ControladorVehiculos;
import controlador.ControladorClientes;
import modelo.Vehiculo;
import modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaVehiculos extends JPanel {

    private final ControladorVehiculos controlador;
    private final ControladorClientes controladorClientes;

    private JTextField txtPlaca;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtAnio;
    private JTextField txtColor;

    private JComboBox<Cliente> cmbPropietario;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VistaVehiculos(
            ControladorVehiculos controlador,
            ControladorClientes controladorClientes) {

        this.controlador = controlador;
        this.controladorClientes = controladorClientes;

        setLayout(new BorderLayout(10, 10));

        crearFormulario();
        crearTabla();
        actualizarTabla();
        cargarClientes();
    }

    private void crearFormulario() {

        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 10));

        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos del vehiculo")
        );

        txtPlaca = new JTextField();
        txtMarca = new JTextField();
        txtModelo = new JTextField();
        txtAnio = new JTextField();
        txtColor = new JTextField();

        cmbPropietario = new JComboBox<>();

        formulario.add(new JLabel("Placa:"));
        formulario.add(txtPlaca);

        formulario.add(new JLabel("Marca:"));
        formulario.add(txtMarca);

        formulario.add(new JLabel("Modelo:"));
        formulario.add(txtModelo);

        formulario.add(new JLabel("Anio:"));
        formulario.add(txtAnio);

        formulario.add(new JLabel("Color:"));
        formulario.add(txtColor);

        formulario.add(new JLabel("Propietario:"));
        formulario.add(cmbPropietario);

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

        btnRegistrar.addActionListener(e -> registrarVehiculo());
        btnBuscar.addActionListener(e -> buscarVehiculo());
        btnModificar.addActionListener(e -> modificarVehiculo());
        btnEliminar.addActionListener(e -> eliminarVehiculo());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void crearTabla() {

        modeloTabla = new DefaultTableModelImpl(
                new String[]{
                    "Placa",
                    "Marca",
                    "Modelo",
                    "Anio",
                    "Color",
                    "Propietario"
                }, 0);

        tabla = new JTable(modeloTabla);

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tabla.getSelectedRow() != -1) {

                int fila = tabla.getSelectedRow();

                txtPlaca.setText(
                        modeloTabla.getValueAt(fila, 0).toString()
                );

                txtMarca.setText(
                        modeloTabla.getValueAt(fila, 1).toString()
                );

                txtModelo.setText(
                        modeloTabla.getValueAt(fila, 2).toString()
                );

                txtAnio.setText(
                        modeloTabla.getValueAt(fila, 3).toString()
                );

                txtColor.setText(
                        modeloTabla.getValueAt(fila, 4).toString()
                );

                seleccionarPropietario(
                        modeloTabla.getValueAt(fila, 5).toString()
                );
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Vehiculos registrados")
        );

        add(scroll, BorderLayout.CENTER);
    }

    private void cargarClientes() {

        cmbPropietario.removeAllItems();

        for (Cliente cliente : controladorClientes.listarClientes()) {
            cmbPropietario.addItem(cliente);
        }
    }
public void actualizarClientes() {
    cargarClientes();
}
    private void registrarVehiculo() {

        try {

            Vehiculo vehiculo = obtenerVehiculoFormulario();

            controlador.registrarVehiculo(vehiculo);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Vehiculo registrado correctamente."
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

    private void buscarVehiculo() {

        try {

            Vehiculo vehiculo = controlador.buscarVehiculo(
                    txtPlaca.getText().trim()
            );

            txtMarca.setText(vehiculo.getMarca());
            txtModelo.setText(vehiculo.getModelo());
            txtAnio.setText(String.valueOf(vehiculo.getAnio()));
            txtColor.setText(vehiculo.getColor());

            seleccionarPropietario(
                    vehiculo.getPropietario().getCodigo()
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Busqueda",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void modificarVehiculo() {

        try {

            String placa = txtPlaca.getText().trim();

            Vehiculo nuevoVehiculo = obtenerVehiculoFormulario();

            controlador.modificarVehiculo(placa, nuevoVehiculo);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Vehiculo modificado correctamente."
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

    private void eliminarVehiculo() {

        try {

            String placa = txtPlaca.getText().trim();

            if (placa.isEmpty()) {
                throw new IllegalArgumentException(
                        "Ingrese la placa del vehiculo."
                );
            }

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar este vehiculo?",
                    "Confirmar eliminacion",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {

                controlador.buscarVehiculo(placa);
                controlador.eliminarVehiculo(placa);

                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(
                        this,
                        "Vehiculo eliminado correctamente."
                );
            }

        } catch (HeadlessException | IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Vehiculo obtenerVehiculoFormulario() {

        if (cmbPropietario.getSelectedItem() == null) {
            throw new IllegalArgumentException(
                    "Primero debe registrar y seleccionar un cliente."
            );
        }

        int anio = Integer.parseInt(txtAnio.getText().trim());

        Cliente propietario = (Cliente) cmbPropietario.getSelectedItem();

        return new Vehiculo(
                txtPlaca.getText().trim(),
                txtMarca.getText().trim(),
                txtModelo.getText().trim(),
                anio,
                txtColor.getText().trim(),
                propietario
        );
    }

    private void seleccionarPropietario(String codigo) {

        for (int i = 0; i < cmbPropietario.getItemCount(); i++) {

            Cliente cliente = cmbPropietario.getItemAt(i);

            if (cliente.getCodigo().equals(codigo)) {
                cmbPropietario.setSelectedIndex(i);
                return;
            }
        }
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Vehiculo vehiculo : controlador.listarVehiculos()) {

            modeloTabla.addRow(new Object[]{
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnio(),
                vehiculo.getColor(),
                vehiculo.getPropietario().getCodigo()
            });
        }
    }

    private void limpiarCampos() {

        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        txtColor.setText("");

        tabla.clearSelection();
        cmbPropietario.setSelectedIndex(-1);
        cargarClientes();
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