package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML
    TextField tx_IP;
    @FXML
    TextField tx_Port;
    Application app;
    MovementModel movementModel;

    public static void main(String[] args) {
        //TODO vielleicht hier Eingabe einer URL forcen
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

     public void moveForward(ActionEvent actionEvent)throws Exception {
        movementModel = new MovementModel();
        movementModel.move(app,"forward");
        System.out.println("test2");
    }

    public void doSitDown(ActionEvent actionEvent) throws Exception {
        if(app == null) {
            System.out.println("App ist null");
        }
        ALRobotPosture posture = new ALRobotPosture(app.session());
        posture.goToPosture("Crouch", 1.0f);
    }
}
