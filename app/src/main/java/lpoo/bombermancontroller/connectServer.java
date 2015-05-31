package lpoo.bombermancontroller;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.AbstractQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Utilizador on 31/05/2015.
 */
public class connectServer extends  Thread{
    String idServer=null;
    DatagramSocket socket;
    ArrayBlockingQueue<String> messagesToSends;

    public connectServer(int port) throws IOException {

        // get a datagram socket
        socket = new DatagramSocket();
        messagesToSends=new ArrayBlockingQueue<String>(10);
    }
    public void run(){
        while(true){
                if(messagesToSends.peek()!=null) {
                sendPacket(messagesToSends.poll());
            }
        }
    }

    void sendPacket(String s) {
        // send request
        byte[] buf = new byte[256];
        buf=s.getBytes();

        InetAddress address = null;
        try {
            address = InetAddress.getByName("192.168.0.15");
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String s){
        messagesToSends.offer(s);
    }
        // get response
        //packet = new DatagramPacket(buf, buf.length);
        //socket.receive(packet);

        // display response
        //String received = new String(packet.getData(), 0, packet.getLength());
        //System.out.println("Quote of the Moment: " + received);
    void closeConnection(){
        socket.close();
    }

    public String getIdServer(){
        return this.idServer;
    }
}
