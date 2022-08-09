package lk.ijse.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    final int PORT = 5000;

    public Server() {
        try {
            ServerSocket ss=new ServerSocket(PORT);
            System.out.println ("Waiting for request");
            Socket s=ss.accept();
            System.out.println ("Connected With"+s.getInetAddress().toString());
            ObjectInputStream   ois = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream  oos = new ObjectOutputStream(s.getOutputStream());

            String req=(String)ois.readObject();
            System.out.println (req);

            File f=new File(req);
            FileInputStream fin=new FileInputStream(f);

            int c;
            int sz=(int)f.length();
            byte b[]=new byte [sz];
            oos.writeObject(new Integer(sz));
            oos.flush();
            int j=0;
            while ((c = fin.read()) != -1) {

                b[j]=(byte)c;
                j++;
            }
            /*for (int i = 0; i<sz; i++)
            {
                System.out.print(b[i]);
            }*/



            fin.close();
            oos.flush();
            oos.write(b,0,b.length);
            oos.flush();
            System.out.println ("Size "+sz);
            System.out.println ("buf size"+ss.getReceiveBufferSize());
            oos.writeObject(new String("Ok"));
            oos.flush();
            ss.close();
        }
        catch (Exception ex) {
            System.out.println ("Error"+ex);
        }
    }
    public static void main (String[] args) {
        Server ob=new Server();
    }
}

