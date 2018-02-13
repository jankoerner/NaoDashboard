package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;
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
    private ALSystem alSystem;
    private TextToSpeechModel textToSpeechModel;
    private boolean timerKiller = false;
    private boolean end = false;
    private boolean RedballTrackerActive = false;
    private TrackerModel trackerModel = new TrackerModel();
    private ALTracker alTracker;
    public void setRedballTrackerActive(boolean isActive){
        RedballTrackerActive = isActive;
    }
    private String landmarkID;
    boolean tracked = false;


    public static void main(String[] args) {

    }
    public void setTracked(boolean tracked2){
        tracked = tracked2;
    }
    public void setBatteryPercentage(double percentage, ProgressBar batteryPercentage){
        batteryPercentage.setProgress(percentage/100);
    }

    public void checkBatteryCharge(Session session, Circle batteryCicle, ProgressBar batteryPercentage, Text batteryPercentText){
        try{
            memory = new ALMemory(session);
            ALBattery alBattery = new ALBattery(session);


            if(end){
                memory.unsubscribeAllEvents();

            }
            if(alBattery.getBatteryCharge() == 0){
                batteryCicle.setFill(Color.BLACK);
                System.out.println("No battery detected.");
                batteryPercentage.setStyle("-fx-background-color: black");
                batteryPercentText.setText("No battery detected");
            }
            memory.subscribeToEvent("BatteryChargeChanged", new EventCallback<Integer>() {
                @Override
                public void onEvent(Integer percentage) throws InterruptedException, CallError {
                    int charge = percentage;
                    if (charge > 75){
                        batteryCicle.setFill(Color.GREEN);
                        System.out.println("Battery remaining " + charge);
                        batteryPercentage.setStyle("-fx-background-color: green");
                    }else if (charge < 75 & charge > 30){
                        batteryCicle.setFill(Color.ORANGE);
                        System.out.println("Battery remaining " + charge);
                        batteryPercentage.setStyle("-fx-background-color: orange");
                    }else if (charge < 30 & charge != 0 ){
                        batteryCicle.setFill(Color.RED);
                        System.out.println("Battery remaining " + charge);
                        batteryPercentage.setStyle("-fx-background-color: red");
                    }
                    setBatteryPercentage(alBattery.getBatteryCharge(), batteryPercentage);
                    batteryPercentText.setText(alBattery.getBatteryCharge().toString());
                }
            });


        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public void checkTemperature(Session session, Text temperatureText, Text rightArmTempText, Text leftArmTempText,
                                 Text rightLegTempText, Text leftLegTempText, Text headTempText){
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
                            for (int i = 0; i < tempEvent.size() ; i++) {
                                if(tempEvent.get(i) instanceof ArrayList){
                                    ArrayList tempe = (ArrayList) tempEvent;
                                    if(tempe.get(0).equals(1) & tempe.get(1).equals("LArm")){
                                        leftArmTempText.setText("Warm");
                                        leftArmTempText.setFill(Color.ORANGE);
                                    }else if(tempe.get(0).equals(2) & tempe.get(1).equals("LArm")){
                                        leftArmTempText.setText("Hot");
                                        leftArmTempText.setFill(Color.RED);
                                    }else if(tempe.get(0).equals(1) & tempe.get(1).equals("RArm")){
                                        rightArmTempText.setText("Warm");
                                        rightArmTempText.setFill(Color.ORANGE);
                                    }else if(tempe.get(0).equals(2) & tempe.get(1).equals("RArm")) {
                                        rightArmTempText.setText("Hot");
                                        rightArmTempText.setFill(Color.RED);
                                    }else if(tempe.get(0).equals(1) & tempe.get(1).equals("LLeg")) {
                                        leftLegTempText.setText("Warm");
                                        leftLegTempText.setFill(Color.ORANGE);
                                    }else if(tempe.get(0).equals(2) & tempe.get(1).equals("LLeg")) {
                                        leftLegTempText.setText("Hot");
                                        leftLegTempText.setFill(Color.RED);
                                    }else if (tempe.get(0).equals(1) & tempe.get(1).equals("RLeg")){
                                        rightLegTempText.setText("Warm");
                                        rightLegTempText.setFill(Color.ORANGE);
                                    }else if(tempe.get(0).equals(2) & tempe.get(1).equals("LLeg")) {
                                        rightLegTempText.setText("Hot");
                                        rightLegTempText.setFill(Color.RED);
                                    }else if (tempe.get(0).equals(1) & tempe.get(1).equals("Head")){
                                        headTempText.setText("Warm");
                                        headTempText.setFill(Color.ORANGE);
                                    }else if(tempe.get(0).equals(2) & tempe.get(1).equals("Head")) {
                                        headTempText.setText("Hot");
                                        headTempText.setFill(Color.RED);
                                    }
                                }

                            }
                            if(tempEvent.get(0).equals(1)){
                                temperatureText.setText("Warm");
                                temperatureText.setFill(Color.ORANGE);
                            }else if(tempEvent.get(0).equals(2)){
                                temperatureText.setText("Hot");
                                temperatureText.setFill(Color.RED);
                            }
                        }else{
                            temperatureText.setText("Cool");
                            temperatureText.setFill(Color.GREEN);
                            rightArmTempText.setText("Cool");
                            rightArmTempText.setFill(Color.GREEN);
                            leftArmTempText.setText("Cool");
                            leftArmTempText.setFill(Color.GREEN);
                            rightLegTempText.setText("Cool");
                            rightLegTempText.setFill(Color.GREEN);
                            leftLegTempText.setText("Cool");
                            leftLegTempText.setFill(Color.GREEN);
                            headTempText.setText("Cool");
                            headTempText.setFill(Color.GREEN);

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

    public void RedballTracker(Session session, String mode)throws Exception{
        if (RedballTrackerActive){
            TrackerModel trackerModel = new TrackerModel();
            memory.subscribeToEvent("redBallDetected", new EventCallback<ArrayList>() {
                @Override
                public void onEvent(ArrayList info ) throws InterruptedException, CallError {
                    try {
                        if (tracked==false){
                            trackerModel.trackRedball(session,info,alTracker);
                            tracked = true;
                            System.out.println("RedBall detected");
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public void enableRedballTracker(Session session)throws Exception{
        if (RedballTrackerActive){
            alTracker = new ALTracker(session);
            ALRedBallDetection redBallDetection = new ALRedBallDetection(session);
            redBallDetection.subscribe("redBallDetected");
        }

    }

    public void systemInfo(Session session, Text systemText)throws Exception{
        alSystem = new ALSystem(session);
        String text = alSystem.systemInfo().toString();
        System.out.println(text);
        systemText.setText(text);
    }
    public void killCheckers(ProgressBar batteryPercentage, Text temperatureText) {
        end = true;
        timerKiller = true;
        batteryPercentage.setStyle("-fx-background-color: transparent");
        temperatureText.setText("-");

    }
}
