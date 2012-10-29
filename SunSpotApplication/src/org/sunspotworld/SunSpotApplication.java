/* 
 * SunSpotApplication.java
 *
 * Created on 2012/10/28 17:21:26;
 */
package org.sunspotworld;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.peripheral.Spot;
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
 * The manifest specifies this class as MIDlet-1, which means it will be
 * selected for execution.
 */
public class SunSpotApplication extends MIDlet {

    Sender sender;
    Receiver receiver;
    final String broadcastAddr = "radiogram://broadcast:110";// Broadcast address.
    final String receiveAddr = "radiogram://:110";//Broadcasted address.
    String sendMessage = "Hello World!";// 送りたいメッセージ
    String recvMessage;//受け取ったメッセージ

    protected void startApp() throws MIDletStateChangeException {
        try {
            sender = new Sender(broadcastAddr);//Senderオブジェクトの新規作成
            receiver = new Receiver(receiveAddr);//Receiverオブジェクトの新規作成
            int i = 0;
            while (true) {
                sender.send(sendMessage + " : " + Integer.toString(i));//sendメソッドでsendMessage変数内のデータをブロードキャスト
                recvMessage = receiver.onMessageReceived();//onMessageReceivedメソッドで送られてきたメッセージをrecvMessageに格納
                System.out.println(recvMessage);//recvMessage変数内の文字列をコンソールに出力。
                i++;
            }
        } catch (IOException ex) {
        }

    }

    protected void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system. It is not called if
     * MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true the MIDlet must cleanup and release all
     * resources.
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }
}

/**
 * データをブロードキャストするクラスです。
 */
class Sender {

    DatagramConnection conn;
    Datagram datagram;
    byte[] data;

    /**
     * [コンストラクタ] addressにDatagramConnectionオブジェクトを適用し通信を確立します。
     *
     * @param address
     * @throws IOException
     */
    public Sender(String address) throws IOException {
        Spot.getInstance().getRadioPolicyManager().setOutputPower(31);//出力の範囲の指定(-32～31)
        conn = (DatagramConnection) Connector.open(address);//addressに対してDatagramConnection(conn)を開く。
        ((RadiogramConnection) conn).setMaxBroadcastHops(3);//最大ホップ数の指定
        datagram = conn.newDatagram(conn.getMaximumLength());//受け取れるデータの最大数の指定
    }

    /**
     * 送りたいmessageをブロードキャストします。
     *
     * @param message
     * @throws IOException
     */
    void send(String sendMessage) throws IOException {
        data = sendMessage.getBytes();//引数sendMessageをbyte[]に型変換してdata変数に格納

        datagram.reset();//datagram内にあるデータをリセット
        datagram.write(data);//datagramに変数dataを書き込む
        conn.send(datagram);//DatagramConnection(conn)でdatagramに書き込まれたデータをブロードキャストする。
    }
}

/**
 * データを受け取るクラスです。
 */
class Receiver {

    DatagramConnection conn;
    Datagram datagram;
    byte[] recv;
    String recvMessage;

    /**
     * [コンストラクタ] addressにDatagramConnectionオブジェクトを適用し通信を確立します。
     *
     * @param address
     * @throws IOException
     */
    public Receiver(String address) throws IOException {
        Spot.getInstance().getRadioPolicyManager().setOutputPower(31);//出力の範囲の指定(-32～31)
        conn = (DatagramConnection) Connector.open(address);//addressに対してDatagramConnection(conn)を開く。
        ((RadiogramConnection) conn).setMaxBroadcastHops(3);//最大ホップ数の指定
        datagram = conn.newDatagram(conn.getMaximumLength());//受け取れるデータの最大数の指定
    }

    /**
     * 受け取ったbyte[]データ(recv)をString型に変換して、その文字列を返します。
     *
     * @throws IOException
     * @return message
     */
    String onMessageReceived() throws IOException {
        datagram.reset();//datagram内にあるデータのリセット
        conn.receive(datagram);//DatagramConnection(conn)に送られたbyte[]データをdatagramに格納
        recv = datagram.getData();//datagram内にあるbyte[]データをrecv変数に格納

        recvMessage = new String(recv);//recv変数に格納されているbyte[]データをString型に型変換してrecvMessage変数に格納

        return recvMessage;
    }
}