package unidad01.ejercicios;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Programa que lista el contenido de directorios usando comandos del sistema operativo.
 * Detecta automáticamente el SO y ejecuta el comando apropiado (ls -l o dir) dependiendo del SO.
 *
 * @author Miguel Angel Ramirez (marp0604)
 */
public class Ejercicio02 {

    /**
     * Método principal que procesa los argumentos y ejecuta el listado de directorios.
     *
     * @param args Array de rutas de directorios a listar
     */
    public static void main(String[] args) {
        // Valída qué argumentos se están pasando
        if (args.length == 0) {
            System.err.println("Error: No se proporcionaron directorios como argumentos.");
            System.exit(1);
        }

        // Confirma que sea un directorio existente
        for (String ruta : args) {
            try {
                lanzarComando(ruta);
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
                System.err.println("\tContinuando con el siguiente directorio...\n");
            } catch (IOException e) {
                System.err.println("Error de E/S: " + e.getMessage());
            } catch (InterruptedException e) {
                System.err.println("Proceso interrumpido: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Encuentra el tipo de SO y ejecuta el comando correspondiente
     *
     * @param ruta Ruta del directorio
     * @throws IllegalArgumentException Si el directorio no es válido
     * @throws IOException Si hay error de E/S
     * @throws InterruptedException Si el proceso es interrumpido
     */
    public static void lanzarComando(String ruta) throws IllegalArgumentException, IOException, InterruptedException {

        // Crea el objeto File al cual se le pasa la ruta
        File directorio = new File(ruta);

        // Valída que el directorio existe
        if (!directorio.exists()) {
            throw new IllegalArgumentException("El directorio '" + ruta + "' no existe");
        }

        // Valída que es un directorio
        if (!directorio.isDirectory()) {
            throw new IllegalArgumentException("'" + ruta + "' no es un directorio");
        }

        // Detecta que tipo de SO es el que está ejecutando el programa
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();

        // Verifica el SO y lanza el comando correspondiente
        ProcessBuilder processBuilder;

        if (sistemaOperativo.contains("win")) {
            // Windows: cmd /c dir
            processBuilder = new ProcessBuilder("cmd.exe", "/c", "dir", ruta);
        } else {
            // Linux/Mac: ls -l
            processBuilder = new ProcessBuilder("ls", "-l", ruta);
        }

        // Combina stdout y stderr en un solo stream
        processBuilder.redirectErrorStream(true);

        // Ejecuta el proceso
        Process proceso = processBuilder.start();

        // Muestra el directorio
        System.out.println(directorio.getAbsolutePath() + "/");

        // Lee y muestra la salida línea por línea
        try (BufferedReader reader = new BufferedReader( new InputStreamReader(proceso.getInputStream()))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        }

        // Espera a que el proceso termine
        int codigoSalida = proceso.waitFor();

        // Verifica el código de salida
        if (codigoSalida != 0) {
            System.err.println("Advertencia: El comando terminó con código " + codigoSalida);
        }
        System.out.println();
    }
}