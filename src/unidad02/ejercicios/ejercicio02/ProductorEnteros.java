package unidad02.ejercicios.ejercicio02;

import java.util.Random;

/**
 * Representa un hilo "Productor"
 *  Su tarea es generar una cantidad fija de elementos y depositarlos en un buffer compartido.
 *  A diferencia del Consumidor, este hilo tiene un trabajo finito y termina su ejecución por sí solo.
 *
 * @author Miguel Angel Ramirez
 */
public class ProductorEnteros implements Runnable{
    private BufferInterface<Integer> buffer;
    private int numElementos;

    private Random random = new Random();

    /**
     * Construye un nuevo Productor.
     *
     * @param buffer El buffer donde se depositarán los elementos.
     * @param numElementos La cantidad total de elementos que este hilo debe producir.
     */
    public ProductorEnteros(BufferInterface<Integer> buffer, int numElementos){
        this.buffer = buffer;
        this.numElementos = numElementos;
    }

    /**
     * La logica del hilo productor.
     * Ejecuta un bucle con un número finito de veces (numElementos).
     * En cada iteración:
     *  1. Genera un entero aleatorio (0-100).
     *  2. Lo añade al buffer (buffer.put()), esperando si está lleno.
     *  3. Imprime un mensaje confirmando la producción.
     *  4. Simula un tiempo de producción (sleep 0-3 segundos).
     * Una vez que el bucle termina, el hilo imprime un mensaje de finalización
     * y termina su ejecución de forma natural.
     *
     * Si el hilo es interrumpido captura la excepción, imprime el error.
     */
    @Override
    public void run(){
        try{
            for (int i = 0; i < numElementos; i++){
                int elemento = random.nextInt(101);
                buffer.put(elemento);
                System.out.println(Thread.currentThread().getName() + " acaba de producir: " + elemento);
                Thread.sleep(random.nextInt(3001));
            }
            System.out.println("--- " + Thread.currentThread().getName() + " ha terminado de producir ---");
        } catch (InterruptedException e){
            System.err.println(Thread.currentThread().getName() + " fue interrumpido");
            Thread.currentThread().interrupt();
        }
    }
}
