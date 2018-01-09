package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class Controller {
    @FXML TextField tx_IP;
    @FXML TextField tx_Port;
    @FXML Slider velocityslider;
    private Application app;
    private MovementModel movementModel = new MovementModel();
    private ALMotion alMotion;
    private Boolean first = true;
    public static void main(String[] args) {
        //TODO vielleicht hier Eingabe einer URL force
    }

    public void btn_ConnectIsPressed(ActionEvent actionEvent) throws Exception {
        ConnectionModel connectionModel = new ConnectionModel();
        if (connectionModel.connect(tx_IP.getText(),tx_Port.getText()))
        {
            app = new Application(new String[] {},connectionModel.getNaoUrl());
            app.start();
        }
        else{
            System.out.println("Ip stimmt nicht, Port stimmt nicht"); // TODO Fehlermeldung im Gui anzeigen
        }

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
