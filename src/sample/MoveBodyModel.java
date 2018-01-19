package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALMotion;

public class MoveBodyModel {
    public static void main(String[] args) {

    }
    private ALMotion alMotion;
    private double upDown = 0;
    private double leftRight=0;

    public void moveKeyboard(Application app, String direction, Float velocity)throws Exception {
        if (alMotion == null){
            alMotion = new ALMotion(app.session());
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

    public void moveKeyboard(Application app, String direction)throws Exception{
        if (alMotion == null){
            alMotion = new ALMotion(app.session());
        }
        double velocity = 1.0;
        boolean isAbsolute = true;

         switch(direction){
            case "i": //Kopf nach oben
                if (upDown > -0.6720 & alMotion.moveIsActive() != true)
                {
                    upDown -= 0.1720;

                alMotion.angleInterpolation("HeadPitch", upDown, velocity, isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                System.out.println("i");
                break;
                }else{

                    break;
                }
            case"k": //Kopf nach unten
                if (upDown < 0.5149 & alMotion.moveIsActive() != true) {
                    upDown += 0.1149;

                    alMotion.angleInterpolation("HeadPitch", upDown, velocity, isAbsolute);
                    //alMotion.waitUntilMoveIsFinished();
                    System.out.println("k");
                    break;
                }else {
                    break;
                }
            case"j": //Kopf nach links
                if (leftRight < 2.08 & alMotion.moveIsActive() != true) {
                    leftRight += 0.08;

                    alMotion.angleInterpolation("HeadYaw", leftRight, velocity, isAbsolute);
                    //alMotion.waitUntilMoveIsFinished();
                    System.out.println("j");
                    break;
                }else {
                    break;
                }
            case"l": //Kopf nach rechts
                if (leftRight > -2.08 & alMotion.moveIsActive() != true) {
                    leftRight -= 0.08;

                    alMotion.angleInterpolation("HeadYaw", leftRight, velocity, isAbsolute);
                    //alMotion.waitUntilMoveIsFinished();
                    System.out.println("l");
                    break;
                }else {
                    break;
                }
            case "m": //Kopf wieder in die Mitte stellen
                if (leftRight != 0 || upDown != 0 & alMotion.moveIsActive() != true) {
                    float center = 0;

                    alMotion.angleInterpolation("HeadYaw", center, velocity, isAbsolute);
                    alMotion.angleInterpolation("HeadPitch", center, velocity, isAbsolute);
                    System.out.println("m");
                    break;
                }else {
                    break;
                }
            case"x":
                alMotion.stopMove();
                break;
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
}
