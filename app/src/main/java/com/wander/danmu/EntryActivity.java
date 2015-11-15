package com.wander.danmu;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class EntryActivity extends AppCompatActivity {
    public static Rect rect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(EntryActivity.this, MainActivity.class));
                finish();
            }
        }, 100);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        rect = PixelTools.getStatusHeight(this);
    }
}
