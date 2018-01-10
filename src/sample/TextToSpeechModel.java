package sample;


import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;

public class TextToSpeechModel {
    ALAnimatedSpeech alAnimatedSpeech;
    public static void main(String[] args) {

    }
    public void say(Application app, String text)throws Exception{
        if (alAnimatedSpeech == null){
            alAnimatedSpeech = new ALAnimatedSpeech(app.session());
        }
        alAnimatedSpeech.say(text);
    }
}
