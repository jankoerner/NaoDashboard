package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

/**
 * Utils contains various helper methods
 */

public class Utils {

    private LogModel log = new LogModel();

    public float round(double i, int v){
        return (float) (Math.round(i/v) * v);
    }

    /**
     * @param cb (Combobox)
     * @return selected item of a combobox (String)
     */
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

    /**
     * @param lv Listview
     * @return selected item of a listview (String)
     */
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

    /**
     * @param number
     * @return true if a string is numeric AND between -180 and 180, false otherwise
     * Nao can only turn between -180 and 180 degrees
     */
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

    /**
     * lets Nao say when he disconnected
     * @param session
     */

    public static void disconnectMessage(Session session){
        try{
            ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(session);
            alAnimatedSpeech.say("Disconnected");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * lets nao say when he connected
     * @param session
     */

    public static void connectedMessage(Session session) {
        try {
            ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(session);
            alAnimatedSpeech.say("You are connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * updates a property of an item on the fx thread during runtime
     * @param property e.g. imageview.ImageProperty()
     * @param value new value of the item
     * @param <T> type needed by the items property
     */

    public static <T> void onFXThread(final ObjectProperty<T> property, final T value)
    {
        Platform.runLater(() -> {
            property.set(value);
        });
    }
}

