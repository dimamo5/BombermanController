package lpoo.bombermancontroller;

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


public class MainActivity extends ActionBarActivity {

    ClientNetwork server;

    public MainActivity() throws IOException {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton explode = (ImageButton) findViewById(R.id.plantBomb);
        explode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("teste", "Down");
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        Log.d("teste", "Up");
                        // RELEASED
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        EditText t = (EditText) findViewById(R.id.ip);
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        String ip = getResources().getString(R.string.ipAddress);

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
            server = new ClientNetwork(t.getText().toString());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        t.setText(server.getIp());
        t.setClickable(false);
        t.setEnabled(false);

        Button b = (Button) findViewById(R.id.buttonConnect);
        b.setClickable(false);
        b.setText("LIGADO A: " + t.getText().toString());

        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.ipAddress), t.getText().toString());
        editor.commit();
    }

    public void sendPlantBomb(View view) {
        server.addMessage("bomba");
    }

    public void sendMoveUp(View view) {
        server.addMessage("moveup");
    }

    public void sendMoveDown(View view) {
        server.addMessage("moveDown");
    }

    public void sendMoveLeft(View view) {
        server.addMessage("moveLeft");
    }

    public void sendMoveRight(View view) {
        server.addMessage("moveRight");
    }


}
