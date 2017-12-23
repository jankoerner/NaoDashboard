package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConnectionModel {
    private StringProperty NaoUrl = new SimpleStringProperty();


    public static void main(String[] args) {

    }

    public boolean connect(String ip, String port){
        if (isIPValid(ip) && isPortValid(port)){
            setNaoUrl(ip,port);
            return true;
        }else {
            return false;
        }
    }

    public boolean isIPValid(String ip){
        boolean valid = true; // TODO Validirungs überlegen
        return valid;
    }

    public boolean isPortValid(String port){
        boolean valid = true;  // TODO Validirungs überlegen
        return valid;
    }

    public final void setNaoUrl(String ip, String port){
        NaoUrl.set("tcp://"+ip + ":" + port  );
    }

    public final String getNaoUrl(){
        return NaoUrl.get();
    }

}
