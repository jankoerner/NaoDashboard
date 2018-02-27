package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALMovementDetection;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MovementDetectionModel {

    private ALMovementDetection alMovementDetection;
    private ALMemory alMemory;
    private ALTextToSpeech alTextToSpeech;
    private long eventID;

    public boolean detectionEnabled = false;


    public void movementDetection(Session session) throws Exception {
        if( alTextToSpeech == null){
            alTextToSpeech = new ALTextToSpeech(session);
            System.out.println("text to speech");
        }
        if(alMemory == null){
            alMemory = new ALMemory(session);
            System.out.println("memory");
        }
        if(alMovementDetection == null){
            alMovementDetection = new ALMovementDetection(session);
            System.out.println("movement detection");
        }
        if(!detectionEnabled){
            alMemory.unsubscribeAllEvents();
        }
        alMovementDetection.setActiveCamera(0);
        alMovementDetection.setColorSensitivity((float)0.5);
        alMovementDetection.subscribe("MovementDetection/MovementDetected");

        eventID= alMemory.subscribeToEvent("MovementDetection/MovementDetected", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                System.out.println("Movement");
                alTextToSpeech.say("Danger Will Robinson! Danger!", "English");
                alMemory.unsubscribeToEvent(eventID);
            }
        });
        System.out.println("Alarm Enabled");



    }

    public boolean isDetectionEnabled() {
        return detectionEnabled;
    }

    public void setDetectionEnabled(boolean detectionEnabled) {
        this.detectionEnabled = detectionEnabled;
    }


}