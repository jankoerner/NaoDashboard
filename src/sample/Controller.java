package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controller {
    @FXML TextField tx_IP;
    @FXML TextField tx_Port;
    Application app;
    MovementModel movementModel = new MovementModel();

    Log log = new Log();
    Logger logger = new Logger(log, "");

    public static void main(String[] args) {
        //TODO vielleicht hier Eingabe einer URL forcen

    }

    public void btn_ConnectIsPressed(ActionEvent actionEvent) throws Exception {
        ConnectionModel connectionModel = new ConnectionModel();
        if (connectionModel.connect(tx_IP.getText(),tx_Port.getText()))
        {
            app = new Application(new String[] {},connectionModel.getNaoUrl());
            app.start();
        } else logger.warn("IP stimmt nicht oder Port stimmt nicht, bitte Verbindung überprüfen");

    }

    public void moveForward(ActionEvent actionEvent)throws Exception {
        movementModel.move(app,"forward");
    }

    public void moveBackward(ActionEvent actionEvent)throws Exception {
        movementModel.move(app,"backward");
        System.out.println("zurück");
    }

    public void moveLeft(ActionEvent actionEvent)throws Exception {
        movementModel.move(app,"left");
        System.out.println("links");
    }

    public void moveRight(ActionEvent actionEvent)throws Exception {
        movementModel.move(app,"right");
        System.out.println("rechts");
    }

    public void doSitDown(ActionEvent actionEvent) throws Exception {
        if(app == null) {
            System.out.println("App ist null");
        }
        ALRobotPosture posture = new ALRobotPosture(app.session());
        posture.goToPosture("Crouch", 1.0f);
    }
}
