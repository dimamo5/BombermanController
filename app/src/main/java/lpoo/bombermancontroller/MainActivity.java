package lpoo.bombermancontroller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.net.ConnectException;


public class MainActivity extends ActionBarActivity {

    ClientNetwork server;

    public MainActivity() throws IOException {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText t = (EditText) findViewById(R.id.ip);
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);

        String ip = sharedPref.getString("IP", "ERRO OBTER STRING");

        t.setText(ip);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();  // Always call the superclass method first

        Log.d("Debug", "Destroyed");
    }

    public void connectServer(View view) {
        EditText t = (EditText) findViewById(R.id.ip);

        try {
            server = new ClientNetwork(t.getText().toString(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }


        server.start();
        t.setText(server.getIp());
        t.setClickable(false);
        t.setEnabled(false);

        Button b = (Button) findViewById(R.id.buttonConnect);
        b.setClickable(false);
        b.setText("LIGADO A: " + t.getText().toString());


        ImageButton buttonUp = (ImageButton) findViewById(R.id.imageButtonUp);
        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        server.addMessage("moveUpPressed");
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        server.addMessage("moveUpReleased");
                        // RELEASED
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        ImageButton buttonDown = (ImageButton) findViewById(R.id.imageButtonDown);
        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        server.addMessage("moveDownPressed");
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        server.addMessage("moveDownReleased");
                        // RELEASED
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        ImageButton buttonLeft = (ImageButton) findViewById(R.id.imageButtonLeft);
        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        server.addMessage("moveLeftPressed");
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        server.addMessage("moveLeftReleased");
                        // RELEASED
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        ImageButton buttonRight = (ImageButton) findViewById(R.id.imageButtonRight);
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        server.addMessage("moveRightPressed");
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        server.addMessage("moveRightReleased");
                        // RELEASED
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
    }


}
