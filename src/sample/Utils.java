package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public class Utils {
    private LogModel log = new LogModel();

    public float round(double i, int v){
        return (float) (Math.round(i/v) * v);
    }

    public String getSelected(ComboBox cb){
       if(cb.getSelectionModel().getSelectedItem()!=null && cb.getSelectionModel().getSelectedItem() instanceof String){
           String selected = (String) cb.getSelectionModel().getSelectedItem();
           return selected;
       }else {
           log.write("Could not get any selected Item. WARN");
           log.write("Please check if you selected an item from the combobox. INFO");
           return null;
       }
    }
    public String getSelected(ListView lv){
        if(lv.getSelectionModel().getSelectedItem()!=null){
            String selected = lv.getSelectionModel().getSelectedItem().toString();
            return selected;
        } else{
            log.write("Could not get any selected Item. WARN");
            log.write("Please check if you selected an item from the list. INFO");
            return null;
        }
    }
    public boolean isNumber(String number){
        float d;
        try
        {
            d = Float.parseFloat(number);
        }
        catch(NumberFormatException nfe)
        {
            log.write("Please make sure to enter numbers. INFO");
            return false;
        }
        return d >= (-180) && d <= 180;
    }

    public static void disconnectMessage(Session session){
        try{
            ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(session);
            alAnimatedSpeech.say("Disconnected");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void connectedMessage(Session session) {
        try {
            ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(session);
            alAnimatedSpeech.say("You are connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
