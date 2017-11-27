package com.luke.caseappmatricula.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luke.caseappmatricula.R;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
    }

    private void goToMenuPrincipal(){
        Intent it = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMenuPrincipal();
    }
}
