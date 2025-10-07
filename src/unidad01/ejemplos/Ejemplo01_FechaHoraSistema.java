package unidad01.ejemplos;

import java.io.IOException;

/**
 * Ejemplo 1: Obtener la fecha y hora actual del sistema.
 *
 * @author Miguel Angel Ramirez (marp0604)
 */
public class Ejemplo01_FechaHoraSistema {

    public static void main(String[] args) {
        try {
            obtenerFechaHora();
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Proceso interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Obtiene y muestra la fecha y hora del sistema usando comandos del SO.
     * @throws IOException Si hay un error al ejecutar el comando
     * @throws InterruptedException Si el proceso es interrumpido
     */
    private static void obtenerFechaHora() throws IOException, InterruptedException {
        // Detecta el sistema operativo
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();

        // Selecciona el comando según el SO
        ProcessBuilder processBuilder;

        if (sistemaOperativo.contains("win")) {
            // Windows: Comando para mostrar fecha
            processBuilder = new ProcessBuilder("cmd.exe", "/c", "date", "/t");
        } else {
            // Linux/Mac: Comando para mostrar fecha y hora con formato
            processBuilder = new ProcessBuilder("date", "+%Y-%m-%d %H:%M:%S");
        }

        // Configura redirección de salida
        // INHERIT hace que la salida del proceso aparezca en la consola
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        // Mostrar información
        System.out.println("Obteniendo fecha y hora del sistema...");
        System.out.println("\tSistema operativo detectado: " + sistemaOperativo);
        System.out.println("-------------------------------------------");

        // Ejecuta el proceso
        Process proceso = processBuilder.start();

        // Espera a que termine y obtiene el código de salida
        int codigoSalida = proceso.waitFor();

        // Verifica el resultado
        System.out.println("-------------------------------------------");

        if (codigoSalida == 0) {
            System.out.println("Comando ejecutado correctamente (código: " + codigoSalida + ")");
        } else {
            System.out.println("Error en la ejecución (código: " + codigoSalida + ")");
        }
    }
}