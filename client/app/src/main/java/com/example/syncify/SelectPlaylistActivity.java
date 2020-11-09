package com.example.syncify;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectPlaylistActivity extends AppCompatActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public Playlist[] getPlaylists() {

        return null;
    }

    void generateList(Playlist[] playlists) {

    }
}
