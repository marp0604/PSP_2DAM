package unidad02.ejercicios.ejercicio01.contador01;


/**
 * @author Miguel Angel Ramirez
 */
public class Contador01 {
    private int valor = 0;

    /**
     * Incrementa el valor de uno en uno
     * Este método NO es thread-safe y causa condición de carrera
     */
    public void incrementarSinSincronizar(){
        this.valor++;
    }

    /**
     * Incrementa el valor de uno en uno
     * El synchronized lo que hace es bloquear el método para que solo un hilo pueda ejecutarlo a la vez
     * y así evita la condición de carrera
     */
    public synchronized void incrementar(){
        this.valor++;
    }

    /**
     * Se hace synchronized para asegurar que el valor se lee lo más actualizado posible
     * @return el valor final del contador
     */
    public int getValor() {
        return this.valor;
    }
}
