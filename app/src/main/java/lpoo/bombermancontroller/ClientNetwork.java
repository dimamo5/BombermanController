package lpoo.bombermancontroller;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Utilizador on 31/05/2015.
 */
public class ClientNetwork extends Thread {

    public static final int PORT = 4445;

    private PrintWriter out;
    private BufferedReader in;

    boolean connectedToServer = false;

    InetAddress address;
    Socket socket;
    String ip;
    ArrayBlockingQueue<String> messagesToSends;

    public ClientNetwork(String ip) throws IOException {


        this.ip = ip;

        address = InetAddress.getByName(ip);
        messagesToSends = new ArrayBlockingQueue<String>(20);


    }

    public void run() {
        try {
            socket = new Socket(address, PORT);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!connectedToServer) {
            sendPacket("ligar");
            if (receivePacket().equals("ACK")) {
                connectedToServer = true;
                break;
            }
        }

        while (connectedToServer) {
            if (messagesToSends.peek() != null) {
                sendPacket(messagesToSends.poll());
            }

        }
    }

    void sendPacket(String s) {
        // send request

        out.println(s);
        out.flush();
    }


    String receivePacket() {
        String s = null;
        try {
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }


    public void addMessage(String s) {
        if (connectedToServer)
            messagesToSends.offer(s);
    }

    void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getIp() {
        return ip;
    }

}


