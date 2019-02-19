package com.stucom.flx.VCTR_DRRN;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stucom.flx.VCTR_DRRN.model.Prefs;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Prefs prefs;
    private static final int[] BUTTONS_ID = new int[] {
      R.id.btnPlay, R.id.btnRanking, R.id.btnSettings, R.id.btnAbout
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs.getInstance(this);
        setContentView(R.layout.activity_main);
        for(int id: BUTTONS_ID) {
            Button button = findViewById(id);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.btnPlay:
                intent = new Intent(MainActivity.this, PlayActivity.class);
                break;
            case R.id.btnRanking:
                intent = new Intent(MainActivity.this, RankingActivity.class);
                break;
            case R.id.btnSettings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                break;
            case R.id.btnAbout:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                break;
        }
        if (intent == null) return;
        startActivity(intent);
    }

    public static class RegisterActivity {
    }
}
