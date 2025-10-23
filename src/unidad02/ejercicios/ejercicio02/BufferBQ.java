package unidad02.ejercicios.ejercicio02;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Miguel Angel Ramirez
 */
public class BufferBQ <E> implements BufferInterface<E> {
    private BlockingQueue<E> queue;

    /**
     * Construye un nuevo buffer.
     * Inicializa la ArrayBlockingQueue con la capacidad máxima especifica
     * @param size Tamaño máximo fijo de la cola.
     */
    public BufferBQ(int size) {
        this.queue = new ArrayBlockingQueue<>(size);
    }

    /**
     * Añade un elemento al buffer
     * Si la cola está llena, el método se bloqueará y esperará hasta que haya un espacio libre.
     *
     * @param elemento de tipo E para añadir.
     * @throws InterruptedException por si el hilo se interrumpe mientas espera.
     */
    @Override
    public void put(E elemento) throws InterruptedException {
        queue.put(elemento);
    }

    /**
     * Extrae y devuelve el primer elemento del buffer.
     * Si la cola está vacía, este método se bloqueará y esperará hasta que un elemento este disponible
     *
     * @return El primer elemento del buffer
     * @throws InterruptedException por si el hilo se interrumpe mientas espera.
     */
    @Override
    public E get() throws InterruptedException {
        return queue.take();
    }
}
