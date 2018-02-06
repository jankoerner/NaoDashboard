package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALBattery;
import com.aldebaran.qi.helper.proxies.ALBodyTemperature;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALTracker;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CheckerModel {

    private ALMemory memory;
    private boolean timerKiller = false;
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

    public void checkBatteryCharge(Session session, Circle batteryCircle, ProgressBar batteryPercentage){
        Timer batteryTimer = new Timer();
        TimerTask checkBattery = new TimerTask() {
            @Override
            public void run(){
                try {
                    if(timerKiller){
                        batteryTimer.cancel();
                    }
                    if (session.isConnected()){
                        ALBattery alBattery = new ALBattery(session);
                        if (alBattery.getBatteryCharge() > 75) {
                            batteryCircle.setFill(Color.GREEN);
                        } else if (alBattery.getBatteryCharge() < 75 & alBattery.getBatteryCharge() > 30) {
                            batteryCircle.setFill(Color.ORANGE);
                        } else if (alBattery.getBatteryCharge() < 30 & alBattery.getBatteryCharge() != 0) {
                            batteryCircle.setFill(Color.RED);
                        } else {
                            batteryCircle.setFill(Color.BLACK);
                            System.out.println("No battery detected.");
                        }
                        setBatteryPercentage(alBattery.getBatteryCharge(), batteryPercentage);
                    }


                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        batteryTimer.scheduleAtFixedRate(checkBattery, 1000, 6000);
    }


    public void setBatteryPercentage(double percentage, ProgressBar batteryPercentage){
        batteryPercentage.setProgress(percentage/100);
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
                                temperatureText.setText("Heiß");
                                temperatureText.setFill(Color.RED);
                            }
                        }else{
                            temperatureText.setText("Kühl");
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

    public void checkTouch(Session session){
        try {
            memory = new ALMemory(session);
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
                    }
                }
            });
            memory.subscribeToEvent("RearTactilTouched", new EventCallback<Float>() {
                @Override
                public void onEvent(Float val) throws InterruptedException, CallError {
                    float touchState = val;
                    if(touchState == 1.0){
                        System.out.println("Rear head bumper has been touched");
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
        memory = null;
        timerKiller = true;

    }
}
