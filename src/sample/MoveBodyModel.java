package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALMotion;

public class MoveBodyModel {
    public static void main(String[] args) {

    }
    private ALMotion alMotion;
    private double upDown = 0;
    private double leftRight=0;
    private long lastStrokeProcessed = 0;

    public void moveKeyboard(Application app, String direction, Float velocity)throws Exception {
        if (alMotion == null){
            alMotion = new ALMotion(app.session());
        }
        switch (direction) {
            case "w":
                alMotion.async().move(velocity, 0f, 0f);
                break;
            case "s":
                alMotion.async().move(-velocity, 0f, 0f);
                break;
            case "a":
                alMotion.async().move(0f, velocity, 0f);
                break;
            case "d":
                alMotion.async().move(0f, -velocity, 0f);
                break;
            case "stop":
                alMotion.stopMove();
                break;
        }
    }

    public void moveKeyboard(Application app, String direction)throws Exception{
        if (alMotion == null){
            alMotion = new ALMotion(app.session());
        }
        double velocity = 1.0;
        boolean isAbsolute = true;

        //timer um zu versichern, dass die KEY_PRESSED Events nicht angestaut werden. . .
        if(System.currentTimeMillis() - lastStrokeProcessed > 100){
         switch(direction){
            case "i": //Kopf nach oben
                if (upDown > -0.6720 & alMotion.moveIsActive() != true)
                {
                    upDown -= 0.0720;

                alMotion.async().angleInterpolation("HeadPitch", upDown, velocity + 1, isAbsolute);
                lastStrokeProcessed = System.currentTimeMillis();
                break;
                }else{
                    break;
                }

            case "k": //Kopf nach unten
                if (upDown < 0.5149 & alMotion.moveIsActive() != true) {
                    upDown += 0.1149;

                    alMotion.async().angleInterpolation("HeadPitch", upDown, velocity + 1, isAbsolute);
                    lastStrokeProcessed = System.currentTimeMillis();
                    break;
                }else {
                    break;
                }

            case "j": //Kopf nach links
                if (leftRight < 2.08 & alMotion.moveIsActive() != true) {
                    leftRight += 0.08;

                    alMotion.async().angleInterpolation("HeadYaw", leftRight, velocity + 1, isAbsolute);
                    lastStrokeProcessed = System.currentTimeMillis();
                    break;

                }else {
                    break;
                }

            case "l": //Kopf nach rechts
                if (leftRight > -2.08 & alMotion.moveIsActive() != true) {
                    leftRight -= 0.08;

                    alMotion.async().angleInterpolation("HeadYaw", leftRight, velocity + 1, isAbsolute);
                    lastStrokeProcessed = System.currentTimeMillis();
                    break;

                }else {
                    break;
                }

            case "m": //Kopf wieder in die Mitte stellen
                if (leftRight != 0 || upDown != 0 & alMotion.moveIsActive() != true) {
                    float center = 0;

                    alMotion.async().angleInterpolation("HeadYaw", center, velocity + 1, isAbsolute);
                    alMotion.async().angleInterpolation("HeadPitch", center, velocity + 1, isAbsolute);
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



    public void turn(Application app, float degree) throws Exception{
        if (alMotion == null){
            alMotion = new ALMotion(app.session());
        }
        alMotion.moveTo(0f,0f,degree);
        alMotion.waitUntilMoveIsFinished();
    }

    public void mode(Application app, String mode) throws Exception{
        if (alMotion == null){
            alMotion = new ALMotion(app.session());
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

    public boolean getMode(Application app) throws Exception{
        if (alMotion == null){
            alMotion = new ALMotion(app.session());
        }
        return alMotion.robotIsWakeUp();
    }
}
