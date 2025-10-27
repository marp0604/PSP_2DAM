package unidad02.ejercicios.ejercicio03;


/**
 * @author Miguel Angel Ramirez
 */
public class ControlTrafico implements CarrilUnico{

    private Direccion direccionActual;
    private int cochesEnCarril = 0;
    private boolean viaOcupada = true;

    @Override
    public synchronized void entrar(Direccion direccion) throws InterruptedException {
        while(cochesEnCarril > 0 && direccionActual != direccion){
            System.out.println("Espera. " + Thread.currentThread().getName() + ". va " + direccion +
                               ". Carril ocupado por coches hacia " + direccionActual);
            wait();
        }

        cochesEnCarril++;
        viaOcupada = true;
        direccionActual = direccion;

        System.out.println("Entra: " + Thread.currentThread().getName() + ". va " + direccion);
        System.out.println("Estado: " + cochesEnCarril + " coches || Direccion: " + direccionActual);
    }

    @Override
    public synchronized void salir(Direccion direccion) {
        cochesEnCarril--;
        System.out.println("Sale. " + Thread.currentThread().getName() +
                           " iba " + direccion + ". Quedan " + cochesEnCarril);

        if (cochesEnCarril == 0){
            direccionActual = Direccion.NINGUNA;
            viaOcupada = false;
            System.out.println("Estado: Carril Libre");
            notifyAll();
        }
    }
}
