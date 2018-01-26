package sample;


import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.util.List;

public class TextToSpeechModel {
    private ALTextToSpeech alTextToSpeech;

    public static void main(String[] args) {

    }

    public void say(Application app, String text, float volume, String language, String pitch, String speed) throws Exception {
        alTextToSpeech= new ALTextToSpeech(app.session());
        alTextToSpeech.async().setVolume(volume);
        alTextToSpeech.say("\\rspd="+speed+"\\\\vct="+pitch+"\\"+text,language);
    }


    public List getLanguages(Application app)throws Exception{
        if (alTextToSpeech == null){
            alTextToSpeech = new ALTextToSpeech(app.session());
        }
        return alTextToSpeech.getAvailableLanguages();
    }
}
