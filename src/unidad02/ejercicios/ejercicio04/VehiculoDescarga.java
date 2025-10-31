package unidad02.ejercicios.ejercicio04;

import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * Clase que representa un vehículo de descarga.
 * El vehículo llega al almacén con contenedores que llena con productos,
 * los intercambia por contenedores vacíos en un punto de encuentro, y repite el proceso.
 *
 * @author Miguel Angel Ramirez
 */

public class VehiculoDescarga implements Runnable{
    private Exchanger<Contenedor> puntoIntercambio;
    private Contenedor miContenedor;
    private int numCiclos;

    /**
     * Constructor que inicializa el vehículo de descarga con sus parámetros.
     *
      * @param puntoIntercambio Exchanger compartido para sincronizar el intercambio de contenedores
     * @param contenedorInicial Contenedor inicial con el que comienza el vehículo
     * @param numCiclos Número de ciclos de intercambio
     */
    public VehiculoDescarga(Exchanger<Contenedor> puntoIntercambio, Contenedor contenedorInicial, int numCiclos){
        this.puntoIntercambio = puntoIntercambio;
        this.miContenedor = contenedorInicial;
        this.numCiclos = numCiclos;
    }

    /**
     * Método que ejecuta el ciclo del vehículo de descarga.
     * En cada ciclo:
     *      - Llena su contenedor con un producto aleatorio
     *      - Espera en el punto de intercambio
     *      - Intercambia su contenedor lleno por uno vacío
     *      - Verifica que el contenedor recibido esté vacío
     *      - Espera 3 segundos antes del siguiente ciclo
     * Al terminar, muestra un mensaje
     */
    @Override
    public void run() {
        Random random = new Random();
        Producto[] productos = {Producto.SERRIN, Producto.NARANJAS, Producto.CANDADOS, Producto.AGUA, Producto.MADERA};

        try{
            for (int i = 1; i <= numCiclos; i++) {
                // 1. Llena el contenedor con un producto aleatorio
                Producto producto = productos[random.nextInt(productos.length)];
                miContenedor.llenar(producto);
                System.out.println(Thread.currentThread().getName() + ". Ciclo " + i + " Contenedor preparado para INTERCAMBIAR: " + miContenedor);

                // 2. Muestra que espera en el punto de encuentro
                System.out.println(Thread.currentThread().getName() + ": esperando punto de encuentro para intercambiar por un contenedor vacío...");

                // 3. Intercambia el contenedor
                miContenedor = puntoIntercambio.exchange(miContenedor);

                // 4. Muestra el contenedor recibido
                System.out.println(Thread.currentThread().getName() + ": se ha hecho el intercambio. Contenedor recibido: " + miContenedor);

                // 5. Verifica si el contenedor está vacío
                if (miContenedor.estaVacio()) {
                    System.out.println(Thread.currentThread().getName() + ": contenedor vacío. Listo para el próximo ciclo.");
                } else {
                    System.out.println(Thread.currentThread().getName() + ": ERROR! El contenedor recibido no estaba vacío.");
                }

                // 6. Espera 3 segundos entre iteraciones
                if (i < numCiclos) {
                    Thread.sleep(3000);
                }
            }

            // 7. Mensaje final cuando termina
            System.out.println(Thread.currentThread().getName() + ": trabajo finalizado!");
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}