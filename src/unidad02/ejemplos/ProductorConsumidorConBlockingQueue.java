package unidad02.ejemplos;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Miguel Angel Ramirez
 */

public class ProductorConsumidorConBlockingQueue {


    // Clase Productor
    public static class Productor implements Runnable {
        private final BlockingQueue<Integer> cola;
        private final int numElementos;


        public Productor(BlockingQueue<Integer> cola, int numElementos) {
            this.cola = cola;
            this.numElementos = numElementos;
        }

        @Override
        public void run() {
            for (int i = 0; i < numElementos; i++) {
                try {
                    // Añade a la cola. Si está llena, se bloquea aquí
                    cola.put(i);
                    System.out.println("Productor: Puso " + i + ". Tamaño cola: " + cola.size());
                    Thread.sleep(200); // Simular producción
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Productor interrumpido.");
                }
            }
            System.out.println("Productor: Finalizó producción.");
        }
    }

    // Clase Consumidor
    public static class Consumidor implements Runnable {
        private final BlockingQueue<Integer> cola;
        private final int numElementos;

        public Consumidor(BlockingQueue<Integer> cola, int numElementos) {
            this.cola = cola;
            this.numElementos = numElementos;
        }

        @Override
        public void run() {
            for (int i = 0; i < numElementos; i++) {
                try {
                    // Toma de la cola. Si está vacía, se bloquea aquí
                    Integer elemento = cola.take();
                    System.out.println("Consumidor: Tomó " + elemento + ". Tamaño cola: " + cola.size());
                    Thread.sleep(500); // Simular consumo
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Consumidor interrumpido.");
                }
            }
            System.out.println("Consumidor: Finalizó consumo.");
        }
    }

    public static void main(String[] args) {
        // ArrayBlockingQueue con capacidad de 5 elementos
        BlockingQueue<Integer> colaCompartida = new ArrayBlockingQueue<>(5);

        int totalElementos = 10;

        // Crear y lanzar hilos
        Thread productorThread = new Thread(
                new Productor(colaCompartida, totalElementos), "Hilo-Productor");
        Thread consumidorThread = new Thread(
                new Consumidor(colaCompartida, totalElementos), "Hilo-Consumidor");

        productorThread.start();
        consumidorThread.start();

        try {
            // Esperar a que ambos terminen
            productorThread.join();
            consumidorThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Hilo principal interrumpido.");
        }

        System.out.println("\n--- Ejecución Finalizada ---");
        System.out.println("Tamaño final de la cola: " + colaCompartida.size());
    }
}