package unidad02.ejercicios.ejercicio03;

import java.util.ArrayList;

/**
 * @author Miguel Angel Ramirez
 */

public class Main {
    public static void main(String[] args) throws InterruptedException{
        System.out.println("--- TRAFICO ---");

        // 1. Se crea un único monitor
        ControlTrafico carril = new ControlTrafico();

        // 2. Se crea una lista para los hilos
        ArrayList<Thread> vehiculos = new ArrayList<>();
        int totalVehiculos = 10;

        // 3. Se crean los vehículos e hilos
        for(int i = 0; i < totalVehiculos; i++){
            Direccion direccion = (i % 2 == 0) ? Direccion.IZDA : Direccion.DCHA;
            String nombre = "Coche-" + (i + 1);

            Vehiculo vehiculoRunnable = new Vehiculo(carril, direccion, nombre);
            Thread hiloVehiculo = new Thread(vehiculoRunnable, nombre);
            vehiculos.add(hiloVehiculo);
        }

        // 4. Pausa de 1 segundo
        Thread.sleep(1000);

        // 5. Se inician los hilos
        System.out.println("Total de Vehículos: " + totalVehiculos);
        for (Thread thread : vehiculos){
            thread.start();
        }

        // 6. Espera a que todos los hilos terminen
        for (Thread thread : vehiculos){
            thread.join();
        }

        System.out.println("Todos los vehículos han cambiado de carril");
    }
}
