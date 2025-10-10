package unidad01.ejercicios.ejercicio03;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * @author Miguel Angel Ramirez
 */
public class Ejercicio03V2 {

    private static final String NOMBRE_SCRIPT = "saluda.sh";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try{
            System.out.print("Introduce la ruta del directorio donde está el script: ");
            String directorioScript = sc.nextLine().trim();

            if (directorioScript.isEmpty()) {
                System.err.println("Error: La ruta del directorio no puede estar vacía.");
                System.exit(1);
            }

            System.out.print("Introduce tu nombre: ");
            String nombre = sc.nextLine().trim();

            if (nombre.isEmpty()) {
                System.err.println("Error: El nombre no puede estar vacío.");
                System.exit(1);
            }

            System.out.println();

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

    public static void lanzarScript(String directorioScript, String nombre)
            throws IllegalArgumentException, IOException, InterruptedException {

        File directorio = new File(directorioScript);

        if (!directorio.exists()){
            throw new IllegalArgumentException("El directorio: " + directorioScript + " no existe.");
        }

        if (!directorio.isDirectory()){
            throw new IllegalArgumentException(directorioScript + " no es un directorio valido");
        }

        Path pathScript = Paths.get(directorioScript, NOMBRE_SCRIPT);
        File archivoScript = pathScript.toFile();

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

        ProcessBuilder processBuilder = new ProcessBuilder(pathScript.toAbsolutePath().toString(), nombre);

        processBuilder.directory(directorio);

        processBuilder.redirectErrorStream(true);

        Process proceso = processBuilder.start();

        System.out.println("Salida del script: ");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))){
            String linea;
            while ((linea = reader.readLine()) != null){
                System.out.println(linea);
            }
        }

        System.out.println("---------------------------------------");

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
