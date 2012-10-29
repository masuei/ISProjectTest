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
    String sendMessage = "Hello World!";// ���肽�����b�Z�[�W
    String recvMessage;//�󂯎�������b�Z�[�W

    protected void startApp() throws MIDletStateChangeException {
        try {
            sender = new Sender(broadcastAddr);//Sender�I�u�W�F�N�g�̐V�K�쐬
            receiver = new Receiver(receiveAddr);//Receiver�I�u�W�F�N�g�̐V�K�쐬
            int i = 0;
            while (true) {
                sender.send(sendMessage + " : " + Integer.toString(i));//send���\�b�h��sendMessage�ϐ����̃f�[�^���u���[�h�L���X�g
                recvMessage = receiver.onMessageReceived();//onMessageReceived���\�b�h�ő����Ă������b�Z�[�W��recvMessage�Ɋi�[
                System.out.println(recvMessage);//recvMessage�ϐ����̕�������R���\�[���ɏo�́B
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
 * �f�[�^���u���[�h�L���X�g����N���X�ł��B
 */
class Sender {

    DatagramConnection conn;
    Datagram datagram;
    byte[] data;

    /**
     * [�R���X�g���N�^] address��DatagramConnection�I�u�W�F�N�g��K�p���ʐM���m�����܂��B
     *
     * @param address
     * @throws IOException
     */
    public Sender(String address) throws IOException {
        Spot.getInstance().getRadioPolicyManager().setOutputPower(31);//�o�͈͂̔͂̎w��(-32�`31)
        conn = (DatagramConnection) Connector.open(address);//address�ɑ΂���DatagramConnection(conn)���J���B
        ((RadiogramConnection) conn).setMaxBroadcastHops(3);//�ő�z�b�v���̎w��
        datagram = conn.newDatagram(conn.getMaximumLength());//�󂯎���f�[�^�̍ő吔�̎w��
    }

    /**
     * ���肽��message���u���[�h�L���X�g���܂��B
     *
     * @param message
     * @throws IOException
     */
    void send(String sendMessage) throws IOException {
        data = sendMessage.getBytes();//����sendMessage��byte[]�Ɍ^�ϊ�����data�ϐ��Ɋi�[

        datagram.reset();//datagram���ɂ���f�[�^�����Z�b�g
        datagram.write(data);//datagram�ɕϐ�data����������
        conn.send(datagram);//DatagramConnection(conn)��datagram�ɏ������܂ꂽ�f�[�^���u���[�h�L���X�g����B
    }
}

/**
 * �f�[�^���󂯎��N���X�ł��B
 */
class Receiver {

    DatagramConnection conn;
    Datagram datagram;
    byte[] recv;
    String recvMessage;

    /**
     * [�R���X�g���N�^] address��DatagramConnection�I�u�W�F�N�g��K�p���ʐM���m�����܂��B
     *
     * @param address
     * @throws IOException
     */
    public Receiver(String address) throws IOException {
        Spot.getInstance().getRadioPolicyManager().setOutputPower(31);//�o�͈͂̔͂̎w��(-32�`31)
        conn = (DatagramConnection) Connector.open(address);//address�ɑ΂���DatagramConnection(conn)���J���B
        ((RadiogramConnection) conn).setMaxBroadcastHops(3);//�ő�z�b�v���̎w��
        datagram = conn.newDatagram(conn.getMaximumLength());//�󂯎���f�[�^�̍ő吔�̎w��
    }

    /**
     * �󂯎����byte[]�f�[�^(recv)��String�^�ɕϊ����āA���̕������Ԃ��܂��B
     *
     * @throws IOException
     * @return message
     */
    String onMessageReceived() throws IOException {
        datagram.reset();//datagram���ɂ���f�[�^�̃��Z�b�g
        conn.receive(datagram);//DatagramConnection(conn)�ɑ���ꂽbyte[]�f�[�^��datagram�Ɋi�[
        recv = datagram.getData();//datagram���ɂ���byte[]�f�[�^��recv�ϐ��Ɋi�[

        recvMessage = new String(recv);//recv�ϐ��Ɋi�[����Ă���byte[]�f�[�^��String�^�Ɍ^�ϊ�����recvMessage�ϐ��Ɋi�[

        return recvMessage;
    }
}