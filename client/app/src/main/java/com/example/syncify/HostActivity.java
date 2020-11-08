package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.*;

public class HostActivity extends MusicPlayerActivity {
    String currentPlaylistName;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
    }

    void play(){

    }
    void pause(){
        //
    }
    void skip(){
        //
    }
    void closeRoom(){
        //
    }
}