package unidad01.ejemplos;


import java.io.File;
import java.io.IOException;

/**
 * @author Miguel Angel Ramirez
 */
public class Ejemplo01 {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Forma INCORRECTA (antigua y propensa a bloqueos)
        // Runtime.getRuntime.exec("comando argumentos");

        // Forma CORRECTA con ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder("comando", "arg1", "arg2");

        // Configurar directorio de trabajo
        processBuilder.directory(new File("/ruta/directorio"));

        // Redirigir salida y errores
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        // Iniciar el proceso
        Process process = processBuilder.start();

        // Esperar a que termine
        int codigoSalida = process.waitFor();
        System.out.println("Proceso terminado con código: " + codigoSalida);
    }
}
