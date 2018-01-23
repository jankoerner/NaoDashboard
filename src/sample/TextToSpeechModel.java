package sample;


import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.util.List;

public class TextToSpeechModel {
    ALTextToSpeech alTextToSpeech;
    public static void main(String[] args) {

    }
    public void say(Application app, String text, float volume, String language)throws Exception{
        if (alTextToSpeech == null){
            alTextToSpeech = new ALTextToSpeech(app.session());
        }
        System.out.println(alTextToSpeech.getAvailableLanguages());
        alTextToSpeech.setVolume(volume);
        alTextToSpeech.say(text, language);

    }

    public List getLanguages(Application app)throws Exception{
        if (alTextToSpeech == null){
            alTextToSpeech = new ALTextToSpeech(app.session());
        }
        return alTextToSpeech.getAvailableLanguages();
    }
}
