
package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import modelo.Factura;
import controlador.ControladorFacturas;
import controlador.ControladorOrdenes;
import excepciones.PagoInsuficienteException;

public class VistaFacturacion extends JPanel {

    private final ControladorFacturas controladorFacturas;

    private JTextField txtNumeroFactura;
    private JTextField txtNumeroOrden;
    private JTextField txtImpuesto;
    private JTextField txtPago;

    private JTextArea areaFactura;

    private JButton btnGenerar;
    private JButton btnRegistrarPago;
    private JButton btnLimpiar;

    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;

    // CONSTRUCTOR
    public VistaFacturacion(
            ControladorFacturas controladorFacturas,
            ControladorOrdenes controladorOrdenes) {

        this.controladorFacturas = controladorFacturas;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        crearFormulario();
        crearTabla();
        crearEventos();

        actualizarTabla();
    }

    // FORMULARIO
    private void crearFormulario() {

        JPanel formulario = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );

        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos de facturación")
        );

        txtNumeroFactura = new JTextField();
        txtNumeroOrden = new JTextField();
        txtImpuesto = new JTextField("15");
        txtPago = new JTextField();

        formulario.add(new JLabel("Número de factura:"));
        formulario.add(txtNumeroFactura);

        formulario.add(new JLabel("Número de orden:"));
        formulario.add(txtNumeroOrden);

        formulario.add(new JLabel("Impuesto (%):"));
        formulario.add(txtImpuesto);

        formulario.add(new JLabel("Pago recibido (L.):"));
        formulario.add(txtPago);

        btnGenerar = new JButton("Generar factura");
        btnRegistrarPago = new JButton("Registrar pago");
        btnLimpiar = new JButton("Limpiar");

        formulario.add(btnGenerar);
        formulario.add(btnRegistrarPago);

        JPanel botones = new JPanel();
        botones.add(btnLimpiar);

        add(formulario, BorderLayout.NORTH);
        add(botones, BorderLayout.SOUTH);
    }

    // TABLA Y DETALLE
    private void crearTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "Número factura",
                    "Número orden",
                    "Cliente",
                    "Total",
                    "Estado"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaFacturas = new JTable(modeloTabla);

        tablaFacturas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollTabla = new JScrollPane(tablaFacturas);

        scrollTabla.setBorder(
                BorderFactory.createTitledBorder("Facturas registradas")
        );

        areaFactura = new JTextArea();
        areaFactura.setEditable(false);

        areaFactura.setFont(
                new Font("Monospaced", Font.PLAIN, 13)
        );

        JScrollPane scrollDetalle = new JScrollPane(areaFactura);

        scrollDetalle.setBorder(
                BorderFactory.createTitledBorder("Detalle de factura")
        );

        JSplitPane division = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                scrollTabla,
                scrollDetalle
        );

        division.setResizeWeight(0.5);

        add(division, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() ->
                division.setDividerLocation(0.5)
        );
    }

    // EVENTOS
    private void crearEventos() {

        btnGenerar.addActionListener(e -> generarFactura());

        btnRegistrarPago.addActionListener(e -> registrarPago());

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaFacturas.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int fila = tablaFacturas.getSelectedRow();

                if (fila != -1) {

                    int filaModelo =
                            tablaFacturas.convertRowIndexToModel(fila);

                    String numeroFactura = modeloTabla.getValueAt(
                            filaModelo, 0
                    ).toString();

                    Factura factura =
                            controladorFacturas.buscarFactura(numeroFactura);

                    areaFactura.setText(factura.toString());
                }
            }
        });
    }

    // GENERAR FACTURA
    private void generarFactura() {

        try {

            String numeroFactura =
                    txtNumeroFactura.getText().trim();

            String numeroOrden =
                    txtNumeroOrden.getText().trim();

            if (numeroFactura.isEmpty() || numeroOrden.isEmpty()) {

                throw new IllegalArgumentException(
                        "Complete el número de factura y el número de orden."
                );
            }

            double impuesto = Double.parseDouble(
                    txtImpuesto.getText().trim()
            );

            Factura factura = controladorFacturas.generarFactura(
                    numeroFactura,
                    numeroOrden,
                    impuesto
            );

            areaFactura.setText(factura.toString());

            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Factura generada correctamente.",
                    "Facturación",
                    JOptionPane.INFORMATION_MESSAGE
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

    // ACTUALIZAR TABLA
    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Factura factura : controladorFacturas.listarFacturas()) {

            modeloTabla.addRow(new Object[]{
                factura.getNumeroFactura(),
                factura.getOrden().getNumero(),
                factura.getOrden().getCliente().getNombreCompleto(),
                String.format("L. %.2f", factura.getTotal()),
                factura.isPagada() ? "PAGADA" : "PENDIENTE"
            });
        }
    }

    // REGISTRAR PAGO
    private void registrarPago() {

        try {

            int fila = tablaFacturas.getSelectedRow();

            if (fila == -1) {

                throw new IllegalArgumentException(
                        "Seleccione una factura de la tabla."
                );
            }

            int filaModelo =
                    tablaFacturas.convertRowIndexToModel(fila);

            String numeroFactura = modeloTabla.getValueAt(
                    filaModelo, 0
            ).toString();

            double pago = Double.parseDouble(
                    txtPago.getText().trim()
            );

            controladorFacturas.registrarPago(
                    numeroFactura,
                    pago
            );

            Factura factura = controladorFacturas.buscarFactura(
                    numeroFactura
            );

            areaFactura.setText(factura.toString());

            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Pago registrado correctamente.\n"
                    + "Cambio: L. "
                    + String.format("%.2f", factura.getCambio()),
                    "Pago exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (PagoInsuficienteException | IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error en el pago",
                    JOptionPane.ERROR_MESSAGE
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

    // LIMPIAR FORMULARIO
    private void limpiarFormulario() {

        txtNumeroFactura.setText("");
        txtNumeroOrden.setText("");
        txtImpuesto.setText("15");
        txtPago.setText("");

        areaFactura.setText("");

        tablaFacturas.clearSelection();
    }
}