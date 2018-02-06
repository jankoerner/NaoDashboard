package sample;

import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALAudioPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

import java.util.List;

public class AudioModel {
    private Controller controller = new Controller();
    private ALAudioPlayer alAudioPlayer;

    public List getSoundFiles(Session session) throws Exception{
        List Soundfiles = null;
        try {
            alAudioPlayer = new ALAudioPlayer(session);
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            // NAOs die keine Soundfiles haben haben auch nicht die Methode "getSoundSetFileNames"
            // so kommt es zu keiner Exception
            if (alAudioPlayer.getMethodList().contains("getSoundSetFileNames") && (alAudioPlayer.getInstalledSoundSetsList().contains("Aldebaran")))
            Soundfiles = alAudioPlayer.getSoundSetFileNames("Aldebaran");

        } catch (com.aldebaran.qi.CallError e){
            e.printStackTrace();
        }
        return Soundfiles;

    }

    public void playSound(String filename/*,float Volume*/){

        try {
            alAudioPlayer = new ALAudioPlayer(controller.getSession());
            //alAudioPlayer.setMasterVolume(Volume); //
            alAudioPlayer.playSoundSetFile(filename);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
