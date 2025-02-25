import java.io.*;
import java.net.*;

public class ClienteSocket {
    private static final String SERVIDOR_IP = "localhost";
    private static final int PUERTO = 8888;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVIDOR_IP, PUERTO);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            
            System.out.println("Conectado al servidor. Escribe un mensaje:");
            
            // Leer entrada del usuario
            String userInput = stdIn.readLine();
            
            // Enviar al servidor
            out.println(userInput);
            
            // Recibir respuesta
            String respuesta = in.readLine();
            System.out.println("Respuesta del servidor: " + respuesta);
            
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + SERVIDOR_IP);
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
    }
}