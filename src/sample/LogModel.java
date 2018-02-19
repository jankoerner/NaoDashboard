package sample;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogModel {
    private static  Integer ListIndex=4;
    private final static SimpleDateFormat timestampFormatter = new SimpleDateFormat("HH:mm:ss");
    ListView lv_log;

    public void initializeLog(ListView lv){
        WelcomeMessage();
        this.lv_log  = lv;
        lv_log.setFixedCellSize(20);
    }


    private void WelcomeMessage() {
        write("Welcome to the \"Nao Dashboard\", a dashboard which lets you control NAOs. INFO");
        write("To start, please enter the IP address and the port of the NAO you wish to connect to. INFO");
        write("The dashboard was created and designed by Jan Körner, Jonathan Schindler and Valentin Lechner. INFO");
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
            this.lv_log.getItems().add(text);
            ListIndex++;
            this.lv_log.scrollTo(ListIndex);
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

    public void write(String message){
        FilterLogLevel(message);
    }

}
