package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.util.ValidationUtil;
import java.io.*;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ChatRoomFormController extends Thread{

    public AnchorPane chatRoomContext;
    public TextField txtMsg;
    //public JFXTextArea txtArea;
    public JFXButton btnSendMsg;
    public VBox vBox;

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private String username;

    final int PORT=5000;

    private LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    private FileChooser fileChooser;
    private File filePath;

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

//                txtTextArea.appendText(cmd+"\n");
                    StringBuilder fullMsg = new StringBuilder();
                    for (int i = 1; i < tokens.length; i++) {
                        fullMsg.append(tokens[i]);
                    }


                    String[] msgToAr = msg.split(" ");
                    String st = "";
                    for (int i = 0; i < msgToAr.length - 1; i++) {
                        st += msgToAr[i + 1] + " ";
                    }
//======================================================================



                    Text text = new Text(st);
                    String firstChars = "";
                    if (st.length() > 3) {
                        firstChars = st.substring(0, 3);

                    }


                    if (firstChars.equalsIgnoreCase("img")) {
                        //for the Images

                        st = st.substring(3, st.length() - 1);



                        File file = new File(st);
                        Image image = new Image(file.toURI().toString());

                        ImageView imageView = new ImageView(image);

                        imageView.setFitHeight(150);
                        imageView.setFitWidth(200);


                        HBox hBox = new HBox(10);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);


                        if (!cmd.equalsIgnoreCase(username)) {

                            vBox.setAlignment(Pos.TOP_LEFT);
                            hBox.setAlignment(Pos.CENTER_LEFT);


                            Text text1=new Text("  "+cmd+" :");
                            hBox.getChildren().add(text1);
                            hBox.getChildren().add(imageView);

                        } else {
                            hBox.setAlignment(Pos.BOTTOM_RIGHT);
                            hBox.getChildren().add(imageView);
                            Text text1=new Text(": Me ");
                            hBox.getChildren().add(text1);
                            System.out.println(fullMsg);

                        }

                        Platform.runLater(() -> vBox.getChildren().addAll(hBox));


                    } else {
                        //For the Text
//                    text.setFill(Color.WHITE);
                        //   text.getStyleClass().add("message");
                        TextFlow tempFlow = new TextFlow();

                        if (!cmd.equalsIgnoreCase(username + ":")) {
                            Text txtName = new Text(cmd + " ");
                            txtName.getStyleClass().add("txtName");
                            tempFlow.getChildren().add(txtName);
                        }

                        tempFlow.getChildren().add(text);
                        tempFlow.setMaxWidth(200); //200

                        TextFlow flow = new TextFlow(tempFlow);

                        HBox hBox = new HBox(12); //12

                        //=================================================


                        if (!cmd.equalsIgnoreCase(username + ":")) {

                            //  tempFlow.getStyleClass().add("tempFlowFlipped");
                            //  flow.getStyleClass().add("textFlowFlipped");
                            vBox.setAlignment(Pos.TOP_LEFT);
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.getChildren().add(flow);
                            System.out.println(flow);

                        } else {
                            // text.setFill(Color.WHITE);
                            // tempFlow.getStyleClass().add("tempFlow");
                            // flow.getStyleClass().add("textFlow");
                            Text text2=new Text(fullMsg+":Me");
                            System.out.println(text2);
                            TextFlow flow2 = new TextFlow(text2);
                            hBox.setAlignment(Pos.BOTTOM_RIGHT);
                            hBox.getChildren().add(flow2);
                            System.out.println(flow2);
                        }
                        //  hBox.getStyleClass().add("hbox");
                        Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }



       /* //==========================thibba eka======================
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
        }*/

    }

    public void sendMsg(){
        String msg = txtMsg.getText();
        writer.println(username + ": " + txtMsg.getText());

        txtMsg.clear();

        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);

        }

       /* //===========thibba eka===========
        String msg = txtMsg.getText();
        writer.println(username + ": " + txtMsg.getText());
        txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtArea.appendText("Me: " + msg + "\n");
        txtMsg.clear();
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
        */

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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        writer.println(username + " " + "img" + filePath.getPath());
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
