package unidad02.ejercicios.ejercicio03;


import java.util.Random;

/**
 * @author Miguel Angel Ramirez
 */
public class Vehiculo implements Runnable{
    private ControlTrafico carril;
    private Direccion direccion;
    private String nombreVehiculo;
    private Random random = new Random();

    public Vehiculo(ControlTrafico carril, Direccion direccion, String nombreVehiculo){
        this.carril = carril;
        this.direccion = direccion;
        this.nombreVehiculo = nombreVehiculo;
    }

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
