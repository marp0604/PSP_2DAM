package unidad02.ejercicios.ejercicio04;

/**
 * @author Miguel Angel Ramirez
 */
public class Contenedor {
    private final int ID;
    private Producto contenido;

    public Contenedor(int id, Producto contenidoInicial){
        this.ID = id;
        this.contenido = contenidoInicial;
    }

    public void llenar(Producto producto){
        this.contenido = producto;
    }

    public void vaciar(){
        this.contenido = Producto.NINGUNO;
    }

    public boolean estaVacio(){
        return this.contenido == Producto.NINGUNO;
    }

    @Override
    public String toString() {
        return "Contenedor <" + ID + "> en estos momentos contiene <" + contenido + ">";
    }
}