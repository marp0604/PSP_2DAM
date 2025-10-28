package unidad02.ejercicios.ejercicio03;


/**
 * Cualquier clase que implemente esta interfaz
 * debe gestionar los métodos entrar y salir de forma sincronizada.
 *
 * @author Miguel Angel Ramirez
 */
public interface CarrilUnico {
    /**
     * Intenta entrar en el carril. Si está ocupado, el hilo debe esperar.
     * @param direccion La dirección del vehículo que entra
     * @throws InterruptedException Si el hilo se interrumpe mientras espera
     */
    void entrar(Direccion direccion) throws InterruptedException;

    /**
     * Sale del carril. Debe notificar a los hilos que espera
     * @param direccion La dirección del vehículo que sale
     */
    void salir(Direccion direccion);
}
