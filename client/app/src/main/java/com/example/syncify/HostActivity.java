package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HostActivity extends AppCompatActivity implements MusicPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
    }
}