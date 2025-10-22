package unidad02.ejercicios.ejercicio01.contador02;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Miguel Angel Ramirez
 */
public class Contador02 {
    private AtomicInteger valor = new AtomicInteger(0);

    /**
     * Incrementa el valor de uno en uno
     * Esta operación es thread-safe
     */
    public void incrementar(){
        this.valor.incrementAndGet();
    }

    /**
     * @return el valor final del contador
     */
    public int getValor(){
        return this.valor.get();
    }
}
