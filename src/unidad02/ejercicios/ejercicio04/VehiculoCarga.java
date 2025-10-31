package unidad02.ejercicios.ejercicio04;

import java.util.concurrent.Exchanger;

/**
 * Clase que representa un vehículo de carga.
 * Este vehículo llega al almacén con los contenedores vacíos,
 * los intercambio por contenedores llenos, vacía los contenedores recibidos y repite el proceso.
 *
 * El vehículo funciona con un hilo independiente que se sincroniza
 * con vehículos de descarga mediante el Exchanger.
 *
 * @author Miguel Angel Ramirez
 */

public class VehiculoCarga implements Runnable {
    private Exchanger<Contenedor> puntoIntercambio;
    private Contenedor miContenedor;
    private int numCiclos;

    /**
     * Constructor que inicializa el vehículo de carga
     *
     * @param puntoIntercambio Exchanger compartido para sincronizar el intercambio de contenedores
     * @param contenedorInicial
     * @param numCiclos  Número de ciclos de intercambio
     */
    public VehiculoCarga(Exchanger<Contenedor> puntoIntercambio, Contenedor contenedorInicial, int numCiclos) {
        this.puntoIntercambio = puntoIntercambio;
        this.miContenedor = contenedorInicial;
        this.numCiclos = numCiclos;
    }

    /**
     * Método que ejecuta el ciclo de trabajo del vehículo de carga
     * En cada ciclo:
     *      - Vacía su contenedor para dejarlo listo
     *      - Espera en el punto de intercambio
     *      - Intercambia su contenedor vacío por uno lleno
     *      - Vacía el contenedor recibido
     *      - Espera 3 segundos antes del siguiente ciclo
     * Al finalizar todos los ciclos, muestra un mensaje.
     */
    @Override
    public void run() {
        try {
            for (int i = 1; i <= numCiclos; i++) {
                // 1. Asegura que el contenedor está vacío
                miContenedor.vaciar();
                System.out.println(Thread.currentThread().getName() + ". Ciclo " + i + " Contenedor preparado para INTERCAMBIAR: " + miContenedor);

                // 2. Muestra que espera en el punto de encuentro
                System.out.println(Thread.currentThread().getName() + ": esperando punto de encuentro para intercambiar por un contenedor lleno...");

                // 3. Intercambia el contenedor
                miContenedor = puntoIntercambio.exchange(miContenedor);

                // 4. Muestra el contenedor recibido
                System.out.println(Thread.currentThread().getName() + ": se ha hecho el intercambio. Contenedor recibido: " + miContenedor);

                // 5. Verifica si el contenedor está lleno
                if (!miContenedor.estaVacio()) {
                    System.out.println(Thread.currentThread().getName() + ": contenedor lleno. Vaciando contenedor...");
                    miContenedor.vaciar();
                    System.out.println(Thread.currentThread().getName() + ": contenedor vaciado. Listo para el próximo ciclo.");
                } else {
                    System.out.println(Thread.currentThread().getName() + ": ERROR! El contenedor recibido estaba vacío.");
                }

                // 6. Espera 3 segundos entre iteraciones
                if (i < numCiclos) {
                    Thread.sleep(3000);
                }
            }

            // 7. Mensaje final cuando termina
            System.out.println(Thread.currentThread().getName() + ": trabajo finalizado!");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}