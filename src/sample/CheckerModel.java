package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALBattery;
import com.aldebaran.qi.helper.proxies.ALBodyTemperature;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.aldebaran.qi.helper.proxies.ALTracker;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CheckerModel {

    private ALMemory memory;
    private TextToSpeechModel textToSpeechModel;
    private boolean timerKiller = false;
    private boolean end = false;

    private boolean LandmarkTrackerActive = false;
    private TextToSpeechModel textToSpeechModel = new TextToSpeechModel();
    private TrackerModel trackerModel = new TrackerModel();
    private ALTracker alTracker;
    public void setLandmarkTrackerActive(boolean isActive){
        LandmarkTrackerActive = isActive;
        System.out.println(LandmarkTrackerActive);
    }
    private String landmarkID;
    boolean tracked = false;


    public static void main(String[] args) {

    }




    public void setBatteryPercentage(double percentage, ProgressBar batteryPercentage){
        batteryPercentage.setProgress(percentage/100);
    }

    public void checkBatteryCharge(Session session, Circle batteryCicle, ProgressBar batteryPercentage){
        try{
            memory = new ALMemory(session);
            ALBattery alBattery = new ALBattery(session);

            if(end){
                memory.unsubscribeAllEvents();
            }
            if(alBattery.getBatteryCharge() == 0){
                batteryCicle.setFill(Color.BLACK);
                System.out.println("No battery detected.");
            }
            memory.subscribeToEvent("BatteryChargeChanged", new EventCallback<Integer>() {
                @Override
                public void onEvent(Integer percentage) throws InterruptedException, CallError {
                    int charge = percentage;
                    if (charge > 75){
                        batteryCicle.setFill(Color.GREEN);
                        System.out.println("Battery remaining " + charge);
                    }else if (charge < 75 & charge > 30){
                        batteryCicle.setFill(Color.ORANGE);
                        System.out.println("Battery remaining " + charge);
                    }else if (charge < 30 & charge != 0 ){
                        batteryCicle.setFill(Color.RED);
                        System.out.println("Battery remaining " + charge);
                    }
                    setBatteryPercentage(alBattery.getBatteryCharge(), batteryPercentage);
                }
            });


        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public void checkTemperature(Session session, Text temperatureText){
        Timer temperatureTimer = new Timer();
        TimerTask checkTemp = new TimerTask() {
            @Override
            public void run() {
                try{
                    if(timerKiller){
                        temperatureTimer.cancel();
                    }
                    if (session.isConnected()){
                        ALBodyTemperature alBodyTemperature = new ALBodyTemperature(session);
                        if(alBodyTemperature.getTemperatureDiagnosis() instanceof ArrayList){
                            ArrayList tempEvent = (ArrayList) alBodyTemperature.getTemperatureDiagnosis();
                            if(tempEvent.get(0).equals(1)){
                                temperatureText.setText("Warm");
                                temperatureText.setFill(Color.ORANGE);
                            }else{
                                temperatureText.setText("Hot");
                                temperatureText.setFill(Color.RED);
                            }
                        }else{
                            temperatureText.setText("Cool");
                            temperatureText.setFill(Color.GREEN);
                        }
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        temperatureTimer.scheduleAtFixedRate(checkTemp, 1000, 6000);
    }

    public void checkTouch(Session session, TextArea midButtonText, TextArea rearButtonText, Slider volumeSlider,
                           Slider voiceSlider, Slider voiceSpeedSlider, ComboBox dropDownLanguages){
        try {
            memory = new ALMemory(session);

            if(textToSpeechModel == null){
                textToSpeechModel = new TextToSpeechModel();
            }

            if(end){
                memory.unsubscribeAllEvents();
            }
            memory.subscribeToEvent("FrontTactilTouched", new EventCallback<Float>() {
                @Override
                public void onEvent(Float val) throws InterruptedException, CallError {
                    float touchState = val;
                    if (touchState == 1.0) {
                        try {
                            MoveBodyModel moveBodyModel = new MoveBodyModel();
                            moveBodyModel.frontTouched(session);
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                }

            });
            memory.subscribeToEvent("MiddleTactilTouched", new EventCallback<Float>() {
                @Override
                public void onEvent(Float val) throws InterruptedException, CallError {
                    float touchState = val;
                    if(touchState == 1.0){
                        System.out.println("Middle head bumper has been touched");
                        if(midButtonText.getText() != null){
                            Float volume = (float) volumeSlider.getValue();
                            String language =(String) dropDownLanguages.getValue();
                            String voice = String.valueOf((int)voiceSlider.getValue());
                            String speed = String.valueOf((int)voiceSpeedSlider.getValue());
                            try{
                                textToSpeechModel.say(session, midButtonText.getText(),volume, language, voice, speed);
                            }catch(Exception exception){
                                exception.printStackTrace();
                            }
                        }
                    }
                }
            });
            memory.subscribeToEvent("RearTactilTouched", new EventCallback<Float>() {
                @Override
                public void onEvent(Float val) throws InterruptedException, CallError {
                    float touchState = val;
                    if(touchState == 1.0){
                        System.out.println("Rear head bumper has been touched");
                        if(rearButtonText.getText() != null){
                            Float volume = (float) volumeSlider.getValue();
                            String language =(String) dropDownLanguages.getValue();
                            String voice = String.valueOf((int)voiceSlider.getValue());
                            String speed = String.valueOf((int)voiceSpeedSlider.getValue());
                            try{
                                textToSpeechModel.say(session, rearButtonText.getText(),volume, language, voice, speed);
                            }catch(Exception exception){
                                exception.printStackTrace();
                            }
                        }

                    }

                }
            });
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void LandmarkTracker(Session session)throws Exception{
        /*ALVideoDevice alVideoDevice = new ALVideoDevice(session);
        alVideoDevice.openCamera(0);
        ALVisionRecognition alVisionRecognition = new ALVisionRecognition(session);
        alVisionRecognition.subscribe("LandmarkDetected");*/
        if (LandmarkTrackerActive){
            TrackerModel trackerModel = new TrackerModel();
            memory.subscribeToEvent("LandmarkDetected", new EventCallback<ArrayList>() {
                @Override
                public void onEvent(ArrayList o) throws InterruptedException, CallError {
                    try {
                        if (tracked==false){
                            trackerModel.trackLandmark(session,o,alTracker);
                            tracked = true;
                        }
                    }catch (Exception e){

                    }
                }
            });
        }
    }

    public void enableLandmarkTracker(Session session)throws Exception{
        if (LandmarkTrackerActive){
            alTracker = new ALTracker(session);
            alTracker.trackEvent("LandmarkDetected");
        }
    }
    public void test()throws Exception{
        System.out.println(memory.getDataListName());
    }


    public void killCheckers() {
        end = true;
        timerKiller = true;

    }
}
