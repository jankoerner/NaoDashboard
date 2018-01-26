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

    public void moveKeyboard(Session session, String direction, Float velocity)throws Exception {
        if (alMotion == null){
            alMotion = new ALMotion(session);
        }
        switch (direction) {
            case "w":
                alMotion.move(velocity, 0f, 0f);
                break;
            case "s":
                alMotion.move(-velocity, 0f, 0f);
                break;
            case "a":
                alMotion.move(0f, velocity, 0f);
                break;
            case "d":
                alMotion.move(0f, -velocity, 0f);
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
        double velocity = 1.0;

        //timer um zu versichern, dass die KEY_PRESSED Events nicht angestaut werden. . .
        if(System.currentTimeMillis() - lastStrokeProcessed > 200){
         switch(direction){
            case "i": //Kopf nach oben
                if (upDown > -0.6720 & !alMotion.moveIsActive())
                {
                    upDown -= 0.1720;

                alMotion.angleInterpolation("HeadPitch", upDown, velocity + 1, true);
                lastStrokeProcessed = System.currentTimeMillis();
                break;
                }else{
                    break;
                }

            case "k": //Kopf nach unten
                if (upDown < 0.5149 & !alMotion.moveIsActive()) {
                    upDown += 0.1149;

                    alMotion.angleInterpolation("HeadPitch", upDown, velocity + 1, true);
                    lastStrokeProcessed = System.currentTimeMillis();
                    break;
                }else {
                    break;
                }

            case "j": //Kopf nach links
                if (leftRight < 2.08 & !alMotion.moveIsActive()) {
                    leftRight += 0.08;

                    alMotion.angleInterpolation("HeadYaw", leftRight, velocity + 1, true);
                    lastStrokeProcessed = System.currentTimeMillis();
                    break;

                }else {
                    break;
                }

            case "l": //Kopf nach rechts
                if (leftRight > -2.08 & !alMotion.moveIsActive()) {
                    leftRight -= 0.08;

                    alMotion.angleInterpolation("HeadYaw", leftRight, velocity + 1, true);
                    lastStrokeProcessed = System.currentTimeMillis();
                    break;

                }else {
                    break;
                }

            case "m": //Kopf wieder in die Mitte stellen
                if (leftRight != 0 || upDown != 0 & !alMotion.moveIsActive()) {
                    float center = 0;

                    alMotion.angleInterpolation("HeadYaw", center, velocity + 1, true);
                    alMotion.angleInterpolation("HeadPitch", center, velocity + 1, true);
                    lastStrokeProcessed = System.currentTimeMillis();
                    break;

                }else {
                    break;
                }

            case "x":
                alMotion.stopMove();
                break;
            }
        }
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
