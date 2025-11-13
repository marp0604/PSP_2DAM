package unidad03.ejercicios.ejercicio01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Clase principal que gestiona el lado del Cliente.
 * Se encarga de conectarse al servidor, enviar mensajes por el Usuario y recibir respuestas del Servidor.
 *
 * @author Miguel Angel Ramírez
 */
public class Cliente {
    private Socket clienteSocket;
    private PrintWriter printWriter;
    private BufferedReader reader;
    private final String HOST = "localhost";
    private final int PUERTO = 54321;

    private static final String FIN_RESPUESTA = "--- FIN RESPUESTA ---";

    /**
     * Establece la conexión con el servidor.
     * Crea el socket y los flujos de entrada y salida para la comunicación.
     *
     * @return true si la conexión fue correcta, false si falla.
     */
    public boolean conectar() {
        try {
            // Crea el socket del cliente y se conecta al servidor con el host y puerto.
            clienteSocket = new Socket(HOST, PUERTO);
            // Configura que el PrintWriter pueda enviar mensajes.
            printWriter = new PrintWriter(clienteSocket.getOutputStream(), true);
            // Configura el BufferedReader para recibir mensajes del servidor.
            reader = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));

            System.out.println("-> Cliente: Conectado al servidor (" + HOST + ": " + PUERTO + ")");
            return true;
        } catch (ConnectException e) {
            // Error si el servidor no está encendido.
            System.err.println("-> Cliente: Error de conexión. Verifica que el servidor está activo.");
            return false;
        } catch (IOException e) {
            System.err.println("-> Cliente: Error al conectar");
            return false;
        }
    }

    /**
     * Método principal para enviar mensajes al servidor y recibir respuestas.
     * Lee la entrada del usuario desde la consola, envía al servidor y muestra la respuesta.
     * Finaliza la comunicación al recibir el comando "#fin".
     */
    public void enviarYRecibir() {
        Scanner sc = new Scanner(System.in);
        String entrada;

        try {
            while (true) {
                System.out.print("-> Cliente (Introduce mensaje o comando(#)): ");
                entrada = sc.nextLine();

                if (entrada.isEmpty()) {
                    continue;
                }

                printWriter.println(entrada);

                if (entrada.equalsIgnoreCase("#fin")) {
                    leerRespuestaCompleta();
                    break;
                }

                leerRespuestaCompleta();
            }
        } catch (IOException e) {
            System.err.println("CLIENTE: Error de entrada/salida durante la comunicación: " + e.getMessage());
        } finally {
            cerrar();
        }
    }

    /**
     * Lee la respuesta completa del servidor hasta encontrar el fin de respuesta.
     * Muestra la respuesta en la consola.
     *
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void leerRespuestaCompleta() throws IOException {
        String linea;
        System.out.println("--- RESPUESTA DEL SERVIDOR ---");

        while ((linea = reader.readLine()) != null) {
            if (linea.equals(FIN_RESPUESTA)) {
                break;
            }
            System.out.println(linea);

            if (linea.startsWith("Fin: Cerrando conexión")) {
                break;
            }
        }
        System.out.println("------------------------------");
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
        Cliente cliente = new Cliente();
        if (cliente.conectar()) {
            cliente.enviarYRecibir();
        }
    }
}