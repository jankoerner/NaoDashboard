package sample;


import com.aldebaran.qi.Session;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class Controller {
    @FXML
    Label lb_abovecbIP, lb_play, lb_velocity, lb_ctrls, lb_turn, lb_turnS, lb_volume, lb_voicepitch, lb_voicespeed, lb_mvmnt, lb_headbuttons, lb_target, lb_tracking;
    @FXML
    AnchorPane anchor1, anchor2, anchor3, anchor4, anchor5, anchor6, anchor7;
    @FXML
    ToggleGroup mode, trackingMode, trackingTarget;
    @FXML
    ToggleButton tb_relax, tb_stand;
    @FXML
    Tab tb_NAO, tab_Connection, tab_Controls, tab_Audio, tab_Camera, tab_Status, tab_LEDPostures;
    @FXML
    Slider velocitySlider, volumeSlider, voiceSlider, voiceSpeedSlider, angleSlider;
    @FXML
    TextArea textToSpeech, midButtonText, rearButtonText;
    @FXML
    Button w,a,s,d,connectButton, disconnectButton, sayButton, poseButton, btn_play, startTrackingButton, stopTrackingButton, alarmButton, test;
    @FXML
    Circle connectCircle, batteryCircle;
    @FXML
    ComboBox dropDownPostures, dropDownLanguages, cb_LEDS, colorBox, cb_IP;
    @FXML
    TextField tx_IP, tx_Port, degreeField;
    @FXML
    ImageView imageView, photoView, iv_camera, iv_leds;
    @FXML
    Text temperatureText, rightArmTempText, leftArmTempText, rightLegTempText, leftLegTempText, headTempText;
    @FXML
    Text batteryPercentText, systemText;
    @FXML
    ListView lv_Sounds, lv_log;
    @FXML
    ProgressBar batteryPercentage;
    @FXML
    RadioButton headRadio, bodyRadio, moveRadio, faceRadio, redBallRadio;
    @FXML
    CheckBox ch_camera;


    VideoModel videoModel = new VideoModel();
    public static LogModel log = new LogModel();
    public static Session session;
    private LEDModel ledModel;
    private ConnectionModel connectionModel = new ConnectionModel();
    private TextToSpeechModel textToSpeechModel;
    private AudioModel audioModel;
    private PosturesModel posturesModel;
    private MoveBodyModel moveBodyModel;
    private TrackerModel trackerModel;
    private CheckerModel checkerModel = new CheckerModel();
    private Utils utils = new Utils();
    private MovementDetectionModel movementDetectionModel = new MovementDetectionModel();
    private static final int ANCHORCOUNT=7;
    public static Session getSession() {
        return session;
    }

    public static LogModel getLog() {
        return log;
    }

    /**
     * initializes classes that need access to the FXML and Updates the FXML items
     */
    public void initialize() {
        log.initializeLog(lv_log);
        connectionModel.initialize(cb_IP, tx_IP, tx_Port);
        UpdateItems(false, true);
    }

    /**
     * sets the textfields IP and Port on the connection tab on the fxml respective to the URL selected in the combobox
     *
     * @param actionEvent
     */

    @FXML
    private void setTextFields(ActionEvent actionEvent) {
        if ((actionEvent.getSource().equals(cb_IP))) {
            Integer Selected = cb_IP.getSelectionModel().getSelectedIndex();
            tx_IP.setText(connectionModel.getIP()[Selected]);
            tx_Port.setText(connectionModel.getPort()[Selected]);
        }
    }

    /**
     * checks the connection to the IP entered in the textfields and connects if the IP is valid
     *
     * @throws Exception
     */

    @FXML
    private void btn_ConnectIsPressed() throws Exception {
        if (connectionModel == null) {
            connectionModel = new ConnectionModel();
        }
        if (tx_Port.getText() != null && tx_IP.getText() != null) {
            log.write("Connecting to tcp://" + tx_IP.getText() + ":" + tx_Port.getText() + "... INFO");
            if (connectionModel.connect(tx_IP.getText(), Integer.parseInt(tx_Port.getText()))) {
                log.write("IP valid. INFO");
                if (session == null) {
                    session = new Session(connectionModel.getNaoUrl());
                }
                if (!session.isConnected()) {
                    session.connect(connectionModel.getNaoUrl()).get();
                }
                if (session.isConnected()) {
                    log.write("Connected. INFO");
                    onConnected();
                }

            } else
                log.write("Connection failed. Check your entered IP&Port and your Network Connection. WARN");
        }

    }

    /**
     * turns the NAO by a degree entered in a Textfield (valid entries: -180<=x<=180)
     */

    @FXML
    private void turn() {
        if (session.isConnected()) {
            if (moveBodyModel == null) {
                moveBodyModel = new MoveBodyModel();
            }
            if (degreeField.getText() != null) {
                String degreeString = degreeField.getText();

                if (utils.isNumber(degreeString)) {
                    Float degree = Float.parseFloat(degreeString);
                    log.write("Nao turns by " + degree + " degrees. ACTION");
                    try {
                        moveBodyModel.turn(session, degree / (45f));
                    } catch (Exception e) {
                        log.write("Unhandled Exception occured: " + e + ". WARN");
                    }
                }

            }
        }

    }

    /**
     * plays a selected sound from a listview. setting the sound value doesnt work yet
     */

    @FXML
    private void playSounds() {
        String filename = utils.getSelected(lv_Sounds);
        if (filename != null) {
            log.write("Nao will now play the sound " + filename + ". ACTION");
            audioModel.playSound(filename/*, (float) volumeSlider.getValue()*/);
        } else log.write("Please make sure to select a file to play. INFO ");
    }

    /**
     * moves nao head depending on which button is pressed on the fxml
     *
     * @param mouseEvent
     */

    @FXML
    private void moveHeadButtons(MouseEvent mouseEvent) {
        if (moveBodyModel == null) {
            moveBodyModel = new MoveBodyModel();
        }
        if (session.isConnected()) {
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                Button button = (Button) mouseEvent.getSource();
                try {
                    moveBodyModel.moveHeadButtons(session, button.getText());
                } catch (Exception e) {
                    log.write("Unhandled Exception occured: " + e + ". WARN");
                }
            }
        }
    }

    /**
     * nao walks depending on which button is pressed on the fxml
     *
     * @param mouseEvent
     */

    @FXML
    private void moveBodyButtons(MouseEvent mouseEvent) {
        if (moveBodyModel == null) {
            moveBodyModel = new MoveBodyModel();
        }
        float velocity = (float) velocitySlider.getValue();
        float angle = (float) angleSlider.getValue();
        float angleRound = utils.round(angle, 5);
        Button button = (Button) mouseEvent.getSource();
        if (session.isConnected()) {
            try {
                if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {

                    moveBodyModel.moveKeyboard(session, button.getText(), velocity, (float) ((angleRound) * (Math.PI / 180)));
                } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                    moveBodyModel.moveKeyboard(session, "stop", 0f, 0f);
                    angleSlider.valueProperty().set(0);
                    if (posturesModel == null) {
                        posturesModel = new PosturesModel();
                    }
                    posturesModel.makePosture(session, "Stand");
                }
            } catch (Exception e) {
                log.write("Unhandled Exception occured: " + e + ". WARN");
            }
        }
    }

    /**
     * nao walks depending on keys pressed and speed selected
     * before he walks nao always stands up
     *
     * @param keyEvent
     */

    @FXML
    private void moveBody(KeyEvent keyEvent) {
        if (moveBodyModel == null) {
            moveBodyModel = new MoveBodyModel();
        }
        try {
            if (session != null && session.isConnected()) {
                if (keyEvent.getText().equals("w") || keyEvent.getText().equals("a") || keyEvent.getText().equals("s")
                        || keyEvent.getText().equals("d")) {
                    float velocity = (float) velocitySlider.getValue();
                    float angle = (float) angleSlider.getValue();
                    float angleRound = utils.round(angle, 5);
                    if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                        moveBodyModel.moveKeyboard(session, keyEvent.getText(), velocity, (float) ((angleRound) * (Math.PI / 180)));
                    } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                        moveBodyModel.moveKeyboard(session, "stop", velocity, angleRound);
                        angleSlider.valueProperty().set(0);
                        if (posturesModel == null) {
                            posturesModel = new PosturesModel();
                        }
                        posturesModel.makePosture(session, "Stand");

                    }

                } else if (keyEvent.getText().equals("i") || keyEvent.getText().equals("j") || keyEvent.getText().equals("k")
                        || keyEvent.getText().equals("l") || keyEvent.getText().equals("m")) {
                    if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                        moveBodyModel.moveKeyboard(session, keyEvent.getText());
                    } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                        moveBodyModel.moveKeyboard(session, "stop");
                    }

                }
            }
        } catch (Exception e) {
            log.write("Unhandled Exception occured: " + e + ". WARN");
        }
    }

    /**
     * lets nao say a text from a textfield
     */

    @FXML
    private void say() {
        if (session.isConnected()) {
            if (textToSpeechModel == null) {
                textToSpeechModel = new TextToSpeechModel();
            }
            if (textToSpeech.getText() != null) {
                Float volume = (float) volumeSlider.getValue();
                String language = (String) dropDownLanguages.getValue();
                String voice = String.valueOf((int) voiceSlider.getValue());
                String speed = String.valueOf((int) voiceSpeedSlider.getValue());
                try {
                    textToSpeechModel.say(session, textToSpeech.getText(), volume, language, voice, speed);
                } catch (Exception e) {
                    log.write("Nao could not say this text. INFO");
                    e.printStackTrace();
                }
            }
        }
    }


    /*
    This method would have enbaled method detection, but was left out because of a bug. The class has been left
    so it can be read through. Without a robot we can't test for where the bug is.

    @FXML
    private void activateAlarm() throws Exception{
        if(!movementDetectionModel.detectionEnabled){
            movementDetectionModel.setDetectionEnabled(true);
        }else{
            movementDetectionModel.setDetectionEnabled(false);
        }
        movementDetectionModel.movementDetection(session);
    }*/

    /**
     * disconnects from nao, clears all tasks and boxes
     */

    @FXML
    private void disconnect() {
        Utils.disconnectMessage(session);
        checkerModel.killCheckers(batteryPercentage, temperatureText);
        VideoModel videoModel = new VideoModel();
        if (ch_camera.isSelected()) {
            videoModel.unsubscribe();
            ch_camera.setSelected(false);
        }
        session.close();
        log.write("Disconnected from Nao " + connectionModel.getNaoUrl() + ". INFO");
        UpdateItems(true, false);
    }

    /**
     * lets nao make a selected posture
     *
     * @param actionEvent
     * @throws Exception
     */

    @FXML
    private void postures(ActionEvent actionEvent) throws Exception {
        if (posturesModel == null) {
            posturesModel = new PosturesModel();
        }
        String actualPose = utils.getSelected(dropDownPostures);
        if (actualPose != null) {
            if (actionEvent.getSource().getClass() != Button.class) {
                posturesModel.changeImage(actualPose, imageView);
            } else {
                posturesModel.makePosture(session, actualPose);
            }
        }
    }

    /**
     * Nao takes the pose of standing or relaxing depending on the togglebutton currently selected
     *
     * @throws Exception
     */

    @FXML
    private void mode() throws Exception {
        if (session.isConnected()) {
            if (moveBodyModel == null) {
                moveBodyModel = new MoveBodyModel();
            }
            ToggleButton object;
            if (mode.getSelectedToggle().getClass() == ToggleButton.class) {
                object = (ToggleButton) mode.getSelectedToggle();
                tb_relax.setStyle("-fx-background-color: lightblue");
                tb_stand.setStyle("-fx-background-color: lightblue");
                object.setStyle("-fx-background-color: deepskyblue; -fx-border-style: solid");
                moveBodyModel.mode(session, object.getText());
            }
        }

    }

    /**
     * Nao shoots a Photo and saves it
     * not yet local but on nao
     * @throws Exception
     */


    /**
     * changes the color of a selected led
     */

    @FXML
    private void changeColor() {
        ledModel = new LEDModel();
        if (colorBox.getValue() != null) {
            ledModel.changeColor(session, cb_LEDS.getValue().toString(), colorBox.getValue().toString().toLowerCase());
        }
    }

    /**
     * changes the Items of the colorlist depending on which body part is selected to set the LEDs
     */

    @FXML
    @SuppressWarnings("unchecked")
    private void changeChoice() {
        String selcetedGroup = cb_LEDS.getValue().toString();
        if (selcetedGroup != null) {
            if (selcetedGroup.equals("BrainLEDs") || selcetedGroup.equals("EarLEDs") || selcetedGroup.equals("Left Ear LEDs") || selcetedGroup.equals("Right Ear LEDs")) {
                Object[] colorArray = {"On", "Off"};
                ObservableList colorList = FXCollections.observableArrayList(Arrays.asList(colorArray));
                colorBox.setValue("");
                colorBox.setItems(colorList);
            } else {
                Object[] colorArray = {"White", "Red", "Green", "Blue", "Yellow", "Magenta", "Cyan"};
                ObservableList colorList = FXCollections.observableArrayList(Arrays.asList(colorArray));
                colorBox.setValue("");
                colorBox.setItems(colorList);
            }
        }

    }

    /**
     * starts Tasks when nao is connected
     */

    @SuppressWarnings("unchecked")
    private void onConnected() {
        connectionModel.write(tx_IP, tx_Port);
        UpdateItems(false, false);
        Utils.connectedMessage(session);
        checkerModel.checkBatteryCharge(session, batteryPercentage, batteryPercentText);
        checkerModel.checkTemperature(session, temperatureText, rightArmTempText, leftArmTempText, rightLegTempText,
                leftLegTempText, headTempText);
        checkerModel.checkTouch(session, midButtonText, rearButtonText, volumeSlider, voiceSlider, voiceSpeedSlider, dropDownLanguages);
        checkerModel.systemInfo(session, systemText);

    }

    /**
     * clears all comboboxes depending on boolean clearboxes
     * sets various fxml elements depending on if the session to the nao is connected or not
     *
     * @param ClearBoxes
     * @param Startup
     */

    @SuppressWarnings("unchecked")
    private void UpdateItems(Boolean ClearBoxes, Boolean Startup) {
        if (session != null) {
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
                ch_camera.setSelected(false);
            }
        } else if (Startup) {
            tb_NAO.setDisable(true);
            connectButton.setDisable(false);
            disconnectButton.setDisable(true);
            Utils.onFXThread(iv_camera.imageProperty(), new Image("file:./default_app.png"));
            Image image = new Image("file:./settingsresized.png");
            tab_Connection.setGraphic(new ImageView(image));
            image = new Image("file:./naoresized.png");
            tb_NAO.setGraphic(new ImageView(image));
            image = new Image("file:./controlsresized.png");
            tab_Controls.setGraphic(new ImageView(image));
            image = new Image("file:./soundsresized.png");
            tab_Audio.setGraphic(new ImageView(image));
            image = new Image("file:./cameraresized.png");
            tab_Camera.setGraphic(new ImageView(image));
            image = new Image("file:./systemresized.png");
            tab_Status.setGraphic(new ImageView(image));
            image = new Image("file:./led.jpg");
            iv_leds.imageProperty().set(image);
            image = new Image("file:./default_app.png");
            imageView.imageProperty().set(image);
        }
    }

    /**
     * Comboboxes are loaded when clicking on NAO tab
     */

    @FXML
    private void naoTab() {
        getBoxes();
    }

    /**
     * loads boxes
     */

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
                lb_play.setVisible(true);
                lb_play.setDisable(false);
            } else {
                log.write("Your Nao does not have the Soundset Aldebaran installed. IMPORTANT");
                log.write("Because of that, you will not see any elements related to audiofiles. INFO");
                btn_play.setDisable(true);
                btn_play.setVisible(false);
                lb_play.setDisable(true);
                lb_play.setVisible(false);
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
                tb_stand.setStyle("-fx-background-color: deepskyblue; -fx-border-style: solid");
                tb_relax.setStyle("-fx-background-color: lightblue");
            } else {
                moveBodyModel.mode(session, "Relax");
                toggle = (Toggle) list.get(1);
                mode.selectToggle(toggle);
                tb_relax.setStyle("-fx-background-color: deepskyblue; -fx-border-style: solid");
                tb_stand.setStyle("-fx-background-color: lightblue");
            }

            if (ledModel == null) {
                ledModel = new LEDModel();
            }
            ObservableList ledGroups = FXCollections.observableArrayList(ledModel.getLEDs(session));
            cb_LEDS.setItems(ledGroups);
            Object[] colorArray = {"White", "Red", "Green", "Blue", "Yellow", "Magenta", "Cyan"};
            ObservableList colorList = FXCollections.observableArrayList(Arrays.asList(colorArray));
            colorBox.setItems(colorList);
            stopTrackingButton.setDisable(true);

        } catch (Exception e) {
            log.write("An error has occured while setting the boxes." + e + ". WARN");
        }
    }

    /**
     * sets the tracker
     *
     * @throws Exception
     */
    public void startTracking() throws Exception {
        if (trackerModel == null) {
            trackerModel = new TrackerModel();
        }
        stopTrackingButton.setDisable(false);
        startTrackingButton.setDisable(true);
        RadioButton target = (RadioButton) trackingTarget.getSelectedToggle();
        RadioButton mode = (RadioButton) trackingMode.getSelectedToggle();
        setRadios(true);
        trackerModel.startTracking(session, target.getId(), mode.getText());
    }

    public void stopTracking() throws Exception {
        if (trackerModel == null) {
            trackerModel = new TrackerModel();
        }
        setRadios(false);
        trackerModel.stopTraker();
        startTrackingButton.setDisable(false);
        stopTrackingButton.setDisable(true);
    }

    private void setRadios(boolean enable) {
        faceRadio.setDisable(enable);
        redBallRadio.setDisable(enable);
        moveRadio.setDisable(enable);
        faceRadio.setDisable(enable);
        bodyRadio.setDisable(enable);
    }

    @FXML
    private void setCamera() {
        if (ch_camera.isSelected()) {
            videoModel.initialize(session, iv_camera);
            log.write("Camera is running. All other activities might lead to complications. IMPORTANT");
            log.write("Please deactivate Camera before disconnecting. WARN");
        } else if (!ch_camera.isSelected()) {
            videoModel.unsubscribe();
        }
    }

    public void setStageAndSetupGuiListeners(Stage primaryStage) {
        final AnchorPane[] ANCHORS={anchor1,anchor2,anchor3,anchor4,anchor5,anchor6,anchor7}; //das Array kann NICHT als Instanzvariable deklariert werden da die fxml elemente ansonsten null sind(noch nicht geladen bei deklaration)
        final Tab[] TABS ={tab_Connection,tb_NAO,tab_Controls, tab_LEDPostures, tab_Audio, tab_Camera};
        setGui(primaryStage, ANCHORS, TABS);
    }

    /**
     * setting x and y values of elements regarding windowsize in width and height listeners
     * oldxpos/oldscenewidth=newxpos/newscenewidth
     * -> new x pos = (oldxpos/oldscenewidth)*newscenewidth
     * same for y
     * @param stage
     * @param ANCHORS
     * @param TABS
     */

    private void setGui(Stage stage, AnchorPane[] ANCHORS, Tab[] TABS) {
        Scene scene = stage.getScene();
        Parent root = scene.getRoot();
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double oldVal = (double) oldValue;
                double newVal = (double) newValue;

                for (AnchorPane anchor:ANCHORS) {
                    if(anchor!=null) { // als sicherung noch drin, als das array als instanzvariable deklariert wurde war anchor immer null
                        anchor.setPrefWidth((double) newValue);
                        for (Node item : anchor.getChildren()) { //die children der anchorpanes sind jeweils die elemente die in den anchorpanes enthalten sind

                            if (item instanceof ListView) {     // ListViews sollen verschoben und skaliert werden
                                ListView lv = (ListView) item;
                                lv.setLayoutX((lv.getLayoutX() / oldVal) * newVal);
                                lv.setPrefWidth((lv.getWidth() / oldVal) * newVal);
                            } else if (item instanceof Label) { // Labels nur verschoben, da sie ansonsten evtl nicht lesbar werden
                                Label lb = (Label) item;
                                lb.setLayoutX((lb.getLayoutX() / oldVal) * newVal);
                            } else if (item instanceof Button) { // siehe Labels
                                Button btn = (Button) item;
                                btn.setLayoutX((btn.getLayoutX() / oldVal) * newVal);
                            } else if (item instanceof TextField) { // siehe Labels
                                TextField tf = (TextField) item;
                                tf.setLayoutX((tf.getLayoutX() / oldVal) * newVal);
                                tf.setPrefWidth((tf.getWidth()/oldVal)*newVal);
                            } else if (item instanceof TabPane) { // das TabPane soll verschoben und skaliert werden
                                TabPane tb = (TabPane) item;
                                tb.setLayoutX((tb.getLayoutX() / oldVal) * newVal);
                                tb.setPrefWidth((tb.getWidth() / oldVal) * newVal);
                            } else if(item instanceof Circle){
                                Circle cl = (Circle)item;
                                cl.setLayoutX((cl.getLayoutX()/oldVal)*newVal);
                                cl.setRadius((cl.getRadius()/oldVal)*newVal);
                            } else if(item instanceof Text){
                                Text tx = (Text)item;
                                tx.setLayoutX((tx.getLayoutX()/oldVal)*newVal);
                            }else if (item instanceof ComboBox) { // siehe Labels
                                ComboBox cb = (ComboBox) item;
                                cb.setLayoutX((cb.getLayoutX() / oldVal) * newVal);
                                cb.setPrefWidth((cb.getWidth()/oldVal)*newVal);
                            }else if (item instanceof ImageView) { // siehe Labels, da das Kamerabild nicht(!) skaliert werden soll
                                ImageView iv = (ImageView) item;
                                iv.setLayoutX((iv.getLayoutX() / oldVal) * newVal);
                            }else if(item instanceof ToggleButton){
                                ToggleButton tb = (ToggleButton)item;
                                item.setLayoutX((tb.getLayoutX()/oldVal)*newVal);
                                tb.setPrefWidth((tb.getWidth()/oldVal)*newVal);
                            }else if(item instanceof TextArea){
                                TextArea ta = (TextArea) item;
                                item.setLayoutX((ta.getLayoutX()/oldVal)*newVal);
                                ((TextArea) item).setPrefWidth((ta.getWidth()/oldVal)*newVal);
                            }else if(item instanceof Line){
                                item.setLayoutX((item.getLayoutX()/oldVal)*newVal);
                            }
                        }
                    }
                }
                redBallRadio.setLayoutX(lb_target.getLayoutX());
                faceRadio.setLayoutX(lb_target.getLayoutX());
                bodyRadio.setLayoutX(lb_tracking.getLayoutX());
                headRadio.setLayoutX(lb_tracking.getLayoutX());
                moveRadio.setLayoutX(lb_tracking.getLayoutX());
                lb_velocity.setLayoutX(w.getLayoutX()-w.getWidth()/2);
                velocitySlider.setLayoutX(lb_velocity.getLayoutX()+25);
                angleSlider.setLayoutX(lb_mvmnt.getLayoutX());
                lb_turn.setLayoutX(lb_mvmnt.getLayoutX());
                voiceSlider.setLayoutX(lb_voicepitch.getLayoutX()+25);
                voiceSpeedSlider.setLayoutX(lb_voicespeed.getLayoutX()+25);
                volumeSlider.setLayoutX(lb_volume.getLayoutX()+25);
                lb_headbuttons.setLayoutX(midButtonText.getLayoutX());
                cb_IP.setLayoutX(lb_abovecbIP.getLayoutX()); // keine ahnung warum, aber diese sachen wurden durch obige befehle nicht verschoben!
                //vor allem slider scheinen merkwürdig zu sein diesbezüglich
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double oldVal = (double) oldValue;
                double newVal = (double) newValue;

                for (AnchorPane anchor:ANCHORS) {
                    if(anchor!=null) { // als sicherung noch drin, als das array als instanzvariable deklariert wurde war anchor immer null
                        anchor.setPrefHeight((double) newValue);
                        for (Node item : anchor.getChildren()) { //die children der anchorpanes sind jeweils die elemente die in den anchorpanes enthalten sind

                            if (item instanceof ListView) {     // ListViews sollen verschoben und skaliert werden
                                ListView lv = (ListView) item;
                                lv.setLayoutY((lv.getLayoutY() / oldVal) * newVal);
                                lv.setPrefHeight((lv.getHeight() / oldVal) * newVal);
                            } else if (item instanceof Label) { // Labels nur verschoben, da sie ansonsten evtl nicht lesbar werden
                                Label lb = (Label) item;
                                lb.setLayoutY((lb.getLayoutY() / oldVal) * newVal);
                            } else if (item instanceof Button) { // siehe Labels
                                Button btn = (Button) item;
                                btn.setLayoutY((btn.getLayoutY() / oldVal) * newVal);
                            } else if (item instanceof TextField) { // siehe Labels
                                TextField tf = (TextField) item;
                                tf.setLayoutY((tf.getLayoutY() / oldVal) * newVal);
                                tf.setPrefHeight((tf.getHeight()/oldVal)*newVal);
                            } else if (item instanceof TabPane) { // das TabPane soll verschoben und skaliert werden
                                TabPane tb = (TabPane) item;
                                tb.setLayoutY((tb.getLayoutY() / oldVal) * newVal);
                                tb.setPrefHeight((tb.getHeight() / oldVal) * newVal);
                            } else if(item instanceof Circle){
                                Circle cl = (Circle)item;
                                cl.setLayoutY((cl.getLayoutY()/oldVal)*newVal);
                                cl.setRadius((cl.getRadius()/oldVal)*newVal);
                            } else if(item instanceof Text){
                                Text tx = (Text)item;
                                tx.setLayoutY((tx.getLayoutY()/oldVal)*newVal);
                            }else if (item instanceof ComboBox) { // siehe Labels
                                ComboBox cb = (ComboBox) item;
                                cb.setLayoutY((cb.getLayoutY() / oldVal) * newVal);
                                cb.setPrefHeight((cb.getHeight()/oldVal)*newVal);
                            }else if (item instanceof ImageView) { // siehe Labels, da das Kamerabild nicht(!) skaliert werden soll
                                ImageView iv = (ImageView) item;
                                iv.setLayoutY((iv.getLayoutY() / oldVal) * newVal);
                            }else if(item instanceof ToggleButton){
                                ToggleButton tb = (ToggleButton)item;
                                item.setLayoutY((tb.getLayoutY()/oldVal)*newVal);
                                tb.setPrefHeight((tb.getHeight()/oldVal)*newVal);
                            }else if(item instanceof TextArea){
                                TextArea ta = (TextArea) item;
                                item.setLayoutY((ta.getLayoutY()/oldVal)*newVal);
                                ((TextArea) item).setPrefHeight((ta.getHeight()/oldVal)*newVal);
                            }else if(item instanceof Line){
                                item.setLayoutY((item.getLayoutY()/oldVal)*newVal);
                            }
                        }
                    }
                }
                cb_IP.setLayoutY(lb_abovecbIP.getLayoutY()+((20/oldVal)*newVal)); // keine ahnung warum, aber cb_IP war die einzige combobox die nicht verschoben wurde durch obige befehle
                // hier theoretisch noch y-werte für slider setzen...
            }
        });

    }

}





