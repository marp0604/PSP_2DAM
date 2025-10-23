package unidad02.ejercicios.ejercicio02;


import java.util.Random;

/**
 * Representa un hilo.
 * Su tarea es extraer elemento de un buffer.
 * Se ejecuta en un buffer infinito y está hecho para ser detenido de forma seguro mediante una interrupción
 *
 * @author Miguel Angel Ramirez
 */
public class ConsumidorEnteros implements Runnable {
    private BufferInterface<Integer> buffer;

    private Random random = new Random();

    /**
     * Construye un nuevo Consumidor.
     * @param buffer El buffer de donde se extraen los elementos
     */
    public ConsumidorEnteros(BufferInterface<Integer> buffer) {
        this.buffer = buffer;
    }

    /**
     * Logica del hilo consumidor.
     * Entra en un bucle que se ejecuta mientras el hilo no sea interrupted
     * En cada iteración:
     *  1. Extrae un elemento del buffer (esperando si está vacío)
     *  2. Imprime el elemento consumido
     *  3. Simula un tiempo de procesamiento (sleep)
     *
     * El hilo termina de forma limpia cuando se captura una InterruptedException.
     * Lo que rompe el bucle while y permite que el metodo run termine.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Integer elemento = buffer.get();
                System.out.println(Thread.currentThread().getName() + " ha consumido: " + elemento);
                Thread.sleep(random.nextInt(1001));
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " fué interrumpido y finalizado");
        }
    }
}
