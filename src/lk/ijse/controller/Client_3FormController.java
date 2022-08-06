package lk.ijse.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client_3FormController {
    public JFXTextArea txtArea;
    public TextField txtMsg;

    final int PORT = 5000;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    BufferedReader bufferedReader;
    String message = "";

    public void initialize(){
        new Thread(()->{
            try {
                socket = new Socket("localhost", PORT);

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                // bufferedReader = new BufferedReader(new InputStreamReader(System.in));

                while (!message.equals("exit")) {
                    message = dataInputStream.readUTF();
                    txtArea.appendText("\n Client : " + message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }



    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        dataOutputStream.writeUTF(txtMsg.getText().trim());
        dataOutputStream.flush();
    }
}
