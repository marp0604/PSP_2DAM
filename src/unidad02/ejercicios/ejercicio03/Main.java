package unidad02.ejercicios.ejercicio03;


import java.util.ArrayList;

/**
 * @author Miguel Angel Ramirez
 */
public class Main {
    public static void main(String[] args) throws InterruptedException{
        System.out.println("--- TRAFICO ---");

        ControlTrafico carril = new ControlTrafico();

        ArrayList<Thread> vehiculos = new ArrayList<>();
        int totalVehiculos = 10;

        for(int i = 0; i < totalVehiculos; i++){
            Direccion direccion = (i % 2 == 0) ? Direccion.IZDA : Direccion.DCHA;
            String nombre = "Coche-" + (i + 1);

            Vehiculo vehiculo = new Vehiculo(carril, direccion, nombre);
            Thread hilo = new Thread(vehiculo, nombre);
            vehiculos.add(hilo);
        }

        Thread.sleep(1000);

        System.out.println("Total de Vehículos: " + totalVehiculos);
        for (Thread thread : vehiculos){
            thread.start();
        }

        for (Thread thread : vehiculos){
            thread.join();
        }

        System.out.println("Todos los vehículos han cambiado de carril");
    }
}
