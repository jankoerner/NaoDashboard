package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ListView;


public class Logger {
    final ListView log;
    private StringProperty Color=new SimpleStringProperty();

    public static void main(String[] args) {

    }

    public Logger(final ListView log){
        this.log = log;
    }

    public String Write (String text){
        log.setAccessibleText(text);
    }

    public final void setColor(String Color){
        Color.setColor(Color);
    }
    public final String GetColor(){
        return Color.get();
    }
}
