package lk.ijse.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    private ServerSocket serverSocket;
    private Socket accept;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer() {

        try {
            while (!serverSocket.isClosed()) {
                accept = serverSocket.accept();
                System.out.println("A new client has Connected!");
                ClientHandler clientHandler = new ClientHandler(accept);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void closeSererSocket(){

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
        ServerSocket serverSocket = new ServerSocket(PORT);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}

