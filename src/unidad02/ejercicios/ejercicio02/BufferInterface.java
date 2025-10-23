package unidad02.ejercicios.ejercicio02;


/**
 * @author Miguel Angel Ramirez
 */
public interface BufferInterface<E> {

    /**
     * Inserta un elemento dentro del buffer. Si está lleno se espera hasta que haya espacio
     * @param elemento es el elemento a insertar
     * @throws InterruptedException Por si el hilo se interrumpe al esperar
     */
    void put(E elemento) throws InterruptedException;

    /**
     * Obtiene un elemento del buffer. Si está vacío, espera hasta que haya un elemento
     * @return El elemento obtenido
     * @throws InterruptedException Por si el hilo se interrumpe al esperar
     */
    E get() throws InterruptedException;
}
