package unidad02.ejercicios.ejercicio03;

import java.util.Random;

/**
 * Representa un vehículo como un hilo.
 * @author Miguel Angel Ramirez
 */
public class Vehiculo implements Runnable{
    private ControlTrafico carril;
    private Direccion direccion;
    private String nombreVehiculo;
    private Random random = new Random();

    /**
     * Constructor de la clase
     * @param carril El monitor ControlTrafico compartido por todos los vehículos
     * @param direccion La Direccion en la que se moverá el vehículo
     * @param nombreVehiculo El nombre del vehículo
     */
    public Vehiculo(ControlTrafico carril, Direccion direccion, String nombreVehiculo){
        this.carril = carril;
        this.direccion = direccion;
        this.nombreVehiculo = nombreVehiculo;
    }

    /**
     * Define el vehículo (hilo)
     * El vehículo intenta entrar, cruza y luego sale
     */
    @Override
    public void run() {
        try{
            System.out.println(nombreVehiculo + " circulando hacia la " + direccion + " ha llegado y quiere entrar");
            carril.entrar(direccion);
            System.out.println(nombreVehiculo + " circulando hacia la " + direccion + " está cruzando el carril");

            Thread.sleep(1000 + random.nextInt(2000));
            carril.salir(direccion);
            System.out.println(nombreVehiculo + " circulando hacia la " + direccion + " sale del carril");
        } catch (InterruptedException e){
            System.err.println(nombreVehiculo + " circulando hacia la " + direccion + " ha sido interrumpido");
            Thread.currentThread().interrupt();
        }
    }
}
