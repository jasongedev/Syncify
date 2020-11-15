package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public abstract class MusicPlayerActivity extends AppCompatActivity {
    TextView songInfoView;
    TextView numListenersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //songInfoView = findViewById(R.id.);
        //numListenersView = findViewById(R.id.);
    }

}