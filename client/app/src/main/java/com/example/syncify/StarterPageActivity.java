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

        final Button host_button = findViewById(R.id.Host_Room);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               HostRoom(v);
            }
        });

        final Button join_button = findViewById(R.id.Join_Room);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JoinRoom(v);
            }
        });
    }

    public void HostRoom(View view) {
        Intent intent = new Intent(this, HostActivity.class);
        startActivity(intent);
    }

    public void JoinRoom(View view) {
        Intent intent = new Intent(this, SearchUserActivity.class);
        startActivity(intent);
    }
}