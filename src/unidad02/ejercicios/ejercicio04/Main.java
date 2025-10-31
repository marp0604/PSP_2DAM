package unidad02.ejercicios.ejercicio04;

import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * Clase MAIN que simula la logistica de los contenedores en un almacen.
 * Coordina el intercambio de contenedores entre vehículos de carga y descarga.
 *
 * Se utiliza Exchanger para sincronizar el intercambio de contenedores.
 *
 * @author Miguel Angel Ramirez
 */

public class Main {
    public static void main(String[] args) {
        // 1. Crea el punto de intercambio
        Exchanger<Contenedor> puntoIntercambio = new Exchanger<>();

        // 2. Crea contenedores principalmente vacíos
        Contenedor contenedorDescarga = new Contenedor(1, Producto.NINGUNO);
        Contenedor contenedorCarga = new Contenedor(2, Producto.NINGUNO);

        // 3. Determina el número de ciclos aleatorio entre 3 y 5
        Random random = new Random();
        int numCiclos = 3 + random.nextInt(3);

        // 4. Muestra un mensaje inicial
        System.out.println("--- INICIO SIMULACIÓN LOGÍSTICA DE CONTENEDORES ---");
        System.out.println("Vehículo de descarga comienza con: " + contenedorDescarga);
        System.out.println("Vehículo de carga comienza con: " + contenedorCarga);
        System.out.println("---------------------------------------------------");
        System.out.println();

        // 5. Crea los vehículos
        VehiculoDescarga vehiculoDescarga = new VehiculoDescarga(puntoIntercambio, contenedorDescarga, numCiclos);
        VehiculoCarga vehiculoCarga = new VehiculoCarga(puntoIntercambio, contenedorCarga, numCiclos);

        // 6. Crea e inicia los hilos
        Thread hiloDescarga = new Thread(vehiculoDescarga, "Vehículo de descarga");
        Thread hiloCarga = new Thread(vehiculoCarga, "Vehículo de carga");

        hiloDescarga.start();
        hiloCarga.start();

        // 7. Espera a que ambos hilos terminen
        try {
            hiloDescarga.join();
            hiloCarga.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 8. Muestra mensaje final
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        System.out.println("Todos los vehículos han completado sus ciclos. Fin del programa.");
    }
}
