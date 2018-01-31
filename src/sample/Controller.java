package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    @FXML ToggleGroup mode;
    @FXML Slider velocitySlider, volumeSlider, voiceSlider, voiceSpeedSlider, angleSlider;
    @FXML TextArea textToSpeech;
    @FXML Button w,a,s,d, connectButton, disconnectButton, sayButton, poseButton, btn_play;
    @FXML Circle connectCircle, batteryCircle;
    @FXML ComboBox dropDownPostures, dropDownLanguages, cb_LEDS, colorBox, cb_IP;
    @FXML TextField tx_IP, tx_Port, degreeField;
    @FXML ImageView imageView, photoView;
    @FXML Text temperatureText;
    @FXML ListView lv_Sounds;
    @FXML ProgressBar batteryPercentage;


    private BufferedWriter writer;
    private FileInputStream file;
    private BufferedReader reader;
    private Session session;
    private LEDModel ledModel;
    private ConnectionModel connectionModel;
    private TextToSpeechModel textToSpeechModel;
    private AudioModel audioModel;
    private PosturesModel posturesModel;
    private MoveBodyModel moveBodyModel;
    private CameraModel cameraModel;
    private ALConnectionManager alConnectionManager;
    private ALMemory memory;
    private static String[] IP = new String[5];
    private static String[] Port = new String[5];

    private String[] getPort(){
        return Port;
    }
    private String[] getIP(){
        return IP;
    }

    public Session getSession() {
        return session;
    }

    public static void main(String[] args) {

    }

    public void initialize()throws Exception {
        read();
        //Main.logger.info("Dies ist ein Test");
        //setLogger();
    }

    //private void setLogger(Stage primaryStage) throws Exception {
    //    Log log = new Log();
    //    Logger logger = new Logger(log, "");
    //    LogViewer logViewer = new LogViewer();
    //    logViewer.start(primaryStage);
    //}


    private void write(String[] ip,String[] port) throws IOException {
        try {
            writer=new BufferedWriter(new FileWriter(new File("connectionlog.txt")));
            if(tx_IP.getText()!=IP[0]){
                IP[4]=IP[3];
                IP[3]=IP[2];
                IP[2]=IP[1];
                IP[1]=IP[0];
                IP[0]=tx_IP.getText();
            }
            if(tx_Port.getText()!=Port[0]){
                Port[4]=Port[3];
                Port[3]=Port[2];
                Port[2]=Port[1];
                Port[1]=Port[0];
                Port[0]=tx_Port.getText();
            }
            for(Integer I =0; I<ip.length; I++) {
                writer.write(ip[I]);
                writer.newLine();
                writer.write(port[I]);
                writer.newLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            writer.close();
        }

    }

    private void read() {
        try {
            try {
                file = new FileInputStream("connectionlog.txt");
                reader = new BufferedReader(new InputStreamReader(file));
                for(Integer I=0; I<IP.length; I++){
                    Port[I] = reader.readLine();
                    IP[I] = reader.readLine();
                }
                cb_IP.setItems(FXCollections.observableArrayList(IP));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            try {
                file.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            tx_IP.setText(IP[0]);
            tx_Port.setText(Port[0]);
            disconnectButton.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setTextFields(ActionEvent actionEvent){
       if((actionEvent.getSource().equals(cb_IP))){
            Integer Selected = cb_IP.getSelectionModel().getSelectedIndex();
            tx_IP.setText(cb_IP.getSelectionModel().getSelectedItem().toString());
            //cb_IP.getSelectionModel().clearSelection(); //wäre nice aber löst actionevent neu aus und führt zu fehler -> informieren wie man dies unterbinden kann
            tx_Port.setText(Port[Selected]);
        }
    }


    public void btn_ConnectIsPressed() throws Exception {
        if (connectionModel == null){
            connectionModel = new ConnectionModel();
        }

        if(tx_Port.getText() != null && tx_IP.getText() != null){
            if (connectionModel.connect(tx_IP.getText(), Integer.parseInt(tx_Port.getText())))
            {
                if (session == null){
                    session = new Session(connectionModel.getNaoUrl());                    ///TODO DISCONNECT & RECONNECT TESTEN!
                }
                if (!session.isConnected()){
                    session.connect(connectionModel.getNaoUrl()).get();
                }
                if (session.isConnected()) onConnected();

            }
        }

    }

    public void turn()throws Exception{
       if (session.isConnected()){
           if (moveBodyModel == null){
               moveBodyModel = new MoveBodyModel();
           }
           if(degreeField.getText() != null){
               String degreeString = degreeField.getText();

               if (isNumber(degreeString)){
                   Float degree = Float.parseFloat(degreeString);
                   moveBodyModel.turn(session,degree/(45f));
               }

           }
        }

    }

    public void playSounds(){
        String filename = lv_Sounds.getSelectionModel().getSelectedItem().toString();
        audioModel.playSound(filename/*, (float) volumeSlider.getValue()*/);
    }
    public void moveHeadButtons(MouseEvent mouseEvent) throws Exception{
        if (moveBodyModel == null){
            moveBodyModel = new MoveBodyModel();
        }
        if (session.isConnected()){
            if (mouseEvent.getEventType().equals( MouseEvent.MOUSE_PRESSED))
            {   Button button = (Button) mouseEvent.getSource();
                moveBodyModel.moveHeadButtons(session,button.getText());

            }
        }
    }

    public void moveBodyButtons(MouseEvent mouseEvent) throws Exception{
        if (moveBodyModel == null){
            moveBodyModel = new MoveBodyModel();
        }
        float velocity = (float) velocitySlider.getValue();
        float angle = (float) angleSlider.getValue();
        float angleRound = round(angle, 5);
        Button button = (Button) mouseEvent.getSource();
        if (session.isConnected()){
            if (mouseEvent.getEventType().equals( MouseEvent.MOUSE_PRESSED))
            {

                moveBodyModel.moveKeyboard(session,button.getText(),velocity,(float)((angleRound)*(Math.PI/180)));
            }else if (mouseEvent.getEventType().equals( MouseEvent.MOUSE_RELEASED))
            {
                moveBodyModel.moveKeyboard(session,"stop",0f,0f);
                angleSlider.valueProperty().set(0);
                if (posturesModel == null) {
                    posturesModel = new PosturesModel();
                }
                posturesModel.makePosture(session, "Stand");
            }
        }
    }

    public void moveBody(KeyEvent keyEvent) throws Exception{
        if (moveBodyModel == null){
            moveBodyModel = new MoveBodyModel();
        }
        if (session!=null && session.isConnected()){
            if (keyEvent.getText().equals("w")|| keyEvent.getText().equals("a") || keyEvent.getText().equals("s")
                    || keyEvent.getText().equals("d")){

                float velocity = (float) velocitySlider.getValue();
                float angle = (float) angleSlider.getValue();
                float angleRound = round(angle, 5);
                if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                    moveBodyModel.moveKeyboard(session, keyEvent.getText(), velocity,(float)((angleRound)*(Math.PI/180)));
                } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                    moveBodyModel.moveKeyboard(session, "stop", velocity,angleRound);
                    angleSlider.valueProperty().set(0);
                    if (posturesModel == null) {
                        posturesModel = new PosturesModel();
                    }
                    posturesModel.makePosture(session, "Stand");

                }

            }else if (keyEvent.getText().equals("i")|| keyEvent.getText().equals("j") || keyEvent.getText().equals("k")
                    || keyEvent.getText().equals("l") || keyEvent.getText().equals("m")){
                if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                    moveBodyModel.moveKeyboard(session, keyEvent.getText());
                }else if(keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)){
                    moveBodyModel.moveKeyboard(session, "stop");
                }

            }
        }
    }

    public void say()throws Exception{
       if (session.isConnected()){
           if (textToSpeechModel == null)
           {
               textToSpeechModel = new TextToSpeechModel();
           }
           if (textToSpeech.getText() != null){
               Float volume = (float) volumeSlider.getValue();
               String language =(String) dropDownLanguages.getValue();
               String voice = String.valueOf((int)voiceSlider.getValue());
               String speed = String.valueOf((int)voiceSpeedSlider.getValue());
               textToSpeechModel.say(session, textToSpeech.getText(),volume, language, voice, speed);
           }
       }

    }

    public void disconnect(){
        session.close();
        connectCircle.setFill(Color.rgb(240,20,20));
        dropDownPostures.getItems().removeAll(dropDownPostures.getItems());
        dropDownLanguages.getItems().removeAll(dropDownLanguages.getItems());
        cb_LEDS.getItems().removeAll(cb_LEDS.getItems());
        colorBox.getItems().removeAll(colorBox.getItems());
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
    }


    public void postures(ActionEvent actionEvent) throws Exception{
        if (posturesModel == null){
            posturesModel = new PosturesModel();
        }
        if (dropDownPostures.getSelectionModel().getSelectedItem() != null){
            String actualPose = (String) dropDownPostures.getValue();
            if (actionEvent.getSource().getClass()!= Button.class ){
                posturesModel.changeImage(actualPose, imageView);
            }else{
                posturesModel.makePosture(session,actualPose);
            }
        }
    }

    public void mode() throws Exception{
        if (session.isConnected()){
            if(moveBodyModel == null){
                moveBodyModel = new MoveBodyModel();
            }
            ToggleButton object;
            if(mode.getSelectedToggle().getClass() == ToggleButton.class){
               object =(ToggleButton) mode.getSelectedToggle();
               moveBodyModel.mode(session, object.getText());
            }
        }

    }

    public void takePhoto()throws Exception{
        if (session.isConnected()){
            if (cameraModel == null){
                cameraModel = new CameraModel();
            }
            cameraModel.takePhoto(imageView, session);
        }
    }

    public void changeColor()throws Exception{
        ledModel = new LEDModel();
        if(colorBox.getValue() != null){
            ledModel.changeColor(session, cb_LEDS.getValue().toString(),colorBox.getValue().toString().toLowerCase());
        }

    }

    public void changeChoice(){
        String selcetedGroup = cb_LEDS.getValue().toString();
        if (selcetedGroup != null){
            if (selcetedGroup.equals("BrainLEDs") || selcetedGroup.equals("EarLEDs") || selcetedGroup.equals("Left Ear LEDs") || selcetedGroup.equals("Right Ear LEDs")){
                Object[] colorArray = {"On","Off"};
                ObservableList colorList = FXCollections.observableArrayList(Arrays.asList(colorArray));
                colorBox.setValue("");
                colorBox.setItems(colorList);

            }else {
                Object[] colorArray = {"White","Red", "Green", "Blue", "Yellow","Magenta", "Cyan"  };
                ObservableList colorList = FXCollections.observableArrayList(Arrays.asList(colorArray));
                colorBox.setValue("");
                colorBox.setItems(colorList);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void onConnected() throws Exception{
        this.write(getPort(),getIP());
        connectCircle.setFill(Color.rgb(60,230,30));
        connectButton.setDisable(true);
        disconnectButton.setDisable(false);
        ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(session);
        alAnimatedSpeech.say("You are connected");

        if(audioModel==null){
            audioModel = new AudioModel();
        }

        if (posturesModel == null){
            posturesModel = new PosturesModel();
        }
        if (ledModel == null){
            ledModel = new LEDModel();
        }
        List SoundFiles=null;
        if(audioModel.getSoundFiles(session)!=null){
            SoundFiles = audioModel.getSoundFiles(session);
        }
        if (SoundFiles!=null){
            lv_Sounds.setItems(FXCollections.observableList(SoundFiles));
            lv_Sounds.setDisable(false);
            lv_Sounds.setVisible(true);
            btn_play.setVisible(true);
            btn_play.setDisable(false);
        }else{
            btn_play.setDisable(true);
            btn_play.setVisible(false);
            lv_Sounds.setDisable(true);
            lv_Sounds.setVisible(false);
        }

        List ledList1 = ledModel.getLEDs(session);
        ObservableList ledList = FXCollections.observableList(ledList1);
        if (!ledList.isEmpty()){
            cb_LEDS.setItems(ledList);
            cb_LEDS.setDisable(false);
        }else
            {
            cb_LEDS.setDisable(true);
        }

        List postureList1 = posturesModel.getPostures(session);
        ObservableList postureList = FXCollections.observableArrayList(postureList1);
        if (!postureList.isEmpty()){
            dropDownPostures.setItems(postureList);
            poseButton.setDisable(false);
        }else
        {
            dropDownPostures.setDisable(true);
            poseButton.setDisable(true);

        }

        if(textToSpeechModel == null) {
            textToSpeechModel = new TextToSpeechModel();
        }
        List languagesList1 = textToSpeechModel.getLanguages(session);
        ObservableList languagesList = FXCollections.observableArrayList(languagesList1);
        if (!languagesList.isEmpty()){
            dropDownLanguages.setItems(languagesList);
            dropDownLanguages.setValue(languagesList.get(0));
            sayButton.setDisable(false);
        }
        else {
            dropDownLanguages.setDisable(true);
            sayButton.setDisable(true);
        }

        if (moveBodyModel == null){
            moveBodyModel = new MoveBodyModel();
        }
        boolean isWakeUp = moveBodyModel.getMode(session);
        List list = mode.getToggles();
        Toggle toggle;
        if (isWakeUp){
            moveBodyModel.mode(session,"Stand");
            toggle = (Toggle) list.get(0);
            mode.selectToggle(toggle);
        }else{
            moveBodyModel.mode(session,"Relax");
            toggle = (Toggle) list.get(1);
            mode.selectToggle(toggle);
        }

        if (ledModel == null){
            ledModel = new LEDModel();
        }
        ObservableList ledGroups = FXCollections.observableArrayList(ledModel.getLEDs(session));
        cb_LEDS.setItems(ledGroups);
        Object[] colorArray = {"White","Red", "Green", "Blue", "Yellow","Magenta", "Cyan"  };
        ObservableList colorList = FXCollections.observableArrayList(Arrays.asList(colorArray));
        colorBox.setItems(colorList);
        memory = new ALMemory(session);
        batteryCharge();
        checkTemperature();
        checkTouch(memory);
    }

    private boolean isNumber(String number){
        float d;
        try
        {
             d = Float.parseFloat(number);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return d >= (-180) && d <= 180;
    }

    private float round(double i, int v){
        return (float) (Math.round(i/v) * v);
    }

    private void batteryCharge(){
            Timer batteryTimer = new Timer();
            TimerTask checkBattery = new TimerTask() {
                @Override
                public void run(){
                    try {
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
                            setBatteryPercentage(alBattery.getBatteryCharge());
                        }


                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            batteryTimer.scheduleAtFixedRate(checkBattery, 1000, 6000);

    }

    private void setBatteryPercentage(double percentage){
        batteryPercentage.setProgress(percentage/100);
    }

    private void checkTemperature(){
            Timer temperatureTimer = new Timer();
            TimerTask checkTemp = new TimerTask() {
                @Override
                public void run() {
                    try{
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

    public void test() throws Exception{
        if (moveBodyModel == null){
            moveBodyModel = new MoveBodyModel();
        }
        moveBodyModel.dab(session);
    }

    public void checkTouch(ALMemory memory){
        try {
           memory.subscribeToEvent("FrontTactilTouched", new EventCallback<Float>() {
                @Override
                public void onEvent(Float val) throws InterruptedException, CallError {
                    float touchState = val;
                    if (touchState == 1.0) {
                           try {
                               test();
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
}





