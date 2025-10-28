package unidad02.ejercicios.ejercicio03;

/**
 * Implementa la interfaz CarrilUnico para gestionar el tráfico en un carril.
 * La clase actúa como monitor que permite el paso de diferentes vehículos siempre en la misma dirección
 * Si el carril está ocupado por vehiculos en la dirección contraria, los nuevos vehículos deben esperar.
 * @author Miguel Angel Ramirez
 */

public class ControlTrafico implements CarrilUnico{

    private Direccion direccionActual;
    private int cochesEnCarril = 0;
    private boolean viaOcupada = false;

    /**
     * Permite a un vehículo entrar en el carril
     * El vehículo debe esperar wait() si ya hay coches dentro del carril.
     *
     * @param direccion La dirección del vehículo que entra
     * @throws InterruptedException Si el hilo se interrumpe mientras espera
     */
    @Override
    public synchronized void entrar(Direccion direccion) throws InterruptedException {

        while(viaOcupada && direccionActual != direccion){
            System.out.println("--- Espera: " + Thread.currentThread().getName() + ". va " + direccion
                               + ". Carril ocupado por coches hacia " + direccionActual);
            wait();
        }

        if (cochesEnCarril == 0) {
            direccionActual = direccion;
            viaOcupada = true;
        }

        cochesEnCarril++;

        System.out.println("> > > - Entra: " + Thread.currentThread().getName() + ". va " + direccion);
        System.out.println("--- Estado: " + cochesEnCarril + " coches || Dirección: " + direccionActual);
    }

    /**
     * Registra la salida de un vehículo del carril
     * Si es el último vehículo en salir (cochesEnCarril == 0),
     * se resetea la dirección del carril a NINGUNA, la via se pone como no ocupada y se notifica a todos los hilos.
     *
     * @param direccion La dirección del vehículo que sale
     */
    @Override
    public synchronized void salir(Direccion direccion) {
        cochesEnCarril--;
        System.out.println("< < < - Sale. " + Thread.currentThread().getName() +
                           " iba " + direccion + ". Quedan " + cochesEnCarril);

        if (cochesEnCarril == 0){
            direccionActual = Direccion.NINGUNA;
            viaOcupada = false;
            System.out.println("--- Estado: Carril Libre");
            notifyAll();
        }
    }
}
