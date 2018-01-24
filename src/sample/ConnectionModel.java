package sample;

import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ConnectionModel {
    private StringProperty NaoUrl = new SimpleStringProperty();
    private ALAnimatedSpeech alAnimatedSpeech;

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

    private boolean isIPValid(String ip, int port, Integer timeout) {
        if ((!ip.equals("")) && (port > 0)) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, port), timeout);
                return true;
            } catch (IOException e) {
                return false; //  timeout / unreachable / failed DNS lookup
            }
        } else return false;
    }

    private final void setNaoUrl(String ip, String port) {
        NaoUrl.set("tcp://"+ip + ":" + port);
    }

    public final String getNaoUrl(){
        return NaoUrl.get();
    }

}
