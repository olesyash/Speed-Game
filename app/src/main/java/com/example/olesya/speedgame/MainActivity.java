package com.example.olesya.speedgame;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    //var declarations
    private Button firstButton, secondButton;
    private TextView timTextView, beTextView;
    private long startTime = 0, endTime = 0, bestTime = 0, gameTime = 0;
    private String btime, gtime;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor e;
    private String left, right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize views
        firstButton = (Button)findViewById(R.id.firstButton);
        secondButton = (Button)findViewById(R.id.secondButton);
        timTextView = (TextView)findViewById(R.id.timerTextView);
        beTextView = (TextView)findViewById(R.id.bestTimeTextView2);

        //Get saved data
        mSharedPreferences = getSharedPreferences("myfile", MODE_PRIVATE);
        gtime = mSharedPreferences.getString("CurrentScore", "");
        btime = mSharedPreferences.getString("BestScore", "");
        left = mSharedPreferences.getString("Left","");
        right = mSharedPreferences.getString("Right","");

        //first initializing
        if(left.isEmpty() && right.isEmpty()) {
            setNumbers();//set the random numbers to the buttons
        }
        //load scores from memory


        //If there is a data show it, if not leave empty
        if(!gtime.isEmpty()) {
            timTextView.setText(gtime+" ms");
            gameTime = Long.parseLong(gtime);
        }
        if(!btime.isEmpty()) {
            beTextView.setText(btime+" ms");
            bestTime = Long.parseLong(btime);
        }

        /* Define listener to first button to start time counter */
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFunction(firstButton.getText().toString());
            }
        });

        /* Define listener to second button to stop game counter and show results*/
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFunction(secondButton.getText().toString());
            }
        });

        /*Define listener to best timer block to start over the game*/
        beTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bestTime = 0;
                gameTime = 0;
                if(beTextView.getText().length()>0)
                    setNumbers();
                beTextView.setText("");
                timTextView.setText("");
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    protected void onStop() {
        super.onStop();
        //save data to memry before the app stops
        mSharedPreferences = getSharedPreferences("myfile", MODE_PRIVATE);
        e = mSharedPreferences.edit();
        if(bestTime>0)
            e.putString("BestScore", ""+ bestTime);
        else
            e.putString("BestScore", "");
        if(gameTime>0)
            e.putString("CurrentScore", ""+ gameTime);
        else
            e.putString("CurrentScore", "");
//        e.putString("CurrentScore", ""+ gameTime);
        e.putString("Left", ""+ left);
        e.putString("Right", ""+ right);
        e.apply();


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    //start counting the time
    //calculation done by calculating the ms time difference
    // between the two clicks using the Epoch time
    private void startTimer()
    {
        if(startTime == 0){ //set start time
            startTime = System.currentTimeMillis();
        }
    }

    private void stopTimer()
    {
        endTime = System.currentTimeMillis();//current time
        gameTime = endTime - startTime;//get time difference between clicks
        startTime = 0;// reset start time to  zero
    }

    // set random numbers to the buttons
    private void setNumbers()
    {
        Random random = new Random();//get random number to the first button
        int leftNum = random.nextInt(2) + 1;// set left button
        int rightNum = 3 - leftNum;//set right to the corresponding
        left = "" + leftNum;
        right = "" + rightNum;
        secondButton.setText(left);
        firstButton.setText(right);

    }

    //sets both buttons clicks
    private void buttonFunction(String s)
    {
       switch (s)//check which bummton are you now
       {
           case "1":
               startTimer();//start the timer
               break;
           case "2":
               if(startTime != 0) { //if running
                   stopTimer();
                   //Show result in result block
                   timTextView.setText(gameTime + " ms");
                   //Update best time
                   if (bestTime == 0 | bestTime > gameTime){
                       bestTime = gameTime;
                       Toast.makeText(this,R.string.newHighScore,Toast.LENGTH_SHORT).show();
                   }
                   beTextView.setText(bestTime + " ms");
                   setNumbers();//set new random numbers
               }
               break;
       }
    }

}
