import java.io.*;
import java.net.*;

public class ServidorSocket {
    private static final int PUERTO = 8888;
    private static final String DIRECTORIO_DESTINO = "archivos_recibidos/";

    public static void main(String[] args) {
        new File(DIRECTORIO_DESTINO).mkdirs(); // Crear directorio si no existe

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor escuchando en puerto " + PUERTO);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     DataInputStream inStream = new DataInputStream(socket.getInputStream()))       
                {
                    System.out.println("Cliente conectado: " + serverSocket.getInetAddress());

                    String opcion = inStream.readUTF();
                    if(opcion.equals("ENVIAR")){
                        recibirArchivoDeApp(serverSocket, inStream);
                    }else if (opcion.equals("RECIBIR")){
                        enviarArchivoAApp(serverSocket, socket, inStream);
                    }

                    

                } catch (IOException e) {
                    System.err.println("Error en conexión: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }

    private static void recibirArchivoDeApp(ServerSocket  socket, DataInputStream inStream) throws IOException{
        // Recibir metadatos        
        String nombreArchivo = inStream.readUTF();
        System.out.println("Nombre: " + nombreArchivo);
        long tamanyoArchivo = inStream.readLong();
        System.out.println("Tamanyo: " + tamanyoArchivo);

        // Preparar para guardar        
        File archivoDestino = new File(DIRECTORIO_DESTINO + nombreArchivo);
        try (FileOutputStream fos = new FileOutputStream(archivoDestino);
        BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            // Recibir y escribir bytes

            byte[] buffer = new byte[4096];
            int count;
            long totalRecibido = 0;
            while (totalRecibido < tamanyoArchivo && (count = inStream.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
                totalRecibido += count;
            }

            System.out.println("Archivo recibido: " + nombreArchivo);
        }
    }


    public static void enviarArchivoAApp(ServerSocket  socket, Socket clientSocket, DataInputStream inStream) throws IOException {
        String rutaArchivo = inStream.readUTF();
        File archivo = new File(DIRECTORIO_DESTINO + File.separator +  rutaArchivo);
        if (!archivo.exists()) {
            throw new FileNotFoundException("Archivo no encontrado: " + rutaArchivo);
        }

        try (DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());
             FileInputStream fis = new FileInputStream(archivo);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            // Enviar metadatos: nombre y tamaño
            outStream.writeUTF(archivo.getName());
            outStream.writeLong(archivo.length());

            // Enviar archivo en bloques de 4 KB
            byte[] buffer = new byte[4096];
            int count;
            while ((count = bis.read(buffer)) != -1) {
                outStream.write(buffer, 0, count);
            }

            System.out.println("Archivo enviado: " + archivo.getName());
        }
    }
}