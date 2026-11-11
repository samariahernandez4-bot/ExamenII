package hilos;

import modelo.OrdenTrabajo;
import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class ProcesoServicioHilo extends SwingWorker<Void, Void> {

    private final OrdenTrabajo orden;
    private final JProgressBar barraProgreso;
    private final JButton boton;
    private final Runnable alTerminar;

    public ProcesoServicioHilo(
            OrdenTrabajo orden,
            JProgressBar barraProgreso,
            JButton boton,
            Runnable alTerminar) {

        this.orden = orden;
        this.barraProgreso = barraProgreso;
        this.boton = boton;
        this.alTerminar = alTerminar;

        // Escuchar los cambios de progreso 
        this.addPropertyChangeListener(evento -> {

            if ("progress".equals(evento.getPropertyName())) {

                int progreso = (Integer) evento.getNewValue();

                barraProgreso.setValue(progreso);
                barraProgreso.setString(progreso + "%");
            }
        });
    }

    @Override
protected Void doInBackground() throws Exception {

    for (int i = 1; i <= 100; i++) {

        Thread.sleep(50);

        setProgress(i);
    }

    orden.finalizar();

    return null;
}

    @Override
    protected void done() {

        boton.setEnabled(true);

        try {

            get();

            barraProgreso.setValue(100);
            barraProgreso.setString("Completado");

            JOptionPane.showMessageDialog(null, """
                                                \u00a1Orden finalizada correctamente!
                                                El inventario ha sido actualizado.""",
                    "Proceso terminado",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            JOptionPane.showMessageDialog(
                    null,
                    "El proceso fue interrumpido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (ExecutionException e) {

            Throwable causa = e.getCause();

            barraProgreso.setString("Error");

            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo finalizar la orden:\n"
                    + causa.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        alTerminar.run();
    }
}