package unidad02.ejercicios.ejercicio02;


import java.util.LinkedList;
import java.util.List;

/**
 * Implementa un buffer para el Productor - Consumidor
 * Tiene un tamaño fijo y es "thread - safe"
 * Utiliza 'synchronized', 'wait()' y 'notifyAll()' para la sincronización
 *
 * @param <E> El tipo de elemento que almacenará el buffer.
 * @author Miguel Angel Ramirez
 */
public class Buffer<E> implements BufferInterface<E>{

    private List<E> buffer;
    private final int SIZE;

    /**
     * Construye un buffer con una capacidad fija máxima
     * @param capacidadMaxima Numéro máximo de elementos que el buffer puede tener.
     */
    public Buffer(int capacidadMaxima){
        this.SIZE = capacidadMaxima;
        this.buffer = new LinkedList<>();
    }

    /**
     * Añade un elemento al buffer.
     * Si el buffer está lleno, el hilo esperará hasta que haya un espacio.
     * @param elemento de tipo E para añadir.
     * @throws InterruptedException por si el hilo se interrumpe mientas espera.
     */
    @Override
    public synchronized void put(E elemento) throws InterruptedException{
        while (buffer.size() == SIZE){
            System.out.println(Thread.currentThread().getName() + " El buffer esta lleno. Esperando...");
            wait();
        }

        buffer.add(elemento);

        notifyAll();
    }

    /**
     * Extrae y devuelve el primer elemento del buffer.
     * Si el buffer está vacío, el hilo actual esperará hasta que un elemento esté disponible.
     * @return El primer elemento del buffer
     * @throws InterruptedException por si el hilo se interrumpe mientas espera.
     */
    @Override
    public synchronized E get() throws InterruptedException{
        while (buffer.isEmpty()){
            System.out.println(Thread.currentThread().getName() + " El buffer esta vacío. Esperando...");
            wait();
        }

        E elemento = buffer.removeFirst();
        notifyAll();
        return elemento;
    }
}
