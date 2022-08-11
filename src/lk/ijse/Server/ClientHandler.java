package lk.ijse.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{
    private ArrayList<ClientHandler> clients;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String clientUsername;

    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            clientUsername = reader.readLine();
            broadcastMessage(clientUsername + " has entered the chat!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (msg.equalsIgnoreCase( "exit")) {
                    return;
                }
                for (ClientHandler cl : clients) {
                    cl.writer.println(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                removeClientHandler();
                System.out.println("Client has Disconnected!");
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void broadcastMessage(String messageToSend){

        for (ClientHandler clientHandler : clients){
            if (!clientHandler.clientUsername.equals(clientUsername)){
                  clientHandler.writer.write(messageToSend);
                  clientHandler.writer.flush();
            }
        }
    }
    public void removeClientHandler(){
        clients.remove(this);
        broadcastMessage( clientUsername + " has left the chat!");
    }
}
