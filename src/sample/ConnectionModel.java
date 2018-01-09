package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionModel {
    private StringProperty NaoUrl = new SimpleStringProperty();


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

}
