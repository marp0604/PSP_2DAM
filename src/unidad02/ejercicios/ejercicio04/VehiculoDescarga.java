package unidad02.ejercicios.ejercicio04;

import java.util.concurrent.Exchanger;

/**
 * @author Miguel Angel Ramirez
 */

public class VehiculoDescarga implements Runnable{
    private Exchanger<Contenedor> puntoIntercambio;
    private Contenedor miContenedor;
    private int numCiclos;

    public VehiculoDescarga(Exchanger<Contenedor> puntoIntercambio, Contenedor contenedorInicial, int numCiclos){
        this.puntoIntercambio = puntoIntercambio;
        this.miContenedor = contenedorInicial;
        this.numCiclos = numCiclos;
    }

    @Override
    public void run() {
    }
}