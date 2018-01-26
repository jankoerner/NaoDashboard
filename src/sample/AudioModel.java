package sample;

import com.aldebaran.qi.helper.proxies.ALAudioPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

import java.util.List;

public class AudioModel {
    @FXML Slider volumeSlider;
    private Controller controller = new Controller();
    private ALAudioPlayer alAudioPlayer;

    public List getSoundFiles() throws  Exception{
        if (alAudioPlayer==null){
            alAudioPlayer = new ALAudioPlayer(controller.getSession());
        }

        List Soundfiles = alAudioPlayer.getSoundSetFileNames("Aldebaran");
        return Soundfiles;
    }

    public void playSound(String filename/*,float Volume*/){

        try {
            if (alAudioPlayer == null) {
                alAudioPlayer = new ALAudioPlayer(controller.getSession());
            }
            //alAudioPlayer.setMasterVolume(Volume);
            alAudioPlayer.playSoundSetFile(filename);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
