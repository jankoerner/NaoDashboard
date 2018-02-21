package sample;


import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.util.List;

/**
 * used to make nao speak
 */
public class TextToSpeechModel {
    private ALTextToSpeech alTextToSpeech;

    /**
     * makes Nao say a text
     * @param session
     * @param text to speak
     * @param volume
     * @param language a language selected from the available languages
     * @param pitch selected pitch
     * @param speed selected speed
     * @throws Exception
     */
    public void say(Session session, String text, float volume, String language, String pitch, String speed) throws Exception {
        alTextToSpeech= new ALTextToSpeech(session);
        alTextToSpeech.async().setVolume(volume);
        alTextToSpeech.say("\\rspd="+speed+"\\\\vct="+pitch+"\\"+text,language);
    }

    /**
     * returns a list of available languages on nao
     * @param session
     * @return
     * @throws Exception
     */
    public List getLanguages(Session session)throws Exception{
        alTextToSpeech = new ALTextToSpeech(session);
        return alTextToSpeech.getAvailableLanguages();
    }
}
