package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.*;
import java.util.*;


public class Controller {
    @FXML
    ToggleGroup mode;
    @FXML
    Slider velocitySlider, volumeSlider;
    @FXML
    TextFlow tfl_log;
    @FXML
    TextArea textToSpeech;
    @FXML
    Button w, a, s, d, connectButton, disconnectButton, sayButton, poseButton;
    @FXML
    Circle connectCircle, batteryCircle;
    @FXML
    ComboBox dropDownPostures, dropDownLanguages;
    @FXML
    TextField tx_IP, tx_Port, degreeField;
    @FXML
    ProgressBar batteryBar;
    @FXML
    Text bodyTempText;

    private BufferedWriter writer;
    private FileInputStream file;
    private BufferedReader reader;
    private String IP;
    private String Port;


    private static Application app;
    private ConnectionModel connectionModel;
    private TextToSpeechModel textToSpeechModel;
    private PosturesModel posturesModel;
    private MoveBodyModel moveBodyModel;

    public static Application getApp() {
        return app;
    }

    public static void main(String[] args) {

    }

    public void initialize() {
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

    private void write(String ip, String port) throws IOException {
        try {
            writer = new BufferedWriter(new FileWriter(new File("connectionlog.txt")));
            writer.write(ip);
            writer.newLine();
            writer.write(port);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }

    }

    private void read() {
        IP = "";
        Port = "";
        try {
            try {
                file = new FileInputStream("connectionlog.txt");
                reader = new BufferedReader(new InputStreamReader(file));
                Port = reader.readLine();
                IP = reader.readLine();

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
            tx_IP.setText(IP);
            tx_Port.setText(Port);
            disconnectButton.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void btn_ConnectIsPressed(ActionEvent actionEvent) throws Exception {
        if (connectionModel == null) {
            connectionModel = new ConnectionModel();
        }

        if (tx_Port.getText() != null && tx_IP.getText() != null) {
            if (connectionModel.connect(tx_IP.getText(), Integer.parseInt(tx_Port.getText()))) {

                if (app == null) {
                    app = new Application(new String[]{}, connectionModel.getNaoUrl());
                    app.start();
                }

                if (app.session().isConnected()) {
                    //write(tx_Port.getText(),tx_IP.getText());
                    onConnected();
                }

            } else {
                //Main.logger.warn("IP stimmt nicht oder Port stimmt nicht, bitte Verbindung überprüfen");
            }
        }

    }

    public void move(ActionEvent actionEvent) throws Exception {
        if (app != null) {
            if (moveBodyModel == null) {
                moveBodyModel = new MoveBodyModel();
            }
            if (degreeField.getText() != null) {
                String degreeString = degreeField.getText();

                if (isNumber(degreeString)) {
                    Float degree = Float.parseFloat(degreeString);
                    moveBodyModel.turn(app, degree / (45f));
                }

            }
        }

    }

    public void moveBody(KeyEvent keyEvent) throws Exception {
        if (moveBodyModel == null) {
            moveBodyModel = new MoveBodyModel();
        }
        if (app != null) {
            if (keyEvent.getText().equals("w") || keyEvent.getText().equals("a") || keyEvent.getText().equals("s")
                    || keyEvent.getText().equals("d")) {

                float velocity = (float) velocitySlider.getValue();
                if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                    moveBodyModel.moveKeyboard(app, keyEvent.getText(), velocity);
                } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                    moveBodyModel.moveKeyboard(app, "stop", velocity);
                    if (posturesModel == null) {
                        posturesModel = new PosturesModel();
                    }
                    posturesModel.makePosture(app, "Stand");

                }

            } else if (keyEvent.getText().equals("i") || keyEvent.getText().equals("j") || keyEvent.getText().equals("k")
                    || keyEvent.getText().equals("l") || keyEvent.getText().equals("m")) {
                if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                    moveBodyModel.moveKeyboard(app, keyEvent.getText());
                } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                    moveBodyModel.moveKeyboard(app, "stop");
                }

            }
        }
    }


    public void say(ActionEvent actionEvent) throws Exception {
        if (app != null) {
            if (textToSpeechModel == null) {
                textToSpeechModel = new TextToSpeechModel();
            }
            if (textToSpeech.getText() != null) {
                String language = (String) dropDownLanguages.getValue();
                System.out.println(language);
                float volume = (float) volumeSlider.getValue();
                textToSpeechModel.say(app, textToSpeech.getText(), volume, language);
            }
        }

    }

    public void doSitDown(ActionEvent actionEvent) throws Exception {
        if (app == null) {
            System.out.println("App ist null");
        } else {
            ALRobotPosture posture = new ALRobotPosture(app.session());
            posture.goToPosture("Crouch", 1.0f);
        }
    }

    public void disconnect() throws Exception {
        app.session().close();

        connectCircle.setFill(Color.rgb(240, 20, 20));
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
    }

    public void postures(ActionEvent actionEvent) throws Exception {
        if (posturesModel == null) {
            posturesModel = new PosturesModel();
        }
        String actualPose = (String) dropDownPostures.getValue();
        posturesModel.makePosture(app, actualPose);
    }

    public void mode(ActionEvent actionEvent) throws Exception {
        if (app != null) {
            if (moveBodyModel == null) {
                moveBodyModel = new MoveBodyModel();
            }
            ToggleButton object;
            if (mode.getSelectedToggle().getClass() == ToggleButton.class) {
                object = (ToggleButton) mode.getSelectedToggle();
                moveBodyModel.mode(app, object.getText());
            }
        }

    }

    private void onConnected() throws Exception {
        this.write(tx_Port.getText(), tx_IP.getText());
        connectCircle.setFill(Color.rgb(60, 230, 30));
        connectButton.setDisable(true);
        disconnectButton.setDisable(false);
        ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(app.session());
        alAnimatedSpeech.say("You are connected");

        if (posturesModel == null) {
            posturesModel = new PosturesModel();
        }
        List postureList1 = posturesModel.getPostures(app);
        ObservableList postureList = FXCollections.observableArrayList(postureList1);
        if (postureList != null) {
            dropDownPostures.setItems(postureList);
            poseButton.setDisable(false);
        } else {
            dropDownPostures.setDisable(true);
            poseButton.setDisable(true);

        }

        if (textToSpeechModel == null) {
            textToSpeechModel = new TextToSpeechModel();
        }
        List languagesList1 = textToSpeechModel.getLanguages(app);
        ObservableList languagesList = FXCollections.observableArrayList(languagesList1);
        if (languagesList != null) {
            dropDownLanguages.setItems(languagesList);
            dropDownLanguages.setValue(languagesList.get(0));
            sayButton.setDisable(false);
        } else {
            dropDownLanguages.setDisable(true);
            sayButton.setDisable(true);
        }
        batteryCharge();
        checkTemperature();
    }

    private boolean isNumber(String number) {
        float d;
        try {
            d = Float.parseFloat(number);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if (d >= (-180) && d <= 180) {
            return true;
        } else {
            return false;
        }
    }

    private void batteryCharge() {
        Timer batteryTimer = new Timer();
        TimerTask checkBattery = new TimerTask() {
            @Override
            public void run() {
                try {
                    ALBattery alBattery = new ALBattery(app.session());
                    double batteryPercent = alBattery.getBatteryCharge();
                    batteryBar.setProgress(batteryPercent);
                    System.out.println(alBattery.getBatteryCharge());
                    if (alBattery.getBatteryCharge() > 75) {
                        batteryCircle.setFill(Color.GREEN);
                        System.out.println(alBattery.getBatteryCharge());
                    } else if (alBattery.getBatteryCharge() < 75 & alBattery.getBatteryCharge() > 30) {
                        batteryCircle.setFill(Color.ORANGE);
                        System.out.println(alBattery.getBatteryCharge());
                    } else if (alBattery.getBatteryCharge() < 30 & alBattery.getBatteryCharge() != 0) {
                        batteryCircle.setFill(Color.RED);
                        System.out.println(alBattery.getBatteryCharge());
                    } else {
                        batteryCircle.setFill(Color.BLACK);
                        System.out.println("No battery detected or no charge remaining.");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        batteryTimer.scheduleAtFixedRate(checkBattery, 1000, 6000);
    }

    private void checkTemperature() {
        Timer tempTimer = new Timer();
        TimerTask checkTemp = new TimerTask() {
            @Override
            public void run() {
                try {
                    ALBodyTemperature alBodyTemperature = new ALBodyTemperature(app.session());
                    alBodyTemperature.setEnableNotifications(true);
                    System.out.println(alBodyTemperature.getTemperatureDiagnosis());
                    if (alBodyTemperature.getTemperatureDiagnosis() instanceof ArrayList){
                        ArrayList arrayList = (ArrayList) alBodyTemperature.getTemperatureDiagnosis();
                        if ( arrayList.get(0).equals(1)){
                            bodyTempText.setText("Warm");
                            bodyTempText.setFill(Color.ORANGE);
                        }else{
                            bodyTempText.setText("Heiß");
                            bodyTempText.setFill(Color.RED);
                        }
                    }else{
                        bodyTempText.setText("Kühl");
                        bodyTempText.setFill(Color.GREEN);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        tempTimer.scheduleAtFixedRate(checkTemp, 1000, 10000);
    }
}




