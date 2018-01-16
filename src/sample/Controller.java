package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Controller {

    @FXML Slider velocitySlider;
    @FXML TextFlow tfl_log;
    @FXML TextArea textToSpeech;
    @FXML Button w,a,s,d, connectButton, disconnectButton;
    @FXML Circle connectCircle;
    @FXML ComboBox dropDownPostures;
    @FXML
    TextField tx_IP;
    @FXML
    TextField tx_Port;

    private FileInputStream file;
    private BufferedReader reader;
    private String IP;
    private String Port;


    private Application app;
    private ConnectionModel connectionModel;
    private MovementModel movementModel = new MovementModel();
    private TextToSpeechModel textToSpeechModel;
    private MoveHeadModel moveHeadModel = new MoveHeadModel();
    private PosturesModel posturesModel;
    private MoveBodyModel moveBodyModel;

    private ALMotion alMotion;
    private Boolean first = true;

    public static void main(String[] args) {

    }

    public void initialize() {
        read();
        //Main.logger.info("Dies ist ein Test");
        //setLogger();
    }

    //private void setLogger(Stage primaryStage) throws Exception {
    //    Log log = new Log();
    //    Logger logger = new Logger(log, "");
    //    LogViewer logViewer = new LogViewer();
    //    logViewer.start(primaryStage);
    //}

    private void read() {
        IP = "";
        Port = "";
        try {
            try {
                file = new FileInputStream("connectionlog.txt");
                reader = new BufferedReader(new InputStreamReader(file));
                IP = reader.readLine();
                Port = reader.readLine();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            try {
                file.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            tx_IP.setText(IP);
            tx_Port.setText(Port);
            disconnectButton.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void btn_ConnectIsPressed(ActionEvent actionEvent) throws Exception {
        if (connectionModel == null){
            connectionModel = new ConnectionModel();
        }

        if(tx_Port.getText() != null && tx_IP.getText() != null){
            if (connectionModel.connect(tx_IP.getText(), Integer.parseInt(tx_Port.getText())))
            {

                if (app == null) {
                    app = new Application(new String[] {},connectionModel.getNaoUrl());
                    app.start();
                }

                if (app.session().isConnected()){
                    connectCircle.setFill(Color.rgb(60,230,30));
                    connectButton.setDisable(true);
                    disconnectButton.setDisable(false);
                    ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(app.session());
                    alAnimatedSpeech.say("You are connected");

                    if (posturesModel == null){
                        posturesModel = new PosturesModel();
                    }
                    List list = posturesModel.getPostures(app);
                    ObservableList postureList = FXCollections.observableArrayList(list);
                    if (postureList != null) dropDownPostures.setItems(postureList);
                }

            }
            else{
                //Main.logger.warn("IP stimmt nicht oder Port stimmt nicht, bitte Verbindung überprüfen");
            }
        }

    }

    public void move(ActionEvent actionEvent)throws Exception{
       Button button = (Button) actionEvent.getSource();
       movementModel.move(app,button.getId());
    }

    public void moveBody(KeyEvent keyEvent) throws Exception{
        if (moveBodyModel == null){
            moveBodyModel = new MoveBodyModel();
        }
        if (app != null){
            if (keyEvent.getText().equals("w")|| keyEvent.getText().equals("a") || keyEvent.getText().equals("s")
                    || keyEvent.getText().equals("d")){

                float velocity = (float) velocitySlider.getValue();
                if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                    moveBodyModel.moveKeyboard(app, keyEvent.getText(), velocity);
                } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                    moveBodyModel.moveKeyboard(app, "stop", velocity);
                    if (posturesModel == null) {
                        posturesModel = new PosturesModel();
                    }
                    posturesModel.makePosture(app, "Stand");

                }

            } else if (keyEvent.getText().equals("j")|| keyEvent.getText().equals("i") || keyEvent.getText().equals("k")
                    || keyEvent.getText().equals("l") || keyEvent.getText().equals("m") ) {

                if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                    moveBodyModel.moveKeyboard(app, keyEvent.getText());
                    //System.out.println(keyEvent.getText());
                }
                else if(keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)){
                    moveBodyModel.moveKeyboard(app, "stop");
                    System.out.println("stop");
                    if (posturesModel == null) {
                        posturesModel = new PosturesModel();
                    }
                    posturesModel.makePosture(app, "Stand");
                }
            }
        }


    }

    public void say(ActionEvent actionEvent)throws Exception{
        if (textToSpeechModel == null)
        {
            textToSpeechModel = new TextToSpeechModel();
        }
        if (textToSpeech.getText() != null){
            textToSpeechModel.say(app, textToSpeech.getText());
        }
    }

    public void doSitDown(ActionEvent actionEvent) throws Exception {
        if(app == null) {
            System.out.println("App ist null");
        }
        else{
            ALRobotPosture posture = new ALRobotPosture(app.session());
            posture.goToPosture("Crouch", 1.0f);
        }
    }

    public void disconnect(){
        app.session().close();
        connectCircle.setFill(Color.rgb(240,20,20));
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
    }

    public void postures(ActionEvent actionEvent) throws Exception{
        if (posturesModel == null){
            posturesModel = new PosturesModel();
        }
        String actualPose = (String) dropDownPostures.getValue();
        posturesModel.makePosture(app,actualPose);
    }
}
