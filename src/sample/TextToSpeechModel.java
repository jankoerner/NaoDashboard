package sample;


import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.util.List;

public class TextToSpeechModel {
    private ALTextToSpeech alTextToSpeech;

    public static void main(String[] args) {

    }

    public void say(Session session, String text, float volume, String language, String pitch, String speed) throws Exception {
        alTextToSpeech= new ALTextToSpeech(session);
        alTextToSpeech.async().setVolume(volume);
        alTextToSpeech.say("\\rspd="+speed+"\\\\vct="+pitch+"\\"+text,language);
    }


    public List getLanguages(Session session)throws Exception{
        alTextToSpeech = new ALTextToSpeech(session);
        return alTextToSpeech.getAvailableLanguages();
    }
}
