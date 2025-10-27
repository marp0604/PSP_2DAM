package unidad02.ejercicios.ejercicio03;


/**
 * @author Miguel Angel Ramirez
 */
public class Vehiculo implements Runnable{
    private ControlTrafico carril;
    private Direccion direccion;
    private String nombreVehiculo;

    public Vehiculo(ControlTrafico carril, Direccion direccion, String nombreVehiculo){
        this.carril = carril;
        this.direccion = direccion;
        this.nombreVehiculo = nombreVehiculo;
    }

    @Override
    public void run() {

    }
}
