package sample;


import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;

import com.aldebaran.qi.helper.proxies.*;
import javafx.application.Platform;
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
import javafx.scene.text.TextFlow;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Arrays;
import java.util.List;


public class Controller {
    @FXML ToggleGroup mode, landmarkTracker, landmarkMode;
    @FXML Tab tb_NAO;
    @FXML Slider velocitySlider, volumeSlider, voiceSlider, voiceSpeedSlider, angleSlider;
    @FXML TextArea textToSpeech, midButtonText, rearButtonText;
    @FXML Button w,a,s,d, connectButton, disconnectButton, sayButton, poseButton, btn_play, searchMarkButton;
    @FXML Circle connectCircle, batteryCircle;
    @FXML ComboBox dropDownPostures, dropDownLanguages, cb_LEDS, colorBox, cb_IP;
    @FXML TextField tx_IP, tx_Port, degreeField;
    @FXML ImageView imageView, photoView;
    @FXML Text temperatureText, rightArmTempText, leftArmTempText, rightLegTempText, leftLegTempText, headTempText;
    @FXML ListView lv_Sounds, lv_log;
    @FXML ProgressBar batteryPercentage;
    @FXML RadioButton headRadio, bodyRadio, moveRadio;

    private static  Integer ListIndex=0;
    private final static SimpleDateFormat timestampFormatter = new SimpleDateFormat("HH:mm:ss");
    private BufferedWriter writer;
    private FileInputStream file;
    private BufferedReader reader;
    private static Session session;
    private LEDModel ledModel;
    private ConnectionModel connectionModel;
    private TextToSpeechModel textToSpeechModel;
    private AudioModel audioModel;
    private PosturesModel posturesModel;
    private MoveBodyModel moveBodyModel;
    private CameraModel cameraModel;
    private TrackerModel trackerModel;
    private CheckerModel checkerModel = new CheckerModel();
    private static String[] IP = new String[5];
    private static String[] Port = new String[IP.length];
    private static String[] URL = new String[Port.length];



    private String[] getPort(){
        return Port;
    }
    private String[] getIP(){
        return IP;
    }

    public static Session getSession() {
        return session;
    }

    public static void main(String[] args) {

    }

    public void initialize()throws Exception {
        read();
        lv_log.setFixedCellSize(20);
        WelcomeMessage();
        UpdateItems(false, true);
    }

    private void WelcomeMessage() {
        Log("Welcome to the \"Nao Dashboard\", a dashboard which lets you control NAOs. INFO");
        Log("To start, please enter the IP address and the port of the NAO you wish to connect to. INFO");
        Log("The dashboard was created and designed by Jan Körner, Jonathan Schindler and Valentin Lechner. INFO");
    }

    private synchronized void addTimestampandColor(String message, String context) {
        Platform.runLater(() -> {
            Date timestamp = new Date();
            String time = timestampFormatter.format(timestamp);
            String log = "\r"+"\n"+ time + ">> "+message+" <<"+"\r"+"\n";
            Text text = new Text(log);
            if(context.equals("INFO")){
                text.setStyle("-fx-fill:green; -fx-font-weight:bold");
            }
            if(context.equals("WARN")){
                text.setStyle("-fx-fill:red; -fx-font-weight: bolder");
            }
            if(context.equals("ACTION")){
                text.setStyle("-fx-fill: black; -fx-font-weight: 500");
            }
            lv_log.getItems().add(text);
            ListIndex++;
            lv_log.scrollTo(ListIndex+4);
        });
    }

    private synchronized void FilterLogLevel(String message){
            if(message.contains("INFO")){
                message = message.replaceAll(" INFO","");
                addTimestampandColor(message,"INFO");
            }
            if(message.contains("WARN")){
                message=message.replaceAll(" WARN","");
                addTimestampandColor(message,"WARN");
            }
            if(message.contains("ACTION")){
                message=message.replaceAll("ACTION","");
                addTimestampandColor(message,"ACTION");
            }
    }

    public void Log(String message){
        FilterLogLevel(message);
    }


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
                    URL[I]=IP[I]+":"+Port[I];
                }
                cb_IP.setItems(FXCollections.observableArrayList(URL));
            } catch (FileNotFoundException e) {
                Log("The text document connectionlog could not be found on your Computer. WARN");
                Log("Please check if the file exists in the correct directory. INFO");
            } catch (IOException e){
                Integer actualEntries=0;
                for (Integer I=0; I<IP.length; I++){
                    if(IP[I].equals("")) {
                         actualEntries = I;
                    }
                }
                Log("Your connection log might be incomplete. It needs to have "+IP.length+" entries, but it has "+actualEntries+". WARN");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setTextFields(ActionEvent actionEvent){
       if((actionEvent.getSource().equals(cb_IP))){
            Integer Selected = cb_IP.getSelectionModel().getSelectedIndex();
            tx_IP.setText(IP[Selected]);
            tx_Port.setText(Port[Selected]);
        }
    }


    public void btn_ConnectIsPressed() throws Exception {
        if (connectionModel == null){
            connectionModel = new ConnectionModel();
        }

        if(tx_Port.getText() != null && tx_IP.getText() != null){
            Log("Connecting to tcp://"+tx_IP.getText()+":"+tx_Port.getText()+"... INFO");
            if (connectionModel.connect(tx_IP.getText(), Integer.parseInt(tx_Port.getText())))
            {
                Log("IP valid. INFO");
                if (session == null){
                    session = new Session(connectionModel.getNaoUrl());
                }
                if (!session.isConnected()){
                    session.connect(connectionModel.getNaoUrl()).get();
                }
                if (session.isConnected()){
                    Log("Connected. INFO");
                    onConnected();
                }

            }else
                Log("Connection failed. Check your entered IP&Port and your Network Connection. WARN");
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
                   Log("Nao turns by "+degree+" degrees. ACTION");
                   moveBodyModel.turn(session,degree/(45f));
               }

           }
        }

    }

    public void playSounds(){
        if(lv_Sounds.getSelectionModel().getSelectedItem()!=null) {
            String filename = lv_Sounds.getSelectionModel().getSelectedItem().toString();
            Log("Nao will now play the sound " + filename + ". ACTION");
            audioModel.playSound(filename/*, (float) volumeSlider.getValue()*/);
        } else Log("Please make sure to select a file to play. INFO ");
    }
    public void moveHeadButtons(MouseEvent mouseEvent) throws Exception{
        if (moveBodyModel == null){
            moveBodyModel = new MoveBodyModel();
        }
        if (session.isConnected()){
            if (mouseEvent.getEventType().equals( MouseEvent.MOUSE_PRESSED))
            {   Button button = (Button) mouseEvent.getSource();
                moveBodyModel.moveHeadButtons(session,button.getText());
                Log("Nao moves his head. ACTION");
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
                    if(angleRound!=0){
                        Log("Nao moves and turns. Speed: "+velocity+", angle: "+angleRound);
                    } else Log("Nao moves. Speed: "+velocity);
                } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                    moveBodyModel.moveKeyboard(session, "stop", velocity,angleRound);
                    Log("Nao stopped moving. ACTION");
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
                    Log("Nao moves his head. ACTION");
                }else if(keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)){
                    moveBodyModel.moveKeyboard(session, "stop");
                    Log("Nao stopped moving his head. ACTION");
                }

            }
        }
    }

    public void say(){
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
               try {
                   textToSpeechModel.say(session, textToSpeech.getText(),volume, language, voice, speed);
               } catch (Exception e) {
                   Log("Nao could not say this text. INFO");
                   e.printStackTrace();
               }
           }
       }
    }

    public void disconnect(){
        session.close();
        Log("Disconnected from Nao "+connectionModel.getNaoUrl()+". INFO");
        UpdateItems(true, false);
        checkerModel.killCheckers(batteryPercentage, temperatureText);

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
        UpdateItems(false, false);
        ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(session);
        alAnimatedSpeech.say("You are connected");
        checkerModel.checkBatteryCharge(session, batteryCircle, batteryPercentage);
        checkerModel.checkTemperature(session, temperatureText, rightArmTempText, leftArmTempText, rightLegTempText,
                leftLegTempText, headTempText);
        checkerModel.checkTouch(session, midButtonText, rearButtonText, volumeSlider, voiceSlider, voiceSpeedSlider,dropDownLanguages);

    }

    @SuppressWarnings("unchecked")
    private void UpdateItems(Boolean ClearBoxes, Boolean Startup) {
          if(session!=null) {
              tb_NAO.setDisable(!session.isConnected());
              connectButton.setDisable(session.isConnected());
              disconnectButton.setDisable(!session.isConnected());
              if (session.isConnected()) {
                  connectCircle.setFill(Color.rgb(60, 230, 30));
              } else connectCircle.setFill(Color.rgb(240, 20, 20));
              if (ClearBoxes) {
                  dropDownPostures.getItems().removeAll(dropDownPostures.getItems());
                  dropDownLanguages.getItems().removeAll(dropDownLanguages.getItems());
                  cb_LEDS.getItems().removeAll(cb_LEDS.getItems());
                  colorBox.getItems().removeAll(colorBox.getItems());
              } //else
                  //getBoxes();
          } else if(Startup){
              tb_NAO.setDisable(true);
              connectButton.setDisable(false);
              disconnectButton.setDisable(true);
          }
    }

    public void naoTab(){
        getBoxes();
    }

    @SuppressWarnings("unchecked")
    private void getBoxes() {
        try {
            if (audioModel == null) {
                audioModel = new AudioModel();
            }

            if (posturesModel == null) {
                posturesModel = new PosturesModel();
            }
            if (ledModel == null) {
                ledModel = new LEDModel();
            }
            List SoundFiles = null;
            if (audioModel.getSoundFiles(session) != null) {
                SoundFiles = audioModel.getSoundFiles(session);
            }
            if (SoundFiles != null) {
                lv_Sounds.setItems(FXCollections.observableList(SoundFiles));
                lv_Sounds.setDisable(false);
                lv_Sounds.setVisible(true);
                btn_play.setVisible(true);
                btn_play.setDisable(false);
            } else {
                btn_play.setDisable(true);
                btn_play.setVisible(false);
                lv_Sounds.setDisable(true);
                lv_Sounds.setVisible(false);
            }

            List ledList1 = ledModel.getLEDs(session);
            ObservableList ledList = FXCollections.observableList(ledList1);
            if (!ledList.isEmpty()) {
                cb_LEDS.setItems(ledList);
                cb_LEDS.setDisable(false);
            } else {
                cb_LEDS.setDisable(true);
            }

            List postureList1 = posturesModel.getPostures(session);
            ObservableList postureList = FXCollections.observableArrayList(postureList1);
            if (!postureList.isEmpty()) {
                dropDownPostures.setItems(postureList);
                poseButton.setDisable(false);
            } else {
                dropDownPostures.setDisable(true);
                poseButton.setDisable(true);

            }

            if (textToSpeechModel == null) {
                textToSpeechModel = new TextToSpeechModel();
            }
            List languagesList1 = textToSpeechModel.getLanguages(session);
            ObservableList languagesList = FXCollections.observableArrayList(languagesList1);
            if (!languagesList.isEmpty()) {
                dropDownLanguages.setItems(languagesList);
                dropDownLanguages.setValue(languagesList.get(0));
                sayButton.setDisable(false);
            } else {
                dropDownLanguages.setDisable(true);
                sayButton.setDisable(true);
            }

            if (moveBodyModel == null) {
                moveBodyModel = new MoveBodyModel();
            }
            boolean isWakeUp = moveBodyModel.getMode(session);
            List list = mode.getToggles();
            Toggle toggle;
            if (isWakeUp) {
                moveBodyModel.mode(session, "Stand");
                toggle = (Toggle) list.get(0);
                mode.selectToggle(toggle);
            } else {
                moveBodyModel.mode(session, "Relax");
                toggle = (Toggle) list.get(1);
                mode.selectToggle(toggle);
            }

            if (ledModel == null) {
                ledModel = new LEDModel();
            }
            ObservableList ledGroups = FXCollections.observableArrayList(ledModel.getLEDs(session));
            cb_LEDS.setItems(ledGroups);
            Object[] colorArray = {"White", "Red", "Green", "Blue", "Yellow", "Magenta", "Cyan"};
            ObservableList colorList = FXCollections.observableArrayList(Arrays.asList(colorArray));
            colorBox.setItems(colorList);
            setTrackButtons(true);
        }catch (Exception e){
            Log("An error has occured while setting the boxes. WARN");
            e.printStackTrace();
        }
    }


    private boolean isNumber(String number){
        float d;
        try
        {
             d = Float.parseFloat(number);
        }
        catch(NumberFormatException nfe)
        {
            Log("Please make sure to enter numbers. INFO");
            return false;
        }
        return d >= (-180) && d <= 180;
    }

    private float round(double i, int v){
        return (float) (Math.round(i/v) * v);
    }


    public void setLandmarkTracker() throws Exception {
        ToggleButton toggle =(ToggleButton) landmarkTracker.getSelectedToggle();
        if (toggle.getText().equals("Enabled")){
            checkerModel.setLandmarkTrackerActive(true);
            checkerModel.enableLandmarkTracker(session);
            checkerModel.LandmarkTracker(session, landmarkMode.getSelectedToggle().selectedProperty().getName());
            setTrackButtons(false);

        }else {
            checkerModel.setLandmarkTrackerActive(false);
            if (trackerModel == null)
            {
                trackerModel = new TrackerModel();
            }
            trackerModel.stopTraker();
            setTrackButtons(true);
        }
    }
    private void setTrackButtons(Boolean enabled){
            searchMarkButton.setDisable(enabled);
            bodyRadio.setDisable(enabled);
            headRadio.setDisable(enabled);
            moveRadio.setDisable(enabled);

    }
    public void searchLandmarks() throws Exception {
        if (trackerModel == null){
            trackerModel = new TrackerModel();
        }
        trackerModel.searchLandmark(session);
    }
    public void changeTrackingMode() throws Exception {
        if (trackerModel == null){
            trackerModel = new TrackerModel();
        }
        System.out.println(landmarkMode.selectedToggleProperty());
        if (landmarkMode.getSelectedToggle().getClass().equals(RadioButton.class))
        {
            RadioButton button = (RadioButton)landmarkMode.getSelectedToggle();
            System.out.println(button.getText());
        }
        //trackerModel.setMode(landmarkMode.getSelectedToggle().getUserData().toString());
    }
}





