package com.example.syncify;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SelectPlaylistActivity extends AppCompatActivity {

    ListView mListView;
    List<Playlist> list;
    Playlist[] playlists;
    ValueEventListener listener;
    Map<String, Playlist> playlistMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        mListView = findViewById(R.id.list_view);
        listenToPlaylistField();
    }

    public void listenToPlaylistField() {
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<Playlist>> t = new GenericTypeIndicator<List<Playlist>>() {};
                //GenericTypeIndicator<Map<String, Playlist>> m = new GenericTypeIndicator<Map<String, Playlist>>() {};
                list = snapshot.getValue(t);

                list = snapshot.getValue(t);
                if (list != null) {
                    createPlaylistArray();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Session.user.child("playlists").addValueEventListener(listener);
    }

    public void createPlaylistArray() {
        Session.user.child("playlists").removeEventListener(listener);
        playlists = convertListToArray(list);
        runOnUiThread(() -> populateListView(playlists));
    }

    private Playlist[] convertListToArray(List<Playlist> pList) {
        Playlist[] arr = new Playlist[pList.size()];

        for (int i = 0; i < pList.size(); i++) {
            arr[i] = pList.get(i);
        }

        return arr;
    }

    private void  populateListView(Playlist[] playlists) {
        Thread thread = new Thread(() -> {
            PlaylistAdapter adapter = new PlaylistAdapter(getBaseContext(), playlists);
            adapter.createBitmaps();
            runOnUiThread(() -> mListView.setAdapter(adapter));
        });
        thread.start();
    }
}
