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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    ClientNetwork server;
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


        ImageButton analog = (ImageButton) findViewById(R.id.analog);

        analog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float currX, currY;
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

                        float deltaX=currX - mPrevX;
                        float deltaY=currY - mPrevY;

                            if(deltaX>25){
                            deltaX=25;
                        }
                        if(deltaY>25){
                            deltaY = 25;
                        }

                        view.setTranslationX(deltaX);
                        view.setTranslationY(deltaY);


                        break;
                    }


                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        view.setTranslationX(0);
                        view.setTranslationY(0);

                        break;
                }

                return true;
            }
        });
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


    }

    public void plantBomb(View view) {
        server.addMessage("plantBomb");

    }
};
