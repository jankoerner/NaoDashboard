package sample;


import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.util.List;

public class TextToSpeechModel {
    private ALTextToSpeech alTextToSpeech;
    private ALAnimatedSpeech alAnimatedSpeech;

    public static void main(String[] args) {

    }

    public void say(Application app, String text, float volume, String language, float pitch) throws Exception {
        if (alTextToSpeech == null) {
            alTextToSpeech = new ALTextToSpeech(app.session());
        }
        alTextToSpeech.setVolume(volume);
        alTextToSpeech.setParameter("pitchShift", pitch );
        System.out.println(alTextToSpeech.getAvailableVoices());
        //alAnimatedSpeech.say(text);
        alTextToSpeech.say(text, language);
        }


    public List getLanguages(Application app)throws Exception{
        if (alTextToSpeech == null){
            alTextToSpeech = new ALTextToSpeech(app.session());
        }
        return alTextToSpeech.getAvailableLanguages();
    }
}
