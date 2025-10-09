package unidad01.ejercicios;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Ejecuta un script de Bash desde Java.
 * El script debe llamarse "saluda.sh" y estar en el directorio introducido.
 *
 * @author Miguel Angel Ramirez (marp0604)
 */
public class Ejercicio03 {

    /**
     * Nombre del script de Bash a ejecutar.
     */
    private static final String NOMBRE_SCRIPT = "saluda.sh";

    /**
     * Método principal. Requiere 2 argumentos: directorio del script y nombre.
     *
     * @param args [0] = directorio del script, [1] = nombre para el script
     */
    public static void main(String[] args) {
        if (args.length != 2){
            System.out.println("Error. Número incorrecto de argumentos.");
        }

        String directorioScript = args[0];
        String nombre = args[1];

        try{
            lanzarScript(directorioScript, nombre);
        } catch (IllegalArgumentException e){
            System.err.println("Error. " + e.getMessage());
            System.exit(1);
        } catch (IOException e){
            System.err.println("Error de E/S. " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e){
            System.err.println("Proceso interrumpido. " + e.getMessage());
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    /**
     * Valída y ejecuta el script de Bash.
     *
     * @param directorioScript Ruta del directorio donde está el script
     * @param nombre Nombre a pasar al script como parámetro
     * @throws IllegalArgumentException Si el directorio o script no son válidos
     * @throws IOException Si hay error de E/S al ejecutar el script
     * @throws InterruptedException Si el proceso es interrumpido
     */
    public static void lanzarScript(String directorioScript, String nombre)
            throws IllegalArgumentException, IOException, InterruptedException {

        // Verifica el directorio
        File directorio = new File(directorioScript);

        if (!directorio.exists()){
            throw new IllegalArgumentException("El directorio: " + directorioScript + " no existe.");
        }

        if (!directorio.isDirectory()){
            throw new IllegalArgumentException(directorioScript + " no es un directorio valido");
        }

        // Construye la ruta del script
        Path pathScript = Paths.get(directorioScript, NOMBRE_SCRIPT);
        File archivoScript = pathScript.toFile();

        // Valida el script
        if (!archivoScript.exists()) {
            throw new IllegalArgumentException("El script " + NOMBRE_SCRIPT + " no existe en el directorio " + directorioScript);
        }

        if (!archivoScript.isFile()){
            throw new IllegalArgumentException(NOMBRE_SCRIPT + " no es un archivo valido");
        }

        if (!archivoScript.canExecute()){
            System.err.println("Advertencia: El script no tiene permisos de ejecución");
            System.err.println("\tIntenta ejecutar: chmod +x " + pathScript.toString());
            throw new IllegalArgumentException("El script " + NOMBRE_SCRIPT + " no tiene permisos de ejecución");
        }

        // Configura y ejecuta ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(pathScript.toAbsolutePath().toString(), nombre);
        processBuilder.directory(directorio);
        processBuilder.redirectErrorStream(true);

        Process proceso = processBuilder.start();

        // Captura la salida
        System.out.println("Salida del script: ");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))){
            String linea;
            while ((linea = reader.readLine()) != null){
                System.out.println(linea);
            }
        }

        System.out.println("---------------------------------------");

        // Espera y muestra el código de salida
        int codigoSalida = proceso.waitFor();

        System.out.println();
        System.out.println("Resultado de la ejecución:");

        if (codigoSalida == 0) {
            System.out.println("Script ejecutado correctamente");
            System.out.println("\tCódigo de salida: " + codigoSalida);
        } else {
            System.out.println("Script terminó con error");
            System.out.println("\tCódigo de salida: " + codigoSalida);
        }

        System.out.println("---------------------------------------");
    }
}
