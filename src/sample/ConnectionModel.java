package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ConnectionModel {
    private StringProperty NaoUrl = new SimpleStringProperty();
    private BufferedWriter writer;
    private FileInputStream file;
    private BufferedReader reader;
    private static String[] IP = new String[5];
    private static String[] Port = new String[IP.length];
    private static String[] URL = new String[Port.length];
    public String[] getPort(){
        return Port;
    }
    public String[] getIP(){
        return IP;
    }
    LogModel log = new LogModel();
    ComboBox cb_IP;
    TextField tx_IP, tx_Port;

    public boolean connect(String ip, Integer port) {
        if (isIPValid(ip, port, 3000)) {
            setNaoUrl(ip, Integer.toString(port));
            return true;
        }else {
            return false;
        }
    }


    private boolean isIPValid(String ip, int port, Integer timeout) {
        if ((!ip.equals("")) && (port > 0)) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, port), timeout);
                return true;
            } catch (IOException e) {
                return false; //  timeout / unreachable / failed DNS lookup
            }
        } else return false;
    }

    private final void setNaoUrl(String ip, String port) {
        NaoUrl.set("tcp://"+ip + ":" + port);
    }

    public final String getNaoUrl(){
        return NaoUrl.get();
    }


    public void write(TextField tx_IP, TextField tx_Port){
        try {
            writer=new BufferedWriter(new FileWriter(new File("connectionlog.txt")));
            String txfieldURL = tx_IP.getText()+":"+tx_Port.getText();
            Integer duplicateIndex  = checkForDuplicates(URL, txfieldURL);
            IP  = changeConnectionArrays(IP, tx_IP, duplicateIndex);
            Port = changeConnectionArrays(Port, tx_Port, duplicateIndex);
            for(Integer I =0; I<IP.length; I++) {
                if(IP[I]!=null) {
                    writer.write(IP[I]);
                    writer.newLine();
                    writer.write(Port[I]);
                    writer.newLine();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String[] changeConnectionArrays(String[] array, TextField tx, Integer duplicateIndex) {
        String dupl;
        switch (duplicateIndex){
            case 0:
                break;
            case 1:
                dupl=array[1];
                array[1]=array[0];
                array[0] = dupl;
                break;
            case 2:
                dupl = array[2];
                array[2]=array[1];
                array[1]=array[0];
                array[0]=dupl;
                break;
            case 3:
                dupl=array[3];
                array[3]=array[2];
                array[2]=array[1];
                array[1]=array[0];
                array[0]=dupl;
                break;
            case 4:
                dupl=array[4];
                array[4]=array[3];
                array[3]=array[2];
                array[2]=array[1];
                array[1]=array[0];
                array[0]=dupl;
                break;
            case 999:
                array[4]=array[3];
                array[3]=array[2];
                array[2]=array[1];
                array[1]=array[0];
                array[0]=tx.getText();
        }
        return array;
    }

    private int checkForDuplicates(String[] array, String txURL) {  //überprüft, ob die gespeicherten URLS bereits die neu eingegebene enthält
        int duplicateArray = 999;                                   //liefert den Index zurück, an dessen Stelle die URL doppelt vorhanden ist
        if(array[0].equals(txURL)) duplicateArray=0;                //falls nicht doppelt liefert es eine zahl zurück, deren größe der array niemals
        else if (array[1].equals(txURL)) duplicateArray=1;          //hat
        else if (array[2].equals(txURL)) duplicateArray=2;
        else if (array[3].equals(txURL)) duplicateArray=3;
        else if (array[4].equals(txURL)) duplicateArray=4;
        return duplicateArray;
    }

    public void initialize(ComboBox cb_IP, TextField tx_IP, TextField tx_Port) {
        this.cb_IP = cb_IP;
        this.tx_IP = tx_IP;
        this.tx_Port = tx_Port;
        read();
    }

    public void read() {
        try {
            try {
                file = new FileInputStream("connectionlog.txt");
                reader = new BufferedReader(new InputStreamReader(file));
                for(Integer I=0; I<IP.length; I++){
                    IP[I] = reader.readLine();
                    Port[I] = reader.readLine();
                    URL[I]=IP[I]+":"+Port[I];
                }
                cb_IP.setItems(FXCollections.observableArrayList(URL));
            } catch (FileNotFoundException e) {
                log.write("The text document connectionlog could not be found on your Computer. WARN");
                log.write("Please check if the file exists in the correct directory. INFO");
            } catch (IOException e){
                Integer actualEntries=0;
                for (Integer I=0; I<IP.length; I++){
                    if(IP[I].equals("")) {
                        actualEntries = I;
                    }
                }
                log.write("Your connection log might be incomplete. It needs to have "+IP.length+" entries, but it has "+actualEntries+". WARN");
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
}
