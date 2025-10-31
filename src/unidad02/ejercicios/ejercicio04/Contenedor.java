package unidad02.ejercicios.ejercicio04;

/**
 * Clase que representa un contenedor.
 * Cada contenedor tiene un ID y puede tener un producto o estar vacío
 *
 * Los contenedores son intercambiados entre vehiculos de carga y descarga.
 *
 * @author Miguel Angel Ramirez
 */

public class Contenedor {
    private final int ID;
    private Producto contenido;

    /**
     * Constructor que crea un contenedor con un ID y un contenidoInicial
     *
     * @param id Identificador único del contenedor
     * @param contenidoInicial Producto inicial
     */
    public Contenedor(int id, Producto contenidoInicial){
        this.ID = id;
        this.contenido = contenidoInicial;
    }

    /**
     * Llena el contenedor con un producto específico
     *
     * @param producto Producto con el que se llenará en el contenedor-
     */
    public void llenar(Producto producto){
        this.contenido = producto;
    }

    /**
     * Vacía el contenedor estableciendo el producto a NINGUNO
     */
    public void vaciar(){
        this.contenido = Producto.NINGUNO;
    }

    /**
     * Comprueba si el contenedor está vacío
     *
     * @return true si el contenedor no contiene ningún producto, false en caso contrario
     */
    public boolean estaVacio(){
        return this.contenido == Producto.NINGUNO;
    }

    /**
     * Devuelve el contenido del contenedor
     *
     * @return String con el formato pedido
     */
    @Override
    public String toString() {
        return "Contenedor <" + ID + "> en estos momentos contiene <" + contenido + ">";
    }
}