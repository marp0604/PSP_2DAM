package unidad02.ejercicios.ejercicio05;


import java.util.concurrent.Semaphore;

/**
 * @author Miguel Angel Ramirez
 */
public class EstacionDeCarga {
    private Semaphore gestionarAcceso;
    private boolean[] puestosOcupados;
    private Object bloqueoPuestos;

    public EstacionDeCarga(int numPuestos){
        this.gestionarAcceso = new Semaphore(numPuestos);

    }
}
