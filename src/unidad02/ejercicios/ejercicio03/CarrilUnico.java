package unidad02.ejercicios.ejercicio03;


/**
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
