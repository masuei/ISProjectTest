/* send
 * SunSpotApplication.java
 *
 * Created on 2012/10/28 17:21:26;
 */

package org.sunspotworld;

import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.resources.Resources;
import com.sun.spot.io.j2me.radiostream.*;
import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.util.IEEEAddress;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;





/**
 * The startApp method of this class is called by the VM to start the
 * application.
 * 
 * The manifest specifies this class as MIDlet-1, which means it will
 * be selected for execution.
 */
public class SunSpotApplication extends MIDlet {

    Sender sender;
    Receiver recv;
    final String broadcast="radiogram://broadcast:110";// Broadcast address.
    final String receive="radiogram://:110";
    String message ="Hello World!";// message

    protected void startApp() throws MIDletStateChangeException {
        try {
            sender = new Sender(broadcast);
            recv = new Receiver(receive);
            int i = 0;
            while(true){
                sender.send(message+" : "+Integer.toString(i));
                i++;

            }
        } catch (IOException ex) {
        }

    }
    
    protected void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system.
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true the MIDlet must cleanup and release all resources.
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }
}
/**
 * データを送るクラスです。
 */
class Sender {
    DatagramConnection conn;
    Datagram datagram;
    
    /**
     * コンストラクタ
     * @param address
     * @throws IOException 
     */
    public Sender(String address) throws IOException{
        conn = (DatagramConnection) Connector.open(address);
        datagram = conn.newDatagram(conn.getMaximumLength());
    }
    
    /** 
     * 送りたいmessageをブロードキャストします。
     * @param message
     * @throws IOException
     */
    void send(String message) throws IOException{
        /* stringをbyteに変換します。 */
        //byte [] data;
        //data = message.getBytes();
        
        datagram.reset();
        datagram.writeUTF(message);
        System.out.println(message);
        conn.send(datagram);
    }
}


/**
 * データを受け取るクラスです。
 */
class Receiver{
    DatagramConnection conn;
    Datagram datagram;
    
    byte[] recv;
    String message;
    
    public Receiver(String receive) throws IOException{
        conn = (DatagramConnection) Connector.open(receive);
        datagram = conn.newDatagram(conn.getMaximumLength());
    }
    
    String message() throws IOException{
        recv = datagram.getData();
        message = new String(recv);
        
        return message;
    }
}