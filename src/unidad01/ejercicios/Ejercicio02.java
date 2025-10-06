package unidad01.ejercicios;


import java.io.File;
import java.io.IOException;

/**
 * @author Miguel Angel Ramirez
 */
public class Ejercicio02 {
    public static void main(String[] args) {

    }

    public static void lanzarComando(String ruta) throws IllegalArgumentException, IOException, InterruptedException {
        File directorio = new File(ruta);

        // Validamos que el directorio existe y que si es un directorio
        if (!directorio.exists()){
            throw new IllegalArgumentException("El directorio " + ruta + " no existe");
        }

        if (!directorio.isDirectory()){
            throw new IllegalArgumentException(ruta + " no es un directorio");
        }

        // Obtenemos que SO es el que estamos utilizando
        String sistemaOperativo = System.getProperty("os name").toLowerCase();

        ProcessBuilder processBuilder;

        if (sistemaOperativo.contains("win")){
            processBuilder = new ProcessBuilder("cmd.exe", "/c", "dir", ruta);
        } else {
            processBuilder = new ProcessBuilder("ls", "-l", ruta);
        }

    }
}
