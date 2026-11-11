package vista;

import javax.swing.*;
import java.awt.*;

import controlador.ControladorClientes;
import controlador.ControladorVehiculos;
import controlador.ControladorServicios;
import controlador.ControladorRepuestos;
import controlador.ControladorOrdenes;
import controlador.ControladorFacturas;

public class VistaPrincipal extends JFrame {

    private final ControladorClientes clientes;
    private final ControladorVehiculos vehiculos;
    private final ControladorServicios servicios;
    private final ControladorRepuestos repuestos;
    private final ControladorOrdenes ordenes;
    private final ControladorFacturas facturas;
    


    private JTabbedPane pestañas;
private VistaVehiculos vistaVehiculos;
private VistaOrdenes vistaOrdenes;
private VistaFacturacion vistaFacturacion;

    public VistaPrincipal(
            ControladorClientes clientes,
            ControladorVehiculos vehiculos,
            ControladorServicios servicios,
            ControladorRepuestos repuestos,
            ControladorOrdenes ordenes,
            ControladorFacturas facturas) {

        this.clientes = clientes;
        this.vehiculos = vehiculos;
        this.servicios = servicios;
        this.repuestos = repuestos;
        this.ordenes = ordenes;
        this.facturas = facturas;

        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {

        setTitle("AutoTech Solutions - Sistema de Gestión Automotriz");

        setSize(1100, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setMinimumSize(new Dimension(900, 600));

    }

    private void crearComponentes() {

        setLayout(new BorderLayout());

        // Encabezado
        JPanel encabezado = new JPanel();

        encabezado.setBackground(new Color(25, 45, 70));

        encabezado.setPreferredSize(new Dimension(1100, 90));

        JLabel titulo = new JLabel("AUTOTECH SOLUTIONS");

        titulo.setForeground(Color.WHITE);

        titulo.setFont(new Font("Arial", Font.BOLD, 28));

        encabezado.add(titulo);

        add(encabezado, BorderLayout.NORTH);

        // Pestañas de navegación
        pestañas = new JTabbedPane();

        agregarPestana("Inicio");
        pestañas.addTab("Clientes", new VistaClientes(clientes));
        vistaVehiculos = new VistaVehiculos(vehiculos, clientes);
pestañas.addTab("Vehículos", vistaVehiculos);
pestañas.addTab("Servicios", new VistaServicios(servicios));        
pestañas.addTab("Repuestos", new VistaRepuestos(repuestos));
vistaOrdenes = new VistaOrdenes(
        ordenes,
        clientes,
        vehiculos,
        servicios,
        repuestos
);

pestañas.addTab("Órdenes de trabajo", vistaOrdenes);
vistaFacturacion = new VistaFacturacion(facturas, ordenes);
pestañas.addTab("Facturación", vistaFacturacion);


VistaReportes vistaReportes = new VistaReportes(ordenes, facturas);
pestañas.addTab("Reportes", vistaReportes);

       pestañas.addChangeListener(e -> {

    if (pestañas.getSelectedComponent() == vistaVehiculos) {
        vistaVehiculos.actualizarClientes();
    }

    if (pestañas.getSelectedComponent() == vistaOrdenes) {
        vistaOrdenes.actualizarDatos();
    }

    if (pestañas.getSelectedComponent() == vistaReportes) {
        vistaReportes.actualizarDatos();
    }
});

        add(pestañas, BorderLayout.CENTER);

        // Pie de página
        JLabel pie = new JLabel(
                "AutoTech Solutions | Sistema de administración automotriz",
                SwingConstants.CENTER
        );

        pie.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(pie, BorderLayout.SOUTH);
    }

    private void agregarPestana(String nombre) {

        JPanel panel = new JPanel(new BorderLayout());

        JLabel mensaje = new JLabel(
                nombre.equals("Inicio")
                ? "Bienvenido al sistema AutoTech Solutions"
                : "Módulo de " + nombre,
                SwingConstants.CENTER
        );

        mensaje.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(mensaje, BorderLayout.CENTER);

        pestañas.addTab(nombre, panel);
    }
}