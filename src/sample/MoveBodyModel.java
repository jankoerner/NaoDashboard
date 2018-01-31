package sample;

import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALMotion;
import javafx.scene.control.Button;

public class MoveBodyModel {
    public static void main(String[] args) {

    }
    private ALMotion alMotion;


    public void moveKeyboard(Session session, String direction, Float velocity, Float angle)throws Exception {
        alMotion = new ALMotion(session);
        System.out.println(direction);
            switch (direction) {
            case "w":
                Controller controller = new Controller();
                alMotion.move(velocity, 0f, angle);
                break;
            case "s":
                alMotion.move(-velocity, 0f, angle);
                break;
            case "a":
                alMotion.move(0f, velocity,angle);
                break;
            case "d":
                alMotion.move(0f, -velocity, angle);
                break;
            case "stop":
                alMotion.stopMove();
                break;
        }
    }

    public void moveKeyboard(Session session, String direction)throws Exception{
        alMotion = new ALMotion(session);
        System.out.println(direction);
        //alMotion.setStiffnesses("Head", 1f);
        switch (direction) {
            case "i":
               alMotion.changeAngles("HeadPitch", -0.015, 1f);
                break;
            case "k":
                alMotion.changeAngles("HeadPitch", 0.015, 1f);
                break;
            case "l":
                alMotion.changeAngles("HeadYaw", -0.015, 1f);
                break;
            case "j":
                alMotion.changeAngles("HeadYaw", 0.015, 1f);
                break;
            case "m":
                alMotion.setAngles("HeadYaw", 0f, 1f);
                alMotion.setAngles("HeadPitch", 0f, 1f);
                break;
        }
        //alMotion.setStiffnesses("Head", 0f);

    }

    public void moveHeadButtons(Session session, String direction) throws  Exception{
        alMotion = new ALMotion(session);

        System.out.println(direction);
        //alMotion.setStiffnesses("Head", 1f);
        switch (direction) {
            case "i":
                alMotion.changeAngles("HeadPitch", -0.75, 1f);
                break;
            case "k":
                alMotion.changeAngles("HeadPitch", 0.75, 1f);
                break;
            case "l":
                alMotion.changeAngles("HeadYaw", -0.75, 1f);
                break;
            case "j":
                alMotion.changeAngles("HeadYaw", 0.75, 1f);
                break;
            case "m":
                alMotion.setAngles("HeadYaw", 0f, 1f);
                alMotion.setAngles("HeadPitch", 0f, 1f);
                break;
        }

    }



    public void turn(Session session, float degree) throws Exception{
        alMotion = new ALMotion(session);
        alMotion.moveTo(0f,0f,degree);
        alMotion.waitUntilMoveIsFinished();
    }

    public void mode(Session session, String mode) throws Exception{
            alMotion = new ALMotion(session);
            switch (mode){
            case "Relax":
                alMotion.rest();
                break;
            case "Stand":
                alMotion.wakeUp();
                break;
        }
    }

    public boolean getMode(Session session) throws Exception{
        alMotion = new ALMotion(session);
        return alMotion.robotIsWakeUp();
    }
}
