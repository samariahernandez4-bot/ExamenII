package main;

import controlador.ControladorClientes;
import controlador.ControladorVehiculos;
import controlador.ControladorServicios;
import controlador.ControladorRepuestos;
import controlador.ControladorOrdenes;
import controlador.ControladorFacturas;
import vista.VistaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ControladorClientes clientes =
                    new ControladorClientes();

            ControladorVehiculos vehiculos =
                    new ControladorVehiculos();

            ControladorServicios servicios =
                    new ControladorServicios();

            ControladorRepuestos repuestos =
                    new ControladorRepuestos();

            ControladorOrdenes ordenes =
                    new ControladorOrdenes(
                            clientes,
                            vehiculos,
                            servicios,
                            repuestos
                    );

            ControladorFacturas facturas =
                    new ControladorFacturas(ordenes);

            VistaPrincipal ventana = new VistaPrincipal(
                    clientes,
                    vehiculos,
                    servicios,
                    repuestos,
                    ordenes,
                    facturas
            );

            ventana.setVisible(true);

        });
    }
}