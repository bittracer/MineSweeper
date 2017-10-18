package com.example.bharatjain.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Bundle bundle = new Bundle();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView _score = (TextView) findViewById(R.id.score);
        _score.setText(preferences.getString("time",""));

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView _score = (TextView) findViewById(R.id.score);
        _score.setText(preferences.getString("time",""));
        //Do your code here
        }

    public void buttonClicked(View view) {

        switch (view.getId()) {

            case R.id.beginner:

                intent = new Intent(this, GameScreen.class);
                bundle.putString("STAGE", "beginner");
                intent.putExtras(bundle);
                startActivity(intent);

                break;

            case R.id.intermediate:

                intent = new Intent(this, GameScreen.class);
                bundle.putString("STAGE", "intermediate");
                intent.putExtras(bundle);
                startActivity(intent);

                break;

            case R.id.advanced:

                intent = new Intent(this, GameScreen.class);
                bundle.putString("STAGE", "advanced");
                intent.putExtras(bundle);
                startActivity(intent);

                break;
        }
    }
}
