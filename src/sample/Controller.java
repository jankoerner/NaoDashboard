package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextFlow;

import java.util.List;

public class Controller {
    @FXML TextField tx_IP, tx_Port;
    @FXML Slider velocitySlider;
    @FXML TextFlow tfl_log;
    @FXML TextArea textToSpeech;
    @FXML Button w,a,s,d, connectButton, disconnectButton;
    @FXML Circle connectCircle;
    @FXML ComboBox dropDownPostures;

    private Application app;
    private ConnectionModel connectionModel;
    private MovementModel movementModel = new MovementModel();
    private TextToSpeechModel textToSpeechModel;
    private MoveHeadModel moveHeadModel = new MoveHeadModel();
    private PosturesModel posturesModel;
    private MoveBodyModel moveBodyModel;

    private ALMotion alMotion;
    private Boolean first = true;

    Log log = new Log();
    Logger logger = new Logger(log, "");

    public static void main(String[] args) {

    }

    public void btn_ConnectIsPressed(ActionEvent actionEvent) throws Exception {
        if (connectionModel == null){
            connectionModel = new ConnectionModel();
        }

        if(tx_Port.getText() != null && tx_IP.getText() != null){
            if (connectionModel.connect(tx_IP.getText(), Integer.parseInt(tx_Port.getText())))
            {

                if(app == null){
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
                    dropDownPostures.setItems(postureList);
                }

            }
            else{
                logger.warn("IP stimmt nicht oder Port stimmt nicht, bitte Verbindung überprüfen");
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

        if (keyEvent.getText().equals("w")|| keyEvent.getText().equals("a") || keyEvent.getText().equals("s")|| keyEvent.getText().equals("d")){
           if (app != null) {
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
           }
       } else if (keyEvent.getText().equals("j")|| keyEvent.getText().equals("i") || keyEvent.getText().equals("k")|| keyEvent.getText().equals("l") ) {
            if (app != null) {
                if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                    moveBodyModel.moveKeyboard(app, keyEvent.getText());
                    System.out.println(keyEvent.getText());
                }
                else if(keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)){
                    moveBodyModel.moveKeyboard(app, "stop");
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
