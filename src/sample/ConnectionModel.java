package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import com.aldebaran.qi.helper.proxies.ALConnectionManager;
import com.aldebaran.qi.helper.proxies.ALUserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;


public class ConnectionModel {
    private StringProperty NaoUrl = new SimpleStringProperty();
    ALAnimatedSpeech alAnimatedSpeech;

    public static void main(String[] args) {

    }

    public boolean connect(String ip, Integer port) {
        if (isIPValid(ip, port, 5000)) {
            setNaoUrl(ip, Integer.toString(port));
            return true;
        }else {
            return false;
        }
    }

    public boolean isIPValid(String ip, int port, Integer timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    public boolean isPortValid(String port){
        boolean valid = true;  // TODO Validirungs überlegen
        return valid;
    }

    public final void setNaoUrl(String ip, String port){
        NaoUrl.set("tcp://"+ip + ":" + port);
    }

    public final String getNaoUrl(){
        return NaoUrl.get();
    }

    public void disconnect(com.aldebaran.qi.Application app)throws Exception{
        alAnimatedSpeech = new ALAnimatedSpeech(app.session());
        alAnimatedSpeech.say("Bye");
        
    }

}
