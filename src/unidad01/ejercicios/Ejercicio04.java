package unidad01.ejercicios;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejecuta otro programa Java (Ejercicio03) desde Java.
 *
 * Este programa lanza el Ejercicio03 pasándole los argumentos necesarios
 * para ejecutar el script de Bash.
 *
 * @author Miguel Angel Ramirez (marp0604)
 */
public class Ejercicio04 {

    /**
     * Nombre de la clase Java a ejecutar (con paquete completo).
     */
    private static final String CLASE_A_EJECUTAR = "unidad01.Ejercicio03";

    /**
     * Método principal. Requiere 2 argumentos: directorio del script y nombre.
     *
     * @param args [0] = directorio del script, [1] = nombre para el script
     */
    public static void main(String[] args) {
        // Valida el número de argumentos
        if (args.length != 2) {
            System.err.println("Error: Número incorrecto de argumentos.");
            System.exit(1);
        }

        String directorioScript = args[0];
        String nombre = args[1];

        try {
            ejecutarProgramaJava(directorioScript, nombre);
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("Proceso interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    /**
     * Ejecuta el programa Java Ejercicio03 con los parámetros dados.
     *
     * Configura el directorio de trabajo, ejecuta el comando java,
     * captura la salida y muestra el código de salida.
     *
     * @param directorioScript Directorio donde está el script de Bash
     * @param nombre Nombre a pasar al script como parámetro
     * @throws IOException Si hay error de E/S al ejecutar el programa
     * @throws InterruptedException Si el proceso es interrumpido
     */
    private static void ejecutarProgramaJava(String directorioScript, String nombre)
            throws IOException, InterruptedException {

        // Muestra información inicial
        System.out.println("Ejecutar Programa Java");
        System.out.println("-------------------------------------");
        System.out.println();

        // Construye el comando para ejecutar el programa Java
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java",          // Comando Java
                CLASE_A_EJECUTAR,          // Clase a ejecutar (con paquete)
                directorioScript,          // Primer argumento para Ejercicio03
                nombre                     // Segundo argumento para Ejercicio03
        );

        // Determina el directorio de trabajo correcto
        File directorioActual = new File(System.getProperty("user.dir"));
        File directorioSrc = new File(directorioActual, "src");

        // Lógica de selección del directorio de trabajo
        if (directorioActual.getName().equals("src")) {
            // Ya estamos en src/
            processBuilder.directory(directorioActual);
            System.out.println("Directorio de trabajo: " + directorioActual.getAbsolutePath());
        } else if (directorioSrc.exists() && directorioSrc.isDirectory()) {
            // Existe src/ como subdirectorio
            processBuilder.directory(directorioSrc);
            System.out.println("Directorio de trabajo: " + directorioSrc.getAbsolutePath());
        } else {
            // Usa el directorio actual
            processBuilder.directory(directorioActual);
            System.out.println("Directorio de trabajo: " + directorioActual.getAbsolutePath());
        }

        // Combina stdout y stderr en un solo stream
        processBuilder.redirectErrorStream(true);

        // Ejecuta el proceso
        Process proceso = processBuilder.start();

        System.out.println();
        System.out.println("Salida del programa Java:");
        System.out.println("-------------------------------------");

        // Captura y muestra la salida del proceso
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(proceso.getInputStream()))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        }

        System.out.println("-------------------------------------");

        // Espera a que el proceso termine y obtener código de salida
        int codigoSalida = proceso.waitFor();

        // Muestra el resultado final
        System.out.println();
        System.out.println("Resultado:");
        System.out.println("-------------------------------------");

        if (codigoSalida == 0) {
            System.out.println("Programa ejecutado correctamente");
            System.out.println("\tCódigo de salida: " + codigoSalida);
        } else {
            System.out.println("Programa terminó con error");
            System.out.println("\tCódigo de salida: " + codigoSalida);
        }

        System.out.println("-------------------------------------");
    }
}