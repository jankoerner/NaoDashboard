package sample;

import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALAudioPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

import java.util.List;

/**
 * this class is responsible for audiofiles on nao
 */
public class AudioModel {
    private Controller controller = new Controller();
    private ALAudioPlayer alAudioPlayer;

    /**
     * returns a list of soundfiles in the setname aldebaran if the set exists
     * @param session
     * @return Soundfiles
     * @throws Exception
     */
    public List getSoundFiles(Session session) throws Exception{
        List Soundfiles = null;
        try {
            alAudioPlayer = new ALAudioPlayer(session);
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            //Naos without the soundset aldebaran installed would return null and create an exception
            //checking if the soundset exists handles this exception
            //for the virtual robot in choregraphe only checking for the method getSoundSetFileNames  is sufficient
            if (alAudioPlayer.getMethodList().contains("getSoundSetFileNames") && (alAudioPlayer.getInstalledSoundSetsList().contains("Aldebaran")))
            Soundfiles = alAudioPlayer.getSoundSetFileNames("Aldebaran");

        } catch (com.aldebaran.qi.CallError e){
            e.printStackTrace();
        }
        return Soundfiles;

    }

    /**
     * plays a selected sound
     * @param filename
     */

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
