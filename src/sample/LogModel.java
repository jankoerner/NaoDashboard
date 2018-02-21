package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class is used to add items to a log on the FXML
 */
public class LogModel {
    private static  Integer ListIndex=4;
    private final static SimpleDateFormat timestampFormatter = new SimpleDateFormat("HH:mm:ss");
    private ListView lv_log;

    /**
     * initializes the listview to be used by this class
     * @param lv
     */

    public void initializeLog(ListView lv){
        WelcomeMessage();
        this.lv_log  = lv;
        lv_log.setFixedCellSize(20);
        this.lv_log.setItems(FXCollections.observableArrayList("")); //damit getItems() am programmstart nicht null liefert
    }

    /**
     * adds a Welcome Message
     */
    private void WelcomeMessage() {
        write("Welcome to the \"Nao Dashboard\", a dashboard which lets you control NAOs. INFO");
        write("To start, please enter the IP address and the port of the NAO you wish to connect to. INFO");
        write("The dashboard was created and designed by Jan Körner, Jonathan Schindler and Valentin Lechner. INFO");
    }

    /**
     * turns the String message into a text, adds style depending on the context of the message and appends it to the listview
     * @param message
     * @param context
     */

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
                text.setStyle("-fx-fill: black; -fx-font-weight: lighter");
            }
            this.lv_log.getItems().add(text);
            ListIndex++;
            this.lv_log.scrollTo(ListIndex);
        });
    }

    /**
     * Filters log messages for context
     * @param message
     */

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

    /**
     * the method used to write text to the log
     * @param message
     */

    public void write(String message){
        FilterLogLevel(message);
    }

}