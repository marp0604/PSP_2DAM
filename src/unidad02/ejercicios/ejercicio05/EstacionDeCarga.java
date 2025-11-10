package unidad02.ejercicios.ejercicio05;

import java.util.concurrent.Semaphore;

/**
 * Clase que gestiona el acceso a estaciones de carga eléctrica mediante semáforos.
 * Controla que solo un número limitado de vehículos se puedan cargar a la vez.
 *
 * @author Miguel Angel Ramirez
 */

public class EstacionDeCarga {
    private Semaphore gestionarAcceso;
    private boolean[] puestosOcupados;
    private Object bloqueoPuestos;

    /**
     * Constructor que inicializa la estación con un número específico de puestos.
     * Todos los puestos se crean libres (false)
     *
     * @param numPuestos Número de puestos de carga disponibles
     */
    public EstacionDeCarga(int numPuestos) {
        this.gestionarAcceso = new Semaphore(numPuestos);
        this.puestosOcupados = new boolean[numPuestos];
        this.bloqueoPuestos = new Object();

        for (int i = 0; i < numPuestos; i++) {
            puestosOcupados[i] = false;
        }
    }

    /**
     * Solicita permiso para acceder a un puesto de carga
     * Si no hay puestos disponibles, el hilo espera hasta que se libere uno.
     *
     * @throws InterruptedException Si el hilo es interrumpido mientras espera.
     */
    public void solicitarAcceso() throws InterruptedException {
        System.out.println("Vehículo LLEGA. Esperando permiso del Semáforo... ");
        gestionarAcceso.acquire();
        System.out.println("Permiso CONCEDIDO. Buscando puesto...");
    }

    /**
     * Libera el permiso del semáforo, permitiendo que otro vehículo pueda acceder.
     * Muestra cuántos puestos quedan disponibles.
     */
    public void liberarAcceso() {
        gestionarAcceso.release();
        System.out.println("Permiso LIBERADO. Puestos disponibles: " + gestionarAcceso.availablePermits());
    }

    /**
     * Asigna un puesto libre al vehículo
     * Sincronizando para evitar que dos hilos asignen el mismo puesto.
     *
     * @return Número de puestos asignados p -1 si no hay puestos libres
     */
    public int asignarPuesto() {
        synchronized (bloqueoPuestos) {
            for (int i = 0; i < puestosOcupados.length; i++) {
                if (!puestosOcupados[i]) {
                    puestosOcupados[i] = true;
                    return i + 1;
                }
            }
            return -1;
        }
    }

    /**
     * Libera un puesto específico marcándolo como disponible.
     * Sincronizando para evitar problemas.
     *
     * @param numeroPuesto Número de puesto a liberar
     *
     */
    public void liberarPuesto(int numeroPuesto) {
        synchronized (bloqueoPuestos) {
            if (numeroPuesto >= 1 && numeroPuesto <= puestosOcupados.length) {
                puestosOcupados[numeroPuesto - 1] = false;
            } else {
                System.err.println("Error: Número de puesto incorrecto: " + numeroPuesto);
            }
        }
    }
}