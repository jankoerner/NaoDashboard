package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALMotion;

public class MoveBodyModel {
    public static void main(String[] args) {

    }
    ALMotion alMotion;
    double upDown = 0;
    double leftRight=0;
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
        double velocity = 2.0;
        boolean isAbsolute = true;

        switch(direction){
            case "i":
                if (upDown > -0.5)
                {
                    upDown =- 0.1720;
                }
                alMotion.angleInterpolation("HeadPitch", upDown, velocity, isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                System.out.println("i");
                break;
            case"k":
                if (upDown < 0.5)
                {
                    upDown =+ 0.1720;
                }
                alMotion.angleInterpolation("HeadPitch", upDown, velocity, isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                System.out.println("k");
                break;
            case"j":
                if (leftRight < 1.8){
                    leftRight=+ 0.2857;
                }
                alMotion.angleInterpolation("HeadYaw", leftRight, velocity, isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                System.out.println("j");
                break;
            case"l":
                if (leftRight > 1.8){
                    leftRight=-0.2857;
                }
                alMotion.angleInterpolation("HeadYaw", leftRight, velocity, isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                System.out.println("l");
                break;
            case"stop":
                alMotion.stopMove();
                alMotion.waitUntilMoveIsFinished();
                break;
                /*leftRight = 0;
                upDown = 0;
                alMotion.angleInterpolation("HeadYaw", leftRight, velocity, true);
                alMotion.angleInterpolation("HeadPitch", upDown, velocity, true);
                alMotion.waitUntilMoveIsFinished();
                break;*/
        }

    }
}
