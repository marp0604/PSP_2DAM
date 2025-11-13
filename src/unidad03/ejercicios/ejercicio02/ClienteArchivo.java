package unidad03.ejercicios.ejercicio02;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Clase principal que gestiona el lado del Cliente.
 * Se encarga de conectarse al servidor, enviar mensajes desde un archivo y recibir respuestas del Servidor.
 *
 * @author Miguel Angel Ramírez
 */
public class ClienteArchivo {
    private Socket clienteSocket;
    private PrintWriter printWriter;
    private BufferedReader reader;
    private final String HOST = "localhost";
    private final int PUERTO = 54321;

    private static final String FIN_RESPUESTA = "--- FIN RESPUESTA ---";

    /**
     * Establece la conexión con el servidor.
     * Crea el socket y los flujos de entrada y salida para la comunicación.
     * @return true si la conexión fue correcta, false si falla.
     */
    public boolean conectar(){
        try{
            clienteSocket = new Socket(HOST, PUERTO);
            printWriter = new PrintWriter(clienteSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            System.out.println("-> Cliente: Conectado al servidor (" + HOST + ": " + PUERTO + ")");
            return true;
        } catch (ConnectException e){
            System.err.println("-> Cliente: Error de conexión. Verifica que el servidor está activo.");
            return false;
        } catch (IOException e){
            System.err.println("-> Cliente: Error al conectar");
            return false;
        }
    }

    /**
     * Envía mensajes al servidor desde un archivo especificado por el usuario.
     * Lee el archivo línea por línea, envía cada línea al servidor y muestra la respuesta.
     * Finaliza la comunicación al enviar el comando "#fin".
     */
    public void enviarDesdeArchivo(){
        Scanner sc = new Scanner(System.in);
        System.out.print("-> Cliente: Introduzca la ruta completa del fichero a enviar: ");
        String rutaArchivo = sc.nextLine();

        BufferedReader fileReader = null;

        try {
            fileReader = new BufferedReader(new FileReader(rutaArchivo));
            String lineaArchivo;

            System.out.println("-> Cliente: Iniciando el envío de mensajes desde el archivo '" + rutaArchivo + "'...");

            // Leer el archivo línea por línea
            while ((lineaArchivo = fileReader.readLine()) != null){

                if (lineaArchivo.isEmpty()){
                    continue; // Ignora líneas vacías
                }

                System.out.println("-> Cliente (Enviando): " + lineaArchivo);
                printWriter.println(lineaArchivo);
                // Asegura que el mensaje se envíe inmediatamente
                printWriter.flush();

                // Si es el comando de fin, leer la respuesta y salir
                if (lineaArchivo.equalsIgnoreCase("#fin")){
                    leerRespuestaCompleta(true); // Indica que es el comando final
                    break;
                }

                // Leer y mostrar la respuesta completa del servidor
                leerRespuestaCompleta(false);
            }

        } catch (FileNotFoundException e) {
            System.err.println("-> Cliente: ERROR: Fichero no encontrado en la ruta especificada.");
        } catch (IOException e) {
            System.err.println("-> Cliente: Error de I/O durante la comunicación o lectura del archivo: " + e.getMessage());
        } finally {
            // Cierra el lector de archivo
            try {
                if (fileReader != null) fileReader.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el lector de archivo.");
            }
            cerrar(); // Cierra la conexión del cliente
        }
    }

    /**
     * Lee la respuesta completa del servidor hasta encontrar el fin de respuesta.
     * Muestra la respuesta en la consola.
     * @param comando Indica si la respuesta es para un comando de cierre.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void leerRespuestaCompleta(boolean comando) throws IOException {
        String linea;

        // Si no es un comando de cierre, mostramos el encabezado
        if (!comando) {
            System.out.println("--- RESPUESTA DEL SERVIDOR ---");
        }

        while ((linea = reader.readLine()) != null) {
            if (linea.equals(FIN_RESPUESTA)) {
                break; // Fin de la respuesta
            }
            System.out.println(linea);

            // Si es el mensaje de cierre, salimos del bucle
            if (linea.startsWith("Fin: Cerrando conexión")) {
                break;
            }
        }
        // Si no es un comando de cierre, mostramos el pie de página
        if (!comando) {
            System.out.println("------------------------------");
        }
    }

    /**
     * Cierra los recursos abiertos: PrintWriter, BufferedReader y Socket.
     */
    public void cerrar() {
        try {
            if (printWriter != null) printWriter.close();
            if (reader != null) reader.close();
            if (clienteSocket != null) clienteSocket.close();
            System.out.println("-> Cliente: Conexión terminada.");
        } catch (IOException e) {
            System.err.println("-> Cliente: Error al cerrar recursos.");
        }
    }

    /**
     * Método principal para ejecutar el cliente.
     */
    public static void main(String[] args) {
        ClienteArchivo cliente = new ClienteArchivo();
        if (cliente.conectar()) {
            cliente.enviarDesdeArchivo();
        }
    }
}
