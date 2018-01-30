package sample;

import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALMotion;

public class MoveBodyModel {
    public static void main(String[] args) {

    }
    private ALMotion alMotion;
    private double upDown = 0;
    private double leftRight=0;
    private long lastStrokeProcessed = 0;

    public void moveKeyboard(Session session, String direction, Float velocity, Float angle)throws Exception {
        if (alMotion == null){
            alMotion = new ALMotion(session);
        }
        switch (direction) {
            case "w":
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
        if (alMotion == null){
            alMotion = new ALMotion(session);
        }
        alMotion.setStiffnesses("Head", 1f);
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
        alMotion.setStiffnesses("Head", 0f);

    }



    public void turn(Session session, float degree) throws Exception{
        if (alMotion == null){
            alMotion = new ALMotion(session);
        }
        alMotion.moveTo(0f,0f,degree);
        alMotion.waitUntilMoveIsFinished();
    }

    public void mode(Session session, String mode) throws Exception{
        if (alMotion == null){
            alMotion = new ALMotion(session);
        }
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
        if (alMotion == null){
            alMotion = new ALMotion(session);
        }
        return alMotion.robotIsWakeUp();
    }
}
