package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;

public class Controller {
    @FXML TextField tx_IP;
    @FXML TextField tx_Port;
    @FXML Slider velocityslider;
    private Application app;
    private MovementModel movementModel = new MovementModel();
    private ALMotion alMotion;
    private Boolean first = true;

    Log log = new Log();
    Logger logger = new Logger(log, "");

    public static void main(String[] args) {
        //TODO vielleicht hier Eingabe einer URL forcen

    }

    public void btn_ConnectIsPressed(ActionEvent actionEvent) throws Exception {
        ConnectionModel connectionModel = new ConnectionModel();
        if (connectionModel.connect(tx_IP.getText(), Integer.parseInt(tx_Port.getText())))
        {
            app = new Application(new String[] {},connectionModel.getNaoUrl());
            app.start();
        } else logger.warn("IP stimmt nicht oder Port stimmt nicht, bitte Verbindung überprüfen");

    }
    public void moveKeyBoard(KeyEvent keyEvent)throws Exception{
        if (app != null){
            if (first){
                alMotion = new ALMotion(app.session());
                first = false;
            }
            float velocity = (float) velocityslider.getValue();
            if(keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)){
                movementModel.moveKeyboard(alMotion,keyEvent.getText(),velocity);
                System.out.println(velocityslider.getValue());
            }
            else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)){
                alMotion.killMove();
                movementModel.moveKeyboard(alMotion,"stop", velocity);
                ALRobotPosture posture = new ALRobotPosture(app.session());
                posture.goToPosture("Stand", 1.0f);

            }
        }


    }

    public void move(ActionEvent actionEvent)throws Exception{
       Button button = (Button) actionEvent.getSource();
       movementModel.move(app,button.getId());
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
}
