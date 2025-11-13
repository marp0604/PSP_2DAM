package unidad03.ejercicios.ejercicio01;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Clase servidor que gestiona las conexiones entrantes de los clientes.
 * El servidor se encarga de procesar los comandos recibidos y enviar las respuestas correspondientes.
 *
 * @author Miguel Angel Ramírez
 */
public class Servidor {
    private ServerSocket serverSocket;
    private Socket clienteSocket;
    private PrintWriter printWriter;
    private BufferedReader reader;
    private String directorio;
    private final int PUERTO = 54321;

    private static final String FIN_RESPUESTA = "--- FIN RESPUESTA ---";

    private static final String CMD_FIN = "#fin";
    private static final String CMD_INFO = "#info";
    private static final String CMD_LIST = "#list";

    /**
     * Inicia el servidor, espera conexiones de clientes y procesa los mensajes recibidos.
     */
    public void start(){
        Scanner sc = new Scanner(System.in);
        // Solicita al usuario el directorio.
        System.out.print("-> Servidor: Especifica el directorio: ");
        directorio = sc.nextLine();
        System.out.println("-> Servidor: Directorio: " + directorio);

        try{
            // Crea el socket del servidor en el puerto especificado.
            serverSocket = new ServerSocket(PUERTO);
            System.out.println("-> Servidor: Esperando conexión en el puerto " + PUERTO + ". ");

            // Espera a que un cliente se conecte.
            clienteSocket = serverSocket.accept();
            System.out.println("-> Servidor: Cliente conectado desde " + clienteSocket.getInetAddress().getHostAddress());

            // Configura la entrada y salida para la comunicación con el cliente.
            printWriter = new PrintWriter(clienteSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));

            // Bucle principal para recibir y procesar mensajes del cliente.
            String mensaje;
            // Lee mensajes hasta que el cliente envíe el comando de fin.
            while ((mensaje = reader.readLine()) != null){
                System.out.println("-> Servidor (Recibido): " + mensaje);

                if (mensaje.startsWith("#")){
                    // Si empieza por #, es un comando.
                    if (procesarComando(mensaje)){
                        break; // Sale del bucle si es el comando de fin.
                    }
                } else {
                    // Si no es un comando, responde con mensaje normal.
                    printWriter.println("Mensaje recibido correctamente.");
                    printWriter.println(FIN_RESPUESTA);
                }
            }
        } catch (IOException e){
            System.err.println("-> Servidor: Error en la conexión: " + e.getMessage());
        } finally {
            // Cierra todos los recursos.
            try {
                if (printWriter != null) printWriter.close();
                if (reader != null) reader.close();
                if (clienteSocket != null) clienteSocket.close();
                if (serverSocket != null) serverSocket.close();
                System.out.println("-> Servidor: Conexión cerrada.");
            } catch (IOException e) {
                System.err.println("-> Servidor: Error al cerrar los recursos.");
            }
        }
    }

    /**
     * Procesa los comandos recibidos del cliente.
     * @param comando El comando recibido. (ej. #fin, #info, #list)
     * @return true si el comando es #fin y se debe cerrar la conexión, false en caso contrario.
     */
    private boolean procesarComando(String comando){
        String respuesta;

        switch (comando.toLowerCase()){
            case CMD_FIN:
                respuesta = "Fin: Cerrando conexión";
                printWriter.println(respuesta);
                return true; // Indica que se debe cerrar la conexión.

            case CMD_INFO:
                try{
                    // Obtiene el nombre del host local.
                    String host = InetAddress.getLocalHost().getHostName();
                    respuesta = "Info: Host: " + host + ". Puerto: " + PUERTO;
                } catch (UnknownHostException e) {
                    respuesta = "Info: Puerto: " + PUERTO + " Host no disponible.";
                }

                printWriter.println(respuesta); // Envía la respuesta al cliente.
                printWriter.println(FIN_RESPUESTA); // Indica el fin de la respuesta.
                break;

            case CMD_LIST:
                // Lista los ficheros del directorio configurado.
                listarFicheros(this.directorio);
                break;

            default:
                // Comando no reconocido.
                printWriter.println("Error: Comando no reconocido (" + comando + ")" );
                printWriter.println(FIN_RESPUESTA);
        }
        return false;
    }

    /**
     * Lista los ficheros y directorios en la ruta especificada.
     *
     * @param ruta La ruta del directorio a listar.
     */
    private void listarFicheros(String ruta){
        File directorio = new File(ruta);

        // Verifica que la ruta es un directorio válido.
        if (!directorio.exists() || !directorio.isDirectory()){
            printWriter.println("Error: La ruta -> " + ruta + ". No es un directorio valido o no existe");
            printWriter.println(FIN_RESPUESTA);
            return;
        }

        // Obtiene la lista de ficheros y directorios.
        File[] ficheros = directorio.listFiles();
        if (ficheros == null || ficheros.length == 0){
            printWriter.println("Lista: Directorio vacío o acceso denegado.");
            printWriter.println(FIN_RESPUESTA);
            return;
        }

        // Envía la lista al cliente.
        printWriter.println("Lista: Contenido de " + ruta + ": ");

        for (File f : ficheros){
            String tipo = f.isDirectory() ? "D" : "F";
            printWriter.println(tipo + " " + f.getName());
        }

        // Indica el fin de la respuesta.
        printWriter.println(FIN_RESPUESTA);
    }

    /**
     * Método principal para iniciar el servidor.
     */
    public static void main(String[] args) {
        new Servidor().start();
    }
}