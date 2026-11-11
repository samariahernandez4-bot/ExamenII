package vista;

import controlador.ControladorOrdenes;
import controlador.ControladorFacturas;
import modelo.OrdenTrabajo;
import modelo.Factura;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VistaReportes extends JPanel {

    private final ControladorOrdenes controladorOrdenes;
    private final ControladorFacturas controladorFacturas;

    private int pendientes;
    private int enProceso;
    private int finalizadas;
    private int canceladas;

    private double ingresos;
    private double ingresosPendientes;

    private JLabel lblTotalOrdenes;
    private JLabel lblIngresos;
    private JLabel lblFinalizadas;
    private JLabel lblPendientes;

    private PanelGraficos panelGraficos;

    private final Color AZUL = new Color(52, 152, 219);
    private final Color VERDE = new Color(46, 204, 113);
    private final Color NARANJA = new Color(243, 156, 18);
    private final Color ROJO = new Color(231, 76, 60);
    private final Color FONDO = new Color(245, 247, 250);

    public VistaReportes(
            ControladorOrdenes controladorOrdenes,
            ControladorFacturas controladorFacturas) {

        this.controladorOrdenes = controladorOrdenes;
        this.controladorFacturas = controladorFacturas;

        setLayout(new BorderLayout(15, 15));
        setBackground(FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        crearEncabezado();
        crearTarjetas();
        crearGraficos();

        actualizarDatos();
    }

    private void crearEncabezado() {

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(FONDO);

        JLabel titulo = new JLabel("Reportes del taller");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        titulo.setForeground(new Color(44, 62, 80));

        JLabel subtitulo = new JLabel(
                "Resumen general de actividad e ingresos"
        );

        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(Color.GRAY);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 5));
        textos.setBackground(FONDO);

        textos.add(titulo);
        textos.add(subtitulo);

        encabezado.add(textos, BorderLayout.WEST);

        add(encabezado, BorderLayout.NORTH);
    }

    private void crearTarjetas() {

        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 15, 15));
        tarjetas.setBackground(FONDO);

        lblTotalOrdenes = new JLabel("0");
        lblIngresos = new JLabel("L. 0.00");
        lblFinalizadas = new JLabel("0");
        lblPendientes = new JLabel("0");

        tarjetas.add(crearTarjeta(
                "Total de órdenes", lblTotalOrdenes, AZUL
        ));

        tarjetas.add(crearTarjeta(
                "Ingresos cobrados", lblIngresos, VERDE
        ));

        tarjetas.add(crearTarjeta(
                "Órdenes finalizadas", lblFinalizadas, NARANJA
        ));

        tarjetas.add(crearTarjeta(
                "Órdenes pendientes", lblPendientes, ROJO
        ));

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(FONDO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));

        contenedor.add(tarjetas, BorderLayout.CENTER);

        add(contenedor, BorderLayout.CENTER);
    }

    private JPanel crearTarjeta(
            String titulo,
            JLabel valor,
            Color color) {

        JPanel tarjeta = new JPanel(new BorderLayout(5, 15));

        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 230, 235)
                ),
                BorderFactory.createEmptyBorder(18, 15, 18, 15)
        ));

        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, 13));
        etiqueta.setForeground(Color.GRAY);

        valor.setFont(new Font("Arial", Font.BOLD, 23));
        valor.setForeground(color);

        tarjeta.add(etiqueta, BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);

        return tarjeta;
    }

    private void crearGraficos() {

        JPanel contenedorGraficos = new JPanel(new GridLayout(1, 2, 15, 15));
        contenedorGraficos.setBackground(FONDO);

        JPanel panelOrdenes = crearPanelGrafico(
                "Órdenes por estado"
        );

        JPanel panelIngresos = crearPanelGrafico(
                "Resumen de ingresos"
        );

        panelGraficos = new PanelGraficos();

        contenedorGraficos.add(panelOrdenes);
        contenedorGraficos.add(panelIngresos);

        panelOrdenes.add(panelGraficos, BorderLayout.CENTER);

        PanelIngresos graficoIngresos = new PanelIngresos();
        panelIngresos.add(graficoIngresos, BorderLayout.CENTER);

        add(contenedorGraficos, BorderLayout.SOUTH);
    }

    private JPanel crearPanelGrafico(String titulo) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 230, 235)
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 16));
        etiqueta.setForeground(new Color(44, 62, 80));

        panel.add(etiqueta, BorderLayout.NORTH);

        return panel;
    }

    public void actualizarDatos() {

        pendientes = 0;
        enProceso = 0;
        finalizadas = 0;
        canceladas = 0;

        ArrayList<OrdenTrabajo> ordenes =
                controladorOrdenes.listarOrdenes();

        for (OrdenTrabajo orden : ordenes) {

            switch (orden.getEstado()) {

                case PENDIENTE:
                    pendientes++;
                    break;

                case EN_PROCESO:
                    enProceso++;
                    break;

                case FINALIZADA:
                    finalizadas++;
                    break;

                case CANCELADA:
                    canceladas++;
                    break;
            }
        }

        ingresos = controladorFacturas.calcularIngresosTotales();

        ingresosPendientes = 0;

        for (Factura factura : controladorFacturas.listarFacturas()) {

            if (!factura.isPagada()) {
                ingresosPendientes += factura.getTotal();
            }
        }

        int totalOrdenes = pendientes + enProceso
                + finalizadas + canceladas;

        lblTotalOrdenes.setText(String.valueOf(totalOrdenes));
        lblIngresos.setText(String.format("L. %.2f", ingresos));
        lblFinalizadas.setText(String.valueOf(finalizadas));
        lblPendientes.setText(String.valueOf(pendientes));

        repaint();
    }

    private class PanelGraficos extends JPanel {

        public PanelGraficos() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(300, 230));
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            dibujarBarra(g2, "Pendientes", pendientes, AZUL, 15);
            dibujarBarra(g2, "En proceso", enProceso, NARANJA, 65);
            dibujarBarra(g2, "Finalizadas", finalizadas, VERDE, 115);
            dibujarBarra(g2, "Canceladas", canceladas, ROJO, 165);

            g2.dispose();
        }

        private void dibujarBarra(
                Graphics2D g2,
                String nombre,
                int cantidad,
                Color color,
                int y) {

            int maximo = Math.max(
                    1,
                    Math.max(
                            Math.max(pendientes, enProceso),
                            Math.max(finalizadas, canceladas)
                    )
            );

            int anchoDisponible = Math.max(80, getWidth() - 145);

            int ancho = (int) (
                    (double) cantidad / maximo * anchoDisponible
            );

            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(new Color(80, 90, 100));

            g2.drawString(nombre, 5, y + 17);

            g2.setColor(new Color(235, 238, 242));
            g2.fillRoundRect(105, y, anchoDisponible, 23, 10, 10);

            if (cantidad > 0) {
                g2.setColor(color);
                g2.fillRoundRect(
                        105, y, Math.max(ancho, 8), 23, 10, 10
                );
            }

            g2.setColor(Color.DARK_GRAY);
            g2.drawString(
                    String.valueOf(cantidad),
                    115 + anchoDisponible,
                    y + 17
            );
        }
    }

    private class PanelIngresos extends JPanel {

        public PanelIngresos() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(300, 230));
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            double mayor = Math.max(
                    ingresos,
                    ingresosPendientes
            );

            if (mayor == 0) {
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                g2.drawString(
                        "Todavía no hay ingresos registrados.",
                        10, 45
                );
                g2.dispose();
                return;
            }

            dibujarIngreso(
                    g2,
                    "Cobrados",
                    ingresos,
                    VERDE,
                    mayor,
                    35
            );

            dibujarIngreso(
                    g2,
                    "Pendientes",
                    ingresosPendientes,
                    NARANJA,
                    mayor,
                    110
            );

            g2.dispose();
        }

        private void dibujarIngreso(
                Graphics2D g2,
                String nombre,
                double cantidad,
                Color color,
                double mayor,
                int y) {

            int anchoDisponible = Math.max(80, getWidth() - 30);

            int ancho = (int) (
                    cantidad / mayor * anchoDisponible
            );

            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.setColor(new Color(70, 80, 90));

            g2.drawString(nombre, 5, y);

            g2.setColor(new Color(235, 238, 242));
            g2.fillRoundRect(
                    5, y + 12, anchoDisponible, 22, 10, 10
            );

            if (cantidad > 0) {
                g2.setColor(color);
                g2.fillRoundRect(
                        5, y + 12, Math.max(ancho, 8), 22, 10, 10
                );
            }

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            g2.drawString(
                    String.format("L. %.2f", cantidad),
                    5, y + 55
            );
        }
    }
}