package unidad02.ejercicios.ejercicio01.contador01;


/**
 * @author Miguel Angel Ramirez
 */
public class Main01 {

    private static final int NUM_HILOS = 100;
    private static final int INCREMENTO_POR_HILO = 1000;

    public static void main(String[] args) throws InterruptedException {

        Contador01 contador = new Contador01();
        Thread[] hilos = new Thread[NUM_HILOS];

        for (int i = 0; i < NUM_HILOS; i++){
            hilos[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTO_POR_HILO; j++){
                    contador.incrementarSinSincronizar();
                }
            }
            );
            hilos[i].start();
        }

        for (Thread hilo : hilos){
            hilo.join();
        }

        int valorEsperado = NUM_HILOS * INCREMENTO_POR_HILO;
        int valorObtenido = contador.getValor();

        System.out.println("---- Main01 Contador01 ----");
        System.out.println("Valor esperado: " + valorEsperado);
        System.out.println("Valor obtenido: " + valorObtenido);

        if (valorObtenido != valorEsperado){
            System.out.println("Se ha detectado una condición de carrera");
            System.out.println("Se han perdido " + (valorEsperado - valorObtenido) + " incrementos.");
        }
    }
}
