package lpoo.bombermancontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Utilizador on 31/05/2015.
 */
public class ClientNetwork extends Thread {

    public static final int PORT = 4445;

    private PrintWriter out;
    private BufferedReader in;

    public boolean connectedToServer = false;

    InetAddress address;
    Socket socket;
    String ip;
    ArrayBlockingQueue<String> messagesToSends;
    Activity context;
    long lastmessageTime;

    public ClientNetwork(String ip, Activity context) throws IOException {

        this.context = context;
        this.ip = ip;

        address = InetAddress.getByName(ip);
        messagesToSends = new ArrayBlockingQueue<String>(20);


    }

    public void run() {

        initConn();

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
                lastmessageTime=SystemClock.uptimeMillis();
            }else{
                if(SystemClock.uptimeMillis()-lastmessageTime>5000){
                    //addMessage("estou vivo boi");
                }
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


    synchronized public void initConn() {
        try {

            socket = new Socket(address, PORT);
        } catch (SocketException e) {
            Looper.prepare();
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Erro IP")
                    .setTitle("Network Error");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
            Looper.loop();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {


            e.printStackTrace();
        }


        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("IP", this.ip);
        editor.commit();

    }
}


