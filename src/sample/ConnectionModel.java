package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class ConnectionModel {
    private StringProperty NaoUrl = new SimpleStringProperty();
    private BufferedWriter writer;
    private FileInputStream file;
    private BufferedReader reader;
    private LogModel log = Controller.getLog();
    private static String[] IP = new String[5];
    private static String[] Port = new String[IP.length];
    private static String[] URL = new String[Port.length];
    public String[] getPort(){
        return Port;
    }
    public String[] getIP(){
        return IP;
    }
    ComboBox cb_IP;
    TextField tx_IP, tx_Port;

    /**
     * initializes the connectionmodel to use fxml items
     * @param cb_IP
     * @param tx_IP
     * @param tx_Port
     */
    public void initialize(ComboBox cb_IP, TextField tx_IP, TextField tx_Port) {
        this.cb_IP = cb_IP;
        this.tx_IP = tx_IP;
        this.tx_Port = tx_Port;
        read();
    }

    /**
     * returns true and sets the property NaoUrl to the entered address if reachable
     * @param ip
     * @param port
     * @return
     */

    public boolean connect(String ip, Integer port) {
        if (isIPValid(ip, port, 3000)) {
            setNaoUrl(ip, Integer.toString(port));
            return true;
        }else {
            return false;
        }
    }

    /**
     * checks if an entered address is reachable within a time
     * @param ip
     * @param port
     * @param timeout
     * @return
     */

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

    /**
     * sets nao url
     * @param ip
     * @param port
     */

    private final void setNaoUrl(String ip, String port) {
        NaoUrl.set("tcp://"+ip + ":" + port);
    }

    /**
     *
     * @return NaoUrl
     */
    public final String getNaoUrl(){
        return NaoUrl.get();
    }

    /**
     * rewrites the arrays Port and Ip to the connectionlog
     * @param tx_IP
     * @param tx_Port
     */

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

    /**
     * changes the arrays. if a new url is entered its set to latest connection, meaning its at [0] of the arrays when reading
     * duplicates also get moved to [0]
     * @param array
     * @param tx
     * @param duplicateIndex
     * @return an entered array, sorted by latest added connections
     */


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

    /**
     * checks if an entered String already is in a String[] and returns the place of the duplicate
     * if no duplicates returns 999
     * @param array
     * @param txURL
     * @return
     */

    private int checkForDuplicates(String[] array, String txURL) {
        int duplicateArray = 999;
        if(array[0].equals(txURL)) duplicateArray=0;
        else if (array[1].equals(txURL)) duplicateArray=1;
        else if (array[2].equals(txURL)) duplicateArray=2;
        else if (array[3].equals(txURL)) duplicateArray=3;
        else if (array[4].equals(txURL)) duplicateArray=4;
        return duplicateArray;
    }


    /**
     * reads connectionlog and adds its connections to IP, Port and URL StringArrays
     * sets the connection textfield to the latest connection added
     */

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
                for (Integer I=0; I<IP.length; I++) {
                    if(IP[I]!=null) cb_IP.getItems().add(IP[I]+":"+Port[I]);
                }
            } catch (FileNotFoundException e) {
                log.write("The text document connectionlog could not be found on your Computer. WARN");
                log.write("Please check if the file exists in the correct directory. IMPORTANT");
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

    /**
     * checks the connection during runtime
     * @return connected
     */

    public boolean checkConnection() {
        final FutureTask checkConnection = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return Controller.getSession().isConnected();
            }
        });
        Platform.runLater(checkConnection);
        String connected = null;
        try {
            connected = checkConnection.get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(connected.equals("true")) return true;
        else return false;
        }

}

