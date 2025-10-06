package unidad01.ejemplos;


/**
 * @author Miguel Angel Ramirez
 */
public class Ej02_MiTarea implements Runnable {
    @Override
    public void run() {
        // Código que se ejecutará en el hilo
        System.out.println("Hilo ejecutándose: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Thread hilo = new Thread(new Ej02_MiTarea());
        hilo.start();


    }

}
