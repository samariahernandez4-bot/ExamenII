package vista;

import controlador.ControladorOrdenes;
import controlador.ControladorClientes;
import controlador.ControladorVehiculos;
import controlador.ControladorServicios;
import controlador.ControladorRepuestos;
import excepciones.ClienteNoEncontradoException;

import modelo.OrdenTrabajo;
import modelo.Cliente;
import modelo.Vehiculo;
import modelo.Servicio;
import modelo.Repuesto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaOrdenes extends JPanel {

    private final ControladorOrdenes controlador;
    private final ControladorClientes controladorClientes;
    private final ControladorVehiculos controladorVehiculos;
    private final ControladorServicios controladorServicios;
    private final ControladorRepuestos controladorRepuestos;

    private JTextField txtNumero;
    private JTextField txtDiagnostico;
    private JTextField txtMecanico;

    private JComboBox<Cliente> cmbCliente;
    private JComboBox<Vehiculo> cmbVehiculo;

    private JComboBox<Servicio> cmbServicio;
    private JComboBox<Repuesto> cmbRepuesto;

    private JTextField txtCantidadRepuesto;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JProgressBar barraProgreso;
    private JButton btnProcesar; 

    public VistaOrdenes(
            ControladorOrdenes controlador,
            ControladorClientes clientes,
            ControladorVehiculos vehiculos,
            ControladorServicios servicios,
            ControladorRepuestos repuestos) {

        this.controlador = controlador;
        this.controladorClientes = clientes;
        this.controladorVehiculos = vehiculos;
        this.controladorServicios = servicios;
        this.controladorRepuestos = repuestos;

        setLayout(new BorderLayout(10, 10));

        crearFormulario();
        crearTabla();

        cargarClientes();
        cargarServicios();
        cargarRepuestos();

        actualizarTabla();
    }

    private void crearFormulario() {

        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 10));

        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos de la orden")
        );

        txtNumero = new JTextField();
        txtDiagnostico = new JTextField();
        txtMecanico = new JTextField();

        cmbCliente = new JComboBox<>();
        cmbVehiculo = new JComboBox<>();

        formulario.add(new JLabel("Numero de orden:"));
        formulario.add(txtNumero);

        formulario.add(new JLabel("Cliente:"));
        formulario.add(cmbCliente);

        formulario.add(new JLabel("Vehiculo:"));
        formulario.add(cmbVehiculo);

        formulario.add(new JLabel("Diagnostico:"));
        formulario.add(txtDiagnostico);

        formulario.add(new JLabel("Mecanico:"));
        formulario.add(txtMecanico);

        JPanel panelServicios = new JPanel(new FlowLayout());

        cmbServicio = new JComboBox<>();

        JButton btnAgregarServicio = new JButton("Agregar servicio");

        panelServicios.add(new JLabel("Servicio:"));
        panelServicios.add(cmbServicio);
        panelServicios.add(btnAgregarServicio);

        JPanel panelRepuestos = new JPanel(new FlowLayout());

        cmbRepuesto = new JComboBox<>();
        txtCantidadRepuesto = new JTextField(5);

        JButton btnAgregarRepuesto = new JButton("Agregar repuesto");

        panelRepuestos.add(new JLabel("Repuesto:"));
        panelRepuestos.add(cmbRepuesto);

        panelRepuestos.add(new JLabel("Cantidad:"));
        panelRepuestos.add(txtCantidadRepuesto);

        panelRepuestos.add(btnAgregarRepuesto);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
barraProgreso = new JProgressBar(0, 100);
barraProgreso.setStringPainted(true);
barraProgreso.setString("Esperando orden...");
barraProgreso.setValue(0);

        JButton btnCrear = new JButton("Crear orden");
JButton btnBuscar = new JButton("Buscar");
JButton btnIniciar = new JButton("Iniciar");
JButton btnFinalizar = new JButton("Finalizar");
btnProcesar = btnFinalizar;
JButton btnCancelar = new JButton("Cancelar");
JButton btnLimpiar = new JButton("Limpiar");

Dimension tamanoBoton = new Dimension(115, 32);

btnCrear.setPreferredSize(tamanoBoton);
btnBuscar.setPreferredSize(tamanoBoton);
btnIniciar.setPreferredSize(tamanoBoton);
btnFinalizar.setPreferredSize(tamanoBoton);
btnCancelar.setPreferredSize(tamanoBoton);
btnLimpiar.setPreferredSize(tamanoBoton);

        panelBotones.add(btnCrear);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnIniciar);
        panelBotones.add(btnFinalizar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnLimpiar);

       JPanel superior = new JPanel();
superior.setLayout(new BoxLayout(superior, BoxLayout.Y_AXIS));

        superior.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        superior.add(formulario);
        superior.add(panelServicios);
        superior.add(panelRepuestos);
        superior.add(panelBotones);
barraProgreso.setPreferredSize(new Dimension(400, 22));

JPanel panelProgreso = new JPanel(new FlowLayout(FlowLayout.CENTER));
panelProgreso.add(barraProgreso);

superior.add(panelProgreso);

add(superior, BorderLayout.NORTH);

        cmbCliente.addActionListener(e -> cargarVehiculosCliente());

        btnCrear.addActionListener(e -> crearOrden());
        btnBuscar.addActionListener(e -> buscarOrden());
        btnIniciar.addActionListener(e -> iniciarOrden());
btnFinalizar.addActionListener(e -> procesarOrdenConHilo());
btnCancelar.addActionListener(e -> cancelarOrden());

        btnAgregarServicio.addActionListener(e -> agregarServicio());
        btnAgregarRepuesto.addActionListener(e -> agregarRepuesto());

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void crearTabla() {

        modeloTabla = new DefaultTableModel(
                new String[]{
                    "Numero",
                    "Cliente",
                    "Vehiculo",
                    "Mecanico",
                    "Estado",
                    "Total"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Ordenes registradas")
        );

        add(scroll, BorderLayout.CENTER);
    }

    private void cargarClientes() {

        cmbCliente.removeAllItems();

        for (Cliente cliente : controladorClientes.listarClientes()) {
            cmbCliente.addItem(cliente);
        }

        cargarVehiculosCliente();
    }
    public void actualizarDatos() {
    cargarClientes();
    cargarVehiculosCliente();
    cargarServicios();
    cargarRepuestos();
}

    private void cargarVehiculosCliente() {

        cmbVehiculo.removeAllItems();

        Cliente cliente = (Cliente) cmbCliente.getSelectedItem();

        if (cliente == null) {
            return;
        }

        for (Vehiculo vehiculo : controladorVehiculos.listarVehiculos()) {

            if (vehiculo.getPropietario().getCodigo()
                    .equals(cliente.getCodigo())) {

                cmbVehiculo.addItem(vehiculo);
            }
        }
    }

   private void cargarServicios() {

    cmbServicio.removeAllItems();

    for (Servicio servicio : controladorServicios.listarServicios()) {
        cmbServicio.addItem(servicio);
    }
}

    private void cargarRepuestos() {

        cmbRepuesto.removeAllItems();

        for (Repuesto repuesto : controladorRepuestos.listarRepuestos()) {
            cmbRepuesto.addItem(repuesto);
        }
    }

    private void crearOrden() {

        try {

            Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
            Vehiculo vehiculo = (Vehiculo) cmbVehiculo.getSelectedItem();

            if (cliente == null || vehiculo == null) {
                throw new IllegalArgumentException(
                        "Debe registrar un cliente y su vehiculo."
                );
            }

            controlador.crearOrden(
                    txtNumero.getText().trim(),
                    cliente.getCodigo(),
                    vehiculo.getPlaca(),
                    txtDiagnostico.getText().trim(),
                    txtMecanico.getText().trim()
            );

            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Orden creada correctamente."
            );

        } catch (ClienteNoEncontradoException | HeadlessException | IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void agregarServicio() {

        try {

            OrdenTrabajo orden = obtenerOrdenSeleccionada();

            Servicio servicio = (Servicio) cmbServicio.getSelectedItem();

            if (servicio == null) {
                throw new IllegalArgumentException(
                        "No hay servicios registrados."
                );
            }

            controlador.agregarServicio(
                    orden.getNumero(),
                    servicio.getCodigo()
            );

            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Servicio agregado correctamente."
            );

        } catch (HeadlessException | IllegalArgumentException ex) {

            mostrarError(ex);
        }
    }

    private void agregarRepuesto() {

        try {

            OrdenTrabajo orden = obtenerOrdenSeleccionada();

            Repuesto repuesto = (Repuesto) cmbRepuesto.getSelectedItem();

            if (repuesto == null) {
                throw new IllegalArgumentException(
                        "No hay repuestos registrados."
                );
            }

            int cantidad = Integer.parseInt(
                    txtCantidadRepuesto.getText().trim()
            );

            controlador.agregarRepuesto(
                    orden.getNumero(),
                    repuesto.getCodigo(),
                    cantidad
            );

            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Repuesto agregado correctamente."
            );

        } catch (HeadlessException | IllegalArgumentException ex) {

            mostrarError(ex);
        }
    }

    private void iniciarOrden() {

        try {

            OrdenTrabajo orden = obtenerOrdenSeleccionada();

            controlador.iniciarOrden(orden.getNumero());

            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Orden iniciada correctamente."
            );

        } catch (HeadlessException ex) {

            mostrarError(ex);
        }
    }


    private void procesarOrdenConHilo() {

    try {

        OrdenTrabajo orden = obtenerOrdenSeleccionada();

        barraProgreso.setValue(0);
        barraProgreso.setString("Procesando orden...");

        btnProcesar.setEnabled(false);

        hilos.ProcesoServicioHilo proceso =
                new hilos.ProcesoServicioHilo(
                        orden,
                        barraProgreso,
                        btnProcesar,
                        () -> actualizarTabla()
                );

        proceso.execute();

    } catch (Exception ex) {

        btnProcesar.setEnabled(true);
        mostrarError(ex);
    }
}
    private void cancelarOrden() {

        try {

            OrdenTrabajo orden = obtenerOrdenSeleccionada();

            controlador.cancelarOrden(orden.getNumero());

            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Orden cancelada correctamente."
            );

        } catch (HeadlessException ex) {

            mostrarError(ex);
        }
    }

    private void buscarOrden() {

        try {

            OrdenTrabajo orden = controlador.buscarOrden(
                    txtNumero.getText().trim()
            );

            JOptionPane.showMessageDialog(
                    this,
                    orden.toString(),
                    "Informacion de la orden",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (HeadlessException ex) {

            mostrarError(ex);
        }
    }

    private OrdenTrabajo obtenerOrdenSeleccionada() {

        String numero = JOptionPane.showInputDialog(
                this,
                "Ingrese el numero de la orden:"
        );

        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe ingresar el numero de la orden."
            );
        }

        return controlador.buscarOrden(numero.trim());
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (OrdenTrabajo orden : controlador.listarOrdenes()) {

            modeloTabla.addRow(new Object[]{
                orden.getNumero(),
                orden.getCliente().getNombreCompleto(),
                orden.getVehiculo().getPlaca(),
                orden.getMecanico(),
                orden.getEstado(),
                orden.calcularCostoTotal()
            });
        }
    }

    private void limpiarCampos() {

        txtNumero.setText("");
        txtDiagnostico.setText("");
        txtMecanico.setText("");
        txtCantidadRepuesto.setText("");

        cargarClientes();
        cargarServicios();
        cargarRepuestos();
    }

    private void mostrarError(Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}