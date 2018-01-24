package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALLeds;
import java.util.List;


public class LEDModel {
    Controller controller;
    private ALLeds alLeds;
    private List ledList;

    public void setAlLeds() throws Exception {
        if (alLeds==null){
            ALLeds alLeds = new ALLeds(controller.getApp().session());
        }
        alLeds.rasta((float) 12312321321321.123213213213213);
    }

    public List getLEDs(Application app) throws Exception {
        if (alLeds == null){
            alLeds = new ALLeds(app.session());
        }
        ledList = alLeds.listGroups();
        return ledList;
    }
}
