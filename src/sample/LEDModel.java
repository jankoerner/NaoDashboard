package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALLeds;
import java.util.List;


public class LEDModel {
    private Controller controller;
    private ALLeds alLeds;
    private List ledList;


    public List getLEDs(Session session) throws Exception {
        if (alLeds == null){
            alLeds = new ALLeds(session);
        }
        ledList = alLeds.listGroups();
        return ledList;
    }
}
