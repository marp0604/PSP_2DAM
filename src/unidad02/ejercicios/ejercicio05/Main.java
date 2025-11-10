package unidad02.ejercicios.ejercicio05;

/**
 * Clase principal que simula la llegada de vehículos eléctricos a una estación de carga.
 *
 * @author Miguel Angel Ramirez
 */
public class Main {

    private static final int PUESTOS_DISPONIBLES = 5;
    private static final int VEHICULOS_QUE_LLEGAN = 10;

    public static void main(String[] args) {
        // Crea la estación de carga
        EstacionDeCarga estacion = new EstacionDeCarga(PUESTOS_DISPONIBLES);

        System.out.println("Estación Eléctrica");
        System.out.println("Número de puestos disponibles: " + PUESTOS_DISPONIBLES);
        System.out.println("Número de vehículos que llegarán: " + VEHICULOS_QUE_LLEGAN);

        // Crea un array de vehículos
        VehiculoElectrico[] vehiculos = new VehiculoElectrico[VEHICULOS_QUE_LLEGAN];

        // Inicializa cada vehículo con su id
        for (int i = 0; i < vehiculos.length; i++){
            vehiculos[i] = new VehiculoElectrico(estacion, i + 1);
        }

        // Inicia todos los hilos con un pequeño sleep
        for (VehiculoElectrico vehiculo : vehiculos){
            vehiculo.start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.err.println("Error: El hilo fue interrumpido - " + e.getMessage());
            }
        }

        // Espera a que todos los vehículos terminen de cargar
        for (VehiculoElectrico vehiculo : vehiculos){
            try {
                // Espera a que el hilo termine
                vehiculo.join();
            } catch (InterruptedException e) {
                System.err.println("Error: El hilo fue interrumpido - " + e.getMessage());
            }
        }

        System.out.println("--------------------------");
    }
}