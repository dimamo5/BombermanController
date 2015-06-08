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

    private enum Dir {LEFT, RIGHT, UP, DOWN, STOP}

    ;
    private Dir lastMessage;
    private float mPrevX;
    private float mPrevY;

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

        ImageButton bb = (ImageButton) findViewById(R.id.plantBomb);
        bb.setClickable(false);

    }

    public String parseMessage(Dir d) {
        if (d == Dir.LEFT) {
            return "moveLeft";
        } else if (d == Dir.DOWN) {
            return "moveDown";
        } else if (d == Dir.UP) {
            return "moveUp";
        } else if (d == Dir.RIGHT) {
            return "moveRight";
        } else {
            return "Stop";
        }
    }

    public Dir parse(double angulo) {
        if (angulo >= -Math.PI / 4 && angulo < Math.PI / 4) {
            return Dir.RIGHT;
        } else if (angulo >= Math.PI / 4 && angulo < 3 * Math.PI / 4) {
            return Dir.DOWN;
        } else if (angulo >= -3 * Math.PI / 4 && angulo < -Math.PI / 4) {
            return Dir.UP;
        } else {
            return Dir.LEFT;
        }

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


        t.setClickable(false);
        t.setEnabled(false);

        Button b = (Button) findViewById(R.id.buttonConnect);
        b.setClickable(false);
        b.setText("LIGADO A: " + server.getIp());

        final int RADIUS = 80;
        ImageButton analog = (ImageButton) findViewById(R.id.analog);

        analog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float currX, currY;
                //float centerX =view.getX()+view.getWidth()/2;
                //float centerY = view.getY()+view.getHeight()/2;

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {

                        mPrevX = event.getRawX();
                        mPrevY = event.getRawY();
                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {

                        currX = event.getRawX();
                        currY = event.getRawY();

                        double deltaX = currX - mPrevX;
                        double deltaY = currY - mPrevY;

                        double angulo = Math.atan2(deltaY, deltaX);

                        if (Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) < RADIUS) {

                            view.setTranslationX((float) deltaX);
                            view.setTranslationY((float) deltaY);
                        } else {
                            view.setTranslationX((float) (RADIUS * Math.cos(angulo)));
                            view.setTranslationY((float) (RADIUS * Math.sin(angulo)));
                        }

                        //Log.d("Cenas:", Double.toString(angulo));

                        if (lastMessage != parse(angulo)) {
                            lastMessage = parse(angulo);
                            server.addMessage(parseMessage(lastMessage));
                        }

                        break;
                    }

                    case MotionEvent.ACTION_UP:
                        view.setTranslationX(0);
                        view.setTranslationY(0);
                        lastMessage = Dir.STOP;
                        server.addMessage(parseMessage(lastMessage));

                        break;
                }

                return true;
            }
        });

        ImageButton bb = (ImageButton) findViewById(R.id.plantBomb);
        bb.setClickable(true);


    }

    public void plantBomb(View view) {
        server.addMessage("plantBomb");

    }
};
