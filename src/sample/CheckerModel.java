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
    private boolean end = false; // to unsubscribe all events set to true
    private LogModel log = new LogModel();



    /**
     * sets the progressbar to a percentage
     * @param percentage
     * @param batteryPercentage
     */
    public void setBatteryPercentage(double percentage, ProgressBar batteryPercentage){
        batteryPercentage.setProgress(percentage/100);
    }

    /**
     * task which gets the battery charge of the nao.
     * @param session
     * @param batteryCicle
     * @param batteryPercentage
     * @param batteryPercentText
     */

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

    /**
     * task which checks the temperature of the nao in its body parts
     * doesnt work on naos (returns weird values)
     * @param session
     * @param temperatureText
     * @param rightArmTempText
     * @param leftArmTempText
     * @param rightLegTempText
     * @param leftLegTempText
     * @param headTempText
     */

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
                                    ArrayList tempe = (ArrayList) tempEvent.get(i);
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

    /**
     * subscribes to touchevents for the sensor on the head
     * the both back sensors allow saying a text
     * the front sensor makes nao dab
     * @param session
     * @param midButtonText
     * @param rearButtonText
     * @param volumeSlider
     * @param voiceSlider
     * @param voiceSpeedSlider
     * @param dropDownLanguages
     */
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


    /**
     * gets System info and prints it. system info is not available on virtual robot from choregraphe
     * @param session
     * @param systemText
     */

    public void systemInfo(Session session, Text systemText){
        try {
            alSystem = new ALSystem(session);
            String text = alSystem.systemInfo().toString();
            System.out.println(text);
            systemText.setText(text);
        } catch (Exception e) {
            log.write("Cannot create new Object alSystem. The referred Object might not exist. WARN");
            log.write("This is e.g. the case for the virtual robot. INFO");
        }
    }

    /**
     * kills timers and unsubscribes to all events
     * sets the progressbar to transparent and the temp text to -
     * @param batteryPercentage
     * @param temperatureText
     */
    public void killCheckers(ProgressBar batteryPercentage, Text temperatureText) {
        end = true;
        timerKiller = true;
        batteryPercentage.setStyle("-fx-background-color: transparent");
        temperatureText.setText("-");

    }
}
