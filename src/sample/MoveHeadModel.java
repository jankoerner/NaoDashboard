package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.*;

public class MoveHeadModel {
    public static void main(String[] args){


    }
    //Winkel für angleInerpolation();

    double[] targetAnglesYaw = {-2.0857, 0, 2.0857}; // Winkel für links und rechts (Radians)

    double[] targetAnglesPitch = {-0.6720, 0, 0.6720};//Winkel (Radians) für oben/unten anders rum als man evtl erwartet
                                                     // d.h. - Grad Winkel kippt den Kopf nach oben und vice versa

    double[] targetTimes = {2.0}; //Zeiten für angleInterpolation(); bzw. Geschwindigkeit der Bewegung.

    boolean isAbsolute = true; //für angleInterpolation(); nötig. Keine Ahnung wofür.

        //the magic happens here!
    public void moveHead(Application app, String headDirection)throws Exception{
        ALMotion alMotion = new ALMotion(app.session());

        switch(headDirection){
            case "i":
                alMotion.angleInterpolation("HeadPitch", targetAnglesPitch[0], targetTimes[0], isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                break;
            case"k":
                alMotion.angleInterpolation("HeadPitch", targetAnglesPitch[2], targetTimes[0], isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                break;
            case"j":
                alMotion.angleInterpolation("HeadYaw", targetAnglesYaw[0], targetTimes[0], isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                break;
            case"l":
                alMotion.angleInterpolation("HeadYaw", targetAnglesYaw[2], targetTimes[0], isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                break;
            case"m":
                alMotion.angleInterpolation("HeadYaw", targetAnglesYaw[1], targetTimes[0], isAbsolute);
                alMotion.angleInterpolation("HeadPitch", targetAnglesPitch[1], targetTimes[0], isAbsolute);
                alMotion.waitUntilMoveIsFinished();
                break;
        }
    }
}


