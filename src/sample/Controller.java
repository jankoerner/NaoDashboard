package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.*;
import javafx.scene.control.*;

public class Controller {
    @FXML
    TextField tx_IP;
    private static String PORT = ":1234";
    private StringProperty NaoUrl = new SimpleStringProperty();

    public static void main(String[] args) {
        //TODO vielleicht hier Eingabe einer URL forcen

    }



    public final String getNaoUrl(){
        return NaoUrl.get();
    }

    public final void setNaoUrl(String IP){

        NaoUrl.set(IP);
    }

    public StringProperty NaoUrlProperty() {
        if(NaoUrl==null){
            NaoUrl = new SimpleStringProperty("127.0.0.1");
        }
        return NaoUrl;
    }


    public void btn_disconnectIsPressed(ActionEvent actionEvent) throws Exception{
        System.out.println("sure you want to disconnect");
    }

    public void btn_SayHelloIsPressed(ActionEvent actionEvent) throws Exception {
        System.out.println("Hallo");                //TODO: grafische Oberfläche bei der eine Speechbubble "Hallo" sagt
        Application app = new Application(new String[] {}, getNaoUrl());
        ALTextToSpeech tts = new ALTextToSpeech(app.session());
        tts.say("Hello Nao!");
    }
    public void btn_ConnectIsPressed(ActionEvent actionEvent) throws Exception {
        if (IsIPValid(tx_IP.getText())) {
            setNaoUrl(tx_IP.getText());
        }

        Application app = new Application(new String[] {}, getNaoUrl()+PORT);
        app.start();
    }

    public void btn_quitConnectionIsPressed(ActionEvent actionEvent) throws Exception{

    }

    public boolean IsIPValid(String NaoUrl){



        boolean Valid = true;
        return Valid;
    }
}
