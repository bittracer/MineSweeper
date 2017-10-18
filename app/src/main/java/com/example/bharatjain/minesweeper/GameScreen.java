package com.example.bharatjain.minesweeper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by bharatjain on 09/10/2017.
 */

public class GameScreen extends AppCompatActivity{

    public static final int BOMB = -99;
    public GridView gridview;
    public CustomGridAdaptar gridAdaptar;
    public Integer[] items = new Integer[81];
    int flagAddr = 2130903041;
    int bomb = 2130903040;
    public ArrayList<Integer> _location =  new ArrayList<Integer>();
    public int[] helperItems = new int[81];
    int topFlag=0;
    Chronometer mChronometer;
    String stage="";
    MediaPlayer mPlayer;

    // Images index
    Integer openAddr[] = new Integer[]{2130903047,2130903048,2130903049,2130903050,2130903051,2130903052,2130903053,2130903054,2130903055};


    int nearby[] = new int[]{1,-1,-9,+9,-10,10,-8,8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamescreen);
        mChronometer=(Chronometer) findViewById(R.id.timer);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 50, 0);

        mPlayer = MediaPlayer.create(this, R.raw.music);
        mPlayer.start();

        for(int i=0;i<81;i++) {
            items[i] = R.mipmap.shadow0;
        }

        gridAdaptar = new CustomGridAdaptar(GameScreen.this,items);
        gridview = (GridView) findViewById(R.id.mygrd);
        gridview.setAdapter(gridAdaptar);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        stage = bundle.getString("STAGE");
        initializeGrid(stage);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // This will be called when user taps on cell
                if(helperItems[position] == BOMB){
                    // Blast all bomb
                    for(Integer location:_location){
                        items[location] = R.mipmap.bombdeath;
                    }
                    stopAndReset();
                }else if(items[position] == flagAddr){
                    return;
                }else {
                    // Check if the clicked cell has any number on it
                    if(helperItems[position] > 0){
                        items[position] = openAddr[helperItems[position]];
                        checkIfUserWin();
                    }else{
                        helperItems[position] = -9;// This means that it was initially zero
                        items[position] = R.mipmap.open0;
                        checkAndOpenNearBy(position);
                    }
                }
                gridAdaptar.notifyDataSetChanged();
                checkIfUserWin();
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                // This will be used when user long press the cell, It will display/remove the flag
                TextView textView = (TextView) findViewById(R.id.flag);

                if(topFlag ==0){
                    Toast.makeText(getApplicationContext(),"No more flags allowd!",Toast.LENGTH_LONG).show();
                    return true;
                }
                if(items[position] == R.mipmap.flag){
                    items[position] = R.mipmap.shadow0;
                    textView.setText(""+ ++topFlag);
                }else{
                    items[position] = R.mipmap.flag;
                    textView.setText(""+--topFlag);
                }
                gridAdaptar.notifyDataSetChanged();
                return true;
            }
        });
        initializeTimer();
    }

    public void initializeGrid(String stage){

        // This will initialize the bomb location and the count associated to each bomb.

        int noOfMines;
        Random rand = new Random();
        if(stage.equals("beginner")){
            noOfMines = 8;
        }else if(stage.equals("intermediate")){
            noOfMines = 24;
        }else{
            noOfMines = 40;
        }

        topFlag = noOfMines;

        //Set Mines images in array
        for(int i=0;i<noOfMines;i++){

            int temp = rand.nextInt(81);

            if(!_location.contains(temp))
            {
                _location.add(temp);
                helperItems[temp] = BOMB;
            }else{
                i--;
            }
        }

        //    int nearby[] = new int[]{1,-1,-9,+9,-10,10,-8,8};
        for(int i=0;i<helperItems.length;i++){
            if(helperItems[i]==BOMB){
                int temp=0;
                for(int j=0;j<nearby.length;j++){
                    if((temp = i + nearby[j]) >= 0 && temp<81 && helperItems[temp]!=BOMB){
                        if(((i+1)%9)==0 && (nearby[j]==1 || nearby[j]==10 || nearby[j]==-8)){
                            continue;
                        }else if((i%9)==0 && (nearby[j]==-1 || nearby[j]==-10 || nearby[j]==8)){
                            continue;
                        }
                        helperItems[temp] += 1;
                    }
                }
            }
        }

        TextView textView = (TextView) findViewById(R.id.flag);
        textView.setText(""+topFlag);
    }

    public void checkAndOpenNearBy(int position){

        // The heart of the game which will open the cell and manage everything based on the user selection of the cell
        int nearby[] = new int[]{1,-1,-9,+9,-10,10,-8,8};
        int temp=0;
        for(int i=0;i<nearby.length;i++){
            if((temp = position + nearby[i]) >= 0 && temp<81) {// Will element out of box values (Top and Down)

                if(helperItems[temp]==0) {

                    if(((position+1)%9)==0 && (nearby[i]==1 || nearby[i]==10 || nearby[i]==-8)){
                        continue;
                    }else if((position%9)==0 && (nearby[i]==-1 || nearby[i]==-10 || nearby[i]==8)){
                        continue;
                    }

                    if(nearby[i] == -10 || nearby[i] == 8){
                        //Top Left
                        if((temp+1)!=0 && ((temp+9)!=0 || (temp-9)!=0)){
                            continue;
                        }

                    }else if(nearby[i] == -8 || nearby[i] == 10){
                        //Top Right
                        if((temp-1)!=0 && ((temp+9)!=0 || (temp-9)!=0)){
                            continue;
                        }

                    }

                    helperItems[temp] = -9;// This means that it was initially zero
                    items[temp] = R.mipmap.open0;
                    checkIfUserWin();
                    checkAndOpenNearBy(temp);
                }else if(helperItems[temp] > 0 && helperItems[temp] < 9){
                    if(((position+1)%9)==0 && (nearby[i]==1 || nearby[i]==10 || nearby[i]==-8)){
                        continue;
                    }else if((position%9)==0 && (nearby[i]==-1 || nearby[i]==-10 || nearby[i]==8)){
                        continue;
                    }
                    items[temp] =  openAddr[helperItems[temp]];
                    helperItems[temp] = -1;
                    checkIfUserWin();
                }
            }
        }
    }

    public void initializeTimer(){

        // This will initialize the game timer
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    public void stopAndReset(){

        // This will be invoked when user lose the game

        mPlayer.stop();
        mPlayer = MediaPlayer.create(this, R.raw.bomb);
        mPlayer.start();
        mChronometer.stop();

        TextView progress = (TextView) findViewById(R.id.progress);
        progress.setTextColor(Color.RED);
        progress.setText("Game Over!");

        loseDialog();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        TextView textView = (TextView) findViewById(R.id.timer);
        editor.putString("time",textView.getText().toString());

        editor.apply();
    }

    public void reset(View view){

        // This will reset the game when pressed on the smiley.
        mPlayer.stop();

        TextView progress = (TextView) findViewById(R.id.progress);
        progress.setText("");

        for(int i=0;i<81;i++) {
            items[i] = R.mipmap.shadow0;
        }

        Arrays.fill(helperItems,0);
        _location.clear();
        initializeGrid(stage);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();

        mPlayer = MediaPlayer.create(this, R.raw.music);
        mPlayer.start();
        gridAdaptar.notifyDataSetChanged();

    }

    public void checkIfUserWin(){

        // Check if user win and if user win the game, this dialog will be diplayed

        for(int i=0;i<items.length;i++){
            if(helperItems[i]>0 && helperItems[i]<9){
                return;
            }
        }

        mChronometer.stop();

        // Check if it is High Score
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        TextView textView = (TextView) findViewById(R.id.timer);
        String[] _asd = preferences.getString("time","").split(":");

        if(Integer.parseInt(textView.getText().toString().split(":")[0]) <= Integer.parseInt(_asd[0])){
            if (Integer.parseInt(textView.getText().toString().split(":")[1]) < Integer.parseInt(_asd[1])){
                editor.putString("time",textView.getText().toString());
            }
        }
        editor.apply();


        ImageView image = new ImageView(this);
            image.setImageResource(R.drawable.win);
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this).
                            setMessage(" ").
                            setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reset(null);
                                    dialog.dismiss();
                                }
                            }).
                            setView(image);
            builder.create().show();
    }

    public void loseDialog(){

        // When user lose the game, this dialog will be diplayed
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.lose);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this).
                        setTitle("Better luck next time!").
                        setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reset(null);
                                dialog.dismiss();
                            }
                        }).
                        setView(image);
        builder.create().show();

    }

    @Override
    public void onBackPressed() {
        // When back button is pressed, it will stop the music
        mChronometer.stop();
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.release();
        super.onBackPressed();
    }
}
