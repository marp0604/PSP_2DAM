package unidad02.ejercicios.ejercicio05;

import java.util.Random;

/**
 * Clase que representa un vehículo eléctrico que necesita cargarse.
 * Cada vehículo es un hilo independiente y quiere acceder a los puestos de carga.
 *
 * @author Miguel Angel Ramirez
 */

public class VehiculoElectrico extends Thread{
    private EstacionDeCarga estacion;
    private int idVehiculo;
    private Random random = new Random();

    /**
     * Constructor que crea un vehículo eléctrico asociado a una estación de carga
     *
     * @param estacion Estación de carga donde el vehículo quiere cargar
     * @param idVehiculo Identificador del vehículo
     */
    public VehiculoElectrico(EstacionDeCarga estacion, int idVehiculo){
        this.estacion = estacion;
        this.idVehiculo = idVehiculo;
    }

    /**
     * Método que ejecuta el ciclo de vida del vehículo:
     *  1. Solicita el acceso a la estación de carga.
     *  2. Se le asigna un puesto específico
     *  3. Simula el tiempo de carga (1 - 3 segundos)
     *  4. Libera el puesto y el acceso
     *
     *  El bloque finally garantiza que siempre se liberen los recursos, incluso si hay una excepción.
     */
    @Override
    public void run(){
        // Almacena el número de puesto asignado
        int puesto = -1;
        try{
            // Solicita el acceso a al estación
            estacion.solicitarAcceso();

            // Obtiene un puesto específico
            puesto = estacion.asignarPuesto();

            if (puesto != -1){
                System.out.println("Vehículo-" + idVehiculo + " ASIGNADO al Puesto " + puesto + ".");

                // Simula tiempo de carga aleatorio entre 1 y 3 segundos
                int duracionCarga = random.nextInt(3) + 1;
                System.out.println("Vehículo-" + idVehiculo + " INICIA Carga. Duración: " + duracionCarga + " sg.");

                Thread.sleep(duracionCarga * 1000);

                System.out.println("Vehículo-" + idVehiculo + " Carga FINALIZADA");
            } else {
                // No debería de ocurrir si el semáforo funciona bien
                System.err.println("Vehículo-" + idVehiculo + " - Error: No se pudo asignar puesto");
            }
        } catch (InterruptedException e){
            System.err.println("Vehículo-" + idVehiculo + " fue interrumpido");
            Thread.currentThread().interrupt();
        } finally {
            // Garantiza la liberación de recursos
            if (puesto != -1){
                // Libera el puesto específico
                estacion.liberarPuesto(puesto);
            }
            // Libera el permiso del semáforo
            estacion.liberarAcceso();
        }
    }
}