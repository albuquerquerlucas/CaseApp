package com.luke.caseappmatricula.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luke.caseappmatricula.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timerThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent Login = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(Login);
                    finish();
                }
            }
        };
        timerThread.start();
    }
}
