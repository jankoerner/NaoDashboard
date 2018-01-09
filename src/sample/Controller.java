package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;

public class Controller {
    @FXML TextField tx_IP;
    @FXML TextField tx_Port;
    Application app;
    MovementModel movementModel = new MovementModel();

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
        movementModel.move(app,"forward");
        System.out.println("vor");
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
