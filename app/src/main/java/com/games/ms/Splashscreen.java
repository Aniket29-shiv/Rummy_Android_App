    package com.games.ms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import com.splunk.mint.Mint;

public class Splashscreen extends AppCompatActivity {
    private static final String MY_PREFS_NAME = "Login_data" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Mint.initAndStartSession(this.getApplication(), "cc552ad8");
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String user_id = prefs.getString("user_id", "");
                    if (user_id.trim().length() > 0) {
                        startActivity(new Intent(Splashscreen.this, Homepage.class));
                    }else {
                        startActivity(new Intent(Splashscreen.this, LoginScreen.class));

                    }

                }

            }
        }).start();


    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
