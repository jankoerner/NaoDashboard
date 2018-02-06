package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALBattery;
import com.aldebaran.qi.helper.proxies.ALBodyTemperature;
import com.aldebaran.qi.helper.proxies.ALMemory;
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

    public static void main(String[] args) {

    }




    public void setBatteryPercentage(double percentage, ProgressBar batteryPercentage){
        batteryPercentage.setProgress(percentage/100);
    }

    public void checkBatteryCharge(Session session, Circle batteryCicle, ProgressBar batteryPercentage){
        try{
            memory = new ALMemory(session);
            ALBattery alBattery = new ALBattery(session);

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

    public void checkTouch(Session session ){
        try {
            memory = new ALMemory(session);
            memory.subscribeToEvent("FrontTactilTouched", new EventCallback<Float>() {
                @Override
                public void onEvent(Float val) throws InterruptedException, CallError {
                    float touchState = val;
                    if (touchState == 1.0) {
                        System.out.println("Front head bumper has been touched.");
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

    public void killCheckers() {
        timerKiller = true;

    }

}
