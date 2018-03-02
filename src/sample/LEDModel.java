package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALLeds;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.List;

/**
 * this class handles the leds
 */
public class LEDModel {
   private ALLeds alLeds;

    /**
     * changes naos leds to colors, depending on groupname
     * @param session
     * @param groupLED
     * @param color
     */
    public void changeColor(Session session, String groupLED, String color) {
        try {
            alLeds = new ALLeds(session);
        } catch (Exception e) {
            Controller.log.write("Cannot create the object alLeds:"+e+". WARN");
        }
        String groupName = "AllLeds";
        switch (groupLED) {
            case "All LEDs":
                groupName = "AllLeds";
                break;
            case "BrainLEDs":
                groupName="BrainLeds";
                break;
            case "ChestLEDs":
                groupName = "ChestLeds";
                break;
            case "EarLEDs":
                groupName="EarLeds";
                break;
            case "Left Ear LEDs":
                groupName = "LeftEarLeds";
                break;
            case "Right Ear LEDs":
                groupName = "RightEarLeds";
                break;
            case "EyesLEDs":
                groupName = "FaceLeds";
                break;
            case "Left Eye LEDs":
                groupName = "LeftFaceLeds";
                break;
            case "Right Eye LEDs":
                groupName = "RightFaceLeds";
                break;
            case "FeetLEDs":
                groupName = "FeetLeds";
                break;

        }
        try{
        if (!color.equals("on") && !color.equals("off")&& !color.equals("")){
            alLeds.fadeRGB(groupName, color.toString(), 1f);
        }else if (color.equals("on")){
            alLeds.on(groupName);
        }else if(color.equals("off")){
            alLeds.off(groupName);
        }} catch (Exception e){
            Controller.log.write("Setting the LEDS was not successfull:"+e+". WARN");
        }
    }

    /**
     * sets ledgroups
     * @param session
     * @return
     * @throws Exception
     */

    public List getLEDs(Session session) throws Exception {
        alLeds = new ALLeds(session);
        String[] ledgroups = {"All LEDs", "BrainLEDs","ChestLEDs", "EarLEDs","Left Ear LEDs","Right Ear LEDs", "EyesLEDs", "Right Eye LEDs","Left Eyes LEDs", "FeetLEDs" };
        return Arrays.asList(ledgroups);
    }


}
