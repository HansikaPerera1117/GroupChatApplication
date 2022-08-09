package lk.ijse.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Server.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatRoomFormController extends Thread{

   // public Label lblName;
    public AnchorPane chatRoomContext;
    public TextField txtMsg;
    public JFXTextArea txtArea;

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private String username;

//    final int PORT=5000;
//
//    ServerSocket serverSocket;
//    Socket socket;
//    DataInputStream dataInputStream;
//    DataOutputStream dataOutputStream;
//    String message ="";



    public void initialize(){

//        new Thread(() -> {
//            try {
//                socket=new Socket("localhost",PORT);
//                dataOutputStream=new DataOutputStream(socket.getOutputStream());
//                dataInputStream=new DataInputStream(socket.getInputStream());
//
//                while (!message.equals("bye")){
//                    message=dataInputStream.readUTF();
//                    //System.out.println(message);
//                    txTextArea.appendText("\nserver: "+message);
//                }
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();

        //lblName.setText(UserLoginFormController.txtUserName.getText());
        username = LogInFormController.userName;
       // String userName=LogInFormController.userName;
       // lblName.setText(String.valueOf(userName));
        try {
            socket = new Socket("localhost", 5000);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(username + " has entered the chat!");
           // writer.println(username + " has left the chat!");

            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            while (true) {

                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                // txTextArea.appendText(cmd+"\n");
                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i]);
                }
                System.out.println(fullMsg);
                //txTextArea.appendText(cmd+" "+fullMsg+"\n");
//                if (cmd.equalsIgnoreCase("Client" + ":")) {
//                    continue;
//                } else if (fullMsg.toString().equalsIgnoreCase("bye")) {
//                    break;
//                }

               // System.out.println("cmd="+cmd+"-----"+"UserName"+username);
                if(!cmd.equalsIgnoreCase(username+":")){
                    txtArea.appendText(msg + "\n");
                }

            }
//            reader.close();
//            writer.close();
//            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void imgSendMsgOnAction(MouseEvent mouseEvent) throws IOException {
//
//        String msg = txtTextMsg.getText();
//        writer.println(txtUserName.getText() + ": " + txtTextMsg.getText());
//        txtTextArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
//        txtTextArea.appendText("Me: " + msg + "\n");
//        txtTextMsg.clear();
//        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
////            System.exit(0);
//            Stage stage = (Stage) txtTextMsg.getScene().getWindow();
//            stage.close();
//        }
//    }

//    public void btnGoOnAction(ActionEvent actionEvent) {
//        txtUserName.setText(txtNicName.getText().trim());
//        pnePopUp.setVisible(false);
//        apnChatForm.setVisible(true);
//    }
//
//    public void AddClientOnAction(MouseEvent mouseEvent) throws IOException {
//        Stage stage=new Stage();
//        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/ChatForm.fxml"))));
//        stage.setResizable(false);
//        //primaryStage.getIcons().add(new Image("location"));
//        stage.setTitle("sample title");
//        stage.centerOnScreen();
//        stage.show();
//
//    }


    public void btnSendOnAction(ActionEvent actionEvent) {
        String msg = txtMsg.getText();
        //writer.println(username + " has entered the chat!");
        writer.println(username + ": " + txtMsg.getText());
        txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtArea.appendText("Me: " + msg + "\n");
        txtMsg.clear();
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
//            Stage stage = (Stage) txtTextMsg.getScene().getWindow();
//            stage.close();
        }
    }
}
