package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StarterPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter_page);
    }

    public void HostRoom(View view) {
        Intent intent = new Intent(this, SelectPlaylistActivity.class);
        startActivity(intent);
    }

    public void JoinRoom(View view) {
        Intent intent = new Intent(this, SearchUserActivity.class);
        startActivity(intent);
    }
}