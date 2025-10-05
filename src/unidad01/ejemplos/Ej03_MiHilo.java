package unidad01.ejemplos;


/**
 * @author Miguel Angel Ramirez
 */
public class Ej03_MiHilo extends Thread{
    @Override
    public void run(){
        // Código que se ejecutará en el hilo
        System.out.println("Hilo ejecutándose: " + getName());
    }

    public static void main(String[] args) {
        Ej03_MiHilo hilo = new Ej03_MiHilo();
        hilo.start();
    }
}
