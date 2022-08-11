package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import lk.ijse.util.ValidationUtil;
import java.io.*;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ChatRoomFormController extends Thread{

    public AnchorPane chatRoomContext;
    public TextField txtMsg;
    public JFXTextArea txtArea;
    public JFXButton btnSendMsg;

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private String username;

    final int PORT=5000;

    private LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

          btnSendMsg.setVisible(false);
          username = LogInFormController.userName;

        try {
            socket = new Socket("localhost", PORT);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("\t\t\t\t" + username);

            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //----------------validation-----------------------
        Pattern msgPattern = Pattern.compile("^[A-z0-9 ,./?;-_`~\\<>*+'\"|!@#$%^&*(){}-]{1,}$");
        map.put(txtMsg,msgPattern);

    }

    @Override
    public void run(){
        try {
            while (true) {

                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i]);
                }
                System.out.println(fullMsg);

                if(!cmd.equalsIgnoreCase(username+":")){
                    txtArea.appendText(msg + "\n");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(){
        String msg = txtMsg.getText();
        writer.println(username + ": " + txtMsg.getText());
        txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtArea.appendText("Me: " + msg + "\n");
        txtMsg.clear();
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
        btnSendMsg.setVisible(false);
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        sendMsg();
    }

    public void txtSendMsgOnAction(ActionEvent actionEvent) {
        if (txtMsg.getLength() != 0){
            sendMsg();
        }

    }

    public void imagesOnAction(MouseEvent event) {
    }

    public void emojiOnAction(MouseEvent event) {

    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnSendMsg);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnSendMsg);

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }


}
