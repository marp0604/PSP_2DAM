package unidad02.ejercicios.ejercicio02;


import java.util.ArrayList;

/**
 * @author Miguel Angel Ramirez
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        int edad = 22;

        // 1. Crea el buffer
        BufferInterface<Integer> buffer = new Buffer<>(edad);

        // 2. Crea la lista de hilos Productores
        ArrayList<Thread > hilosProductores = new ArrayList<>();
        hilosProductores.add(new Thread(new ProductorEnteros(buffer, 5), "Productor-1"));
        hilosProductores.add(new Thread(new ProductorEnteros(buffer, 8), "Productor-2"));
        hilosProductores.add(new Thread(new ProductorEnteros(buffer, 7), "Productor-3"));
        hilosProductores.add(new Thread(new ProductorEnteros(buffer, 3), "Productor-4"));

        // 3. Crea los hilos Consumidor
        Thread consumidor1 = new Thread(new ConsumidorEnteros(buffer), "Consumidor-1");
        Thread consumidor2 = new Thread(new ConsumidorEnteros(buffer), "Consumidor-2");

        // 4. Inicia todos los hilos
        consumidor1.start();
        consumidor2.start();
        for (Thread p : hilosProductores) {
            p.start();
        }

        // 5. Espera a que todos los productores terminen
        for (Thread p : hilosProductores) {
            p.join();
        }
        System.out.println("--- Productores han terminado. ---");

        // 6. Los consumidores se interrumpen para finalizar
        System.out.println("--- Interrumpiendo consumidores... ---");
        consumidor1.interrupt();
        consumidor2.interrupt();

        // 7. Espera a que los consumidores terminen
        consumidor1.join();
        consumidor2.join();

        System.out.println("--- Simulación (wait/notify) finalizada. ---");
    }
}
