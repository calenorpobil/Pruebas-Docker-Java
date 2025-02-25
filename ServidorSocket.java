import java.io.*;
import java.net.*;

public class ServidorSocket {

    private static final int PUERTO = 8888;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado. Esperando clientes...");
            
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                    
                    // Recibir mensaje del cliente
                    String mensajeCliente = in.readLine();
                    System.out.println("Mensaje recibido: " + mensajeCliente);

                    int num = Integer.parseInt(mensajeCliente);
                    num++;
                    out.println(num+"");
                    


                    // Enviar respuesta
                    out.println("Servidor recibió: " + mensajeCliente);
                    
                } catch (IOException e) {
                    System.err.println("Error en la conexión con el cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}